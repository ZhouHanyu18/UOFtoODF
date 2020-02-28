package temp_structs;


public class Stored_Data {
	//
	private static String _cellStyle_set = "";
	//应放到styles.xml的<office:styles>里去的styles
	private static String _styles_in_stylexml = "";  
	//应放到content.xml里去的auto-styles
	private static String _autostyles_in_contentxml = ""; 
	//应放到styles.xml里去的auto-styles
	private static String _autostyles_in_stylesxml = "";

	
	//initialize
	public static void init(){
		_cellStyle_set = "";
		_styles_in_stylexml = "";
		_autostyles_in_contentxml = "";
		_autostyles_in_stylesxml = "";
	}
	
	public static void addCellStyle(String style){
		_cellStyle_set += style;
	}
	public static String getCellStyleSet(){
		return _cellStyle_set;
	}
//*********************************
		
//	*********************************	
	public static void addStylesInStylesXml(String style) {
		_styles_in_stylexml += style;
	}
	public static String getStylesInStylesXml() {
		String defaults = "";
		
		if(Common_Data.get_file_type().equals("text")){
			defaults += Default_Styles._paragraph;
		}
		else if(Common_Data.get_file_type().equals("spreadsheet")){
			defaults += Default_Styles._table;
			defaults += Default_Styles._table_row;
			defaults += Default_Styles._table_cell;
			defaults += Default_Styles._de_table_cell;
		}
		
		return defaults + _styles_in_stylexml;
	}
//	*********************************
	
//	*********************************	
	public static void addAutoStylesInContentXml(String style) {
		_autostyles_in_contentxml += style;
	}
	public static String getAutoStylesInContentXml() {
		return _autostyles_in_contentxml;
	}
	
	public static void setAutoStylesInContentXml(String str) {
		_autostyles_in_contentxml = str;
	}
//	*********************************

	public static void addAutoStylesInStylesXml(String style){
		_autostyles_in_stylesxml += style;
	}
	
	public static String getAutoStylesInStylesXml(){
		return _autostyles_in_stylesxml;
	}
}
