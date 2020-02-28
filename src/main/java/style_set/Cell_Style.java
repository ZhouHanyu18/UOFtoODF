package style_set;

import org.xml.sax.Attributes;
import convertor.IDGenerator;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;


public class Cell_Style extends Common_Pro{
	private static String _chs = "";
	//attributes for <style:style>
	private static String _ele_atts = "";
	//attributes for <style:table-cell-properties>
	private static String _cell_pro = "";
	//attributes for <style:paragraph-properties>
	private static String _para_pro = "";
	//attributes for style:text-properties
	private static String _text_pro = "";
	//
	private static String _number_style = "";
	//type of style
	private static boolean _is_default = false;
	//tag for <字体格式>
	private static boolean _text_pro_tag = false;
	//tag for border
	private static boolean _border_tag = false;

	private static void clear(){
		_chs = "";
		_ele_atts = "";
		_cell_pro = "";
		_para_pro = "";
		_text_pro = "";
		_number_style = "";
		_is_default = false;
	}

	//store the result
	public static void commit_result(){
		String result = "";

		result += _number_style;
		result += "<style:style style:family=\"table-cell\"" + _ele_atts + ">";

		if(!_cell_pro.equals("")){
			result += "<style:table-cell-properties" + _cell_pro + "/>";
		}
		if(!_para_pro.equals("")){
			result += "<style:paragraph-properties" + _para_pro + "/>";
		}
		if(!_text_pro.equals("")){
			result += "<style:text-properties" + _text_pro + "/>";
		}

		result += "</style:style>";

		if (_is_default){
			Stored_Data.addStylesInStylesXml(result);
		} else{
			Stored_Data.addAutoStylesInContentXml(result);
		}

		clear();
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if (_text_pro_tag){
			Sent_Style.process_start(qName,atts);
		}

		if (qName.equals("uof:单元格式样")) {
			attVal = atts.getValue("表:类型");
			attVal = (attVal==null) ? "" : attVal;
			//The default style goes to styles.xml
			if (attVal.equals("default") || attVal.equals("custom")){
				_is_default = true;
			}

			attVal = atts.getValue("表:标识符");
			_ele_atts += " style:name=\"" + attVal + "\"";

			if((attVal=atts.getValue("表:基式样引用")) != null){
				_ele_atts += " style:parent-style-name=\"" + attVal + "\"";
			}
		}
		else if (qName.equals("表:字体格式")) {
			_text_pro_tag = true;

			if (atts.getValue("表:式样引用") != null) {
				/* _text_pro += */
			}
		}

		else if (qName.equals("表:水平对齐方式")) {
			_cell_pro += " style:text-align-source=\"fix\"";
		}

		else if (qName.equals("表:自动换行")) {
			attVal = atts.getValue("表:值");
			if(attVal != null){
				String val = (attVal.equals("true"))?"wrap":"no-wrap";
				_cell_pro += " fo:wrap-option=\"" + val + "\"";
			}
		}
		else if (qName.equals("表:缩小字体填充")) {
			attVal = atts.getValue("表:值");
			if(attVal != null){
				_cell_pro += " style:shrink-to-fit=\"" + attVal + "\"";
			}
		}

		//===边框的子元素===
		else if(_border_tag){
			_cell_pro += get_borders(qName, atts);
		}
		else if(qName.equals("表:边框")){
			_border_tag = true;
		}

		else if (qName.equals("表:数字格式")) {
			String type = atts.getValue("表:分类名称");
			String styleCode = atts.getValue("表:格式码");

//			Data_Type.add_data_type(_style_name,type);
			String name = IDGenerator.get_number_id();
			_number_style = Number_Style.process_style(name,type,styleCode);
			if(!_number_style.equals("")){
				_ele_atts += " style:data-style-name=\"" + name + "\"";
			}
		}

		else if(qName.equals("图:图片")){
			//todo..
		}
	}

	public static void process_chars(String chs){
		if (_text_pro_tag){
			Sent_Style.process_chars(chs);
		}
		else {
			_chs = chs;
		}
	}

	public static void process_end(String qName){

		if (qName.equals("uof:单元格式样")) {
			commit_result();
		}
		else if (qName.equals("表:字体格式")) {
			_text_pro += Sent_Style.get_text_pro();
			_text_pro_tag = false;
		}
		else if (_text_pro_tag){
			Sent_Style.process_end(qName);
		}
		else if(qName.equals("表:边框")){
			_border_tag = false;
		}
		else if (qName.equals("表:水平对齐方式")) {
			if(_chs.equals("fill")){
				_cell_pro += " style:repeat-content=\"true\"";
			}
			if(!conv_text_align(_chs).equals("")){
				_para_pro += " fo:text-align=\"" + conv_text_align(_chs) + "\"";
			}
		}
		//<表:缩进>的单位为一个字符宽度（暂定为10pt）
		else if (qName.equals("表:缩进")) {
			float fval = Integer.parseInt(_chs) * 10.0f;
			_para_pro += " fo:margin-left=\"" + fval+ Common_Data.get_unit() + "\"";

		}
		else if (qName.equals("表:垂直对齐方式")) {
			_cell_pro += " style:vertical-align=\"" + conv_vertical_align(_chs) + "\"";
		}

		else if (qName.equals("表:文字方向")) {
			if (_chs.equals("horizontal")){
				_cell_pro += " style:direction=\"ltr\"";
			}
			else{
				_cell_pro += " style:direction=\"ttb\"";
			}
		}
		else if (qName.equals("表:文字旋转角度")) {
			_cell_pro += " style:rotation-angle=\"" + conv_rotation_angle( _chs) + "\"";
			_cell_pro += " style:rotation-align=\"none\"";

		}
		else if(qName.equals("图:颜色")){
			_cell_pro += " fo:background-color=\"" + _chs + "\"";
		}

		_chs = "";
	}
	private static String conv_text_align(String val){
		String convVal = "";

		if(val.equals("left")){
			convVal = "start";
		}
		else if(val.equals("right")){
			convVal = "end";
		}
		else if(val.equals("justify")){
			convVal = "justify";
		}
		else if(val.equals("center")){
			convVal = "center";
		}

		return convVal;
	}

	private static String conv_vertical_align(String val){
		String convVal = "automatic";

		if(val.equals("top")){
			convVal = "top";
		}
		else if(val.equals("center")){
			convVal = "middle";
		}
		else if(val.equals("bottom")){
			convVal = "bottom";
		}

		return convVal;
	}

	//-90~90>>>0~360
	private static String conv_rotation_angle(String val){
		int i = Integer.parseInt(val);

		if(i < 0){
			i = 360 + i;
		}

		return String.valueOf(i);
	}
}
