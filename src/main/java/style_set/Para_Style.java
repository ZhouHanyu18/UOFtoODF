package style_set;

import org.xml.sax.Attributes;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;

/**
 * Create a <style:style> whose style:family
 * is "paragraph" for every <uof:段落式样> in 
 * <uof:式样集> or <字:段落属性> in body.
 * @author xie
 *
 */
public class Para_Style extends Common_Pro{
	private static String _chs = "";
	//Attributes of <style:style>
	private static String _ele_atts = "";
	//"default" type of paragraph-style in UOF,
	//this type of style goes to styles.xml
	private static boolean _is_default = false;
	//Attributes of <style:paragraph-properties>
	private static String _para_pro = "";
	//Attributes of <style:text-properties>
	private static String _sent_pro = "";
	//<style:tab-stops>
	private static String _tab_stops = "";
	//<style:drop-cap>
	private static String _drop_cap = "";
	//<style:background-image>
	private static String _background_image = "";
	//name of indent-element
	private static String _indent_name = "";
	//fo:margin-left
	private static String _margin_left = "";
	//<字:段间距>
	private static String _para_space = "";
	private static String _style_name = "";	
	private static boolean _sent_tag = false;
	
	private static void clear(){
		_ele_atts = "";
		_is_default = false;
		_para_pro = "";
		_sent_pro = "";
		_margin_left = "";
		_tab_stops = "";
		_drop_cap = "";
		_background_image = "";
	}
	
	//Store the result
	private static void commit_result(){
		String rst = "";
		String subEles = _drop_cap + _tab_stops + _background_image;
		
		rst = "<style:style style:family=\"paragraph\"" + _ele_atts + ">";
		rst += "<style:paragraph-properties"+ _para_pro + ">" + subEles;
		rst += "</style:paragraph-properties>";
		if(!_sent_pro.equals("")){
			rst += "<style:text-properties" + _sent_pro + "/>";
		}
		rst += "</style:style>";
		
		if (_is_default){
			Stored_Data.addStylesInStylesXml(rst);
		} else{
			Stored_Data.addAutoStylesInContentXml(rst);
		}
		
		clear();
	}
	
	//Return <style:paragraph-properties> element for 
	//the styles getting from <字:段落>. Else If it's 
	//empty, return "".Invoked by text.Para_Pro
	public static String get_para_pro(){
		String paraPro = "";		
		String subEles = _drop_cap + _tab_stops + _background_image;
		
		if(_para_pro.equals("") && subEles.equals("")){
			paraPro = "";
		}else {
			paraPro = "<style:paragraph-properties" + _para_pro + ">" + subEles;
			paraPro += "</style:paragraph-properties>";
		}
		if(!_sent_pro.equals("")){
			paraPro += "<style:text-properties" + _sent_pro + "/>";
		}
		
		clear();
		return paraPro;
	}
	
