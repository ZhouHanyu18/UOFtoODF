package convertor;

public class IDGenerator {
	private static int _table_id = 0;
	private static int _column_id = 0;
	private static int _number_id = 0;
	private static int _cell_style_id = 0;
	private static int _draw_page_id = 0;
	private static int _mp_dp_id = 0;
	private static int _olts_id = 0;
	private static int _gradient_id = 0;
	private static int _fill_image_id = 0;
	private static int _chart_id = 0;
	
	//initialize
	public static void init(){
		_table_id = 0;
		_column_id = 0;
		_number_id = 0;
		_cell_style_id = 0;
		_draw_page_id = 0;
		_mp_dp_id = 0;
		_olts_id = 0;
		_gradient_id = 0;
		_fill_image_id = 0;
		_chart_id = 0;
	}
	
	public static void restart(){
		_table_id = 0;
		_column_id = 0;
		_number_id = 0;
		_draw_page_id = 0;
	}
	
	public static String get_table_id(){
		_table_id ++;
		return "ex_ta" + _table_id;
	}
	
	public static String get_column_id(){
		_column_id ++;
		return "co" + _column_id;
	}
	
	public static String get_number_id(){
		_number_id ++;
		return "N" + _number_id;
	}
	
	//used for the cell style that contains
	//<style:map>s as its children
	public static String get_cell_style_id(){
		_cell_style_id ++;
		return "cond_ce" + _cell_style_id;
	}
	
	//name for draw-page styles used by draw page
	public static String get_draw_page_id(){
		_draw_page_id ++;
		
		return "dp" + _draw_page_id;
	}
	
	//name for draw-page styles used by master page
	public static String get_mpdp_id(){
		_mp_dp_id ++;
		
		return "mpdp" + _mp_dp_id;
	}
	
	//return a name for text-style used 
	//by list-style
	public static String get_olts_id(){
		_olts_id ++;
		return "OLTS" + _olts_id;
	}
	
	//name for <draw:gradient>
	public static String get_gradient_id(){
		_gradient_id ++;
		
		return "draw_gradient_" + _gradient_id;
	}
	
	//name for <draw:fill-image>
	public static String get_fill_image_id(){
		_fill_image_id ++;
		
		return "fill_image_" + _fill_image_id;
	}
	
	public static String get_chart_id(){
		_chart_id ++;
		
		return "Object " + _chart_id;
	}
}
