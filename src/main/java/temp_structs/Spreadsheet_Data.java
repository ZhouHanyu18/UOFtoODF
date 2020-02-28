package temp_structs;

import java.util.ArrayList;
import java.util.Stack;

public class Spreadsheet_Data {		
	private static ArrayList<Integer> row_start_list = new ArrayList<Integer>();
	private static ArrayList<Integer> row_end_list = new ArrayList<Integer>();
	private static ArrayList<Integer> column_start_list = new ArrayList<Integer>();
	private static ArrayList<Integer> column_end_list = new ArrayList<Integer>();
	private static Stack<String> table_charts_stack = new Stack<String>();
	
	//initialize
	public static void init(){
		row_start_list.clear();
		row_end_list.clear();
		column_start_list.clear();
		column_end_list.clear();
		table_charts_stack.clear();
	}
	
//****************************************************
	public static void add_row_start(int i){
		row_start_list.add(i);
	}
	
	public static boolean pop_row_start(int i){
		Integer S = new Integer(i);
		
		if(row_start_list.contains(S)){
			row_start_list.remove(S);
			return true;
		}
		
		return false;		
	}
	
	public static void add_row_end(int i){
		row_end_list.add(i);
	}
	
	public static boolean pop_row_end(int i){
		Integer S = new Integer(i);
		
		if(row_end_list.contains(S)){
			row_end_list.remove(S);
			return true;
		}
		
		return false;		
	}
	public static void add_column_start(int i){
		column_start_list.add(i);
	}
	
	public static boolean pop_column_start(int i){
		Integer S = new Integer(i);
		
		if(column_start_list.contains(S)){
			column_start_list.remove(S);
			return true;
		}
		
		return false;		
	}
	
	public static void add_column_end(int i){
		column_end_list.add(i);
	}
	
	public static boolean pop_column_end(int i){
		Integer S = new Integer(i);
		
		if(column_end_list.contains(S)){
			column_end_list.remove(S);
			return true;
		}
		
		return false;		
	}
	
//***************************************************
	
	public static void add_table_chart(String chartName) {
		table_charts_stack.push(chartName);
	}
	public static String get_table_chart() {
		if (table_charts_stack.isEmpty())
			return "";
		else
			return table_charts_stack.pop();
	}
}
