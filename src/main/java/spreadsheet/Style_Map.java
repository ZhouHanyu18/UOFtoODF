package spreadsheet;

import org.xml.sax.Attributes;
import convertor.IDGenerator;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;

//������ʽ��
public class Style_Map {
	private static String _chs = "";				//
	
	private static String _style_name = "";			//
	private static String _operator = "";			//������
	private static String _value_one = "";			//��:��һ������
	private static String _value_two = "";			//��:�ڶ�������
	private static String _apply_style_name = "";	//��ʽ	
	private static String _cond_type = "";			//��������formula/cell value
	
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
		
		if(qName.equals("��:������ʽ��")){
			_style_name = IDGenerator.get_cell_style_id();
		}
		else if(qName.equals("��:����")){
			_cond_type = atts.getValue("��:����");
			if(_cond_type == null){
				_cond_type = "cell value";
			}
		}
		else if(qName.equals("��:��ʽ")){
			attVal = atts.getValue("��:ʽ������");
			if(attVal != null){
				_apply_style_name = attVal;
			}
		}
	}
	
	public static void process_chars(String chs){
		_chs = chs;
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
				_base_address = range.get_base_address();
				range.set_style_name(_style_name);
				Table_Cell.add_cell_range(range);
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
		else if(qName.equals("��:����")){
			String oneMap = "";
			
			oneMap = "<style:map";
			oneMap += " style:condition=\"" + get_condition() + "\"";
			oneMap += " style:apply-style-name=\"" + _apply_style_name + "\"";
			oneMap += " style:base-cell-address=\"" + _base_address + "\"";
		    oneMap += "/>";
			
			_style_maps += oneMap;
		}
		else if(qName.equals("��:������ʽ��")){
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
