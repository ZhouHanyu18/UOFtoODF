package spreadsheet;

import org.xml.sax.Attributes;
import temp_structs.Spreadsheet_Data;
import style_set.Table_Row;
import style_set.Table_Column;
import style_set.Table_Style;

public class Sheet_Table {
	//the result
	private static String _result = "";
	//table name
	private static String _table_name = "";	
	//行计数，用于取得行的style-name
	private static int _row_counter = 0;	
	//列计数，用于取得列的style-name
	private static int _col_counter = 0;	
	//表计数，用于取得表的style-name
	private static int _tab_counter = 0;				
	//当前列号
	private static int _cur_col_num = 0;
	//当前行号
	private static int _cur_row_num = 0;				
	//tag for <表:单元格>
	private static boolean _table_cell_tag = false;	
	//tag for <表:筛选>
	private static boolean _table_filter_tag =false;
	//tag for filtretiion
	private static boolean _filter_tag = false;	
	//the max column number
	private static int _max_col_num = 0;
	//contains 表:列-ele or not
	private static boolean _has_col_ele = false;
	
	//initialize
	public static void init(){
		_row_counter = 0;
		_col_counter = 0;
		_tab_counter = 0;
	}
	
	public static void set_has_col(boolean bval){
		_has_col_ele = bval;
	}
	
	private static void clear(){
		_result = "";
		_table_name = "";
		_cur_col_num = 0;
		_cur_row_num = 0;
		_max_col_num = 0;
		_has_col_ele = false;
	}
	
	public static String get_result(){
		String result = _result;
		clear();
		return result;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String ID = "";
					
		if(_filter_tag){
			return;		
		}
		else if(qName.equals("表:分组集")||qName.equals("表:工作表属性")){
			_filter_tag = true;
		}
		
		else if(_table_filter_tag){
			Table_Filter.process_start(qName,atts);
		}
		else if(qName.equals("表:筛选")){
			_table_filter_tag = true;
			Table_Filter.process_start(qName, atts);
		}
		
		else if(_table_cell_tag){
			Table_Cell.process_start(qName,atts);
		}
		else if(qName.equals("表:单元格")){
			_table_cell_tag = true;
			Table_Cell.process_start(qName,atts);
		}
		
		else if(qName.equals("表:工作表")){
			String table = "";

			_tab_counter ++;
			_table_name = atts.getValue("表:名称");
			Table_Filter.set_table_id(_table_name);
			Table_Cell.set_table_name(_table_name);
			
			table += "<table:table table:name=\"" + _table_name +"\"";			
			if((ID=Table_Style.get_tab_id(_tab_counter)) != null){
				table += " table:style-name=\"" + ID + "\"";
			}
			table += Name_Expression.get_print_area(_tab_counter-1);
			_result += table + ">";
		}	
		
		else if(qName.equals("表:工作表内容")){
			String shapes = Table_Shapes.get_table_shapes(_table_name);
			
			if(shapes != null && !shapes.equals("")){
				_result += "<table:shapes>" + shapes + "</table:shapes>";
			}
			
			if(!_has_col_ele){
				attVal = atts.getValue("表:最大列");
				_result += empty_default_cols(attVal);
			}
		}
		
		else if(qName.equals("表:列")){ 
			_col_counter ++;
			String column = "";
			
			attVal = atts.getValue("表:列号");			
			if(attVal != null){
				int preColNum = _cur_col_num;
				_cur_col_num = Integer.parseInt(attVal);
				
				column += get_empty_col(_cur_col_num - preColNum - 1);
				while(Spreadsheet_Data.pop_column_start(_cur_col_num)){
					column += "<table:table-column-group>";
				}
			}
			_max_col_num = _cur_col_num;
			
			column += "<table:table-column";
			ID = Table_Column.get_style_id(_col_counter);
			if(ID != null){
				column += " table:style-name=\"" + ID + "\"";
			}
					
			if((attVal=atts.getValue("表:隐藏"))!=null){
				if(attVal.equals("true")){
					column += " table:visibility=\"collapse\"";
				}
			}
			if((attVal=atts.getValue("表:跨度")) != null){
				if(Integer.valueOf(attVal) != 0){
					column += " table:number-columns-repeated=\"" + attVal + "\"";
				}
			}
			
			String defaultCell = atts.getValue("表:式样引用");
			if(defaultCell == null){
				defaultCell = Table_Cell.in_col_range(_cur_col_num,_table_name);
				//this "de_Default" cell style exists in styles.xml
				defaultCell = (defaultCell.equals("")) ? "de_Default" : defaultCell;		
			}
			column += " table:default-cell-style-name=\"" + defaultCell + "\"";			
			column += "/>";
			
			_result += column;
		}
		
		else if(qName.equals("表:行")){
			String row = "";
			_row_counter ++;
			int preRowNum = _cur_row_num;
			
			attVal = atts.getValue("表:行号");			
			if(attVal != null){
				_cur_row_num = Integer.parseInt(attVal);
			}else{
				_cur_row_num ++;					//默认加1
			}		
			Table_Cell.see_new_row(_cur_row_num);
			
			row += get_empty_row(_cur_row_num,preRowNum,_max_col_num);
			
			while(Spreadsheet_Data.pop_row_start(_cur_row_num)){
				row += "<table:table-row-group>";
			}	
			
			row += "<table:table-row";
			ID = Table_Row.get_style_id(_row_counter);
			ID = (ID==null) ? Table_Row._DE_name : ID;
			row += " table:style-name=\"" + ID + "\"";		
			
			if((attVal=atts.getValue("表:隐藏"))!=null){
				if(attVal.equals("true")){
					row += " table:visibility=\"collapse\"";
				}
			}
			if((attVal=atts.getValue("表:跨度"))!=null){
				//todo
			}
			if((attVal=atts.getValue("表:式样引用"))!=null){
				row += " table:default-cell-style-name=\"" + attVal + "\"";
			}
			row += ">";
			
			_result += row;
		}
	}
	
