package text;

import org.xml.sax.Attributes;
import temp_structs.Text_Data;

public class Tracked_changes {
	
	//处理具体的转化并保存结果
	public static void process(String qName,Attributes atts){
		
		if(qName.equals("字:修订信息")){		
			String id = atts.getValue("字:标识符");
			
			Text_Data.addTrackedInfo(id,getChange_info(atts));
		}
	}
	
	private static String getChange_info(Attributes atts){
		String str = "";
		String att_val = "";
		
		str += "<office:change-info>";
		if((att_val=atts.getValue("字:作者"))!= null){
			str += "<dc:creator>" + Text_Data.getUser(att_val) + "</dc:creator>";
		}
		
		if((att_val=atts.getValue("字:日期"))!= null){
			str += "<dc:date>" + att_val + "</dc:date>";
		}
		str += "</office:change-info>";
		
		return str;
	}
}
