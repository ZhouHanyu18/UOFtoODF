package presentation;

import org.xml.sax.Attributes;

import style_set.Common_Pro;

/**
 * 
 * @author xie
 *
 */
public class Page_Layout_p extends Common_Pro{
	//
	private static String _chs = "";
	//page-layout properties
	private static String _pl_pros = ""; 
	//@style:name
	private static String _id = "";	
	//the result
	private static String _result = "";
	
	
	protected static void clear(){
		_chs = "";
		_pl_pros = "";
		_id = "";
	}
	
	private static String get_one_PL(){
		String str = "";
		
		str = "<style:page-layout style:name=\"" + _id + "\">";
		str += "<style:page-layout-properties" + _pl_pros + "/>";	
		str += "</style:page-layout>";
		
		clear(); 
		return str;
	} 
	
	public static String get_result(){
		String pls = "";
		
		pls = _result;
		_result = "";
		
		return pls;
	}
	
	public static void process_start(String qName,Attributes atts){
		if(qName.equals("演:页面设置")){
			_id = atts.getValue("演:标识符");
		}
		else if(qName.equals("演:纸张")){
			_pl_pros += get_page(atts);
		}
		else if(qName.equals("演:页边距")){
			_pl_pros += get_margins(atts);
		}
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){
		if(qName.equals("演:纸张方向")){
			_pl_pros += " style:print-orientation=\"" + _chs + "\"";
		}
		else if(qName.equals("演:页码格式")){   
			_pl_pros += " style:num-format\"" + conv_num_format(_chs) + "\"";
		}
		else if(qName.equals("演:页面设置")){
			_result += get_one_PL();
		}
		_chs = "";
	}
}

