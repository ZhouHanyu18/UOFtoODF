package object_set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.io.*;

import temp_structs.*;

public class Other_Object {
	
	private static String _text_node = "";   //���ڴ���ı��ڵ������
	private static boolean _need_to_store_text = false;   //��ʶ�Ƿ���Ҫȡ���ı��ڵ��ֵ
	
	private static String _obj_id = "";   //ID����body������ʱ���õ�
	private static String _obj_path = "";
	private static String _obj_type = "";
	
	private static final String _obj_folder = "result/Pictures/";  //�����ŵ�·��
	public Other_Object() {	
		
	}
	
	public static void process_start(String qName,Attributes atts){
		if (qName.equals("uof:��������")) {
			_obj_id = atts.getValue("uof:��ʶ��");
			if (atts.getValue("uof:��������") != null) {
				_obj_type = "." + atts.getValue("uof:��������");
				_obj_path = _obj_folder + _obj_id + _obj_type;
			}
		}
		else if (qName.equals("uof:����")) {
			_need_to_store_text = true;
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("uof:��������")) {
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
		else if (qName.equals("uof:����")) {
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
//		else if (qName.equals("uof:·��")) {
//			_obj_content = " xlink:href=\"" + _text_node + "\"";  //·����ODF������Ϊĳ��Ԫ�ص�����
//		}
		
		//ÿ��Ԫ�ؽ���ʱ��Ҫ���_text_node������_need_to_store_text
		_text_node = "";   
		_need_to_store_text = false;
	}
	
	public static void process_chars(String chs){
		if (_need_to_store_text) {
			_text_node += chs;			
		}
	}
}
