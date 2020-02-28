package style_set;

import org.xml.sax.Attributes;

import convertor.IDGenerator;

import temp_structs.*;

public class AutoNum_Set {
	
	private static String _chs = ""; 
	//The result
	private static String _result = ""; 
	//The level element's name
	private static String _level_qName = "";
	//Attributes for <_level_qName>
	private static String _level_atts = "";	
	//Attributes for <style:list-level-properties>
	private static String _level_pro = "";
	//Attributes for <style:text-properties>
	private static String _text_pro = ""; 
	//The indent type of UOF:"left" or "first"
	private static String _indent_type = "";
	//Indent value for <字:左>
	private static float _indent_left = 0.0f;
	//tag for <字:符号字体>
	private static boolean _sent_tag = false;
	//name of the text-style
	private static String _text_style_name = "";	
	
	
	private static void clear(){
		_level_qName = "";
		_level_atts = "";
		_level_pro = "";
		_text_pro = "";
		_indent_type = "";
		_indent_left = 0.0f;
		_text_style_name = "";
	}
	
	//Get an entire level element
	private static String get_level(){
		String level = "";
		String name = "";
		
		name = commit_text_style();
		if(!name.equals("")){
			_level_atts += " text:style-name=\"" + name + "\"";
		}
		
		if(_level_qName.equals("")){
			//The default element name
			_level_qName = "text:list-level-style-number";
		}
		
		level = "<" + _level_qName + _level_atts + ">";
		level += "<style:list-level-properties" + _level_pro + "/>";
		if(!_text_pro.equals("")){
			level += "<style:text-properties" + _text_pro + "/>";
		}		
		level += "</" + _level_qName + ">";
		
		clear();
		return level;
	}
	
	private static String commit_text_style(){
		String name = "";
		String style = "";
		
		if(_text_pro.equals("")){
			name = _text_style_name;
		}
		else {
			name = IDGenerator.get_olts_id();
			
			style = "<style:style style:family=\"text\"";
			style += " style:name=\"" + name + "\"";
			if(!_text_style_name.equals("")){
				style += " style:parent-style-name=\"" + _text_style_name + "\"";
			}
			style += ">";
			style += "<style:text-properties" + _text_pro + "/>";
			style += "</style:style>";
		}
		
		Stored_Data.addStylesInStylesXml(style);
		
		return name;
	}
	
	public static String get_reuslt(){
		String rst = "";
		
		rst = _result;
		_result = "";
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if (qName.equals("字:自动编号")){
			_result += "<text:list-style style:name=\"" 
				+ atts.getValue("字:标识符") + "\">"; 
		}
		
		else if (qName.equals("字:级别")) {
			attVal = atts.getValue("字:级别值");
			attVal = (attVal==null) ? "0" : attVal;
			int level = Integer.parseInt(attVal) + 1;
			_level_atts += " text:level=\"" + level + "\"";
			
			attVal = atts.getValue("字:编号对齐方式");
			attVal = (attVal==null) ? "left" : attVal;
			_level_pro += " fo:text-align=\"" + attVal + "\"";
		}

		else if(_sent_tag){
			Sent_Style.process_start(qName,atts);
		}
		else if (qName.equals("字:符号字体")) {
			attVal = atts.getValue("字:式样引用");		
			if(attVal != null){
				_text_style_name = attVal;
			}		
			_sent_tag = true;
		}
/*		
		else if(qName.equals("字:字体")){
			//The default font-name is StarSymbol
			attVal = atts.getValue("字:西文字体引用");
			attVal = (attVal==null) ? "StarSymbol" : attVal;
			_text_pro += " style:font-name=\"" + attVal + "\"";
		}
	*/	
		else if(qName.equals("字:左")){
			_indent_type = "left";
		}
		else if(qName.equals("字:首行")){
			_indent_type = "first";
		}
		else if(qName.equals("字:绝对")){
			attVal = atts.getValue("字:值");
			attVal = (attVal==null) ? "0" : attVal;
			
			if(_indent_type.equals("left")){
				_indent_left = Float.parseFloat(attVal);
			}
			else if(_indent_type.equals("first")){
				//todo
			}
		}
	}
	
