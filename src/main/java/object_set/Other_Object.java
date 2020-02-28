package object_set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.*;

import temp_structs.*;

public class Other_Object {
	
	private static String _text_node = "";   //用于存放文本节点的内容
	private static boolean _need_to_store_text = false;   //标识是否需要取出文本节点的值
	
	private static String _obj_id = "";   //ID，在body中引用时会用到
	private static String _obj_path = "";
	private static String _obj_type = "";
	
	private static final String _obj_folder = "result/Pictures/";  //对象存放的路径
	public Other_Object() {	
		
	}
	
	public static void process_start(String qName,Attributes atts){
		if (qName.equals("uof:其他对象")) {
			_obj_id = atts.getValue("uof:标识符");
			if (atts.getValue("uof:公共类型") != null) {
				_obj_type = "." + atts.getValue("uof:公共类型");
				_obj_path = _obj_folder + _obj_id + _obj_type;
			}
		}
		else if (qName.equals("uof:数据")) {
			_need_to_store_text = true;
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("uof:其他对象")) {
			String path = "Pictures/" + _obj_id + _obj_type;
			
			String styles = Stored_Data.getAutoStylesInContentXml();
			int i = styles.indexOf("\"" + _obj_id + "\"");
			int j = styles.indexOf(" ",i);
			styles = styles.substring(0,i + 1) + path + styles.substring(j - 1);
			Stored_Data.setAutoStylesInContentXml(styles);
			
			Object_Set_Data.addOtherObj(_obj_id,path);	
			_obj_id = "";
			_obj_path = "";
			_obj_type = "";
		}
		else if (qName.equals("uof:数据")) {
			String base64Str = _text_node;
			ByteArrayOutputStream f = new ByteArrayOutputStream();
			File resultFile = new File(_obj_path);
			
			try {
				if(!(new File(_obj_folder).isDirectory())) {
					new File(_obj_folder).mkdir();
				}
				
				resultFile.createNewFile();
				OutputStream _outstream = new FileOutputStream(_obj_path);	
				
				byte buf[] = Base64Decoder.base64Dec(base64Str);
				f.write(buf);	
				f.writeTo(_outstream);
				_outstream.close();
			} catch (IOException e) {
				System.err.println(e);
			}
		}
//		else if (qName.equals("uof:路径")) {
//			_obj_content = " xlink:href=\"" + _text_node + "\"";  //路径在ODF中是作为某个元素的属性
//		}
		
		//每个元素结束时，要清空_text_node并设置_need_to_store_text
		_text_node = "";   
		_need_to_store_text = false;
	}
	
	public static void process_chars(String chs){
		if (_need_to_store_text) {
			_text_node += chs;			
		}
	}
}
