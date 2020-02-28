package presentation;

import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;

import style_set.Para_Style;
import temp_structs.Stored_Data;

/**
 *
 * @author xie
 *
 */
public class Presentation_Style {
	//the result
	private static String _result = "";
	//chars
	private static String _chs = "";
	//style name
	private static String _style_name = "";
	//tag for filtration
	private static boolean _filter_tag = false;
	//style's type: "notes" or "slide"
	private static String _type = "";
	//name of the master-page
	//private static String _master_name = "";
	//para-style name for title
	private static String _title_name = "";
	//para-style name for outline
	private static String _outline_name = "";
	//para-style name for notes
	private static String _notes_name = "";
	//list-style name for text-lists in draw-page
	private static String _list_style_name = "Pr_List";
	//store the style names and their corresponding outline level
	private static Map<String,Integer> _ps_level_map = new TreeMap<String,Integer>();


	//initialize
	public static void init(){
		_ps_level_map.clear();
	}
	public static void set_master_name(String name){
		//_master_name = name;
	}
	public static String get_title_name(){
		return _title_name;
	}
	public static String get_outline_name(){
		return _outline_name;
	}
	public static String get_notes_name(){
		return _notes_name;
	}
	public static String get_list_style_name(){
		return _list_style_name;
	}
	public static void put_ps_level(String styleName,int level){
		_ps_level_map.put(styleName,level);
	}
	public static Integer get_ps_level(String styleName){
		return _ps_level_map.get(styleName);
	}

	private static String get_one_style(){
		String style = "";

		style = "<style:style style:name=\""
			+ _style_name + "\" style:family=\"presentation\">";
		style += Para_Style.get_para_pro();
		style += "</style:style>";

		return style;
	}

	public static String get_result(){
		String rst = "";

		rst = _result;
		_result = "";

		return rst;
	}

	public static void process_start(String qName,Attributes atts){
		if(qName.equals("uof:段落式样")){
			_filter_tag = false;
			_type = atts.getValue("字:名称");
			if(_type == null){
				_type = "";
			}
			if(!_type.equals("slide") && !_type.equals("notes")){
				_filter_tag = true;
			}

			_style_name = atts.getValue("字:标识符");
		}
		else if(_filter_tag){
			return ;
		}
		else {
			Para_Style.process_start(qName,atts);
		}
	}

	public static void process_chars(String chs){
		if(_filter_tag)		return ;

		Para_Style.process_chars(chs);
		_chs = chs;
	}

	public static void process_end(String qName){

		if(qName.equals("演:文本式样集")){
			Stored_Data.addStylesInStylesXml(get_result());
			Stored_Data.addAutoStylesInStylesXml(content_style());
			Stored_Data.addAutoStylesInContentXml(content_style());
			Stored_Data.addAutoStylesInContentXml(pr_list_style());
		}

		else if(_filter_tag){
			return ;
		}

		else if(qName.equals("uof:段落式样")){
			_result += get_one_style();
		}
		else if(qName.equals("字:大纲级别")){
			if(_type.equals("notes")){
				if(_chs.equals("1")){
					_notes_name = _style_name;
				}
				else {
					_filter_tag = true;
				}
			}
			else if(_type.equals("slide")){
				if(_chs.equals("0")){
					//_style_name = _master_name + "-title";
					_title_name = _style_name;
				}
				else if(_chs.equals("1")){
					//_style_name = _master_name + "-outline" + _chs;
					_outline_name = _style_name;
				}
			}
		}
		else {
			Para_Style.process_end(qName);
		}
	}

	private static String content_style(){
		String style = "";

		//title
		style += "<style:style"
			  + " style:name=\"" + "pr_" + _title_name + "\""
			  + " style:family=\"presentation\""
			  + " style:parent-style-name=\"" + _title_name + "\""
			  + ">" + "<style:graphic-properties/>"
			  + "</style:style>";
		//outline
		style += "<style:style"
			  + " style:name=\"" + "pr_" + _outline_name + "\""
			  + " style:family=\"presentation\""
			  + " style:parent-style-name=\"" + _outline_name + "\""
			  + ">" + "<style:graphic-properties/>"
			  + "</style:style>";
		//notes
		style += "<style:style"
			  + " style:name=\"" + "pr_" + _notes_name + "\""
			  + " style:family=\"presentation\""
			  + " style:parent-style-name=\"" + _notes_name + "\""
			  + ">" + "<style:graphic-properties/>"
			  + "</style:style>";

		return style;
	}

	private static String pr_list_style(){
		String list =
			"<text:list-style style:name=\"" + _list_style_name + "\">" +
				"<text:list-level-style-bullet text:level=\"1\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"0.3cm\" text:min-label-width=\"0.9cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"2\" text:bullet-char=\"–\">" +
					"<style:list-level-properties text:space-before=\"1.6cm\" text:min-label-width=\"0.8cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"75%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"3\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"3cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"4\" text:bullet-char=\"–\">" +
					"<style:list-level-properties text:space-before=\"4.2cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"75%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"5\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"5.4cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"6\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"6.6cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"7\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"7.8cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"8\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"9cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
				"<text:list-level-style-bullet text:level=\"9\" text:bullet-char=\"●\">" +
					"<style:list-level-properties text:space-before=\"10.2cm\" text:min-label-width=\"0.6cm\"/>" +
					"<style:text-properties fo:font-family=\"StarSymbol\" style:use-window-font-color=\"true\" fo:font-size=\"45%\"/>" +
				"</text:list-level-style-bullet>" +
			"</text:list-style>";

		return list;
	}
}
