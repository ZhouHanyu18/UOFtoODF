package object_set;

import org.xml.sax.Attributes;

public class Formula {

	private static String _text_node = "";   //���ڴ���ı��ڵ������
	private static boolean _need_to_store_text = false;   //��ʶ�Ƿ���Ҫȡ���ı��ڵ��ֵ
	
	public Formula() {
		
	}
	
	public static void process_start(String qName,Attributes atts){
		//To do.
	}
	
	public static void process_end(String qName){
		//To do.
		
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
