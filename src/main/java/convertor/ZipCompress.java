package convertor;

import java.io.*;
import java.util.zip.*;
import java.util.Stack;
import temp_structs.Common_Data;


public class ZipCompress {
	//The source files that the zip data will go to.
//	private static String _src_file = "result";
	//The source directory that will be zipped.
	private static String _src_dir = "result/";
	//The order of these files shouldn't be changed.
	private static String[] _files = {
		"mimetype",
		"Configurations2/",
		"Pictures/",
		"layout-cache",
		"content.xml",
		"styles.xml",
		"meta.xml",
		"Thumbnails/",
		"settings.xml",
		"META-INF/manifest.xml"
	};

	private static Stack<String> addtionoal_folders = new Stack<String>();


	public static void init() throws IOException{
		File dir = new File(_src_dir);
		dir.mkdirs();

		File mateInf = new File(_src_dir + "META-INF/");
		mateInf.mkdir();

		for(int i=0; i<_files.length; i++){
			File f = new File(_src_dir + _files[i]);
			if(_files[i].endsWith("/")){
				f.mkdir();
			}else{
				f.createNewFile();
			}
		}
	}

	public static String compress(String rstName) throws IOException{
		String fileType = "";
		File odfFile = null;
		String type = Common_Data.get_file_type();

		if(type.equals("text")){
			fileType = "odt";
		}
		else if(type.equals("spreadsheet")){
			fileType = "ods";
		}
		else if(type.equals("presentation")){
			fileType = "odp";
		}
		//make sure the type of rst file is right
		rstName = rstName.substring(0, rstName.length()-3) + fileType;

		odfFile = new File(rstName);
		if(odfFile.exists()){
			String error = "Error: File name:" + rstName
						+ " exists already！Please input a new file name for the result！";

			Convertor_UOF_To_ODF.write_source_ta(error);
			throw new IOException(error);
		}

		//create a ZipOutputStream to zip the data to
		ZipOutputStream zos = new
		ZipOutputStream(new CheckedOutputStream(
				new FileOutputStream(odfFile), new CRC32()));

		//loop through _src_dir, and zip files
		for(int i=0; i<_files.length; i++){
			zipFile(_src_dir, _files[i], zos);
		}

		//zip "Object" folders
		while(!addtionoal_folders.isEmpty()) {
			zipFile(_src_dir, addtionoal_folders.pop() + "/", zos);
		}

		//close the stream
		zos.close();

		return rstName;
	}

	public static void zipFile(String dir, String fileName, ZipOutputStream zos){
		try{
			int bytesIn = 0;
			byte[] readBuffer = new byte[2156];
			File src = new File(dir + fileName);

			if(!src.exists()){
				//System.err.println("File: " + dir + fileName + " does not exist.");
				return;
			}else {
				System.err.println("compressing file:" + dir + fileName);
			}

			if(src.isDirectory()){
				ZipEntry anEntry = new ZipEntry(fileName);
				zos.putNextEntry(anEntry);

				String[] dirList = src.list();
				//loop through dirList, and zip the files
				for(int i=0; i<dirList.length; i++) {
					zipFile(dir, fileName + dirList[i], zos);
				}
			}
			else {
				//if we reached here, the File object src was not
				//a directory create a FileInputStream on top of src
				FileInputStream fis = new FileInputStream(src);
				// create a new zip entry
				ZipEntry anEntry = new ZipEntry(fileName);
				//place the zip entry in the ZipOutputStream object
				zos.putNextEntry(anEntry);
				//now write the content of the file to the ZipOutputStream
				while((bytesIn = fis.read(readBuffer)) != -1) {
					zos.write(readBuffer, 0, bytesIn);
				}
				//close the Stream
				fis.close();
			}
		} catch (Exception e){

		}
	}

	public static void addFolder(String folderName){
		try{
			File folder = new File(_src_dir + folderName + "/");
			folder.mkdir();
			addtionoal_folders.push(folderName);
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	public static void addFile(String folderName, String fileName){
		try{
			File file = new File(_src_dir + folderName + "/" + fileName);
			file.createNewFile();
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}

	public static void writeFile(String folderName, String fileName, String content){
		String path = _src_dir + folderName + "/" + fileName;
		try{
			FileOutputStream outstream = new FileOutputStream(path);
			OutputStreamWriter writer = new OutputStreamWriter(outstream,"UTF8");
			writer.write(content,0,content.length());
			writer.flush();
			System.out.print(content + "...\n");
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
}
