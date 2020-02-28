package text;

import org.xml.sax.Attributes;
import temp_structs.Text_Data;

public class Tracked_changes {
	
	//��������ת����������
	public static void process(String qName,Attributes atts){
		
		if(qName.equals("��:�޶���Ϣ")){		
			String id = atts.getValue("��:��ʶ��");
			
			Text_Data.addTrackedInfo(id,getChange_info(atts));
		}
	}
	
	private static String getChange_info(Attributes atts){
		String str = "";
		String att_val = "";
		
		str += "<office:change-info>";
		if((att_val=atts.getValue("��:����"))!= null){
			str += "<dc:creator>" + Text_Data.getUser(att_val) + "</dc:creator>";
		}
		
		if((att_val=atts.getValue("��:����"))!= null){
			str += "<dc:date>" + att_val + "</dc:date>";
		}
		str += "</office:change-info>";
		
		return str;
	}
}
