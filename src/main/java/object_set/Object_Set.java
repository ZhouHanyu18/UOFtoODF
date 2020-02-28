package object_set;

import org.xml.sax.Attributes;

public class Object_Set {
	
	private static boolean _drawing_tag = false;
	private static boolean _formula_tag = false;
	private static boolean _other_object_tag = false;
	
	public Object_Set() {
	}
	
	public static void process_start(String qName,Attributes atts){
		if (_drawing_tag)
			Drawing.process_start(qName,atts);
		if (_formula_tag)
			Formula.process_start(qName,atts);
		if (_other_object_tag)
			Other_Object.process_start(qName,atts);
		
		if(qName.equals("图:图形")) {
			_drawing_tag = true;
			Drawing.process_start(qName,atts);
		}
		else if(qName.equals("uof:数学公式")) {
			_formula_tag = true;
			Formula.process_start(qName,atts);
		}
		else if(qName.equals("uof:其他对象")) {
			_other_object_tag = true;
			Other_Object.process_start(qName,atts);
		}
	}
	
	public static void process_end(String qName){
		if(qName.equals("图:图形")) {
			_drawing_tag = false;
			Drawing.process_end(qName);
		}
		else if(qName.equals("uof:数学公式")) {
			_formula_tag = false;
			Formula.process_end(qName);
		}
		else if(qName.equals("uof:其他对象")) {
			_other_object_tag = false;
			Other_Object.process_end(qName);
		}
		
		if (_drawing_tag)
			Drawing.process_end(qName);
		if (_formula_tag)
			Formula.process_end(qName);
		if (_other_object_tag)
			Other_Object.process_end(qName);
	}
	
	public static void process_chars(String chs){
		if (_drawing_tag)
			Drawing.process_chars(chs);
		if (_formula_tag)
			Formula.process_chars(chs);
		if (_other_object_tag)
			Other_Object.process_chars(chs);
	}
}
