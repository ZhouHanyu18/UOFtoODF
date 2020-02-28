package spreadsheet;

import org.xml.sax.Attributes;
import style_set.Common_Pro;
import text.Text_Content;

/**
 *
 * @author xie
 *
 */
public class Page_Layout_S extends Common_Pro{
	//
	private static String _chs = "";
	//page-layout properties
	private static String _pl_pros = "";
	//@style:name
	private static String _id = "";
	//<style:page-layout>
	private static String _page_layout = "";
	//<style:header>
	private static String _style_header = "";
	//<style:footer>
	private static String _style_footer = "";
	//left/center/right
	private static String _position = "";
	//<style:master-page>
	private static String _master_page = "";
	//tag for text content
	private static boolean _text_content_tag = false;


	//initialize
	public static void init(){
		_pl_pros = "";
		_page_layout = "";
		_style_header = "";
		_style_footer = "";
		_master_page = "";
		_position = "";
	}

	public static String get_page_layout(){
		return _page_layout;
	}

	public static String get_master_page(){
		return _master_page;
	}

	private static String get_one_PL(){
		String str = "";

		str = "<style:page-layout style:name=\"" + _id + "\">";
		str += "<style:page-layout-properties" + _pl_pros + "/>";
		str += "</style:page-layout>";

		_pl_pros = "";

		return str;
	}

	private static String get_one_MP(){
		String str = "";

		str = "<style:master-page style:name=\"Default\" style:page-layout-name=\"" + _id + "\">";
		str += "<style:header>" + _style_header + "</style:header>";
		str += "<style:footer>" + _style_footer + "</style:footer>";
		str += "</style:master-page>";

		_style_footer = "";
		_style_header = "";

		return str;
	}

	public static void process_start(String qName,Attributes atts){
		if(qName.equals("表:页面设置")){
			_id = atts.getValue("表:名称");
		}
		else if(qName.equals("表:纸张")){
			_pl_pros += get_page(atts);
		}
		else if(qName.equals("表:页边距")){
			_pl_pros += get_margins(atts);
		}
		else if(_text_content_tag){
			Text_Content.process_start(qName,atts);
		}
		else if(qName.equals("表:页眉页脚")){
			_text_content_tag = true;

			if(atts.getValue("表:位置") != null){
				_position = atts.getValue("表:位置");
			}
		}
	}

	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		} else {
			_chs = chs;
		}
	}

	public static void process_end(String qName){
		if(qName.equals("表:纸张方向")){
			_pl_pros += " style:print-orientation=\"" + _chs + "\"";
		}
		else if(qName.equals("表:缩放")){
			_pl_pros += " style:scale-to=\"" + _chs + "%\"";
		}
		else if(_text_content_tag){
			Text_Content.process_end(qName);

			if(qName.equals("表:页眉页脚")){
				_text_content_tag = false;

				if(_position.contains("footer")){
					_style_footer = style_region(Text_Content.get_result(),_position);
				}
				else {
					_style_header = style_region(Text_Content.get_result(),_position);
				}
			}
		}
		else if(qName.equals("表:页面设置")){
			if(_page_layout.equals("")){
				_page_layout = get_one_PL();
			}

			if(_master_page.equals("")){
				_master_page = get_one_MP();
			}
		}
		_chs = "";
	}

	private static String style_region(String content,String position){
		String regionEle = "style:region-center";

		if(position.contains("left")){
			regionEle = "style:region-left";
		}
		else if(position.contains("center")){
			regionEle = "style:region-center";
		}
		else if(position.contains("right")){
			regionEle = "style:region-right";
		}

		return "<" + regionEle + ">" + content + "</" + regionEle + ">";
	}
}
