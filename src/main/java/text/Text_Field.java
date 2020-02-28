package text;

import java.util.ArrayList;
import java.util.Stack;

public class Text_Field {
	//the end element name for text-field
	private static Stack<String> _end_ele_stack = new Stack<String>();
	//store variables to generate <text:sequence-decl>
	private static ArrayList<String> _seq_varialbes = new ArrayList<String>();
	
	//initialize
	public static void init(){
		_end_ele_stack.clear();
		_seq_varialbes.clear();
	}
	
	//parse the @text:name from the code-string
	private static String parse_seq_vari(String code){
		int ind1 = code.indexOf(" ");
		int ind2 = code.indexOf(" ",ind1+1);
		String vari = "";

		if(ind1 != -1 && ind2 != -1){
			vari = code.substring(ind1+1,ind2);
		}
		
		return vari;
	}
	
	public static void add_seq_vari(String code){
		String vari = parse_seq_vari(code);
		
		if(!vari.equals("")){
			_seq_varialbes.add(vari);
		}
	}
	
	public static String create_seq_decls(){
		String decls = "";
		
		decls = "<text:sequence-decls>";
		for(int i = 0; i < _seq_varialbes.size(); i ++){
			decls += "<text:sequence-decl text:display-outline-level=\"0\"";
			decls += " text:name=\"" + _seq_varialbes.get(i) + "\"/>";
		}
		decls += "</text:sequence-decls>";
		
		return decls;
	}
	
	//return the end name
	public static String get_end_ele(){
		String end = "";
		
		end = _end_ele_stack.pop();
		end = (end==null) ? "" : end;
		
		return end;
	}
	
	private static String parse_field_type(String code){
		String type = "";
		int ind = code.indexOf(" ");
		
		code = code.toUpperCase();
		if(ind != -1){
			type = code.substring(0,ind);
		}
		
		return type;
	}
	
	private static String parse_field_format(String code){
		String format = "";
		int ind = code.lastIndexOf(" ");
		
		if(ind != -1){
			format = code.substring(ind + 1);
		}
		
		return format;
	}
	
	//return the start name
	public static String process_field(String code){
		String startEle = "";
		String endEle = "";
		String type = "";
		
		type = parse_field_type(code);
		
		if(type.equals("DATE")){				//日期
			startEle = "<text:date>";
			endEle = "</text:date>";
		}
		
		else if(type.equals("TIME")){			//时间
			startEle = "<text:time>";
			endEle = "</text:time>";
		}
		
		else if(type.equals("NUMWORDS")){		//字数
			startEle = "<text:word-count>";
			endEle = "</text:word-count>";
		}
		
		else if(type.equals("NUMCHARS")){		//字符数
			startEle = "<text:character-count>";
			endEle = "</text:character-count>";
		}
		
		else if(type.equals("EDITTIME")){		//编辑时间
			startEle = "<text:editing-duration>";
			endEle = "</text:editing-duration>";
		}
		
		else if(type.equals("PAGE")){			//页码
			String format = parse_field_format(code);
			
			startEle = "<text:page-number text:select-page=\"current\"";
			startEle += " style:num-format=\"" + conv_num_format(format) + "\">";
			
			endEle = "</text:page-number>";
		}
		
		else if(type.equals("NUMPAGES")){		//页数
			startEle = "<text:page-count>";
			endEle = "</text:page-count>";
		}
		
		else if(type.equals("AUTHOR")){			//作者
			startEle = "<text:initial-creator>";
			endEle = "</text:initial-creator>";
		}
		
		else if(type.equals("REVNUM")){			//修订次数
			startEle = "<text:editing-cycles>";
			endEle = "</text:editing-cycles>";
		}
		
		else if(type.equals("FILENAME")){		//文件名
			startEle = "<text:file-name>";
			endEle = "</text:file-name>";
		}
		
		else if(type.equals("SEQ")){			//题注
			String vari = parse_seq_vari(code);
			String format = parse_field_format(code);
			
			startEle = "<text:sequence";
			startEle += " text:name=\"" + vari + "\"";
			startEle += " text:formula=\"ooow:" + vari + "+1\"";
			startEle += " style:num-format=\"" + conv_num_format(format) + "\">";
			
			endEle += "</text:sequence>";
		}
		
		_end_ele_stack.push(endEle);
		
		return startEle;
	}
	
	private static String conv_num_format(String uofVal){
		String odfVal = "1";
		
		if(uofVal == null){
			
		}
		else if(uofVal.equals("Arabic")||uofVal.equals("ARABIC")){
			odfVal = "1";
		}
		else if(uofVal.equals("alphabetic")){
			odfVal = "a";
		}
		else if(uofVal.equals("ALPHABETIC")){
			odfVal = "A";
		}
		else if(uofVal.equals("roman")){
			odfVal = "i";
		}
		else if(uofVal.equals("ROMAN")){
			odfVal = "I";
		}
		else if(uofVal.equals("CHINESENUM3")){
			odfVal = "一, 二, 三, ...";
		}
		else if(uofVal.equals("CHINESENUM2")){
			odfVal = "壹, 贰, 叁, ...";
		}
		else if(uofVal.equals("ZODIAC1")){
			odfVal = "甲, 乙, 丙, ...";
		}
		else if(uofVal.equals("ZODIAC2")){
			odfVal = "子, 丑, 寅, ...";
		}
		else if(uofVal.equals("GB3")){
			odfVal = "①, ②, ③, ...";
		}
		
		return odfVal;
	}
}
