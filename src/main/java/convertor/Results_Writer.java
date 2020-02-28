package convertor;

import java.io.*;

public class Results_Writer {
	private static String _meta_file_name = "result/meta.xml";
	private static String _styles_file_name = "result/styles.xml";
	private static String _content_file_name = "result/content.xml";

	private static OutputStreamWriter metaOutStreamWriter = null;
	private static OutputStreamWriter styleOutStreamWriter = null;
	private static OutputStreamWriter contentOutStreamWriter = null;

	public Results_Writer() {
		
	}
	
	public static void initialize() throws IOException{
		File metaFile = new File(_meta_file_name);
		File styleFile = new File(_styles_file_name);
		File contentFile = new File(_content_file_name);
		
		metaFile.createNewFile();
		styleFile.createNewFile();
		contentFile.createNewFile();
		
		metaOutStreamWriter = 
			new OutputStreamWriter(
					new FileOutputStream(_meta_file_name),"UTF8");
		
		styleOutStreamWriter =
			new OutputStreamWriter(
					new FileOutputStream(_styles_file_name),"UTF8");
		
		contentOutStreamWriter = 
			new OutputStreamWriter(
					new FileOutputStream(_content_file_name),"UTF8");
		
		printFileHeader();
	}
	
	public static void close() throws IOException{
		metaOutStreamWriter.close();
		styleOutStreamWriter.close();
		contentOutStreamWriter.close();
	}
	
	public static void processMetaResult(String result) {
		try {
			metaOutStreamWriter.write(result,0,result.length());
			metaOutStreamWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}
	 
	public static void processStyleResult(String result) {
		try {
			styleOutStreamWriter.write(result,0,result.length());
			styleOutStreamWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}
	
	public static void processContentResult(String result) {
		//print(result);
			
		try {
			contentOutStreamWriter.write(result,0,result.length());
			contentOutStreamWriter.flush();
			
			Convertor_UOF_To_ODF.write_result_ta(result.replace(">",">\n"));
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}
	
	private static void printFileHeader(){
		String headerOfAll = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		String contentHeader = "<office:document-content xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"" +
									" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"" +
									" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
									" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"" +
									" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\"" +
									" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\"" +
									" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:xforms=\"http://www.w3.org/2002/xforms\"" +
									" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:smil=\"urn:oasis:names:tc:opendocument:xmlns:smil-compatible:1.0\"" +
									" xmlns:anim=\"urn:oasis:names:tc:opendocument:xmlns:animation:1.0\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\" office:version=\"1.0\">";
		String metaHeader = "<office:document-meta xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
								" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\" office:version=\"1.0\">";
		String styleHeader = "<office:document-styles xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"" +
								" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"" +
								" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
								" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"" +
								" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\" xmlns:math=\"http://www.w3.org/1998/Math/MathML\"" +
								" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\" xmlns:ooo=\"http://openoffice.org/2004/office\"" +
								" xmlns:ooow=\"http://openoffice.org/2004/writer\" xmlns:oooc=\"http://openoffice.org/2004/calc\" xmlns:dom=\"http://www.w3.org/2001/xml-events\" xmlns:presentation=\"urn:oasis:names:tc:opendocument:xmlns:presentation:1.0\"" +
								" xmlns:smil=\"urn:oasis:names:tc:opendocument:xmlns:smil-compatible:1.0\" xmlns:anim=\"urn:oasis:names:tc:opendocument:xmlns:animation:1.0\" office:version=\"1.0\">";
		
		processMetaResult(headerOfAll);
		processMetaResult(metaHeader);
		
		processContentResult(headerOfAll);
		processContentResult(contentHeader);
		
		processStyleResult(headerOfAll);
		processStyleResult(styleHeader);
	}
}
