package text;

import java.util.TreeMap;
import java.util.Map;

import org.xml.sax.Attributes;

/**
 * Process <字:批注> element, and store
 * <office:annotation> element and its 
 * id in the TreeMap
 * @author xie
 *
 */
public class Annotation {
	//store the result
	private static String _result = ""; 	
	//tag for text content
	private static boolean _text_content_tag = false;
	//annoID for current <office:annotation>
	private static String _current_annoID = "";			
	//store (annoID,<office:annotation>-ele) pairs
	private static Map<String,String> 
		_anno_map = new TreeMap<String,String>();
	
	//initialize
	public static void init(){
		_anno_map.clear();
	}
	
	private static void clear(){
		_result = "";   
		_current_annoID = "";
	}
	
	//return the <office:annotation> element.Invoked in Text_p
	public static String get_anno(String id){
		return _anno_map.get(id);
	}
	
	public static void process_start(String qName,Attributes atts){
		String att_val = "";
		
		if(_text_content_tag){
			Text_Content.process_start(qName,atts);
		}

		else if(qName.equals("字:批注")){
			_text_content_tag = true;
			_current_annoID = atts.getValue("字:区域引用");
			
			String ele = "";
			ele = "<office:annotation>";
			if((att_val=atts.getValue("字:作者"))!=null){
				ele += "<dc:creator>" + att_val + "</dc:creator>";
			}
			if((att_val=atts.getValue("字:日期"))!=null){
				ele += "<dc:date>" + att_val + "</dc:date>";
			}
			
			_result += ele;
		}		
	}
	
	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		}
	}
	
	//处理元素结束标签
	public static void process_end(String qName){
		
		if(qName.equals("字:批注")){
			_text_content_tag = false;
			_result += Text_Content.get_result();
			_result += "</office:annotation>";
			
			_anno_map.put(_current_annoID,_result);
			
			
			clear();  
		}	
		else if(_text_content_tag){
			Text_Content.process_end(qName);
		}
	}
}
