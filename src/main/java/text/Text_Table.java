package text;

import org.xml.sax.Attributes;
import java.util.Stack;

import style_set.Table_Row;
import style_set.Table_Style;
import convertor.IDGenerator;

public class Text_Table {
	//the result
	private static String _result = "";
	//
	private static boolean _filter_tag = false;
	//table:table-header-rows
	private static boolean _is_header_row = false;
	//tag for <text:p>
	private static boolean _para_tag = false;
	//stack for nesting
	private static Stack<String> _stack = new Stack<String>();
	//This counter is corresponding to Table_Row's
	private static int _row_counter = 0;
	//This counter is corresponding to Cell_Pro's
	private static int _cell_counter = 0;
	//<table:covered-table-cell>s
	private static String _covered_cells = "";
	//to stack _covered_cells in case of table's nesting.
	private static Stack<String>
		_covered_cell_stack = new Stack<String>();
	//it's true if<字:单元格属性> is lost in this cell
	private static boolean _no_pro_tag = false;


	//initialize
	public static void init(){
		_cell_counter = 0;
		_row_counter = 0;
	}

	private static void clear(){
		_result = "";
		_covered_cells = "";
	}

	public static String get_result(){
		String str = _result;
		clear();

		return str;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(_filter_tag)		return;

		if(_para_tag){
			_stack.push(qName);
			Text_p.process_start(qName,atts);
		}
		else if(qName.equals("字:文字表")){
			_result += "<table:table>";
		}
		else if(qName.equals("字:行")){
			_result += "<table:table-row>";
		}
		else if(qName.equals("字:表行属性")){
			_row_counter ++;
			String id = Table_Row.get_style_id(_row_counter);
			if(id != null){
				String style = " table:style-name=\"" + id + "\"";
				_result = insert_style(_result, style);
			}
		}
		else if(qName.equals("字:单元格")){
			_no_pro_tag = true;
			_result += "<table:table-cell>";
		}
		else if(qName.equals("字:段落")){
			_stack.push(qName);
			_para_tag = true;
			Text_p.process_start(qName,atts);
		}
		else if(qName.equals("字:单元格属性")){
			_no_pro_tag = false;
			_filter_tag = true;
			_cell_counter ++;

			String attsAndCols = Cell_Pro.get_atts(_cell_counter);
			int ind = attsAndCols.indexOf("|");
			String att = attsAndCols.substring(0,ind);

			if(!att.equals("")){
				_result = insert_style(_result, att);
			}

			//if table:number-columns-spanned > 1, then several
			//<table:covered-table-cell/>-eles should be added.
			String cols = attsAndCols.substring(ind + 1);
			cols = (cols.equals("")) ? "0" : cols;
			int numSpan = Integer.parseInt(cols);
			for(int i = 0; i < numSpan - 1; i ++){
				_covered_cells += "<table:covered-table-cell/>";
			}
			_covered_cell_stack.push(_covered_cells);
			_covered_cells = "";
		}
		else if(qName.equals("字:表头行")){
			_is_header_row = true;
		}
		else if(qName.equals("字:文字表属性")){
			attVal = IDGenerator.get_table_id();
			String style = " table:name=\"" + attVal
						+ "\" table:style-name=\"" + attVal + "\"";
			_result = insert_style(_result, style);
			_result += Table_Style.get_columns(attVal);
		}
	}

	public static void process_chars(String chs){
		if(_filter_tag)		return;

		if(_para_tag){
			Text_p.process_chars(chs);
		}
	}

	public static void process_end(String qName){
		if(_no_pro_tag){
			//to make sure it doesn't go wrong when
			// <字:单元格属性> is lost in the cell
			_covered_cell_stack.push("");
			_no_pro_tag = false;
		}
		else if(qName.equals("字:文字表")){
			_result += "</table:table>";
		}
		else if(qName.equals("字:行")){
			if(_is_header_row){
				insertHeaderRow("<table:table-header-rows>");
				_result += "</table:table-row>";
				_result += "</table:table-header-rows>";
				_is_header_row = false;
			}
			else{
				_result += "</table:table-row>";
			}
		}
		else if(qName.equals("字:单元格")){
			_result += _covered_cell_stack.pop();
			_result += "</table:table-cell>";
		}
		else if(qName.equals("字:单元格属性")){
			_filter_tag = false;
		}
		else if(_para_tag){
			Text_p.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_para_tag = false;
				_result += Text_p.get_result();
			}
		}

		if(_filter_tag) 	return;
	}

	//Insert the table:style-name attribute for the element
	private static String insert_style(String str, String style){
		String rst = "";
		int len = str.length();
		if(!str.endsWith(">")){
			rst = str;
		}
		//get rid of ">"
		rst += str.substring(0,len-1);
		rst += style;
		//add ">"
		rst += ">";

		return rst;
	}

	private static void insertHeaderRow(String str){
		StringBuffer str_buf = new StringBuffer(_result);
		int index = str_buf.lastIndexOf("<table:table-row");

		if(index == -1)	return;
		str_buf = str_buf.insert(index,str);
		_result = str_buf.toString();
	}
}