	public static void process_chars(String chs){
		if(_sent_tag){
			Sent_Style.process_chars(chs);
		}
		else {
			_chs = chs;			
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("字:自动编号")){
			_result += "</text:list-style>";
		}
		
		else if (qName.equals("字:级别")) {
			_result += get_level();
		}
		
		else if(_sent_tag){
			Sent_Style.process_end(qName);
			if(qName.equals("字:符号字体")){
				_sent_tag = false;
				_text_pro = Sent_Style.get_text_pro();
			}
		}
		else if (qName.equals("字:项目符号")) {
			if(!_chs.equals("")){
				_level_qName = "text:list-level-style-bullet";
				_level_atts += " text:bullet-char=\"" + _chs + "\"";
			}
		}
		
		else if (qName.equals("字:编号格式")) {
			_level_atts += " style:num-format=\"" + Common_Pro.conv_num_format(_chs) + "\"";
		}
		
		else if(qName.equals("字:编号格式表示")){
			if(!parse_prefix(_chs).equals("")){
				_level_atts += " style:num-prefix=\"" + parse_prefix(_chs) + "\"";
			}
			
			if(!parse_suffix(_chs).equals("")){
				_level_atts += " style:num-suffix=\"" + parse_suffix(_chs) + "\"";
			}
			
			int levels = display_levels(_chs);
			if(levels != 0){
				_level_atts += " text:display-levels=\"" + levels + "\"";
			}
		}
		
		else if (qName.equals("字:图片符号引用")) {
			if(!_chs.equals("")){
				_level_qName = "text:list-level-style-image";
				_level_atts += " xlink:href=\"" 
					+ Object_Set_Data.getOtherObj(_chs) + "\"";
				_level_atts += " xlink:type=\"simple\"" +
						" xlink:show=\"embed\" xlink:actuate=\"onLoad\"";
				
				//For <text:list-level-style-image>, the width and 
				//height attributes are needed
				_level_pro += " fo:width=\"9.01pt\" fo:height=\"9.01pt\"";
			}
		}
		
		else if (qName.equals("字:缩进")){
			float space = _indent_left;
			_level_pro += " text:space-before=\"" + space + Common_Data.get_unit() + "\"";		
		}
		
		else if (qName.equals("字:制表符位置")){
			float tab = 0.0f;
			float labelWid = 18;
			float labelDis = 0.0f;
			_chs = (_chs.equals("")) ? "0" : _chs;		
			tab = Float.parseFloat(_chs);

			_level_pro += " text:min-label-width=\"" 
				+ labelWid + Common_Data.get_unit() + "\"";
			
			labelDis = tab - _indent_left - labelWid;
			_level_pro += " text:min-label-distance=\"" 
				+ labelDis + Common_Data.get_unit() + "\"";
		}
		
		else if (qName.equals("字:起始编号")) {
			if(!_chs.equals("")){
				_level_atts += " text:start-value=\"" + _chs + "\"";
			}
		}
		
		_chs = "";   
	}
	
	//Parse the style:num-prefix attribute for ODF
	//from a string like "<prefix>%1.%2<suffix>"
	private static String parse_prefix(String val){
		String prefix = "";
		int index = 0;
		
		index = val.indexOf("%");
		
		if(index == -1){
			prefix = "";
		}else{
			prefix = val.substring(0,index);
		}
		
		return prefix;
	}
	
	//Parse the style:num-suffix attribute for ODF
	//from a string like "<prefix>%1.%2<suffix>"
	private static String parse_suffix(String val){
		String suffix = "";
		int index = 0;
		
		index = val.lastIndexOf("%");
		if(index == -1){
			suffix = "";
		}else {
			suffix = val.substring(index+2);
		}
		
		return suffix;
	}
	
	//Parse the style:display-levels attribute for odf
	private static int display_levels(String val){
		int count = 0;
		
		for(int i = 0; i < val.length(); i ++){
			if(val.charAt(i) == '%'){
				count ++;
			}
		}
		return count;
	}
}
