package temp_structs;

import java.util.HashMap;
import java.util.Map;

public class Object_Set_Data {
	//formulaҪ���������ļ�����˲��洢�ڴ�
	private static Map<String,String> 
		_drawings_map = new HashMap<String,String>();   		//���ͼ��
	private static Map<String,String> 
		_other_objs_map = new HashMap<String,String>();   		//������������·��
	private static Map<String,String> 
		_drawing_overturn_map = new HashMap<String,String>();   //���ͼ���Ƿ��з�ת����ʱֻ����line����β�����ж�  
	private static Map<String,String>
		_ref_obj_map = new HashMap<String,String>();   			//���frame���õ���������id
	
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
