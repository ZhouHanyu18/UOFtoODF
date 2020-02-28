package spreadsheet;

import org.xml.sax.Attributes;
import temp_structs.Common_Data;

/**
 * Process the conversion from <��:������Ч��>
 * to <table:content-validation>
 * 
 * @author xie
 *
 */
public class Content_Validation {
	//the result
	private static String _result = "";
	//
	private static String _chs = "";	
	//used for generating validation name
	private static int _valid_id = 0;	
	//validation name
	private static String _valid_name = "";	
	//attributes for <table:content-validation>
	private static String _valid_atts = "";	
	private static String _check_type = "";			//У������
	private static String _operator = "";			//������
	private static String _value_one = "";			//��:��һ������
	private static String _value_two = "";			//��:�ڶ�������
	private static String _help_message = "";		//������ʾ
	private static String _error_message = "";		//������ʾ
	
	private static void clear(){
		_chs = "";
		_valid_atts = "";
		_check_type = "";
		_operator = "";
		_value_one = "";
		_value_two = "";
		_help_message = "";
		_error_message = "";
	}

	//Get one <table:content-validation> element
	private static String get_one_validation(){
		String oneValid = "";
		
		oneValid = "<table:content-validation table:name=\"" + _valid_name + "\"" + _valid_atts;		
		if(!get_condition().equals("")){
			oneValid += " table:condition=\"" + get_condition() + "\"";
		}
		oneValid += ">";
		
		oneValid += _help_message + _error_message;
		oneValid += "</table:content-validation>";
		
		clear();
		return oneValid;
	}
	
	//return <table:content-validations>
	public static String get_result(){
		String rst = "";
		
		if(!_result.equals("")){
			rst = "<table:content-validations>" + _result + "</table:content-validations>";
			_result = "";
		}
		
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("��:������Ч�Լ�")){
			//todo
		}	
		else if(qName.equals("��:������Ч��")){
			_valid_id ++;
			_valid_name = "Val" + _valid_id;
		}
		else if(qName.equals("��:���Կո�")){
			if((attVal=atts.getValue("��:ֵ"))!=null){		
				_valid_atts += " table:allow-empty-cell=\"" + attVal + "\"";
			}
		}		
		else if(qName.equals("��:������ͷ")){
			if((attVal=atts.getValue("��:ֵ"))!=null){
				if(attVal.equals("false")){		
					_valid_atts += " table:display-list=\"none\"";
				}
				else{
					_valid_atts += " table:display-list=\"sort-ascending\"";	
				}
			}
		}		
		else if(qName.equals("��:������ʾ")){
			
			_help_message +="<table:help-message";
			
			if((attVal=atts.getValue("��:��ʾ"))!=null){
				_help_message += " table:display=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("��:����"))!=null){	
				_help_message += " table:title=\"" + attVal + "\"";
			}
			_help_message += ">";
			
			if((attVal=atts.getValue("��:����"))!=null){	
				_help_message += "<text:p>" + attVal + "</text:p>";
			}
			
			_help_message +="</table:help-message>";
		}
		
		if(qName.equals("��:������ʾ")){
			
			_error_message += "<table:error-message";
			
			if((attVal=atts.getValue("��:��ʾ"))!=null){
				_error_message += " table:display=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("��:����"))!=null){	
				_error_message += " table:title=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("��:����"))!=null){
				if(attVal.equals("��ֹ")){
					_error_message += " table:message-type=\"stop\"";
				}
				else if(attVal.equals("����")){
					_error_message += " table:message-type=\"warning\"";
				}
				else if(attVal.equals("��Ϣ")){
					_error_message += " table:message-type=\"information\"";
				}
			}
			_error_message += ">";
			
			if((attVal=atts.getValue("��:����"))!=null){	
				_error_message += "<text:p>" + attVal + "</text:p>";
			}
			_error_message += "</table:error-message>";
		}
	}
	
	public static void process_chars(String chs){
		_chs += chs;
	}
	
	public static void process_end(String qName){

		if(qName.equals("��:����")){				
			Cell_Range_Struct range = new Cell_Range_Struct();
			try{
				range.process(_chs);
			}catch (Exception e){
				range = null;
				System.err.println(e.getMessage());
			}
			
			if(range != null){	
				range.set_valid_name(_valid_name);
				Table_Cell.add_cell_range(range);
				_valid_atts +=" table:base-cell-address=\"" + range.get_base_address() + "\"";
			}
		}
		else if(qName.equals("��:������")){
			_operator = _chs;
		}
		else if(qName.equals("��:��һ������")){
			_value_one = _chs;
		}	
		else if(qName.equals("��:�ڶ�������")){
			_value_two = _chs;
		}
		else if(qName.equals("��:У������")){
			_check_type = _chs.trim();
		}
		else if(qName.equals("��:������Ч��")){		
			_result += get_one_validation();	
		}	
		else if(qName.equals("��:������Ч�Լ�")){
			//todo
		}

		_chs = "";
	}
	
	private static String get_condition(){
		String cond = "";
		String checkStyle = get_check_style(_check_type);
		
		if(_check_type.equals(""))	return "";
		
		if(_check_type.equals("list")){
			cond = "oooc:cell-content-is-in-list(" + _value_one + ")";
		}
		else if(_check_type.equals("text length")){
			if(!get_op(_operator).equals("")){
				cond = "oooc:cell-content-text-length()" + get_op(_operator) + _value_one;
			}
			else if(_operator.equals("between")){
				cond = "oooc:cell-content-text-length-is-between(" + _value_one + "," + _value_two + ")";
			}
			else if(_operator.equals("not between")){
				cond = "oooc:cell-content-text-length-is-not-between(" + _value_one + "," + _value_two + ")";
			}
		}
		else if(!checkStyle.equals("")){		
			if(!get_op(_operator).equals("")){
				cond += "cell-content()" + get_op(_operator) + _value_one; 
			}
			else if(_operator.equals("between")){
				cond += "cell-content-is-between(" + _value_one + "," + _value_two + ")";
			}
			else if(_operator.equals("not between")){
				cond += "cell-content-text-length-is-not-between(" + _value_one + "," + _value_two + ")";
			}
			
			if(!cond.equals("")){
				cond = checkStyle + " and " + cond;
			}
		}
		
		return cond;
	}
		
	private static String get_check_style(String value){
		String trueFunction = "";
		
		if(value.equals("whole number")){
			trueFunction = "oooc:cell-content-is-whole-number()";
		}
		else if(value.equals("decimal")){
			trueFunction = "oooc:cell-content-is-decimal-number()";
		}
		else if(value.equals("date")){
			trueFunction = "oooc:cell-content-is-date()";
		}
		else if(value.equals("time")){
			trueFunction = "oooc:cell-content-is-time()";
		}
		else if(value.equals("any value")){
			trueFunction = "oooc:cell-content-is-text()";
		}
		
		return trueFunction;
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
