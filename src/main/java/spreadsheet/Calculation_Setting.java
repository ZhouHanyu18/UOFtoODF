package spreadsheet;

import org.xml.sax.Attributes;

public class Calculation_Setting {
	//表:计算设置
	private static String _iteration = "";
	//表:日期系统-190
	private static String _null_date = "";
	//表:精确度以显示值为准
	private static String _precision = "";
	
	private static void clear(){
		_iteration = "";
		_null_date = "";
		_precision = "";
	}
	
	public static String get_result(){
		String result = "";
		
		result += "<table:calculation-settings" + _precision + ">";
		result += _null_date;
		result += _iteration;
		result += "</table:calculation-settings>";
		
		clear();
		return result;
	}
	
	public static void process_start(String qName, Attributes atts){
		String attVal = "";
		
		if(qName.equals("表:计算设置")){			
			String iter = "<table:iteration";
			if((attVal=atts.getValue("表:迭代次数"))!=null){
				iter += " table:steps=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("表:偏差值"))!=null){
				iter += " table:maximum-difference=\"" + attVal + "\"";
			}
			iter += "/>";
			_iteration = iter;
		}
		else if(qName.equals("表:日期系统-1904")){
			String date = "<table:null-date";
			if((attVal=atts.getValue("表:值"))!=null){
				if(attVal.equals("true")){
					date += " table:date-value-type=\"01/01/1904\""; 
				}
			}
			date += "/>";
			_null_date = date;
		}
		else if(qName.equals("表:精确度以显示值为准")){
			String pcs = "";
			if((attVal=atts.getValue("表:值"))!=null){
				pcs += " table:precision-as-shown=\"" + attVal + "\"";
			}
			_precision = pcs;
		}
	}
	
	//public static void process_chars(String qName) {}
}
