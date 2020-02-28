package presentation;

import org.xml.sax.Attributes;

import temp_structs.Common_Data;
import temp_structs.Object_Set_Data;

/**
 *
 * @author xie
 *
 */
public class Draw_Page {
	//tag for <演:幻灯片备注>
	private static boolean _notes_tag = false;
	//content of <draw:page>
	private static String _content = "";
	//attributes of <draw:page>
	private static String _ele_atts = "";
	//counter for <演:幻灯片>
	private static int _dp_counter = 0;
	//演:放映动画
	private static boolean _animation_tag = false;
	//type of placeholder
	private static String _holder_type = "";


	//initialize
	public static void init(){
		_dp_counter = 0;
		_holder_type = "";
	}

	public static String get_holder_type(){
		return _holder_type;
	}

	private static void clear(){
		_content = "";
		_ele_atts = "";
	}

	public static String get_result(){
		String page = "";

		page = "<draw:page" + _ele_atts + ">";
		page += _content;
		page += "</draw:page>";

		clear();
		return page;
	}

	private static String add_att(String name,String val){
		return (" " + name + "=\"" + val + "\"");
	}
	private static String skip_null(String val){
		return (val==null) ? "" : val;
	}
	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("演:幻灯片备注")){
			_notes_tag = true;

			_content += "<presentation:notes>";
		}

		else if(_animation_tag){
			Animation.process_start(qName,atts);
		}
		else if(qName.equals("演:幻灯片")){
			attVal = atts.getValue("演:标识符");
			if(attVal != null){
				_ele_atts += add_att("draw:name",attVal);
			}

			_dp_counter ++;
			String name = Draw_Page_Style.get_style_name(_dp_counter);
			if(name != null){
				_ele_atts += add_att("draw:style-name",name);
			}

			attVal = atts.getValue("演:母版引用");
			_ele_atts += add_att("draw:master-page-name",attVal);

			attVal = atts.getValue("演:页面版式引用");
			_ele_atts += add_att("presentation:presentation-page-layout-name",attVal);
		}
		else if(qName.equals("uof:锚点")){
			String drawAtts = "";

			String drawID = skip_null(atts.getValue("uof:图形引用"));
			String drawing = Object_Set_Data.getDrawing(drawID);

			attVal = skip_null(atts.getValue("uof:占位符"));
			_holder_type = Presentation_Page_Layout.conv_PH_type(attVal);

			if(_holder_type.equals("title")){
				drawAtts += add_att("presentation:style-name","pr_"+Presentation_Style.get_title_name());
			}
			else if(_holder_type.equals("outline") || _holder_type.equals("subtitle")){
				drawAtts += add_att("presentation:style-name","pr_"+Presentation_Style.get_outline_name());
			}
			else if(_holder_type.equals("notes")){
				drawAtts += add_att("presentation:style-name","pr_"+Presentation_Style.get_notes_name());
			}

			drawAtts += add_att("draw:layer","layout");

			if (drawing.startsWith("<draw:line")) {
				float x = Float.valueOf(atts.getValue("uof:x坐标"));
				float y = Float.valueOf(atts.getValue("uof:y坐标"));
				float width = Float.valueOf(atts.getValue("uof:宽度"));
				float height = Float.valueOf(atts.getValue("uof:高度"));
				float x1, y1, x2, y2;
				if (Object_Set_Data.getDrawingOverturn(drawID) != null) {
					x1 = x;
					y1 = y + height;
					x2 = x + width;
					y2 = y;
				}
				else {
					x1 = x;
					y1 = y;
					x2 = x + width;
					y2 = y + height;
				}
				drawAtts += add_att("svg:x1",measure_floatval(x1));
				drawAtts += add_att("svg:y1",measure_floatval(y1));
				drawAtts += add_att("svg:x2",measure_floatval(x2));
				drawAtts += add_att("svg:y2",measure_floatval(y2));
			}
			else {
				drawAtts += add_att("svg:x",measure_val(atts.getValue("uof:x坐标")));
				drawAtts += add_att("svg:y",measure_val(atts.getValue("uof:y坐标")));
			}

			if (_holder_type.length() != 0) {
				//drawAtts += add_att("presentation:placeholder","true");
				drawAtts += add_att("presentation:class",_holder_type);
			}

			if (drawing.contains("draw:frame")) {
				int i = drawing.indexOf("<draw:frame");
				while (i >= 0) {
					i = drawing.indexOf("draw:style-name", i);
					i = drawing.indexOf("\"", i) + 1;
					String fid = drawing.substring(i, drawing.indexOf("\"", i));
					if(Object_Set_Data.getRefObj(fid) != null) {
						String image = "<draw:image xlink:href=\""
							+ Object_Set_Data.getOtherObj(Object_Set_Data.getRefObj(fid))
							+"\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>";
						int index = drawing.indexOf("</", i);
						drawing = drawing.substring(0, index) + image + drawing.substring(index);
					}
					i = drawing.indexOf("<draw:frame", i);
				}
			}
			if (!drawing.startsWith("<draw:g")) {
				int ind = drawing.indexOf(" ");
				drawing = drawing.substring(0,ind) + drawAtts + drawing.substring(ind);
			}

			_content += drawing;
		}
		else if(qName.equals("演:放映动画")){
			_animation_tag = true;
		}

		else if(qName.equals("演:切换")){

		}
	}

	public static void process_chars(String chs){
		if(_animation_tag){
			Animation.process_chars(chs);
		}
	}

	public static void process_end(String qName){
		if(_notes_tag){
			if(qName.equals("演:幻灯片备注")){
				_notes_tag = false;
				_content += "</presentation:notes>";
			}
		}
		else if(qName.equals("演:幻灯片")){
			//
		}
		else if(qName.equals("演:放映动画")){
			_animation_tag = false;
			_content += Animation.getResult();
		}
		else if(_animation_tag){
			Animation.process_end(qName);
		}
	}
	//
	private static String measure_val(String val){
		return Float.valueOf(val) * Common_Data.get_graphratio() + Common_Data.get_unit();
	}
	private static String measure_floatval(float val){
		return val * Common_Data.get_graphratio() + Common_Data.get_unit();
	}
}
