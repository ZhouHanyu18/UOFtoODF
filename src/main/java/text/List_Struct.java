package text;

/**
 * A data struct to record the applied text-list
 *  name and level for corresponding paragraph.
 * @author xie
 *
 */
public class List_Struct {
	private int _counter_of_para = 0;
	private String _list_name = "";
	private int _list_level = 0;
	
	public void set_cop(int c){
		_counter_of_para = c;
	}
	public int get_cop(){
		return _counter_of_para;
	}
	
	public void set_list_name(String name){
		_list_name = name;
	}
	public String get_list_name(){
		return _list_name;
	}
	
	public void set_list_level(int level){
		_list_level = level;
	}
	public int get_list_level(){
		return _list_level;
	}
}
