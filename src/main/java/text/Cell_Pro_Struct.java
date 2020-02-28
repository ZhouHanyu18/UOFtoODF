package text;

public class Cell_Pro_Struct {
	private String _style_name = "";
	private String _ele_atts = "";
	private String _cell_pro = "";
	
	public boolean equals_to(Cell_Pro_Struct proStr){
		return this._ele_atts.equals(proStr._ele_atts)
			&& this._cell_pro.equals(proStr._cell_pro);
	}
	
	public void set_style_name(String name){
		_style_name = name;
	}
	public String get_style_name(){
		return _style_name;
	}
	
	public void set_ele_atts(String atts){
		_ele_atts = atts;
	}
	public String get_ele_atts(){
		return _ele_atts;
	}
	
	public void set_cell_pro(String pro){
		_cell_pro = pro;
	}
	public String get_cell_pro(){
		return _cell_pro;
	}
}
