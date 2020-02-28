package spreadsheet;

import org.xml.sax.Attributes;
import convertor.IDGenerator;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;

//条件格式化
public class Style_Map {
	private static String _chs = "";				//
	
	private static String _style_name = "";			//
	private static String _operator = "";			//操作码
	private static String _value_one = "";			//表:第一操作数
	private static String _value_two = "";			//表:第二操作数
	private static String _apply_style_name = "";	//格式	
	private static String _cond_type = "";			//条件类型formula/cell value
	
	private static String _base_address = "";
	private static String _style_maps = ""; 
	
	private static void clear(){
		_chs = "";
		_style_name = "";
		_operator = "";
		_value_one = "";
		_value_two = "";
		_apply_style_name = "";
		_cond_type = "";
		_base_address = "";
		_style_maps = "";
	}
	
	private static String get_result(){
		String rst = "";
		
		rst += "<style:style style:family=\"table-cell\""
			+ " style:name=\"" + _style_name + "\">";
		rst += _style_maps;
		rst += "</style:style>";
		
		clear();
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("表:条件格式化")){
			_style_name = IDGenerator.get_cell_style_id();
		}
		else if(qName.equals("表:条件")){
			_cond_type = atts.getValue("表:类型");
			if(_cond_type == null){
				_cond_type = "cell value";
			}
		}
		else if(qName.equals("表:格式")){
			attVal = atts.getValue("表:式样引用");
			if(attVal != null){
				_apply_style_name = attVal;
			}
		}
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){

		if(qName.equals("表:区域")){		
			Cell_Range_Struct range = new Cell_Range_Struct();
			try{
				range.process(_chs);
			}catch (Exception e){
				range = null;
				System.err.println(e.getMessage());
			}
			
			if(range != null){
				_base_address = range.get_base_address();
				range.set_style_name(_style_name);
				Table_Cell.add_cell_range(range);
			}
		}
		else if(qName.equals("表:操作码")){
			_operator = _chs;
		}
		else if(qName.equals("表:第一操作数")){
			_value_one = _chs;
		}	
		else if(qName.equals("表:第二操作数")){
			_value_two = _chs;
		}
		else if(qName.equals("表:条件")){
			String oneMap = "";
			
			oneMap = "<style:map";
			oneMap += " style:condition=\"" + get_condition() + "\"";
			oneMap += " style:apply-style-name=\"" + _apply_style_name + "\"";
			oneMap += " style:base-cell-address=\"" + _base_address + "\"";
		    oneMap += "/>";
			
			_style_maps += oneMap;
		}
		else if(qName.equals("表:条件格式化")){
			Stored_Data.addAutoStylesInContentXml(get_result());
		}
		_chs = "";
	}
	
	private static String get_condition(){
		String cond = "";
		
		if(_cond_type.equals("cell value")){
			if(_operator.equals("between")){
				cond = "cell-content-is-between(" 
					+ _value_one + "," + _value_two + ")"; 
			}
			else if(_operator.equals("not between")){
				cond += "cell-content-is-not-between("
					+ _value_one + "," + _value_two + ")";
			}
			else {
				cond += "cell-content()" 
					+ get_op(_operator) + _value_one;
			}
		}
		else if(_cond_type.equals("formula")){
			cond = "is-true-formula("  
				+ Formula.get_formula(_value_one) + ")";
		}
		
		return cond;
	}
	
	private static String get_op(String value){
		String op = "";
		
		if(value.equals("greater than or equal to")){
			op = ">=";
		}
		else if(value.equals("less than or equal to")){
			op = Common_Data.LTAG + "=";		//"<="
		}
		else if(value.equals("not equal to")){
			op = "!=";
		}
		else if(value.equals("equal to")){
			op = "=";
		}
		else if(value.equals("greater than")){
			op = ">";
		}
		else if(value.equals("less than")){
			op = Common_Data.LTAG; 				//"<"
		}

		return op;
	}
}
