package temp_structs;

public class Common_Data {
	private static String _file_type = "";
	//default value is "pt"
	private static String _unit = "pt";

	public static final String LTAG = "&lt;";			//左标签
	public static final String RTAG = "&gt;";			//右标签
	public static final String ANDTAG = "&amp;";		//AND
	public static final String APOTAG = "&apos;";		//单引号
	public static final String QUOTAG = "&quot;";		//双引号

	//========because of 永中========
	public static final float GRAPHRATIO = (float)0.75;

//	*********************************
	public static void set_file_type(String type){
		_file_type = type;
	}
	public static String get_file_type(){
		return _file_type;
	}
//*********************************

//	*********************************
	public static void set_unit(String unit){
		_unit = unit;
	}
	public static String get_unit(){
		return _unit;
	}
//*********************************

	public static float get_graphratio() {
		return GRAPHRATIO;
	}
}

