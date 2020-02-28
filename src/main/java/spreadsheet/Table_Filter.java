package spreadsheet;

import org.xml.sax.Attributes;

public class Table_Filter {
	//
	private static String _table_name = "";
	//
	private static String _chs = "";
	//
	private static String _filter_conditions = "";
	//
	private static String _target_range = "";
	//
	private static String _field_num = "";
	//and/or
	private static String _condition_type = "";	
	//
	private static String _operator_type = "";
	//
	private static String _operand = "";	
	//
	private static boolean _is_advanced = false;
	
	
	
	private static void clear(){
		_table_name = "";
		_chs = "";
		_filter_conditions = "";
		_target_range = "";
		_field_num = "";
		_condition_type = "";
		_operator_type = "";
		_operand = "";
		_is_advanced = false;
	}
	
	//Return the result
	public static String get_result(){
		String result = "";
		
		if(!_is_advanced && !_filter_conditions.equals("")){
			result += "<table:database-range";
			result += " table:target-range-address=\"" + _target_range + "\"";
			result += " table:display-filter-buttons=\"true\">";	//default is true or false?
			result += "<table:filter><table:filter-and>";
			result += _filter_conditions;
			result += "</table:filter-and></table:filter>";
			result += "</table:database-range>";
		}
		
		clear();
		return result;
	}
	
	public static void set_table_id(String id){
		_table_name = id;
	}
	
	public static void process_start(String qName,Attributes atts){		
		String attVal = "";
		
		if(qName.equals("��:ɸѡ")){
			if((attVal=atts.getValue("��:����"))!=null){
				_is_advanced = (attVal.equals("advance"))?true:false;
			}
		}
		
		else if(qName.equals("��:����")){
			_field_num = atts.getValue("��:�к�");
		}
		
		else if(qName.equals("��:�Զ���")){
			attVal = atts.getValue("��:����");		
			if(attVal != null){
				_condition_type = attVal;
			}
			
			if(_condition_type.equals("or")){
				_filter_conditions += "<table:filter-or>";
			}
			else if(_condition_type.equals("and")){
				_filter_conditions += "<table:filter-and>";
			}
		}
		else if(qName.equals("��:��ͨ")){			
			if((attVal=atts.getValue("��:����"))!=null){
				_operator_type = conv_operator_type(attVal);
			}
			if((attVal=atts.getValue("��:ֵ"))!=null){
				_operand = attVal;
			}	
			
			String oneCondition = "";
			oneCondition += "<table:filter-condition"; 
			oneCondition += " table:field-number=\"" + _field_num + "\"";
			oneCondition += " table:operator=\"" + _operator_type + "\"";
			oneCondition += " table:value=\"" + _operand + "\"";
			oneCondition += "/>";
			_filter_conditions += oneCondition;
		}
		
		else if(qName.equals("��:��������")){
			//todo
		}
		
		else if(qName.equals("��:�������")){
			//todo
		}
	}  
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){
		if(qName.equals("��:ɸѡ")){
			//todo
		}
		else if(qName.equals("��:��Χ")){
			_target_range = 
				Cell_Address.get_cell_range(_chs,_table_name);
		}
		else if(qName.equals("��:�Զ���")){
			if(_condition_type.equals("or")){
				_filter_conditions += "</table:filter-or>";
			}
			else if(_condition_type.equals("and")){
				_filter_conditions += "</table:filter-and>";
			}
			_condition_type = "";
		}
		else if(qName.equals("��:��������")){
			//todo
		}	
		else if(qName.equals("��:�������")){
			//todo
		}
		
		else if(qName.equals("��:������")){
			_operator_type = conv_operator_type(_chs);
		}
		
		else if(qName.equals("��:ֵ")){	
			_operand = _chs;
		}
		else if(qName.equals("��:��������")){
			String oneCondition = "";
			
			if(!_operator_type.equals("")){
				oneCondition += "<table:filter-condition";
				oneCondition += " table:field-number=\"" + _field_num + "\"";
				oneCondition += " table:operator=\"" + _operator_type + "\"";
				oneCondition += " table:value=\"" + _operand + "\"";
				oneCondition += "/>";
				_filter_conditions += oneCondition;
			}
		}
		
		_chs = "";
	}
	
	private static String conv_operator_type(String val){
		String convVal = "";
		
		if(val.equals("value")){
			convVal = "empty";
		}
		else if(val.equals("bottomitem")){
			convVal = "bottom values";
		}
		else if(val.equals("bottomitem")){
			convVal = "bottom values";
		}
		else if(val.equals("topitem")){
			convVal = "top values";
		}
		else if(val.equals("bottompercent")){
			convVal = "bottom percent";
		}
		else if(val.equals("toppercent")){
			convVal = "top percent";
		}
		else if(val.equals("bottomitem")){
			convVal = "bottom values";
		}
		
		else if(val.equals("equal to")){
			convVal = "=";
		}
		else if(val.equals("not equal to")){
			convVal = "!=";
		}
		else if(val.equals("less than")){
			convVal = "<";
		}
		else if(val.equals("greater than")){
			convVal = ">";
		}
		else if(val.equals("less than or equal to")){
			convVal = "<=";
		}
		else if(val.equals("greater than or equal to")){
			convVal = ">=";
		}
		
		return convVal;
	}
}
