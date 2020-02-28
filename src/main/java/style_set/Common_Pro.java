package style_set;

import org.xml.sax.Attributes;
import temp_structs.Common_Data;

public class Common_Pro {
	   private static boolean _double_tag = false;

	//处理纸张的属性
	protected static String get_page(Attributes atts){
		String str = "";
		String attVal = "";

		if((attVal=atts.getValue("uof:宽度"))!=null){
			str += " fo:page-width=\"" + attVal + Common_Data.get_unit() + "\"";
		}
		if((attVal=atts.getValue("uof:高度"))!=null){
			str += " fo:page-height=\"" + attVal + Common_Data.get_unit() + "\"";
		}
		if((attVal=atts.getValue("uof:纸型"))!=null){
			str += " style:paper-tray-name=\"" + attVal + "\"";
		}

		return str;
	}

	//处理页边距的属性
	protected static String get_margins(Attributes atts){
		String str = "";
		String att_val = "";

		if((att_val=atts.getValue("uof:上"))!=null){
			str += " fo:margin-top=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("uof:下"))!=null){
			str += " fo:margin-bottom=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("uof:左"))!=null){
			str += " fo:margin-left=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("uof:右"))!=null){
			str += " fo:margin-right=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		return str;
	}

	//处理 单元格边距 的子元素及其属性
	protected static String get_padding(Attributes atts){
		String str = "";
		String att_val = "";

		if((att_val=atts.getValue("字:上"))!=null){
			str += " fo:padding-top=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("字:下"))!=null){
			str += " fo:padding-bottom=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("字:左"))!=null){
			str += " fo:padding-left=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		if((att_val=atts.getValue("字:右"))!=null){
			str += " fo:padding-right=\"" + att_val + Common_Data.get_unit() + "\"";
		}
		return str;
	}

	//处理 边框 的子元素及其属性
	protected static String get_borders(String qName, Attributes atts){
		String str = "";

		if(qName.equals("uof:左")){
			str += " fo:border-left=\"" + get_border_pro(atts) + "\"";
		}
		if(qName.equals("uof:上")){
			str += " fo:border-top=\"" + get_border_pro(atts) + "\"";
		}
		if(qName.equals("uof:右")){
			str += " fo:border-right=\"" + get_border_pro(atts) + "\"";
		}
		if(qName.equals("uof:下")){
			str += " fo:border-bottom=\"" + get_border_pro(atts) + "\"";
		}
		if(qName.equals("uof:对角线1")){
			str += " style:diagonal-tl-br=\"" + get_border_pro(atts) + "\"";

		}
		if(qName.equals("uof:对角线2")){
			str += " style:diagonal-bl-tr=\"" + get_border_pro(atts) + "\"";
		}

		return str;
	}

	private static String get_border_pro(Attributes atts){
		String rst = "";
		String attVal = "";
		String width = "";
		String type = "";
		String color = "";

		if((attVal=atts.getValue("uof:宽度"))!=null){
			width = attVal + Common_Data.get_unit();
		}
		if((attVal=atts.getValue("uof:类型"))!=null){
			type = border_style(attVal);
			_double_tag = attVal.equals("double");
		}
		if((attVal=atts.getValue("uof:颜色"))!=null){
			color = attVal.equals("auto") ? "#000000" : attVal;
		}

		if((attVal=atts.getValue("uof:类型"))!=null){
			if(attVal.equals("none")){
				rst = "none";
			}else{
				rst = width + " " + type + " " + color + " ";
			}
		}

		return rst;
	}

	private static String border_style(String val){
		String sty = "";

		if(val.contains("dash")){
			sty = "dashed";
		}
		else if(val.contains("dot")){
			sty = "dotted";
		}
		else if(val.equals("double")){
			sty = "double";
		}
		else {
			sty = "solid";
		}

		return sty;
	}

	//If the line type is double, the attribute
	//@style:border-line-width should be returned
	protected static String get_double_line_width(){
		String line = "";

		if(_double_tag){
			line = " style:border-line-width="
					+ "\"0.088cm 0.035cm 0.035cm\"";
			_double_tag = false;
		}

		return line;
	}

	//Convert num-format from UOF to ODF
	public static String conv_num_format(String val){
		String format = "1";

		if (val.equals("decimal")){
			format = "1";
		}
		else if (val.equals("lower-roman")){
			format = "i";
		}
		else if (val.equals("upper-roman")){
			format = "I";
		}
		else if (val.equals("lower-letter")){
			format = "a";
		}
		else if (val.equals("upper-letter")){
			format = "A";
		}
		else if (val.equals("ideograph-zodiac")){
			format = "子, 丑, 寅, ...";
		}
		else if(val.equals("chinese-counting")){
			format = "一, 二, 三, ...";
		}
		else if(val.equals("chinese-legal-simplified")){
			format = "壹, 贰, 叁, ...";
		}
		else if(val.equals("ideograph-traditional")){
			format = "甲, 乙, 丙, ...";
		}
		else if(val.equals("decimal-enclosed-circle-chinese")){
			format = "①, ②, ③, ...";
		}
		else if(val.equals("decimal-full-width")){
			format = "１, ２, ３, ...";
		}
		//to be continued.

		return format;
	}
}
