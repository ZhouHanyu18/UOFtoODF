package style_set;

import java.util.*;
import org.xml.sax.Attributes;
import temp_structs.Common_Data;

/**
 * 
 * @author xie
 *
 */
public class Table_Row {
	//table-row counter
	private static int _row_counter = 0;
	
	//Used to generate a different style name
	private static int _id_counter = 0;
	
	//The table-row-style attributes for <text>
	private static String _text_atts = "";
	
	//Each new row attributes will be keeped in
	// the map with its style name. For <text>
	private static Map<String,String> 
			_text_atts_map = new TreeMap<String,String>();
	
	//Each <table:row> is associated with a corresponding
	//style name. For both <text> and <spreadsheet>
	private static Map<Integer,String>
			_style_name_map = new TreeMap<Integer,String>();
	
	//Each new row height (different from others) will
	//be keeped in the map with its style name.
	//For <spreadsheet>
	private static Map<String,String> 
			_sheet_att_map = new TreeMap<String,String>();
	//name of default table-row style in <spreadsheet>
	public static final String _DE_name = "de_row";
	

	//initialize
	public static void init(){
		_row_counter = 0;
		_id_counter = 0;
		_text_atts = "";
		_text_atts_map.clear();
		_style_name_map.clear();
		_sheet_att_map.clear();
	}
	
	//Generate a row style name.
	private static String gen_row_id(){
		_id_counter ++;
		return "ro" + _id_counter;
	}
	
	//Return the style name of the specified table-row
	//For both <text> and <spreadsheet>
	public static String get_style_id(int rowName){
		return _style_name_map.get(rowName);
	}
	
	//Get three attributes for <style:table-row-properties>
	public static void process_text_atts(String qName, Attributes atts){
		String attVal = "";		

		if(qName.equals("字:表行属性")){
			_row_counter ++;
		}
		
		else if(qName.equals("字:高度")){
			if((attVal=atts.getValue("字:最小值")) != null){
				_text_atts += " style:min-row-height=\"" 
					+ attVal + Common_Data.get_unit() + "\"";
			}
			if((attVal=atts.getValue("字:固定值")) != null){
				_text_atts += " style:row-height=\"" 
					+ attVal + Common_Data.get_unit() + "\"";
				
			}
		}
		
		else if(qName.equals("字:跨页")){
			if((attVal=atts.getValue("字:值")) != null){
				_text_atts += " fo:keep-together=\"" + attVal + "\"";
			}
		}
	}
	
	//if _text_atts isn't empty, we need to create a new 
	//table-row style when it's different from others
	// and associate current row with a style name. 
	public static void add_text_atts(){
		String styleName = "";
		
		if(!_text_atts.equals("")){
			for(Iterator<String> it=_text_atts_map.keySet().iterator();it.hasNext();){
				String name = it.next();
				if(_text_atts_map.get(name).equals(_text_atts)){
					styleName = name;
					break;
				}
			}
			if(styleName.equals("")){
				styleName = gen_row_id();
				_text_atts_map.put(styleName,_text_atts);
			}
			
			//it should be cleared.
			_text_atts = "";
			_style_name_map.put(_row_counter, styleName);
		}
	}
	
	//Get <style:row-height>-att for <style:table-row-properties>
	public static void process_sheet_row(String qName, Attributes atts){
		String height = "";
		String styleName = "";
		
		if(qName.equals("表:行")){
			_row_counter ++;			
			if(atts.getValue("表:行高") != null){
				height = atts.getValue("表:行高");
			}
		}	
		
		//if height isn't empty, we need to create a new 
		//table-row style when this height is different from
		//others and associate current row with a style name. 
		if(!height.equals("")){
			for(Iterator<String> it=_sheet_att_map.keySet().iterator();it.hasNext();){
				String name = it.next();
				if(_sheet_att_map.get(name).equals(height)){
					styleName = name;
					break;
				}
			}
			if(styleName.equals("")){
				styleName = gen_row_id();
				_sheet_att_map.put(styleName,height);

			}
			
			_style_name_map.put(_row_counter, styleName);
		}
		
		//
		if(qName.equals("表:工作表内容")){
			height = atts.getValue("表:缺省行高");
			if(height != null){
				_sheet_att_map.put(_DE_name,height);
			}
		}
	}
	
	//Get all the table-row styles keeped in the map.<text>
	public static String get_text_results(){
		String rst = "";
		
		for(Iterator<String> it=
			_text_atts_map.keySet().iterator(); it.hasNext();){
			String id = it.next();

			rst += "<style:style style:name=\"" + id + "\" style:family=\"table-row\">" 
					+ "<style:table-row-properties" + _text_atts_map.get(id) + "/>" 
					+ "</style:style>";
		}
		
		return rst;
	}
	
	//Get all the table-row styles keeped in the map.<spreadsheet>
	public static String get_sheet_results(){
		String rst = "";
		
		for(Iterator<String> it=
			_sheet_att_map.keySet().iterator(); it.hasNext();){
			String id = it.next();
			rst += "<style:style style:name=\"" + id + "\" style:family=\"table-row\">" 
					+ "<style:table-row-properties style:row-height=\"" + _sheet_att_map.get(id) 
					+ Common_Data.get_unit() + "\"/></style:style>";
		}
		
		return rst;
	}
	
	public static String get_result(){
		String rst = "";
		
		if(Common_Data.get_file_type().equals("text")){
			rst = get_text_results();
		}
		else if(Common_Data.get_file_type().equals("spreadsheet")){
			rst = get_sheet_results();
		}
		
		return rst;
	}
}
