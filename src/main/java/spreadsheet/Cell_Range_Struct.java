package spreadsheet;

/**
 * This struct contains a cell range which is 
 * represented by four Integers. The cell range
 * is belonged to a Validation or a cell-style.
 * @author xie
 *
 */
public class Cell_Range_Struct {
	private String _table_name = "";		//��������
	
	private String _valid_name = "";		//������Ч����(starts with "VN")
	private String _style_name = "";		//��Ԫ��ʽ����(starts with "SN")
	
	private int _col_start = 0;				//��ʼ��Ԫ�� �к�
	private int _row_start = 0;				//��ʼ��Ԫ�� �к�
	private int _col_end = 0;				//������Ԫ�� �к�
	private int _row_end = 0;				//������Ԫ�� �к�
	
	public String get_base_address(){
		String base = "";
		
		base = _table_name + ".";
		base += Cell_Address.num_to_uppercase(_col_start);
		base += (_row_start==0)?1:_row_start;
		
		return base;
	}
	
	//'������1'!$2:$2(δ����)
	//'������1'!$A:$A
	//'������1'!$A$1 or '������1'!$A$1:$C$2
	public void process(String val) throws Exception{
		int index1, index2, index3;
		String colName = "";
		String rowName = "";
		val.trim();
		
		_table_name = Cell_Address.get_table_name(val);
		
		index1 = val.indexOf("$");
		index2 = val.indexOf("$",index1 + 1);
		index3 = val.indexOf(":");
		if(index3 != -1 && index2 > index3){	//$A:$A or $2:$2
			colName = val.substring(index1 + 1, index3);
			if(Character.isUpperCase(colName.charAt(0))){
				_col_start = Cell_Address.uppercase_to_num(colName);
			}
			return;
		}
		
		colName = val.substring(index1 + 1, index2);
		_col_start = Cell_Address.uppercase_to_num(colName);
		
		if(!val.contains(":")){
			rowName = val.substring(index2 + 1);
			_row_start = Integer.parseInt(rowName);
			_col_end = _col_start;
			_row_end = _row_start;
		}
		else{
			index3 = val.indexOf(":");
			rowName = val.substring(index2 + 1, index3);
			_row_start = Integer.parseInt(rowName);
			
			index1 = val.indexOf("$",index3);
			index2 = val.indexOf("$",index1 + 1);
			colName = val.substring(index1 + 1, index2);
			_col_end = Cell_Address.uppercase_to_num(colName);
			rowName = val.substring(index2 + 1);
			_row_end = Integer.parseInt(rowName);
		}
	}
	
	public boolean in_range(int col, int row){
		return (col>=_col_start) && (col<=_col_end) 
			&& (row>=_row_start) && (row<=_row_end);
	}
	
	public boolean in_col_range(int col){
		return (col==_col_start) && (_col_end==0)
			&&(_row_start==0) && (_row_end==0);
	}
	
	public void set_table_name(String name){
		_table_name = name;
	}	
	public String get_table_name(){
		return _table_name;
	}
	
	//validation name should start with "VN"
	public void set_valid_name(String name){
		_valid_name = "VN" + name;
	}	
	public String get_valid_name(){
		return _valid_name;
	}
	
	//cell style name should start with "SN"
	public void set_style_name(String name){
		_style_name = "SN" + name;
	}
	public String get_style_name(){
		return _style_name;
	}
	
	public void set_col_start(int start){
		_col_start = start;
	}	
	public int get_col_start(){
		return _col_start;
	}
	
	public void set_row_start(int start){
		_row_start = start;
	}	
	public int get_row_start(){
		return _row_start;
	}
	
	public void set_col_end(int end){
		_col_end = end;
	}
	public int get_col_end(){
		return _col_end;
	}
	
	public void set_row_end(int end){
		_row_end = end;
	}
	public int get_row_end(){
		return _row_end;
	}
}
