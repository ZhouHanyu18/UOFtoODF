package text;

import org.xml.sax.Attributes;

import style_set.Common_Pro;
import temp_structs.Common_Data;

/**
 * 
 * @author xie
 *
 */
public class Text_Config {
	//the result
	private static String _result = "";
		
	public static String get_result(){
		String tc = "";
		
		tc = _result;
		_result = "";
		
		return tc;
	}
	
	public static void process(String qName,Attributes atts){
		String attVal = "";
		String config = "";
		
		if(qName.equals("��:βע����") || qName.equals("��:��ע����")){	
			config = "<text:notes-configuration";
			
			if(qName.equals("��:βע����")){
				config += " text:note-class=\"endnote\"";
			}
			else{
				config += " text:note-class=\"footnote\"";
			}
			
			if((attVal = atts.getValue("��:��ʽ")) != null){
				config += " style:num-format=\"" + Common_Pro.conv_num_format(attVal) + "\"";
			}
			if((attVal = atts.getValue("��:��ʼ���")) != null){
				config += " text:start-value=\"" + (Integer.parseInt(attVal)-1) + "\"";
			}
			if((attVal = atts.getValue("��:λ��")) != null){
				if(attVal.equals("page-bottom")){
					config += " text:footnotes-position=\"page\"";
				}
				else if(attVal.equals("below-text")){
					config += " text:footnotes-position=\"text\"";
				}
			}
			config += "/>";
			
			_result += config;
		}
		
		else if(qName.equals("��:�к�����")){
			config = "<text:linenumbering-configuration";
			
			attVal = atts.getValue("��:ʹ���к�");
			attVal = (attVal==null) ? "false" :attVal;
			config += " text:number-lines=\"" + attVal + "\"";
			
			if((attVal = atts.getValue("��:��߽�")) != null){
				config += " text:offset=\"" + attVal + Common_Data.get_unit() + "\"";
			}
			
			if((attVal = atts.getValue("��:��ʼ���")) != null){
				config += " text:start-value=\"" + (Integer.parseInt(attVal)-1) + "\"";
			}
			
			config += " text:number-position=\"left\"";
			
			if((attVal = atts.getValue("��:�кż��")) != null){
				config += " text:increment=\"" + attVal + "\"";
			}
			
			config += "/>";
			
			_result += config;
		}
	}
}
