package temp_structs;

import java.util.HashMap;
import java.util.Map;

public class Object_Set_Data {
	//formula要单独生成文件，因此不存储在此
	private static Map<String,String> 
		_drawings_map = new HashMap<String,String>();   		//存放图形
	private static Map<String,String> 
		_other_objs_map = new HashMap<String,String>();   		//存放其他对象的路径
	private static Map<String,String> 
		_drawing_overturn_map = new HashMap<String,String>();   //存放图形是否有翻转，暂时只用在line的首尾坐标判断  
	private static Map<String,String>
		_ref_obj_map = new HashMap<String,String>();   			//存放frame引用的其他对象id
	
	//initialize
	public static void init(){
		_drawings_map.clear();
		_other_objs_map.clear();
		_drawing_overturn_map.clear();
		_ref_obj_map.clear();
	}
	
	public static void addDrawing(String ID,String drawing) {
		_drawings_map.put(ID,drawing);
	}
	
	public static String getDrawing(String ID) {
		return _drawings_map.get(ID);
	}
	
	public static void addOtherObj(String ID,String other_obj) {
		_other_objs_map.put(ID,other_obj);
	}
	
	public static String getOtherObj(String ID) {
		return _other_objs_map.get(ID);
	}
	
	public static void addDrawingOverturn(String ID,String overturn) {
		_drawing_overturn_map.put(ID,overturn);
	}
	
	public static String getDrawingOverturn(String ID) {
		return _drawing_overturn_map.get(ID);
	}
	
	public static void addRefObj(String ID,String objID) {
		_ref_obj_map.put(ID,objID);
	}
	
	public static String getRefObj(String ID) {
		return _ref_obj_map.get(ID);
	}
}
