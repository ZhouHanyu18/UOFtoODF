package style_set;

import org.xml.sax.Attributes;

/**
 * Process the conversion from <uof:���弯>
 * to <office:font-face-decls>
 * @author xie
 *
 */
public class Font_Set {
	//Stored the result
	private static String _font_set = "";
	//default font name
	private static String _default_name = "Times New Roman";
	//default asian font name
	private static String _default_name_asian = "����";
	//default complex font name
	private static String _default_name_complex = "Tahoma";
		
	
	//initialize
	public static void init(){
		_font_set = "";
	}
	//return the result
	public static String get_result(){	
		return _font_set;
	}
	
	public static void process_start(String qName,Attributes atts){
		if (qName.equals("uof:���弯")){
			_font_set += "<office:font-face-decls>";
			
			//the default fonts will be added at first
			_font_set += gen_font_face(_default_name, _default_name);
			_font_set += gen_font_face(_default_name_asian, _default_name_asian);
			_font_set += gen_font_face(_default_name_complex, _default_name_complex);
		}
		else if (qName.equals("uof:��������")) {
			String id = atts.getValue("uof:��ʶ��");
						
			if(!id.equals(_default_name) 
					&& !id.equals(_default_name_asian) 
					&& !id.equals(_default_name_complex)){
				_font_set += gen_font_face(id,atts.getValue("uof:������"));
			}
		}
	}
	
	public static void process_end(String qName){
		if (qName.equals("uof:���弯")){
			_font_set += "</office:font-face-decls>";
		}
	}
	
	private static String gen_font_face(String name, String family){
		return "<style:font-face style:name=\"" + name + "\" svg:font-family=\"" + family + "\"/>";
	}
}
