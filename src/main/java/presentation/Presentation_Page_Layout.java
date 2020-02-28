package presentation;

import org.xml.sax.Attributes;

import temp_structs.Stored_Data;

public class Presentation_Page_Layout {
	//the result
	private static String _result = "";
	
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("演:页面版式")){
			_result += "<style:presentation-page-layout";
			_result += " style:name=\"" + atts.getValue("演:标识符") + "\"";
			_result += ">";
		}
		else if(qName.equals("演:占位符")){
			_result += "<presentation:placeholder";
			
			attVal = atts.getValue("演:类型");
			if(attVal != null){
				_result += " presentation:object=\"" + conv_PH_type(attVal) + "\"";
			}
			_result += "/>";
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("演:页面版式")){
			_result += "</style:presentation-page-layout>";
		}
		else if(qName.equals("演:页面版式集")){
			Stored_Data.addStylesInStylesXml(_result);
			_result = "";
		}
	}
	
	
	//There are 21 types in UOF in all:
	//title,subtitle,text,clipart,graphics,
	//chart,spreadsheet,object,header,footer,
	//date,number,vertical-text,vertical-title
	//vertical-subtitle,media-clip,table,centertitle,
	//outline,handout,notes
	public static String conv_PH_type(String val){
		String type = "";
		
		if(val.equals("text")){
			type = "outline";
		}
		else if(val.equals("clipart")){
			type = "graphic";
		}
		else if(val.equals("graphics")){
			type = "graphic";
		}
		else if(val.equals("spreadsheet")){
			type = "table";
		}
		else if(val.equals("date")){
			type = "date-time";
		}
		else if(val.equals("number")){
			type = "page-number";
		}
		else if(val.equals("media-clip")){
			type = "object";
		}
		else if(val.equals("centertitle")){
			type = "title";
		}
		else{
			type = val;
		}
		
		return type;
	}
}