	public static void process_chars(String chs){
		if(_table_cell_tag){
			Table_Cell.process_chars(chs);
		}
		else if(_table_filter_tag){
			Table_Filter.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("表:分组集")||qName.equals("表:工作表属性")){
			_filter_tag = false;
		}
		else if(_filter_tag){
			return ;
		}
		
		else if(_table_filter_tag){
			Table_Filter.process_end(qName);
			if(qName.equals("表:筛选")){
				_table_filter_tag = false;
			}
		}
		
		else if(_table_cell_tag){
			Table_Cell.process_end(qName);
			if(qName.equals("表:单元格")){
				_table_cell_tag = false;
				_result += Table_Cell.get_result();
			}
		}
		
		else if(qName.equals("表:工作表")){
			_result += "</table:table>";
		}
		else if(qName.equals("表:列")){
			String columnEnd = "";
			
			while(Spreadsheet_Data.pop_column_end(_cur_col_num)){
				columnEnd += "</table:table-column-group>";
			}
			_result += columnEnd;
		}
		else if(qName.equals("表:行")){
			String rowEnd = "";
			int num = _max_col_num - Table_Cell.get_col_num();
			
			rowEnd += get_empty_cell(num) + "</table:table-row>";
			
			while(Spreadsheet_Data.pop_row_end(_cur_row_num)){
				rowEnd += "</table:table-row-group>";
			}
			_result += rowEnd;
		}
	}
	
	private static String get_empty_cell(int num){
		String emptyCells = "";
		
		if(num == 1){
			emptyCells += "<table:table-cell/>";
		}
		else if(num > 1){
			emptyCells += "<table:table-cell table:number-columns-repeated=\"" + num + "\"/>";
		}
		
		return emptyCells;
	}
	
	private static String get_empty_row(int cur, int pre, int maxCol){
		int num = cur - pre - 1;
		String emptyRow = "";
		
		if(num == 1){
			emptyRow = "<table:table-row>" + get_empty_cell(maxCol) + "</table:table-row>";
		}
		else if(num > 1){
			emptyRow = "<table:table-row" + " table:number-rows-repeated=\"" + num + "\">";
			emptyRow += get_empty_cell(maxCol);
			emptyRow += "</table:table-row>";
		}
		
		return emptyRow;
	}
	
	private static String get_empty_col(int num){
		String emptyCol = "";
		String deStyle = " table:default-cell-style-name=\"de_Default\"";
		
		if(num == 1){
			emptyCol = "<table:table-column" + deStyle + "/>";
		}
		else if(num > 1){
			emptyCol = "<table:table-column table:number-columns-repeated=\"" + num + "\"" + deStyle + "/>";
		}
		
		return emptyCol;
	}
	
	private static String empty_default_cols(String num){
		String deCols = "";
		
		if(num == null || num.equals("0")){
			num = "1";
		}
		deCols = "<table:table-column table:style-name=\"" + Table_Column._DE_name + "\""
				+ " table:number-columns-repeated=\"" + num 
				+ "\" table:default-cell-style-name=\"de_Default\"/>";
		
		return deCols;
	}
}
