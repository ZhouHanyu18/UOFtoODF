package style_set;

public class Number_Style {
	private static String _name = "";
	private static String _type = "";
	private static String _style_code = "";

	public static String process_style(String name, String type, String styleCode){
		_name = name;
		_type = type;
		_style_code = styleCode;
		String rst = "";

		if(_type.equals("percentage")){
			rst = process_percentage_style();
		}
		else if(_type.equals("number")){
			rst = process_number_style();
		}
		else if(_type.equals("currency")){
			rst = process_currency_style();
		}
		else if(_type.equals("date")){
			rst = process_date_style();
		}
		else if(_type.equals("time")){
			rst = process_time_style();
		}

		return rst;
	}

	private static String process_percentage_style(){
		String perc = "";

		perc = "<number:percentage-style style:name=\"" + _name + "\">";
		perc += get_number_number();
		perc += "<number:text>%</number:text>";
		perc += "</number:percentage-style>";

		return perc;
	}

	private static String process_number_style(){
		String num = "";

		num = "<number:number-style style:name=\"" + _name + "\">";
		num += get_number_number();
		num += "</number:number-style>";

		return num;
	}

	private static String process_currency_style(){
		String curr = "";

		curr = "<number:currency-style";
		curr += " style:name=\"" + _name + "P0\" style:volatile=\"true\">";
		curr += get_currency_symbol();
		curr += get_number_number();
		curr += "</number:currency-style>";

		curr += "<number:currency-style style:name=\"" + _name + "\">";
		curr += "<number:text>-</number:text>";
		curr += get_currency_symbol();
		curr += get_number_number();
		curr += "<style:map style:condition=\"value()>=0\"";
		curr += " style:apply-style-name=\"" + _name + "P0\"/>";
		curr += "</number:currency-style>";

		return curr;
	}

	private static String get_currency_symbol(){
		char symb = ' ';
		String rst = "";

		int index = _style_code.indexOf("#");
		if(index > 0){
			symb = _style_code.charAt(index-1);
		}

		if(symb != ' '){
			rst = "<number:currency-symbol>" + symb + "</number:currency-symbol>";
		}
		return rst;
	}

	private static String process_date_style(){
		String date = "";

		date += "<number:date-style style:name=\"" + _name + "\">";
		if(_style_code.contains("yy")){
			date += "<number:year number:style=\"long\"/>";
			date += "<number:text>年</number:text>";
		}
		if(_style_code.contains("m")){
			date += "<number:month number:style=\"long\"/>";
			date += "<number:text>月</number:text>";
		}
		if(_style_code.contains("d")){
			date += "<number:day number:style=\"long\"/>";
			date += "<number:text>日</number:text>";
		}
		if(_style_code.contains("h")){
			date += "<number:hours number:style=\"long\"/>";
			date += "<number:text>时</number:text>";
		}
		if(_style_code.contains("mm")){
			date += "<number:minutes number:style=\"long\"/>";
			date += "<number:text>分</number:text>";
		}
		if(_style_code.contains("ss")){
			date += "<number:seconds number:style=\"long\"/>";
			date += "<number:text>秒</number:text>";
		}
		date += "</number:date-style>";

		return date;
	}

	private static String process_time_style(){
		String time = "";
		String text = "";

		time += "<number:time-style style:name=\"" + _name + "\">";
		if(_style_code.contains(":")){
			text = "<number:text>:</number:text>";
		}

		time = "<number:time-style style:name=\"" + _name + "\">";
		if(_style_code.contains("上午/下午")||_style_code.contains("AM/PM")){
			time += "<number:am-pm/>";
			time += "<number:text/>";
		}
		if(_style_code.contains("h")){
			time += "<number:hours number:style=\"long\"/>";
			if(text.equals("")){
				time += "<number:text>时</number:text>";
			}else {
				time += text;
			}
		}
		if(_style_code.contains("mm")){
			time += "<number:minutes number:style=\"long\"/>";
			if(text.equals("")){
				time += "<number:text>分</number:text>";
			}else {
				time += text;
			}
		}
		if(_style_code.contains("ss")){
			time += "<number:seconds number:style=\"long\"/>";
			if(text.equals("")){
				time += "<number:text>秒</number:text>";
			}
		}
		time += "</number:time-style>";

		return time;
	}

	private static String get_number_number(){
		String rst = "";
		int decimal = 0;
		int min = 0;
		boolean grouping = false;
		int index = 0;

		index = _style_code.indexOf(".");
		if(index != -1){
			for(int i = index+1; i < _style_code.length(); i++){
				if(_style_code.charAt(i) == '0'){
					decimal ++;
				}else {
					break;
				}
			}
			for(int j = index-1; j >= 0; j--){
				if(_style_code.charAt(j) == '0'){
					min ++;
				}else {
					break;
				}
			}
		}

		if(_style_code.contains("#,##")){
			grouping = true;
		}

		rst = "<number:number number:decimal-places=\"" + decimal + "\"";
		rst += " number:min-integer-digits=\"" + min + "\"";
		if(grouping){
			rst += " number:grouping=\"" + grouping + "\"";
		}
		rst += "/>";

		return rst;
	}
}