	private static String add_att(String attName,String val){
		return " " + attName + "=\"" + val + "\"";
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		if (qName.equals("uof:段落式样")){
			attVal = atts.getValue("字:类型");
			attVal = (attVal==null) ? "" : attVal;
			//The default style goes to styles.xml
			if (attVal.equals("default") || attVal.equals("custom")){
				_is_default = true;
			}
			
			if ((attVal = atts.getValue("字:标识符")) != null){
				_ele_atts += " style:name=\"" + attVal + "\"";
				_style_name += attVal;
			}
			if ((attVal = atts.getValue("字:基式样引用")) != null){
				_ele_atts += " style:parent-style-name=\"" + attVal + "\"";
			}
			if((attVal = atts.getValue("字:别名")) != null){
				_ele_atts += " style:display-name=\"" + attVal + "\"";				
			}
		}
		
		else if (_sent_tag){
			Sent_Style.process_start(qName,atts);
		}
		else if (qName.equals("字:句属性")){
			_sent_tag = true;
		}
		
		//对齐
		else if (qName.equals("字:对齐")){
			if ((attVal = atts.getValue("字:水平对齐")) != null){
				_para_pro += add_att("fo:text-align",conv_text_align(attVal));
			}
			if ((attVal = atts.getValue("字:文字对齐")) != null){
				_para_pro += add_att("fo:vertical-align",conv_vertical_align(attVal));
			}
		}
		
		//缩进	
		else if(qName.equals("字:左")){
			_indent_name = "fo:margin-left";
		}
		else if(qName.equals("字:右")){
			_indent_name = "fo:margin-right";
		}
		else if(qName.equals("字:首行")){
			_indent_name = "fo:text-indent";
		}
		else if(qName.equals("字:绝对") && atts.getValue("字:值") != null){
			attVal = atts.getValue("字:值");
			
			if(_indent_name.equals("fo:margin-left")){
				_margin_left = add_att(_indent_name, attVal + Common_Data.get_unit());
			}
			else if(_indent_name.equals("fo:margin-right")){
				_para_pro += add_att(_indent_name, attVal + Common_Data.get_unit());
			}
			else if(_indent_name.equals("fo:text-indent")){
				if(attVal.startsWith("-")){		//the value is negative
					String positive = attVal.substring(1);
					_para_pro += add_att("fo:margin-left",positive + Common_Data.get_unit());
				}
				else {
					_para_pro += _margin_left;
				}
				_para_pro += add_att(_indent_name, attVal + Common_Data.get_unit());
			}
		}
		else if (qName.equals("字:相对") && atts.getValue("字:值") != null){
			float words = Float.parseFloat(atts.getValue("字:值"));
			float size = Sent_Style.get_fontsize() * words;			
			
			if(_indent_name.equals("fo:margin-left")){
				_margin_left = add_att(_indent_name, size + "pt");
			}
			else if(_indent_name.equals("fo:margin-right")){
				_para_pro += add_att(_indent_name, size + "pt");
			}
			else if(_indent_name.equals("fo:text-indent")){
				if(size < 0){		//the value is negative
					_para_pro += add_att("fo:margin-left",Math.abs(size) + "pt");
				}
				else {
					_para_pro += _margin_left;
				}
				_para_pro += add_att(_indent_name, size + "pt");
			}
		}
		
		//段间距
		else if(qName.equals("字:段前距")){
			_para_space = "fo:margin-top";
		}
		else if(qName.equals("字:段后距")){
			_para_space = "fo:margin-bottom";
		}
		else if(qName.equals("字:绝对值")){
			if((attVal = atts.getValue("字:值")) != null){ 
				_para_pro += add_att(_para_space, attVal + Common_Data.get_unit());
			}
		}
		else if (qName.equals("字:相对值")){
			if((attVal = atts.getValue("字:值")) != null){  
				float words = Float.parseFloat(attVal);
				float size = Sent_Style.get_fontsize() * words;
				_para_pro += add_att(_para_space, size + Common_Data.get_unit());
			}
		}
		
		//行距
		else if (qName.equals("字:行距")){
			if ((attVal = atts.getValue("字:类型")) != null){
				if (attVal.equals("fixed")){
					attVal = atts.getValue("字:值");
					_para_pro += " fo:line-height=\"" + attVal + "\"";
				}
				else if (attVal.equals("multi-lines")){
					attVal = atts.getValue("字:值");
					_para_pro += " fo:line-height=\"" + (Float.valueOf(attVal)*100) + "%" + "\"";
				}
				else if (attVal.equals("at-least")){
					attVal = atts.getValue("字:值");
					_para_pro += " style:line-height-at-least=\"" + attVal + "\"";
				}
				else if (attVal.equals("line-space")){
					attVal = atts.getValue("字:值");
					_para_pro += " style:line-spacing=\"" + attVal + "\"";
				}
			}
		}
		
		else if (qName.equals("字:段中不分页")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true"))
					_para_pro += " fo:keep-together=\"always\"";
				else
					_para_pro += " fo:keep-together=\"auto\"";
			}
		}
		else if (qName.equals("字:与下段同页")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true")){
					_para_pro += " fo:keep-with-next=\"always\"";
				}
				else{
					_para_pro += " fo:keep-with-next=\"auto\"";
				}
			}
		}
		else if (qName.equals("字:段前分页")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("false")){
					_para_pro+= " fo:break-before=\"auto\"";
				}
				else{
					_para_pro += " fo:break-before=\"page\"";
				}
			}
		}
		
		else if (qName.equals("字:边框")){
			
		}
		else if (qName.equals("uof:上")||qName.equals("uof:下")
				||qName.equals("uof:左")||qName.equals("uof:右")){
			_para_pro += get_borders(qName, atts);
		}
		
		//填充
		else if (qName.equals("字:填充")){
			//todo
		}
		
		else if(qName.equals("图:图案")){
			String back = atts.getValue("图:背景色");
			String fore = atts.getValue("图:前景色");

			if(back != null && !back.equals("auto")){
				_para_pro += " fo:background-color=\"" + back + "\"";
			}
			else if(fore != null && !fore.equals("auto")){
				_para_pro += " fo:background-color=\"" + fore + "\"";
			}
		}
		else if (qName.equals("图:图片")){
			_background_image += "<style:background-image";
			if ((attVal = atts.getValue("图:对齐方式")) != null){
				if (attVal.equals("stretch")){
					_background_image += " style:repeat=\"streatch\"";
				}
				else if (attVal.equals("tile")){
					_background_image += " style:repeat=\"repeat\"";
				}
				else{
					_background_image += " style:repeat=\"no-repeat\"";
				}
			}
			if ((attVal = atts.getValue("图:图形引用")) != null){
				//这里,图形引用是对象集中的一个式样标识符,需要通过该式样标识符找到对应的文件存储路径,赋给xlink:href
				//to do
				_background_image += " xlink:type=\"simple\" xlink:actuate=\"onLoad\"";
			}
			if ((attVal = atts.getValue("图:类型")) != null){
				_background_image += " style:filter-name=\"" + attVal + "\"";
			}
		}
		else if (qName.equals("字:制表位设置")){
			_tab_stops += "<style:tab-stops>";
		}
		else if (qName.equals("字:制表位")){
			_tab_stops += "<style:tab-stop";
			if ((attVal = atts.getValue("字:位置")) != null){
				_tab_stops += " style:position=\"" 
					+ attVal + Common_Data.get_unit() + "\"";
			}
			if ((attVal = atts.getValue("字:类型")) != null)
				if (attVal.equals("left") 
					|| attVal.equals("right") 
					|| attVal.equals("center")){
					_tab_stops += " style:type=\"" + attVal + "\"";
				}
			if ((attVal = atts.getValue("字:前导符")) != null){
				String style = "";
				
				if(attVal.equals("0")){
					style = "none";
				}
				else if(attVal.equals("1")){
					style = "dotted";
				}
				else if(attVal.equals("2")){
					style = "dash";
				}
				else if(attVal.equals("3")){
					style = "solid";
				}
				else if(attVal.equals("4")){
					style = "dotted";
				}
				
				_tab_stops += " style:leader-text=\".\"";
				_tab_stops += " style:leader-style=\"" + style + "\"";
			}
			_tab_stops += "/>";
		}
		else if (qName.equals("字:对齐网格")){
			if ((attVal = atts.getValue("字:值")) != null){
				_para_pro += " style:snap-to-layout-grid=\"" + attVal + "\"";			
			}
		}
		else if (qName.equals("字:首字下沉")){
			_drop_cap += "<style:drop-cap";
			//需要创建一个句式样
			if ((attVal = atts.getValue("字:字体引用")) != null){
				_para_pro += " style:style-name=\"d_" + _style_name + "\"";
				String sent = "<style:style style:family=\"text\" style:name=\"d_" 
					+ _style_name +"\"><style:text-properties style:font-name=\"" + attVal +">";
				
				if (_is_default){
					Stored_Data.addStylesInStylesXml(sent);
				}else{					
					Stored_Data.addAutoStylesInContentXml(sent);
				}
				
				_para_pro += " style:style-name=\"d_" + _style_name + "\"";
			}
			if ((attVal = atts.getValue("字:字符数")) != null){
				if (attVal.equals("1")){
					_drop_cap += " style:length=\"word\"";
				}
				else{
					_drop_cap += " style:length=\"" + attVal +"\"";
				}
			}
			if ((attVal = atts.getValue("字:行数")) != null){
				_drop_cap += " style:lines=\"" + attVal +"\"";
			}
			if ((attVal = atts.getValue("字:间距")) != null){
				_drop_cap += " style:distance=\"" + attVal + Common_Data.get_unit() + "\"";
			}
			_drop_cap += "/>";
		}
		else if (qName.equals("字:取消断字")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true")){
					_para_pro += " style:line-break=\"strict\"";
				}
				else{
					_para_pro += " style:line-break=\"normal\"";
				}
			}
		}
		else if (qName.equals("字:取消行号")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true")){
					_para_pro += " text:number-lines=\"false\"";
				}
				else{
					_para_pro += " text:number-lines=\"true\"";
				}
			}
		}
		else if (qName.equals("字:是否行首标点压缩 ")){
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true")){
					_para_pro += " style:punctuation-wrap=\"hanging\"";
				}
				else{
					_para_pro += " style:punctuation-wrap=\"simple\"";
				}
			}
		}
		else if (qName.equals("字:自动调整中英文字符间距"))
			if ((attVal = atts.getValue("字:值")) != null){
				if (attVal.equals("true"))
					_para_pro += " style:text-autospace=\"ideograph-alpha\"";
				else
					_para_pro += " style:text-autospace=\"none\"";
			}
		
	}
	public static void process_chars(String chs){
		if(_sent_tag){
			Sent_Style.process_chars(chs);
		}else {
			_chs = chs;	
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("uof:段落式样")){
			commit_result();
		}
		else if (qName.equals("字:句属性")){
			_sent_pro = Sent_Style.get_text_pro();
			_sent_tag = false;
		}
		else if(_sent_tag){
			Sent_Style.process_end(qName);
		}
		else if (qName.equals("字:大纲级别")){
			_para_pro += " style:default-outlinelevel=\"" + _chs + "\"";
		}
		else if (qName.equals("字:孤行控制")){
			_para_pro += " fo:widows=\"" + _chs + "\"";
		}
		else if (qName.equals("字:寡行控制")){
			_para_pro += " fo:orphans=\"" + _chs + "\"";
		}
		else if (qName.equals("字:制表位设置")){
			_tab_stops += "</style:tab-stops>";
		}
		else if (qName.equals("字:边框")){
			//	if (_shadow_tag){			//??????????????????????????
			//		_para_properties += " style:shadow=\"#808080 0.18cm 0.18cm\"";
			//	}
		}
		//else if (qName.equals("图:颜色")){
		//	_para_pro += " fo:background-color=\"" + _chs + "\"";
		//}
		
		_chs = "";
	}
	
	private static String conv_text_align(String val){
		String align = "";
		
		if(val.equals("center")){
			align = "center";
		}
		else if(val.equals("justified")){
			align = "justify";
		}
		else if(val.equals("left")){
			align = "start";
		}
		else if(val.equals("right")){
			align = "end";
		}
		
		return align;
	}

	private static String conv_vertical_align(String val){
		String convVal = "auto";
		
		if(val.equals("top")){
			convVal = "top";
		}
		else if(val.equals("center")){
			convVal = "middle";
		}
		else if(val.equals("bottom")){
			convVal = "bottom";
		}
		else if(val.equals("auto")){
			convVal = "auto";
		}
		
		return convVal;
	}
}
