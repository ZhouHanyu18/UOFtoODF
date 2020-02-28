package text;

import java.util.*;
import org.xml.sax.Attributes;

import style_set.Sent_Style;
import temp_structs.Stored_Data;

/**
 * Process a few style information existing 
 * inside <字:段落> to create new text-styles.
 * @author xie
 *
 */
public class Text_Pro {
	//counter of <字:句属性>.There may
	//be more than one in <字:段落>-element.
	private static int _counter_of_text = 0;
	//value of @字:式样引用
	private static String _style_name = "";
	//id for generating text style names
	private static int _text_id = 0;
	//store all different text-pros we have got
	private static Map<String,String>
		_text_pros = new TreeMap<String,String>();
	//store <counter,text style name>-pairs
	private static Map<Integer,String>
		_text_name_map = new TreeMap<Integer,String>();
	//whether or not this 段落属性 is in 节属性
	private static boolean _is_sec_type = false;


	//initialize
	public static void init(){
		_counter_of_text = 0;
		_style_name = "";
		_text_id = 0;
		_text_pros.clear();
		_text_name_map.clear();
	}
	
	public static void in_sec_type(boolean bval){
		_is_sec_type = bval;
	}
	
	private static void clear(){
		_style_name = "";
	}
	
	//Invoked by First_ConvHandler
	public static void plus_text_counter(){
		_counter_of_text ++;
	}
	
	//return the style name for the corresponding
	//<text:span>. Invoked by Text_p
	public static String get_text_name(int textCounter){
		return _text_name_map.get(textCounter);
	}
	
	//create a new text style and store it.
	private static void commit_text_style(){
		String style = "";
		String name = "";
		String textPro = Sent_Style.get_text_pro();
		
		if(textPro.equals(""))		return;

		//if there is a text-pro with the same content
		//then return its name
		for(Iterator<String> it=_text_pros.keySet().iterator(); it.hasNext();){
			String tmpName = it.next();
			if((textPro + _style_name).equals(_text_pros.get(tmpName))){
				name = tmpName;
				break;
			}
		}
		
		//else, create a new text style and store it.
		if(name.equals("")){
			name = gen_text_id();
			_text_pros.put(name, textPro + _style_name);
			
			textPro = "<style:text-properties" + textPro + "/>";
		
			style = "<style:style style:family=\"text\"";
			style += " style:name=\"" + name + "\"";
			if(!_style_name.equals("")){
				style += " style:parent-style-name=\"" + _style_name + "\"";
			}
			style += ">" + textPro + "</style:style>";
			
			if(_is_sec_type){
				Stored_Data.addAutoStylesInStylesXml(style);
			}
			Stored_Data.addAutoStylesInContentXml(style);
		}	
		_text_name_map.put(_counter_of_text,name);

		clear();
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("字:句属性")){
			attVal = atts.getValue("字:式样引用");
			_style_name = (attVal==null) ? "" : attVal;
		}
		else {
			Sent_Style.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		Sent_Style.process_chars(chs);
	}
	
	public static void process_end(String qName){
		if(qName.equals("字:句属性")){
			 commit_text_style();
		 }
		 else{
			 Sent_Style.process_end(qName);
		 }
	}
	
	//Generate a name for text style
	private static String gen_text_id(){
		_text_id ++;
		return "ex_T" + _text_id;
	}
}
