package spreadsheet;

import java.util.ArrayList;

import org.xml.sax.Attributes;

/**
 * 
 * @author xie
 *
 */
public class Name_Expression {
	//the result
	private static String _result = "";	
	//table:name
	private static String _name = "";
	//table:base-cell-address
	private static String _base_address = "";	
	//table:cell-range-address	
	private static String _cell_range = "";
	//@table:print-ranges for table:table
	private static ArrayList<String> _print_areas = new ArrayList<String>();
		

	public static String get_print_area(int i){
		String pa = "";
		
		if(_print_areas.size() > i){
			pa = _print_areas.get(i);
		}
		
		return pa;
	}
	
	private static void clear(){
		_name = "";
		_base_address = "";
		_cell_range = "";
	}
	
	//Get one <table:named-range> element
	private static String get_one_expression(){
		String expression = "";
		
		expression += "<table:named-range table:name=\"" + _name + "\"";
		expression += " table:base-cell-address=\"" + _base_address + "\"";
		expression += " table:cell-range-address=\"" + _cell_range + "\"/>";
		
		clear();
		return expression;
	}
	
	public static String get_result(){
		String rst = "";
		
		if(!_result.equals("")){
			rst = "<table:named-expressions>" + _result + "</table:named-expressions>";
			_result = "";
		}
		
		_print_areas.clear();
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("uof:书签")){
			_name = atts.getValue("uof:名称");
		}
		else if(qName.equals("uof:命名表达式")){
			String tableName = "";
			String range = "";
			
			if((attVal=atts.getValue("uof:工作表名"))!=null){
				tableName = attVal;
			}
			
			if((attVal=atts.getValue("uof:行列区域"))!=null){
				range = attVal;
				
				if(tableName.equals("")){
					tableName = Cell_Address.get_table_name(attVal);
				}
				_base_address = Cell_Address.get_base_address(attVal,tableName);
				_cell_range = Cell_Address.get_base_address(attVal,tableName);
			}
			
			if(_name.equals("Print_Area")){
				String att = "";
				
				att = " table:print-ranges=\"" + Cell_Address.get_cell_range(range,tableName) + "\"";
				_print_areas.add(att);
			}
			else if(_name.equals("Print_Titles")){
				//nothing todo
			}
			else {
				_result += get_one_expression();
			}
		}
	}
}
