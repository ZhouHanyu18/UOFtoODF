package object_set;

import java.util.HashMap;
import java.util.Map;

public class Draw_Type_Table {

	private static Map<String,String> 
	_hashmap = new HashMap<String,String>();
	
	public static void create_map() {	
//		_hashmap.put("rectangle","11.Rectangle");
//		_hashmap.put("round-rectangle","15.Rounded Rectangle");
//		_hashmap.put("ellipse","19.Oval");
		_hashmap.put("Isosceles Triangle","isosceles-triangle");
		_hashmap.put("Right Triangle","right-triangle");
		_hashmap.put("Trapezoid","trapezoid");
		_hashmap.put("Parallelogram","parallelogram");
		_hashmap.put("Diamond","diamond");
		_hashmap.put("Parallelogram","pentagon1");
		_hashmap.put("Cross","cross");
//		_hashmap.put("hexagon","110.Hexagon");
//		_hashmap.put("octagon","16.Octagon");
		_hashmap.put("Can","can");
		_hashmap.put("Cube","cube");
		_hashmap.put("Block Arc","block-arc");
		_hashmap.put("Donut","ring");
		_hashmap.put("Folded Corner","paper");
		_hashmap.put("Smile Face","smiley");
		_hashmap.put("Sun","sun");
		_hashmap.put("Moon","moon");
		_hashmap.put("Heart","heart");
		_hashmap.put("Lightning","non-primitive");
		_hashmap.put("\"No\" Symbol","forbidden");
		_hashmap.put("Double Bracket","bracket-pair");
		_hashmap.put("Left Bracket","left-bracket");
		_hashmap.put("Right Bracket","right-bracket");
		_hashmap.put("Double Brace","brace-pair");
		_hashmap.put("Left Brace","left-brace");
		_hashmap.put("Right Brace","right-brace");
		_hashmap.put("Bevel","quad-bevel");
		_hashmap.put("Left Arrow","left-arrow");
		_hashmap.put("Right Arrow","right-arrow");
		_hashmap.put("Up Arrow","up-arrow");
		_hashmap.put("Down Arrow","down-arrow");
		_hashmap.put("Left-Right Arrow","left-right-arrow");
		_hashmap.put("Up-Down Arrow","up-down-arrow");
		_hashmap.put("Quad Arrow","quad-arrow");
		_hashmap.put("Striped Right Arrow","mso-spt100");
		_hashmap.put("Notched Right Arrow","notched-right-arrow");
		_hashmap.put("Pentagon Arrow","pentagon-right");
		_hashmap.put("Chevron Arrow","chevron");
		_hashmap.put("Right Arrow Callout","right-arrow-callout");
		_hashmap.put("Left Arrow Callout","left-arrow-callout");
		_hashmap.put("Up Arrow Callout","up-arrow-callout");
		_hashmap.put("Down Arrow Callout","down-arrow-callout");
		_hashmap.put("Left-Right Arrow Calout","left-right-arrow-callout");
		_hashmap.put("Up-Down Arrow Callout","up-down-arrow-callout");
		_hashmap.put("Quad Arrow Callout","quad-arrow-callou");
//		_hashmap.put("pentagon","112.Regual Pentagon");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
		_hashmap.put("","");
	}
	
	public static String get_draw_type(String drawType) {
		String type = "";
		if(_hashmap.get(drawType) != null)
			type = _hashmap.get(drawType);
		return type;
	}
}
