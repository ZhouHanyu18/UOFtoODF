package spreadsheet;

import java.util.ArrayList;
import java.util.Iterator;
import org.xml.sax.Attributes;

import temp_structs.Object_Set_Data;
import text.HyperLink;
//import style_set.Data_Type;
import style_set.Date_Time;

public class Table_Cell {
	private static String _chs = "";
	//name of current table
	private static String _table_name = "";
	//上一个单元格列号
	private static int _pre_col_num = 0;
	//列号
	private static int _col_num = 0;
	//行号
	private static int _row_num = 0;
	//attributes needed by <table:cell>
	private static String _cell_atts = "";
	//formula
	private static String _formula = "";
	//The data type of this cell
	private static String _value_type = "";
	//content of cell without tag
	private static String _cell_data = "";
	//content of cell with complete needed tag
	private static String _para_data = "";
	//Style name of <text:span>
	private static String _span_style = "";
	//empty table cells
	private static String _empty_cell = "";
	//Tag for <字:文本串>
	private static boolean _span_tag = false;
	//hyperlink in table
	private static String _link_ref = "";
	//annotation in table
	private static String _annotation = "";
	//
	private static ArrayList<Cell_Range_Struct>
		_cell_valids = new ArrayList<Cell_Range_Struct>();


	//initialize
	public static void init(){
		_cell_valids.clear();
	}

	private static void clear(){
		_cell_atts = "";
		_formula = "";
		_value_type = "";
		_cell_data = "";
		_para_data = "";
		_span_style = "";
		_empty_cell = "";
		_link_ref = "";
		_annotation = "";
	}

	public static String get_result(){
		String cell = "";
		double digits = 0;

		try{
			digits = Double.parseDouble(_cell_data);
		}catch(Exception e){
			digits = 0;
		}

		if(_formula.length()!=0){
			_cell_atts += " table:formula=\"" + _formula + "\"";
		}
		if(_value_type.length()!=0){
			_cell_atts += " office:value-type=\"" + _value_type + "\"";
		}

		if(_value_type.equals("float")){
			_cell_atts += " office:value=\"" + _cell_data + "\"";
		}
		else if(_value_type.equals("date")){
			_cell_atts += " office:date-value=\"" + Date_Time.convert_date(digits) + "\"";
		}
		else if(_value_type.equals("time")){
			_cell_atts += " office:time-value=\"" + Date_Time.convert_time(digits) + "\"";
		}

		cell += _empty_cell;

		String linkAtt = HyperLink.get_link_atts(_link_ref);
		if(linkAtt != null){
			_para_data = "<text:a" + linkAtt + ">" + _para_data + "</text:a>";
		}
		if(_para_data.equals("") && _annotation.equals("")){
			cell += "<table:table-cell" + _cell_atts + "/>";
		}
		else {
			cell += "<table:table-cell" + _cell_atts + ">";
			cell += _annotation;
			cell += "<text:p>" + _para_data + "</text:p>";
			cell += "</table:table-cell>";
		}

		clear();
		return cell;
	}

	public static void see_new_row(int rowNum){
		_pre_col_num = 0;
		_col_num = 0;
		_row_num = rowNum;
	}

	public static int get_col_num(){
		return _col_num;
	}

	public static void add_cell_range(Cell_Range_Struct valid){
		_cell_valids.add(valid);
	}

	//Given the cell address, decide which cell range
	//the address is in. Return the validation name or
	//cell style name the cell range belongs to.
	private static String in_cell_range(int col, int row){
		String name = "";

		for(Iterator<Cell_Range_Struct> it = _cell_valids.iterator(); it.hasNext();){
			Cell_Range_Struct v = it.next();
			if(v.get_table_name().equals(_table_name) && v.in_range(col, row)){
				name = v.get_valid_name();
				if(name.equals("")){
					name = v.get_style_name();
				}
				break;
			}
		}
		return name;
	}

	//Given the column num, decide which cell range
	//the address is in. Return the validation name or
	//cell style name the cell range belongs to.
	public static String in_col_range(int col, String tableName){
		String name = "";

		for(Iterator<Cell_Range_Struct> it = _cell_valids.iterator(); it.hasNext();){
			Cell_Range_Struct v = it.next();

			if(v.get_table_name().equals(tableName) && v.in_col_range(col)){
				name = v.get_style_name();
				//"SN" is not needed here
				name = name.substring(2);
				break;
			}
		}
		return name;
	}

