package spreadsheet;

import org.xml.sax.Attributes;
import temp_structs.Spreadsheet_Data;

//处理<表:分组集>
public class Table_Group {
	
	public static void process_start(String qName, Attributes atts){
		int start = 0;
		int end = 0;
		
		if(qName.equals("表:行")){
			start = Integer.parseInt(atts.getValue("表:起始"));
			end = Integer.parseInt(atts.getValue("表:终止"));
			Spreadsheet_Data.add_row_start(start);
			Spreadsheet_Data.add_row_end(end);
		}
		else if(qName.equals("表:列")){
			start = Integer.parseInt(atts.getValue("表:起始"));
			end = Integer.parseInt(atts.getValue("表:终止"));
			Spreadsheet_Data.add_column_start(start);
			Spreadsheet_Data.add_column_end(end);
		}
	}
	
	//public static void process_end(String qName){}
}
