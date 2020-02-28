package style_set;

import java.util.*;
import org.xml.sax.Attributes;
import temp_structs.Common_Data;

/**
 * 
 * @author xie
 *
 */
public class Table_Column {
	//Used to generate a different style name
	private static int _id_counter = 0;
	//table-column counter. For <spreadsheet>
	private static int _col_counter = 0;	
	//The table-column-<style:style>. For <text>
	private static String _col_styles = "";
	//<table:column>-elements. For both <text> and <spreadsheet>
	private static String _table_cols = "";
	//Each <table:column> is associated with a 
	//corresponding style name. For <spreadsheet>
	private static Map<Integer,String> _style_name_map = new TreeMap<Integer,String>();
	//Each new col width (different from others) will
	//be stored in the map with its style name. For both <text> and <spreadsheet>
	private static Map<String,String> _col_width_map = new TreeMap<String,String>();
	//name of default table-column style in <spreadsheet>
	public static final String _DE_name = "de_col";
	
	
	//initialize
	public static void init(){
		_id_counter = 0;
		_col_counter = 0;
		_col_styles = "";
		_table_cols = "";
		_style_name_map.clear();
		_col_width_map.clear();
	}
	
	//Generate a column style name.
	private static String gen_col_id(){
		_id_counter ++;
		return "co" + _id_counter;
	}
	
	//Return the style name of the specified table-column
	//Invoked by Sheet_Table. For <spreadsheet>
	public static String get_style_id(int counter){
		return _style_name_map.get(counter);
	}
	
	//Return the <table:column>-elements. Invoked
	//by Table_Style. For <text>
	public static String get_table_cols(){
		String cols = _table_cols;
		_table_cols = "";
		return cols;
	}
	
	//Create <style:style> for table-column and generate
	//<table:column>-element for <table:table>. For <text>
	public static void process_text_col(String width){	
		String styleName = "";
	
		//if width isn't empty, we need to create a new 
		//table-col style when this width is different from
		//others and associate current row with a style name. 
		if(!width.equals("")){
			for(Iterator<String> it=
					_col_width_map.keySet().iterator();it.hasNext();){
				String name = it.next();
				if(_col_width_map.get(name).equals(width)){
					styleName = name;
					break;
				}
			}
			if(styleName.equals("")){
				styleName = gen_col_id();
				_col_width_map.put(styleName,width);
				
				String style = "";	
				style = "<style:style style:name=\"" + styleName + "\" style:family=\"table-column\">";
				style += "<style:table-column-properties" + " style:column-width=\"" + width
						+ Common_Data.get_unit() + "\"/>";   
				style += "</style:style>";
				_col_styles += style;
			}
			
			_table_cols += "<table:table-column table:style-name=\"" + styleName + "\"/>";
		}
	}
	
	public static void process_sheet_col(String qName, Attributes atts){
		String width = "";
		String styleName = "";
		
		if(qName.equals("表:列")){
			_col_counter ++;
			width=atts.getValue("表:列宽");			
			if(width == null){
				width = "";
			}
			else {
				width = " style:column-width=\"" + width + Common_Data.get_unit() + "\"";
			}
		}	
		
		//if width isn't empty, we need to create a new 
		//table-col style when this width is different from
		//others and associate current row with a style name. 
		if(!width.equals("")){
			for(Iterator<String> it=_col_width_map.keySet().iterator();it.hasNext();){
				String name = it.next();
				if(_col_width_map.get(name).equals(width)){
					styleName = name;
					break;
				}
			}
			if(styleName.equals("")){
				styleName = gen_col_id();
				_col_width_map.put(styleName,width);
			}
			
			_style_name_map.put(_col_counter, styleName);
		}
		
		//
		if(qName.equals("表:工作表内容")){
			width = atts.getValue("表:缺省列宽");
			if(width != null){
				width = " style:column-width=\"" + width + Common_Data.get_unit() + "\"";
				_col_width_map.put(_DE_name,width);
			}
		}
	}
	
	//Return table-column-<style:style>s
	//For both <text> and <spreadsheet>
	public static String get_result(){
		String rst = "";
		if(Common_Data.get_file_type().equals("text")){
			rst = _col_styles;
		}
		else if(Common_Data.get_file_type().equals("spreadsheet")){
			for(Iterator<String> it=
				_col_width_map.keySet().iterator(); it.hasNext();){
				String id = it.next();
				rst += "<style:style style:name=\"" + id + "\" style:family=\"table-column\">" 
					+ "<style:table-column-properties" + _col_width_map.get(id) + "/>"
					+ "</style:style>";
			}
		}
		
		return rst;
	}
}

