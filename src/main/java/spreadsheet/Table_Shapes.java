package spreadsheet;

import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;
import temp_structs.Common_Data;
import temp_structs.Object_Set_Data;

public class Table_Shapes {
	//the current table name
	private static String _table_id = "";
	//shape used by annotation
	private static boolean _rect_in_anno = false;
	//store the shapes with their corresponding table
	private static Map<String,String> _shapes_map = new TreeMap<String,String>();


	//initialize
	public static void init(){
		_shapes_map.clear();
	}

	//set table name
	public static void set_table_id(String id){
		_table_id = id;
	}

	//add one shape(or chart) to the map
	public static void add_one_shape(String shape){
		String shps = "";

		shps = _shapes_map.get(_table_id);
		if(shps == null){
			shps = shape;
		}
		else {
			shps += shape;
		}

		_shapes_map.put(_table_id,shps);
	}

	public static String get_table_shapes(String tableID){
		return _shapes_map.get(tableID);
	}

	public static void process_start(String qName,Attributes atts){
		if(qName.equals("表:批注")){
			_rect_in_anno = true;
		}

		else if(qName.equals("uof:锚点")){
			String drawAtts = "";

			String drawID = atts.getValue("uof:图形引用");
			String drawing = Object_Set_Data.getDrawing(drawID);

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

			if(_rect_in_anno){
				drawing = drawing.replace("<draw:rect","<office:annotation");
				drawing = drawing.replace("</draw:rect>","</office:annotation>");
				Object_Set_Data.addDrawing(drawID,drawing);
			}
			else {
				add_one_shape(drawing);
			}
		}
	}

	public static void process_end(String qName){
		if(qName.equals("表:批注")){
			_rect_in_anno = false;
		}
	}

	private static String add_att(String name,String val){
		return (" " + name + "=\"" + val + "\"");
	}
	private static String measure_val(String val){
		return Float.valueOf(val) * Common_Data.get_graphratio() + Common_Data.get_unit();
	}
	private static String measure_floatval(float val){
		return val * Common_Data.get_graphratio() + Common_Data.get_unit();
	}
}
