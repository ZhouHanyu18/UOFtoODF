package style_set;

import java.util.*;

import org.xml.sax.Attributes;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;
import convertor.IDGenerator;

/**
 * Create a <style:style> which style:family
 * is "table" for <uof:文字表式样> / <字:文字表属性>
 * in <text> or <表:工作表> in <spreadsheet>
 * @author xie
 *
 */
public class Table_Style {
	private static String _chs = "";

	//attributes of <style:style>
	private static String _ele_atts = "";
	//
	private static String _table_pro = "";
	//"default" type of table-style in UOF,
	//this type of style goes to styles.xml
	private static boolean _is_default = false;
	//table name
	private static String _table_id = "";
	//parent table name
	private static String _parent = "";
	//
	private static int _table_counter = 0;
	//store the result
	private static String _table_styles = "";
	//store <table coounter,table id>
	//pairs for <spreadsheet>
	private static Map<Integer,String>
		_sheet_tab_map = new TreeMap<Integer,String>();
	//Store <table:column>-elements associated
	// with a table id.For <text>
	private static Map<String,String>
		_table_cols = new TreeMap<String,String>();


	//initialize
	public static void init(){
		_table_counter = 0;
	}

	private static void clear(){
		_ele_atts = "";
		_table_pro = "";
		_is_default = false;
		_table_id = "";
		_parent = "";
	}

	//Return <table:column>-elements for the specified table
	public static String get_columns(String tableID){
		return _table_cols.get(tableID);
	}

	public static String get_tab_id(int counter){
		return _sheet_tab_map.get(counter);
	}

	private static String get_one_style(){
		String rst = "";

		rst = "<style:style style:family=\"table\"" + _ele_atts + ">";
		rst += "<style:table-properties" + _table_pro + "/>";
		rst += "</style:style>";

		clear();
		return rst;
	}

	public static String get_result(){
		String rst = "";

		if(Common_Data.get_file_type().equals("text")){
			rst = _table_styles;
		}
		else if(Common_Data.get_file_type().equals("spreadsheet")){
			rst = _table_styles;
		}

		_table_styles = "";
		return rst;
	}

	public static void process_sheet_atts(String qName, Attributes atts){
		String attVal = "";
		String tableAtt = "";
		String styleName = "";

		if(qName.equals("表:工作表")){
			_table_counter ++;
			styleName = "ta" + _table_counter;

			if((attVal=atts.getValue("表:隐藏"))!=null){
				if(attVal.equals("false")){
					tableAtt += " table:display=\"true\"";
				}else{
					tableAtt += " table:display=\"false\"";
				}
			}

			if((attVal=atts.getValue("表:背景"))!=null){
				tableAtt += " fo:background-color=\""
					+ attVal + "\"";
			}

			if(!tableAtt.equals("")){
				String style = "";
				style = "<style:style style:name=\"" + styleName
				+ "\" style:family=\"table\">";
				style += "<style:table-properties" + tableAtt + "/>";
				style += "</style:style>";

				_table_styles += style;
				_sheet_tab_map.put(_table_counter, styleName);
			}
		}
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if (qName.equals("uof:文字表式样")) {
			attVal = atts.getValue("字:类型");
			attVal = (attVal==null) ? "" : attVal;

			if(attVal.equals("default") || attVal.equals("custom")){
				_is_default = true;
			}

			if ((attVal = atts.getValue("字:标识符")) != null){
				_ele_atts += " style:name=\"" + attVal + "\"";
				_table_id = attVal;
			}
			if ((attVal = atts.getValue("字:基式样引用")) != null){
				_ele_atts += " style:parent-style-name=\"" + attVal + "\"";
				_parent = attVal;
			}
			if((attVal = atts.getValue("字:别名")) != null){
				_ele_atts += " style:display-name=\"" + attVal + "\"";
			}
		}
		else if(qName.equals("字:文字表属性")){
			_table_id = IDGenerator.get_table_id();
			_ele_atts += " style:name=\"" + _table_id + "\"";

			if ((attVal = atts.getValue("字:式样引用")) != null){
				_ele_atts += " style:parent-style-name=\"" + attVal + "\"";
				_parent = "";
			}
		}
		else if (qName.equals("字:宽度")) {
			if((attVal=atts.getValue("字:绝对宽度")) != null){
				_table_pro += " style:width=\"" + attVal
						+ Common_Data.get_unit() + "\"";
			}
			//To do:相对宽度两个标准的表示法不一样
			if (atts.getValue("字:相对宽度") != null)
				_table_pro += " style:rel-width=\""
					+ atts.getValue("字:相对宽度") + "\"";
		}
		else if (qName.equals("字:列宽")) {
			//nothing todo
		}
		else if (qName.equals("字:对齐")) {

		}
		else if (qName.equals("字:边距")) {
			if ((attVal=atts.getValue("字:上")) != null){
				_table_pro += " fo:margin-top=\"" + attVal + "\"";
			}
			else if ((attVal=atts.getValue("字:左")) != null){
				//_table_pro += " fo:margin-left=\"" + attVal + "\"";
			}
			else if ((attVal=atts.getValue("字:右")) != null){
				_table_pro += " fo:margin-right=\"" + attVal + "\"";
			}
			else if ((attVal=atts.getValue("字:下")) != null){
				_table_pro += " fo:margin-bottom=\"" + attVal + "\"";
			}
		}

		else if (qName.equals("字:图片")) {
			//似乎无对应
		}
		else if (qName.equals("字:图案")) {
			//似乎无对应
		}
		else if (qName.equals("字:渐变")) {
			//似乎无对应
		}
	}

	public static void process_chars(String chs){
		_chs = chs;
	}

	public static void process_end(String qName){
		if (qName.equals("uof:文字表式样") || qName.equals("字:文字表属性")) {
			//store <table:column> elements for the table
			String cols = Table_Column.get_table_cols();
			if(cols.equals("") && !_parent.equals("")){
				//get the parent table's <table:column>s
				cols = _table_cols.get(_parent);
				cols = (cols==null) ? "" : cols;
			}

			_table_cols.put(_table_id, cols);

			if(_is_default){
				//"default" type of style goes to styles.xml
				Stored_Data.addStylesInStylesXml(get_one_style());
			}else {
				//store one table-style
				_table_styles += get_one_style();
			}
		}
		else if(qName.equals("字:左缩进")){
			_table_pro +=" fo:margin-left=\""
				+ _chs + Common_Data.get_unit()+ "\"";
		}
		else if (qName.equals("字:列宽")) {
			Table_Column.process_text_col(_chs);
		}
		else if (qName.equals("字:对齐")) {
			_table_pro += " table:align=\"" + _chs + "\"";
		}
		else if (qName.equals("字:颜色")) {
			_table_pro += " fo:background-color=\"" + _chs + "\"";
		}

		_chs = "";
	}
}
