package text;

import org.xml.sax.Attributes;

/**
 *
 * @author xie
 *
 */
public class Master_Page {
	//to hold the result
	private static String _result = "";
	//headers and footers
	private static String _page_content = "";
	//tag for text content
	private static boolean _text_content_tag = false;
	//tag for filtration
	private static boolean _filter_tag = false;
	//
	private static int _page_layout_id = 0;


	//initialize
	public static void init(){
		_result = "";
		_page_content = "";
		_page_layout_id = 0;
	}

	//generate a page-layout-name
	private static String gen_PL_id(){
		_page_layout_id++;
		return "pm" + _page_layout_id;
	}

	//store one <style:master-page>
	//invoked by Page_Layout
	public static void commit_result(){
		String rst = "";

		rst = "<style:master-page";
		rst += " style:name=\"Standard\"";	//暂设为Standard,有待改进
		rst += " style:page-layout-name=\"" + gen_PL_id() + "\">";
		rst += _page_content;
		rst += "</style:master-page>";

		_result += rst;
		_page_content = "";
	}

	//return the result
	public static String get_result(){
		return _result;
	}

	public static void process_start(String qName,Attributes atts){
		if(_filter_tag){
			return;		    //退出,不处理
		}
		else if(_text_content_tag){;
			Text_Content.process_start(qName,atts);
		}

		else if(qName.equals("字:奇数页页眉")){
			_text_content_tag = true;
			_page_content += "<style:header>";
		}
		else if(qName.equals("字:偶数页页眉")){
			_text_content_tag = true;
			_page_content += "<style:header-left>";
		}
		else if(qName.equals("字:奇数页页脚")){
			_text_content_tag = true;
			_page_content += "<style:footer>";
		}
		else if(qName.equals("字:偶数页页脚")){
			_text_content_tag = true;
			_page_content += "<style:footer-left>";
		}
		else if(qName.equals("字:首页页眉")||qName.equals("字:首页页脚")){
			_filter_tag = true;
		}
	}

	public static void process_chars(String chs){
		if(_text_content_tag){
			Text_Content.process_chars(chs);
		}
		else if(_filter_tag){
			return;
		}
	}

	public static void process_end(String qName){

		if(qName.equals("字:奇数页页眉")){
			_text_content_tag = false;
			_page_content += Text_Content.get_result();
			_page_content += "</style:header>";
		}
		else if(qName.equals("字:偶数页页眉")){
			_text_content_tag = false;
			_page_content += Text_Content.get_result();
			_page_content += "</style:header-left>";
		}
		else if(qName.equals("字:奇数页页脚")){
			_text_content_tag = false;
			_page_content += Text_Content.get_result();
			_page_content += "</style:footer>";
		}
		else if(qName.equals("字:偶数页页脚")){
			_text_content_tag = false;
			_page_content += Text_Content.get_result();
			_page_content += "</style:footer-left>";
		}
		else if(qName.equals("字:首页页眉")||qName.equals("字:首页页脚")){
			_filter_tag = false;
		}
		else if(_text_content_tag){
			Text_Content.process_end(qName);
		}
		else if(_filter_tag){
			return;
		}
	}
}
