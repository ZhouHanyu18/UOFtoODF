package object_set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import temp_structs.*;
import text.Text_Content;

public class Drawing {
	
	private static String _text_node = "";   //用于存放文本节点的内容
	private static boolean _need_to_store_text = false;   //标识是否需要取出文本节点的值
	
	private static String _drawing_id = "";
	private static String _drawing_content = "";   //将会写入content.xml的<body>的内容
	private static String _drawing_style = "";     //将会写入content.xml的<style:automatic-style>的内容
	
	//以下是_drawing_content的组成部分
	private static String _drawing_name = "";
	private static String _drawing_atts = "";
	private static String _drawing_text = "";
	private static String _draw_type = "";
	
	//以下是图形的style的组成部分
	private static String _graphic_pro = "<style:graphic-properties";
	private static String _para_pro = "<style:paragraph-properties";
	private static String _text_pro = "<style:text-properties";
	private static String _bg_img = "";
	
	private static String _draw_transform = "";   //ODF中draw:transform属性值，可能由UOF中多个元素决定
	
	private static boolean _is_text_box = false;   //标识是否文本框
	private static boolean _text_content_tag = false;   //标识是否是文本内容
	private static boolean _frame_tag = false;  //标识是否frame;
	private static boolean _g_tag = false;  //标识是否draw:g;
	
	private static String _textbox_height = "";
	
	private static float _width = 0;
	private static float _height = 0;
	
	private static boolean _overturn_tag = false;
	private static boolean _viewBox_tag = false;
	
	public Drawing() {
		
	}
	
