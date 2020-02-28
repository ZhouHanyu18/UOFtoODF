package object_set;

import org.xml.sax.Attributes;

public class Formula {

	private static String _text_node = "";   //用于存放文本节点的内容
	private static boolean _need_to_store_text = false;   //标识是否需要取出文本节点的值
	
	public Formula() {
		
	}
	
	public static void process_start(String qName,Attributes atts){
		//To do.
	}
	
	public static void process_end(String qName){
		//To do.
		
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
