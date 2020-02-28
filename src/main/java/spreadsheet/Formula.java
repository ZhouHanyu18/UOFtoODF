package spreadsheet;

public class Formula {
	
	//C3 >>> [.C3] or C3:C8 >>> [.C3:.C8]
	private static String conv_cell_name(String val){
		int index = 0;
		String name = "";
		
		index = val.indexOf(":");
		if(index == -1){
			name = "[" + "." + val + "]";
		}else{
			String cell1 = val.substring(0,index);
			String cell2 = val.substring(index + 1);
			
			name = "[." + cell1 + ":." + cell2 + "]";
		}
		
		return name;
	}
	
	private static int get_one_parameter(String val, int offset){
		char c;
		int count = 0;
		int end = 0;
		int len = val.length();
		
		if(!val.contains(",") || val.lastIndexOf(",")<offset){
			return len-1;
		}
		
		for(int i=offset; i<len; i++){
			c = val.charAt(i);
			if(c == '('){
				count ++;
			}
			if(c == ')'&&(--count)==0){
				end = i+1;
				break;
			}
			if(c == ',' && count==0){
				end = i;
				break;
			}
		}
		
		return end;
	}
	
	//SUM(A1:C1)>0
	//=AVERAGE(C3:C8) >>> =AVERAGE([.C3:.C8])
	//=AVERAGE(C3,C8) >>> =AVERAGE(.C3.;.C8)
	//=语言平均成绩 >>> =语言平均成绩
	//=AVERAGE(SUM(E3:E5),SUM(D3:D4,D5)) >>> =AVERAGE(SUM([.E3:.E5]);SUM([.D3:.D4];[.D5]))
	public static String get_formula(String val){
		int offset = 0;
		int index = 0;
		String formula = "";
		String oneParameter = "";
		
		try{
			String postStr = "";
			
			if(!val.contains("(")){
				return val;
			}
			if(!val.endsWith(")")){
				index = val.lastIndexOf(")");
				postStr = val.substring(index+1);
				val = val.substring(0,index+1);
			}
			
			index = val.indexOf("(");
			formula += val.substring(0,index) + "(";
			offset = index + 1;
			
			while(index != val.length()-1){
				index = get_one_parameter(val, offset);
				oneParameter = val.substring(offset,index);
				if(oneParameter.contains("(")){
					formula += get_formula(oneParameter);
				}
				else {
					formula += conv_cell_name(oneParameter);
				}
				
				offset = index + 1;
				if(offset != val.length()){
					formula += ";";
				}
			}	
			
			formula += ")" + postStr;
		}catch(Exception e){
			formula = "";
			System.err.println(e.getMessage());
			System.err.println("Invalid parameter: can not get formula.");
		}
		
		return formula;
	}
}