	public static void process_start(String qName,Attributes atts){
		if (_text_content_tag)
			Text_Content.process_start(qName,atts);
		
		else if (qName.equals("图:图形")) {  
			_drawing_id = atts.getValue("图:标识符");
			_drawing_atts = " draw:style-name=\"" + _drawing_id + "\"";
			if (atts.getValue("图:层次") != null)
				_drawing_atts += " draw:z-index=\"" + atts.getValue("图:层次") + "\"";
			_drawing_style += "<style:style style:family=\"graphic\" style:name=\"" + _drawing_id + "\"";
			if (atts.getValue("图:其他对象") != null) {
				Object_Set_Data.addRefObj(_drawing_id, atts.getValue("图:其他对象"));
				_frame_tag = true;
			}
			if (atts.getValue("图:组合列表") != null) {
				_g_tag = true;
				String gStr = atts.getValue("图:组合列表");
				String gID = "";
				int i = gStr.indexOf(" ");
				while (i > 0) {
					gID = gStr.substring(0, i);
					_drawing_text += Object_Set_Data.getDrawing(gID);
					gStr = gStr.substring(i + 1);
					i = gStr.indexOf(" ");
				}
				_drawing_text += Object_Set_Data.getDrawing(gStr);
				//_drawing_content 
			}
		}
//		else if (qName.equals("图:svg图形对象")) {
			//To do.
//		}
		
		else if (qName.equals("图:关键点坐标")) {
			_drawing_name = "draw:path";
			_viewBox_tag = true;
			String path = atts.getValue("图:路径").toLowerCase();
			path = calculate_path(path);
			_drawing_atts += " svg:d=\"" + path + "\"";
		}
		
		//<类别>和<名称>是一一对应的，只处理两者之一即可
		//<生成软件>没有对应
		//未处理:图案、图片、渐变
		//未处理:前端箭头、后端箭头
		//未处理：锁定纵横比、相对原始比例、打印对象、Web文字
		else if (qName.equals("图:线颜色") || qName.equals("图:名称") || qName.equals("图:颜色")
				|| qName.equals("图:线型") || qName.equals("图:线粗细") || qName.equals("图:宽度")
				|| qName.equals("图:高度") || qName.equals("图:旋转角度") || qName.equals("图:X-缩放比例")
				|| qName.equals("图:Y-缩放比例") || qName.equals("图:透明度")) {
			_need_to_store_text = true;
		}
		else if (qName.equals("图:文本内容")) {	
			if (atts.getValue("图:文本框") != null && atts.getValue("图:文本框").equals("true")) {
				_drawing_name = "draw:frame";   //ODF中框对象为<draw:text-box>，是<draw:frame>的子元素
				_is_text_box = true;
				_drawing_text += "<draw:text-box";
				if (atts.getValue("图:后一链接") != null)
					_drawing_text += (" draw:chain-next-name=\"" + atts.getValue("图:后一链接") + "\"");
				_drawing_text += ">";
			}
			String attVal = "";
			if ((attVal=atts.getValue("图:左边距")) != null)
				_graphic_pro += " fo:padding-left=\"" + attVal + Common_Data.get_unit() + "\"";
			if ((attVal=atts.getValue("图:右边距")) != null)
				_graphic_pro += " fo:padding-right=\"" + attVal + Common_Data.get_unit() + "\"";
			if ((attVal=atts.getValue("图:上边距")) != null)
				_graphic_pro += " fo:padding-top=\"" + attVal + Common_Data.get_unit() + "\"";
			if ((attVal=atts.getValue("图:下边距")) != null)
				_graphic_pro += " fo:padding-bottom=\"" + attVal + Common_Data.get_unit() + "\"";
			if (atts.getValue("图:自动换行") != null) {
				if (atts.getValue("图:自动换行").equals("true")) {
					_graphic_pro += " fo:wrap-option=\"no-wrap\"";	
				}
				else {
					_graphic_pro += " fo:wrap-option=\"wrap\"";
				}
			}
			if (atts.getValue("图:大小适应文字") != null) {
				if (atts.getValue("图:大小适应文字").equals("true"))
					_graphic_pro += " draw:auto-grow-width=\"ture\" draw:auto-grow-height=\"ture\"";
				else
					_graphic_pro += " draw:auto-grow-width=\"false\" draw:auto-grow-height=\"false\"";
			}
			
			_text_content_tag = true;
			Text_Content.process_start(qName,atts);
		}
		
		//<字:文字表>在ODF中没有对应，所以不处理
		else if (qName.equals("字:段落")) {
//			_text_content_tag = true;
//			Text_Content.process_start(qName,atts);
		}
		
		else if (qName.equals("图:控制点")) {
			//To do.
		}
		else if (qName.equals("图:翻转")) {
			_overturn_tag = true;
			String overturn = atts.getValue("图:方向");
			Object_Set_Data.addDrawingOverturn(_drawing_id,overturn);
		}
		else if (qName.equals("图:组合位置")) {
			float svgX = Float.valueOf(atts.getValue("图:x坐标")) * Common_Data.get_graphratio();
			float svgY = Float.valueOf(atts.getValue("图:y坐标")) * Common_Data.get_graphratio();
			String unit = Common_Data.get_unit();
			if (_drawing_name.equals("draw:line")) {
				if (_overturn_tag)
					_drawing_atts += " svg:x1=\"" + svgX + unit
					+ "\" svg:y1=\"" + (svgY + _height) + unit
					+ "\" svg:x2=\"" + (svgX + _width) + unit
					+ "\" svg:y2=\"" + svgY + unit + "\"";
				else
					_drawing_atts += " svg:x1=\"" + svgX + unit
					+ "\" svg:y1=\"" + svgY + unit
					+ "\" svg:x2=\"" + (svgX + _width) + unit
					+ "\" svg:y2=\"" + (svgY + _height) + unit + "\"";
			}
			else
				_drawing_atts += " svg:x=\"" + svgX + unit + "\" svg:y=\"" + svgY + unit + "\"";
		}
		else if (qName.equals("图:图片")) {
			_bg_img = "<style:background-image xlink:type=\"simple\" xlink:actuate=\"onLoad\""; 
			_bg_img += " xlink:href=\"" + atts.getValue("图:图形引用") + "\"";
			if(atts.getValue("图:位置") != null)
				_bg_img += " style:repeat=\"" + atts.getValue("图:位置") + "\"";
			_bg_img += "/>";
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("图:图形")) {
			if (_viewBox_tag)
				_drawing_atts += "  svg:viewBox=\"0 0 " + (_width/Common_Data.get_graphratio()) + " " + (_height/Common_Data.get_graphratio()) + "\"";
					
			if (_drawing_name.length() == 0)
				_drawing_name = "draw:custom-shape";   //暂时把不能判断名称的都设为一个draw:custom-shape.有待改进
			
			if (_is_text_box) {
				//Temporarily. To be modified.
				_graphic_pro += " fo:border=\"0.002cm solid #000000\"";
	
				int i = _drawing_text.indexOf(">");
				_drawing_text = _drawing_text.substring(0, i) + _textbox_height + _drawing_text.substring(i);
			}
			
			if(_draw_type.length() != 0)
				_draw_type = "<draw:enhanced-geometry draw:type=\"" + _draw_type + "\"/>";
			
				_drawing_content = "<" + _drawing_name + _drawing_atts + ">" + _drawing_text
				+ _draw_type +  "</" + _drawing_name + ">";


			Object_Set_Data.addDrawing(_drawing_id, _drawing_content);			
			
			_drawing_style += (">" + _graphic_pro + "/>" + _bg_img
								+ _para_pro + "/>" + _text_pro + "/>" + "</style:style>");
			Stored_Data.addAutoStylesInContentXml(_drawing_style);
			
			_drawing_id = "";
			_drawing_content = "";
			_drawing_style = "";
			_drawing_name = "";
			_drawing_atts = "";
			_drawing_text = "";
			_draw_type = "";
			_graphic_pro = "<style:graphic-properties";
			_para_pro = "<style:paragraph-properties";
			_text_pro = "<style:text-properties";
			_bg_img = "";
			_draw_transform = "";
			_is_text_box = false;
			_frame_tag = false;
			_textbox_height = "";
			_g_tag = false;
			_width = 0;
			_height = 0;
			_overturn_tag = false;
			_viewBox_tag = false;
		}
		else if (qName.equals("图:名称")) {
			//Many to do...
			if (_text_node.equals("Rectangle") || _text_node.equals("Rounded Rectangle")) {
				if (_frame_tag)
					_drawing_name = "draw:frame";
				else if (_g_tag)
					_drawing_name = "draw:g";
				else
					_drawing_name = "draw:rect";   //To do: UOF中没有圆角矩形的draw:corner-radius信息
			}
			else if (_text_node.equals("Line"))
				_drawing_name = "draw:line";
			else if (_text_node.equals("Freeform"))
				_drawing_name = "draw:polygon";
			else if (_text_node.equals("Octagon")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"false\" draw:corners=\"8\"";
			}
			else if (_text_node.equals("Hexagon")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"false\" draw:corners=\"6\"";
			}
			else if (_text_node.equals("Regular Pentagon")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"false\" draw:corners=\"5\"";
			}
			else if (_text_node.equals("4-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"8\"";
			}
			else if (_text_node.equals("5-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"10\"";
			}
			else if (_text_node.equals("8-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"16\"";
			}
			else if (_text_node.equals("16-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"32\"";
			}
			else if (_text_node.equals("24-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"48\"";
			}
			else if (_text_node.equals("32-Point Star")) {
				_drawing_name = "draw:regular-polygon";
				_drawing_atts += " draw:concave=\"true\" draw:corners=\"64\"";
			}
			else if (_text_node.equals("Oval")) {
				_drawing_name = "draw:ellipse";
				_drawing_atts += " draw:kind=\"full\"";
			}
			else if (_text_node.equals("Arc")) {
				_drawing_name = "draw:circle";
				_drawing_atts += " draw:kind=\"arc\"";
			}
			else if (_text_node.equals("Elbow Connector")) {
				_drawing_name = "draw:connector";
				_drawing_atts += " draw:type=\"standard\"";
			}
			else if (_text_node.equals("Straight Connector")) {
				_drawing_name = "draw:connector";
				_drawing_atts += " draw:type=\"line\"";
			}
			else if (_text_node.equals("Curved Connector")) {
				_drawing_name = "draw:connector";
				_drawing_atts += " draw:type=\"curve\"";
			}
			else if (_text_node.equals("Rounded Rectangular Callout") || _text_node.equals("Rectangular Callout"))
				_drawing_name = "draw:caption";   //To do: draw:corner-radius
			else if (_text_node.equals("Line")) {
				_drawing_name = "draw:path";
			}
			
			else if (Draw_Type_Table.get_draw_type(_text_node).length() != 0) {
				_draw_type = Draw_Type_Table.get_draw_type(_text_node);
			}
			
			//To do: add other types.
		}
		else if (qName.equals("图:颜色")) {
			_graphic_pro += (" draw:fill=\"solid\" draw:fill-color=\"" + _text_node + "\"");
		}
		else if (qName.equals("图:线颜色")) {
			_graphic_pro += (" svg:stroke-color=\"" + _text_node + "\"");
		}
		else if (qName.equals("图:线型")) {
			if (_text_node.equals("none")) {
				_graphic_pro += (" draw:stroke=\"none\"");
			}
			else if (_text_node.equals("single")||_text_node.equals("double")||_text_node.equals("thick")) {
				_graphic_pro += (" draw:stroke=\"solid\"");
			}
			else if (_text_node.equals("dash")||_text_node.equals("dashed-heavy")||_text_node.equals("dash-long")
					||_text_node.equals("dash-long-heavy")||_text_node.equals("dot-dash")||_text_node.equals("dash-dot-heavy")
					||_text_node.equals("dot-dot-dash")||_text_node.equals("dash-dot-dot-heavy")) {
				_graphic_pro += (" draw:stroke=\"dash\"");
			}
		}
		else if (qName.equals("图:线粗细")) {
			_graphic_pro += (" svg:stroke-width=\""
					+ Float.valueOf(_text_node) * Common_Data.get_graphratio() + Common_Data.get_unit() 
					+ "\"");
		}
		else if (qName.equals("图:宽度")) {
			_width = Float.valueOf(_text_node) * Common_Data.get_graphratio();
			_drawing_atts += (" svg:width=\"" + _width + Common_Data.get_unit() + "\"");
		}
		else if (qName.equals("图:高度")) {
			_height = Float.valueOf(_text_node) * Common_Data.get_graphratio();
			_drawing_atts += (" svg:height=\"" + _height + Common_Data.get_unit() + "\"");
			_textbox_height = " fo:min-height=\"" + _height + Common_Data.get_unit() + "\"";
		}
		else if (qName.equals("图:旋转角度")) {
			_draw_transform += ("rotate(" + _text_node + ")");
		}
		else if (qName.equals("图:透明度")) {
//			_graphic_pro += (" draw:opacity=\"" + _text_node + "\"");
			_graphic_pro += (" style:background-transparency=\"" + _text_node + "%\"");
		}
		else if (qName.equals("图:文本内容")){
			_drawing_text += Text_Content.get_result();
			_text_content_tag = false;
			if(_is_text_box)
				_drawing_text += "</draw:text-box>";
		}
		else if (_text_content_tag)
			Text_Content.process_end(qName);
		
		//每个元素结束时，要清空_text_node并设置_need_to_store_text
		_text_node = "";   
		_need_to_store_text = false;
	}
	
	public static void process_chars(String chs){		
		if (_text_content_tag)
			Text_Content.process_chars(chs);
		
		if (_need_to_store_text)
			_text_node += chs;			
	}
	
	//UOF uses absolute coors, ODF uses relative coors.
	private static String calculate_path(String path) {
		String result = "";
		String ins = "";
		float preX = 0, preY = 0;
		boolean endTag = false;
		
		while (!endTag) {
			int index = 2;
			if (index > path.length())
				index = path.length();
			while (index < path.length() 
					&& !(path.charAt(index) == 'm' || path.charAt(index) == 'l' || path.charAt(index) == 'c'
				|| path.charAt(index) == 'h' || path.charAt(index) == 'z')) {
				index++;
			}
			if (index == path.length()) {
				endTag = true;
			}		
			
			ins = endTag?path:path.substring(0, index);
			
			if (ins.startsWith("h") || ins.startsWith("z"))
				result += ins;
			else {
				if (ins.startsWith("m") || ins.startsWith("l")) { //"m x y"  "l x y"
					ins = ins.trim();
					int index1 = ins.indexOf(" ");
					int index2 = ins.indexOf(" ", index1 + 1);
					float x = Float.valueOf(ins.substring(index1 + 1, index2));
					float y = Float.valueOf(ins.substring(index2 + 1));
					if (ins.startsWith("m"))
						result += "m" + (x - preX) + " " + (y - preY);
					else
						result += "l" + (x - preX) + " " + (y - preY);
					preX = x;
					preY = y;
				}
				else {  //"c x1 y1 x2 y2 x3 y3"
					ins = ins.trim();
					int index1 = ins.indexOf(" ");
					int index2 = ins.indexOf(" ", index1 + 1);
					int index3 = ins.indexOf(" ", index2 + 1);
					int index4 = ins.indexOf(" ", index3 + 1);
					int index5 = ins.indexOf(" ", index4 + 1);
					int index6 = ins.indexOf(" ", index5 + 1);
					float x1 = Float.valueOf(ins.substring(index1 + 1, index2));
					float y1 = Float.valueOf(ins.substring(index2 + 1, index3));
					float x2 = Float.valueOf(ins.substring(index3 + 1, index4));
					float y2 = Float.valueOf(ins.substring(index4 + 1, index5));
					float x3 = Float.valueOf(ins.substring(index5 + 1, index6));
					float y3 = Float.valueOf(ins.substring(index6 + 1));
					result += "c" + (x1 - preX) + " " + (y1 - preY) + " "
					+ (x2 - preX) + " " + (y2 - preY) + " "
					+ (x3 - preX) + " " + (y3 - preY);
					preX = x3;
					preY = y3;
				}
			}
			if (!endTag)
				path = path.substring(index);
			
		}
		return result;
	}
}
