package temp_structs;

import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

public class Text_Data {	
	private static Map<String,String> change_type_map = new HashMap<String,String>();
	private static Map<String,String> tracked_info_map = new HashMap<String,String>();
	private static Map<String,String> deletion_data_map = new HashMap<String,String>();
	private static Map<String,String> user_map = new HashMap<String,String>();
	
	
	//initialize
	public static void init(){
		change_type_map.clear();
		tracked_info_map.clear();
		deletion_data_map.clear();
		user_map.clear();
	}
	
//	******************************************	
	public static void addChangeType(String id, String type){
		change_type_map.put(id,type);
	}
	
	public static void addDeletionData(String id, String data){
		deletion_data_map.put(id,data);
	}
	
	public static void addTrackedInfo(String id,String changeInfo){
		tracked_info_map.put(id,changeInfo);
	}	
	
	public static String getTrackedChanges(){
		String id = "";
		String type = "";
		String changes = "";
		Set<String> infoSet = tracked_info_map.keySet();
		
		for(Iterator<String> iter = infoSet.iterator(); iter.hasNext();){
			id = iter.next();
			type = change_type_map.get(id);
			if(id==null || type==null){
				continue;
			}
			
			changes += "<text:changed-region text:id=\"" + id + "\">";
			if(type.equals("insertion")){
				changes += "<text:insertion>";
				changes += tracked_info_map.get(id);
				changes += "</text:insertion>";
			}
			else if(type.equals("deletion")){
				changes += "<text:deletion>";
				changes += tracked_info_map.get(id);
				changes += deletion_data_map.get(id);
				changes += "</text:deletion>";
			}
			else if(type.equals("format-change")){
				changes += "<text:format-change>";
				changes += tracked_info_map.get(id);
				changes += "</text:format-change>";
			}
			changes += "</text:changed-region>";
		}
		
		if(changes.equals(""))	return "";
		
		return "<text:tracked-changes>" + changes + "</text:tracked-changes>";
	}
//	*******************************************

	
//	******************************************	
	public static void addUser(String id,String name){
		user_map.put(id,name);   
	}	
	public static String getUser(String id){
		return user_map.get(id);
	}
//	*******************************************
}
