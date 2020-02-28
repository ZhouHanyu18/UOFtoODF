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
	//tag for <ÑÝ:»ÃµÆÆ¬±¸×¢>
	private static boolean _notes_tag = false;
	//content of <draw:page>
	private static String _content = ""; 	
	//attributes of <draw:page>
	private static String _ele_atts = "";
	//counter for <ÑÝ:»ÃµÆÆ¬>
	private static int _dp_counter = 0;
	//ÑÝ:·ÅÓ³¶¯»­
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
		
		if(qName.equals("ÑÝ:»ÃµÆÆ¬±¸×¢")){
			_notes_tag = true;
			
			_content += "<presentation:notes>";
		}
		
		else if(_animation_tag){
			Animation.process_start(qName,atts);
		}
		else if(qName.equals("ÑÝ:»ÃµÆÆ¬")){
			attVal = atts.getValue("ÑÝ:±êÊ¶·û");
			if(attVal != null){
				_ele_atts += add_att("draw:name",attVal);
			}
			
			_dp_counter ++;
			String name = Draw_Page_Style.get_style_name(_dp_counter);
			if(name != null){
				_ele_atts += add_att("draw:style-name",name);
			}
			
			attVal = atts.getValue("ÑÝ:Ä¸°æÒýÓÃ");
			_ele_atts += add_att("draw:master-page-name",attVal);
			
			attVal = atts.getValue("ÑÝ:Ò³Ãæ°æÊ½ÒýÓÃ");
			_ele_atts += add_att("presentation:presentation-page-layout-name",attVal);
		}
		else if(qName.equals("uof:Ãªµã")){
			String drawAtts = "";
			
			String drawID = skip_null(atts.getValue("uof:Í¼ÐÎÒýÓÃ"));
			String drawing = Object_Set_Data.getDrawing(drawID);
			
			attVal = skip_null(atts.getValue("uof:Õ¼Î»·û"));
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
				float x = Float.valueOf(atts.getValue("uof:x×ø±ê"));
				float y = Float.valueOf(atts.getValue("uof:y×ø±ê"));
				float width = Float.valueOf(atts.getValue("uof:¿í¶È"));
				float height = Float.valueOf(atts.getValue("uof:¸ß¶È"));
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
				drawAtts += add_att("svg:x",measure_val(atts.getValue("uof:x×ø±ê")));
				drawAtts += add_att("svg:y",measure_val(atts.getValue("uof:y×ø±ê")));
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
		else if(qName.equals("ÑÝ:·ÅÓ³¶¯»­")){
			_animation_tag = true;
		}
		
		else if(qName.equals("ÑÝ:ÇÐ»»")){
			
		}
	}
	
	public static void process_chars(String chs){		
		if(_animation_tag){
			Animation.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if(_notes_tag){
			if(qName.equals("ÑÝ:»ÃµÆÆ¬±¸×¢")){
				_notes_tag = false;
				_content += "</presentation:notes>";
			}
		}
		else if(qName.equals("ÑÝ:»ÃµÆÆ¬")){
			//
		}
		else if(qName.equals("ÑÝ:·ÅÓ³¶¯»­")){
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