	public static void set_table_name(String name){
		_table_name = name;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("表:单元格")){
			boolean isSet = false;
			String attName = "";

			if((attVal=atts.getValue("表:列号"))!=null){
				_col_num = Integer.parseInt(attVal);
			}
			else{
				_col_num ++;
			}

			attName = in_cell_range(_col_num, _row_num);
			isSet = attName.startsWith("SN");
			if(!attName.equals("")){
				_cell_atts += VN_or_SN_att(attName);
			}

			if(_col_num > (_pre_col_num + 1)){
				int empCount = 0;

				for(int i = _pre_col_num+1; i < _col_num; i++){
					attName = in_cell_range(i, _row_num);
					isSet = attName.startsWith("SN");

					if(!attName.equals("")){
						_empty_cell += print_empty_cells(empCount);
						_empty_cell += "<table:table-cell" + VN_or_SN_att(attName) + "/>";
						empCount = 0;
					}else{
						empCount ++;
					}
				}

				_empty_cell += print_empty_cells(empCount);
			}

			if((attVal=atts.getValue("表:式样引用"))!=null && !isSet){
				_cell_atts += " table:style-name=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("表:合并列数"))!=null){
				_cell_atts += " table:number-columns-spanned=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("表:合并行数"))!=null){
				_cell_atts += " table:number-rows-spanned=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("表:超链接引用"))!=null){
				_link_ref = attVal;
			}

			_pre_col_num = _col_num;
		}

		else if(qName.equals("表:数据")){
			attVal=atts.getValue("表:数据类型");

			if(_value_type.equals("") && attVal != null){
				_value_type = attVal;
			}
			_value_type = conv_value_type(_value_type);
		}

		else if(qName.equals("字:文本串")){
			_span_tag = true;
		}

		else if(qName.equals("字:句属性")){
			if((attVal=atts.getValue("字:式样引用"))!=null){
				_span_style = attVal;
			}
		}

		else if(qName.equals("字:区域开始")){
			attVal = atts.getValue("字:标识符");
			_para_data += "<text:a" + HyperLink.get_link_atts(attVal) + ">";
		}

		else if(qName.equals("字:区域结束")){
			_para_data += "</text:a>";
		}

		else if(qName.equals("uof:锚点")){	//shapes in annotation
			if((attVal=atts.getValue("uof:图形引用")) != null){
				if(Object_Set_Data.getDrawing(attVal) != null){
					_annotation = Object_Set_Data.getDrawing(attVal);
				}
			}
		}
	}

	public static void process_chars(String chs){
		if(_span_tag){
			_cell_data = chs;

			if(!_span_style.equals("")){
				_para_data += "<text:span text:style-name=\"" + _span_style + "\">" ;
				_para_data += chs + "</text:span>";
			}
			else{
				_para_data += chs;
			}
		}else{
			_chs = chs;
		}
	}

	public static void process_end(String qName){
		if(qName.equals("表:公式")){
			_formula = "oooc:" + Formula.get_formula(_chs.trim());
		}

		else if(qName.equals("字:文本串")){
			_span_tag = false;
		}

		_chs = "";
	}

	//return the attribute
	private static String VN_or_SN_att(String name){
		String att = "";

		if(name.startsWith("VN")){
			att = " table:content-validation-name=\"" + name.substring(2) + "\"";
		}
		else if(name.startsWith("SN")){
			att = " table:style-name=\"" + name.substring(2) + "\"";
		}

		return att;
	}

	private static String print_empty_cells(int count){
		String emptys = "";

		if(count == 1){
			emptys = "<table:table-cell/>";
		}
		else if(count > 1){
			emptys = "<table:table-cell";
			emptys += " table:number-columns-repeated=\"" + count + "\"";
			emptys += "/>";
		}

		return emptys;
	}

	private static String conv_value_type(String oldType){
		String newType = "string";

		if(oldType.equals("text")){
			newType = "string";
		}
		else if(oldType.equals("number")){
			newType = "float";
		}
		else if(oldType.equals("percentage")){
			newType = "percentage";
		}
		else if(oldType.equals("currency")){
			newType = "currency";
		}
		else if(oldType.equals("date")){
			newType =  "date";
		}
		else if(oldType.equals("boolean")){
			newType = "boolean";
		}
		else if(oldType.equals("time")){
			newType = "time";
		}

		return newType;
	}
}
