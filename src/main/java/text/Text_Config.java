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

		if(qName.equals("字:尾注设置") || qName.equals("字:脚注设置")){
			config = "<text:notes-configuration";

			if(qName.equals("字:尾注设置")){
				config += " text:note-class=\"endnote\"";
			}
			else{
				config += " text:note-class=\"footnote\"";
			}

			if((attVal = atts.getValue("字:格式")) != null){
				config += " style:num-format=\"" + Common_Pro.conv_num_format(attVal) + "\"";
			}
			if((attVal = atts.getValue("字:起始编号")) != null){
				config += " text:start-value=\"" + (Integer.parseInt(attVal)-1) + "\"";
			}
			if((attVal = atts.getValue("字:位置")) != null){
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

		else if(qName.equals("字:行号设置")){
			config = "<text:linenumbering-configuration";

			attVal = atts.getValue("字:使用行号");
			attVal = (attVal==null) ? "false" :attVal;
			config += " text:number-lines=\"" + attVal + "\"";

			if((attVal = atts.getValue("字:距边界")) != null){
				config += " text:offset=\"" + attVal + Common_Data.get_unit() + "\"";
			}

			if((attVal = atts.getValue("字:起始编号")) != null){
				config += " text:start-value=\"" + (Integer.parseInt(attVal)-1) + "\"";
			}

			config += " text:number-position=\"left\"";

			if((attVal = atts.getValue("字:行号间隔")) != null){
				config += " text:increment=\"" + attVal + "\"";
			}

			config += "/>";

			_result += config;
		}
	}
}
