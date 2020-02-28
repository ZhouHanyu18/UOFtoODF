package text;

import org.xml.sax.Attributes;

import temp_structs.Common_Data;
import temp_structs.Object_Set_Data;
import temp_structs.Stored_Data;

public class Text_Anchor {
	private static boolean _anchor_tag = false;  //tag for <字:锚点>
	private static boolean _anchor_hori_tag = false;
	private static boolean _anchor_vert_tag = false;
	private static float _svgX = -1;
	private static float _svgY = -1;
	private static float _anchor_width = 0;
	private static float _anchor_height = 0;
	private static String _anchor_hori_rel = "";
	private static String _anchor_hori_pos = "";
	private static String _anchor_vert_rel = "";
	private static String _anchor_vert_pos = "";

	private static String _chs = "";

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("字:锚点")){
			_anchor_tag = true;
		}
		else if(qName.equals("字:水平") && _anchor_tag){
			_anchor_hori_tag = true;
			if ((attVal = atts.getValue("字:相对于")) != null)
				_anchor_hori_rel = attVal;
		}
		else if(qName.equals("字:垂直") && _anchor_tag){
			_anchor_vert_tag = true;
			if ((attVal = atts.getValue("字:相对于")) != null)
				_anchor_vert_rel = attVal;
		}
		else if(qName.equals("字:绝对")){
			attVal = atts.getValue("字:值");
			if (_anchor_hori_tag)
				_svgX = Float.valueOf(attVal) * Common_Data.get_graphratio();
			else if (_anchor_vert_tag)
				_svgY = Float.valueOf(attVal) * Common_Data.get_graphratio();
		}
		else if(qName.equals("字:相对")){
			attVal = atts.getValue("字:值");
			if (_anchor_hori_tag)
				_anchor_hori_pos = attVal;
			else if (_anchor_vert_tag)
				_anchor_vert_pos = attVal;
		}
		else if(qName.equals("字:图形")){
			attVal = atts.getValue("字:图形引用");
			String drawingStr = Object_Set_Data.getDrawing(attVal);
			if (drawingStr.contains("draw:frame")) {
				if(Object_Set_Data.getRefObj(attVal) != null) {
					String image = "<draw:image xlink:href=\""
						+ Object_Set_Data.getOtherObj(Object_Set_Data.getRefObj(attVal))
						+"\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/>";
					int index = drawingStr.indexOf("</");
					drawingStr = drawingStr.substring(0, index) + image + drawingStr.substring(index);
				}
			}
			if (_svgX != -1 && !drawingStr.contains("<draw:g")) {   //there are svg:x and svg:y
				String unit = Common_Data.get_unit();
				int i = drawingStr.indexOf(">");
				if (!drawingStr.contains("draw:line"))
					drawingStr = drawingStr.substring(0,i) + " svg:x=\"" + _svgX + unit
					+ "\" svg:y=\"" + _svgY + unit + "\""
					+ drawingStr.substring(i);
				else {  //for draw:line
					float x1, y1, x2, y2;
					if (Object_Set_Data.getDrawingOverturn(attVal) != null) {
						x1 = _svgX;
						y1 = _svgY + _anchor_height;
						x2 = _svgX + _anchor_width;
						y2 = _svgY;
					}
					else {
						x1 = _svgX;
						y1 = _svgY;
						x2 = _svgX + _anchor_width;
						y2 = _svgY + _anchor_height;
					}
					drawingStr = drawingStr.substring(0,i) + " svg:x1=\"" + x1 + unit
					+ "\" svg:y1=\"" + y1 + unit
					+ "\" svg:x2=\"" + x2 + unit
					+ "\" svg:y2=\"" + y2 + unit + "\""
					+ drawingStr.substring(i);
				}
			}
			else {  //some atts in <style:graphic-properties> decide the frame's position
				String contentAutoStyle = Stored_Data.getAutoStylesInContentXml();
				int i = contentAutoStyle.indexOf(attVal);  //find the <style:style> correspond to this frame
				i = contentAutoStyle.indexOf("<style:graphic-properties", i);  //find the <style:graphic-properties>
				i = contentAutoStyle.indexOf(" ", i);  //find the place to insert some atts
				String insert = "";
				if (_anchor_hori_rel.length() != 0)
					insert += " style:horizontal-rel=\"" + _anchor_hori_rel + "\"";
				if (_anchor_hori_pos.length() != 0)
					insert += " style:horizontal-pos=\"" + _anchor_hori_pos + "\"";
				if (_anchor_vert_rel.length() != 0)
					insert += " style:vertical-rel=\"" + _anchor_vert_rel + "\"";
				if (_anchor_vert_pos.length() != 0)
					insert += " style:vertical-pos=\"" + _anchor_vert_pos + "\"";
				contentAutoStyle = contentAutoStyle.substring(0, i) + insert + contentAutoStyle.substring(i);
				Stored_Data.setAutoStylesInContentXml(contentAutoStyle);
			}
			Object_Set_Data.addDrawing(attVal, drawingStr);
		}
	}

	public static void process_end(String qName){
		if(qName.equals("字:锚点")){
			 _anchor_tag = false;
			 _svgX = -1;
			 _svgY = -1;
			 _anchor_width = 0;
			 _anchor_height = 0;
			 _anchor_hori_rel = "";
			 _anchor_hori_pos = "";
			 _anchor_vert_rel = "";
			 _anchor_vert_pos = "";
		 }
		 else if(qName.equals("字:宽度")){
			 _anchor_width = Float.valueOf(_chs) * Common_Data.get_graphratio();
		 }
		 else if(qName.equals("字:高度")){
			 _anchor_height = Float.valueOf(_chs) * Common_Data.get_graphratio();
		 }
		 else if(qName.equals("字:水平") && _anchor_tag){
			 _anchor_hori_tag = false;
		 }
		 else if(qName.equals("字:垂直") && _anchor_tag){
			 _anchor_vert_tag = false;
		 }

		 _chs = "";
	}

	public static void process_chars(String chs){
		_chs = chs;
	}
}
