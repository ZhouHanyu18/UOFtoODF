package text;

import java.util.*;
import org.xml.sax.Attributes;

import style_set.Para_Style;
import temp_structs.Stored_Data;

/**
 * Process a few style information existing 
 * inside <��:����> to create new paragraph-
 * styles.
 * @author xie
 *
 */
public class Para_Pro {
	//counter of <��:��������>
	private static int _counter_of_para = 0;
	//value of @��:ʽ������
	private static String _style_name = "";
	//id for generating paragraph style names
	private static int _para_id = 0;
	//store all different para-pros we have got
	private static Map<String,String>
		_para_pros = new TreeMap<String,String>();
	//store <counter,paragraph style name>-pairs
	private static Map<Integer,String>
		_para_name_map = new TreeMap<Integer,String>();
	//whether or not this �������� is in ������
	private static boolean _is_sec_type = false;


	//initialize
	public static void init(){
		_counter_of_para = 0;
		_style_name = "";
		_para_id = 0;
		_para_pros.clear();
		_para_name_map.clear();
		_is_sec_type = false;
	}
	
	public static void in_sec_type(boolean bval){
		_is_sec_type = bval;
	}
	
	private static void clear(){
		_style_name = "";
	}
	
	//Invoked by First_ConvHandler
	public static void plus_para_counter(){
		_counter_of_para ++;
	}

	//return the style name for the corresponding
	//<text:p>. Invoked by Text_p
	public static String get_para_name(int paraCounter){
		return _para_name_map.get(paraCounter);
	}
	
	//create a new paragraph style and store it.
	private static void commit_para_style(){
		String style = "";
		String name = "";
		String paraPro = Para_Style.get_para_pro();
		
		if(paraPro.equals(""))	return;
		
		//if there is a para-pro with the same content, return its name
		for(Iterator<String> it=_para_pros.keySet().iterator(); it.hasNext();){
			String tmpName = it.next();
			
			if(_para_pros.get(tmpName).equals(paraPro + _style_name)){
				name = tmpName;
				break;
			}
		}

		//else, create a new paragraph style and store it.
		if(name.equals("")){
			name = gen_para_id();
			_para_pros.put(name, paraPro + _style_name);
			
			style = "<style:style style:family=\"paragraph\"";
			style += " style:name=\"" + name + "\"";
			if(!_style_name.equals("")){
				style += " style:parent-style-name=\"" + _style_name + "\"";
			}
			style += ">" + paraPro + "</style:style>";
			
			if(_is_sec_type){
				Stored_Data.addAutoStylesInStylesXml(style);
			}
			Stored_Data.addAutoStylesInContentXml(style);
		}
		_para_name_map.put(_counter_of_para,name);
		
		clear();
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("��:��������")){		
			attVal = atts.getValue("��:ʽ������");
			_style_name = (attVal==null) ? "" : attVal;
		}
		
		else {
			Para_Style.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){		
		Para_Style.process_chars(chs);
	}
	
	public static void process_end(String qName){
		 if(qName.equals("��:��������")){
			commit_para_style();
		 }
		 else {
			 Para_Style.process_end(qName);
		 }
	}
	
	//Gennerate a name for paragraph style
	private static String gen_para_id(){
		_para_id ++;
		return "ex_P" + _para_id;
	}
}
