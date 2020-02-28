package spreadsheet;

public class Cell_Address {
	
	//1 >>> A, 26>>>Z,27>>>AA...
	public static String num_to_uppercase(int colNum){
		int i1, i2;
		char c = 'A';
		String rst = "";
		
		i2 = colNum;
		while(i2 > 0){
			i1 = i2 % 26;
			i2 = i2 / 26;
			
			if(i1 == 0){
				c = 'Z';
				i2 --;
			}else{
				c = 'A';
				c += i1 - 1;
			}
			
			rst = String.valueOf(c) + rst;
		}	
		
		return rst;
	}
	
	//A>>>1,Z>>>26,AA>>>27,...
	public static int uppercase_to_num(String letters) throws Exception{
		int num = 0;
		int len = letters.length();
		
		for(int i=0; i<len; i++){
			char c = letters.charAt(i);
			
			if(!Character.isUpperCase(c)){
				throw new Exception("The letter must be in a uppercase");
			}
			int d = c - 'A' + 1;
			for(int j=0; j<(len-i-1); j++){
				d = d * 26;
			}
			num += d;
		}
		
		return num;
	}
	
	//'工作表1'!$C$9 >>> 工作表1
	public static String get_table_name(String val){
		int index1 = 0;
		int index2 = 0;
		String name = "";
		
		index1 = val.indexOf("'");
		index2 = val.indexOf("'",index1+1);
		if(index1!=-1&&index2!=-1){
			name = val.substring(index1+1,index2);
		}
		
		return name;
	}
	
	//'工作表1'!$C$9 >>> C9 
	private static String get_cell_address(String val) throws Exception{
		int index1 = 0;
		int index2 = 0;
		String address = "";
		
		index1 = val.indexOf("$");
		index2 = val.indexOf("$",index1+1);
		address += val.substring(index1+1,index2);
		address += val.substring(index2+1);
		
		return address;
	}
	
	//$B$2:$E$18>>>工作表1.B2:工作表1.E18 or '工作表1'!$C$9 >>> 工作表1.C9 
	public static String get_cell_range(String val,String tableName){
		int index = 0;
		String range = "";
		
		try{
			index = val.indexOf(":");
			if(index == -1){
				range += tableName + "." + get_cell_address(val);
			}else{
				String sub1 = val.substring(0,index);
				String sub2 = val.substring(index+1);
				
				range += tableName + "." + get_cell_address(sub1);
				range += ":";
				range += tableName + "." + get_cell_address(sub2);
			}
		}catch(Exception e){
			range = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get cell range.");
		}
		return range;
	}
	
	//'工作表1'!$C$9 >>> $工作表1.$C$9 
	public static String get_base_address(String val,String tableName){
		int index1 = 0;
		int index2 = val.length();
		String base = "";
		
		try{
			if(val.contains("!")){
				index1 = val.indexOf("!") + 1;
			}
			if(val.contains(":")){
				index2 = val.indexOf(":");
			}
			
			base = "$" + tableName + "." + val.substring(index1,index2);
		}catch(Exception e){
			base = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get base cell address.");
		}
		return base;
	}
}
