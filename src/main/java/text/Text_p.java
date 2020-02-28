package text;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import org.xml.sax.Attributes;

import presentation.Presentation_Style;
import temp_structs.Common_Data;
import temp_structs.Object_Set_Data;


public class Text_p {
	//text node
	private static String _chs = "";
	//the result
	private static String _result = "";
	//tag for filtration
	private static boolean _filter_tag = false;
	//stack for <字:域代码>'s nesting
	private static Stack<String> _field_stack = new Stack<String>();
	//<text:table-of-content>'s tag.
	private static boolean _toc_start = false;
	//<text:table-of-content>'s end-tag.
	// It's set in <字:域结束>
	private static boolean _toc_end = false;
	//tag for <字:域代码>
	private static boolean _field_code = false;
	//<字:大纲级别>
	private static String _outline_level = "";
	//map for <字:区域开始>类型
	private static Map<String,String> _sec_type_map = new TreeMap<String,String>();
	//tag for <字:文本串>
	private static boolean _is_chars = false;
	//<text:span>'s style name
	private static String _span_id = "";
	//字:式样引用
	private static String _style_name = "";
	//stack for <text:span>'s style name in case of <字:句>'s nesting
	private static Stack<String> _span_id_stack = new Stack<String>();
	//修订信息引用
	private static String _change_id = "";
	//counter of paragraph,corresponding to List_Para's
	private static int _para_counter = 0;
	//counter of <字:句属性>,corresponding to Para_Pro's
	private static int _counter_of_text = 0;
	//counter of <字:段落属性>,corresponding to Para_Pro's
	private static int _counter_of_para = 0;
	//
	private static String _pr_style_name = "";


	//initialize
	public static void init(){
		_change_id = "";
		_para_counter = 0;
		_counter_of_para = 0;
		_counter_of_text = 0;
		_pr_style_name = "";
	}

	public static void restart(){
		_counter_of_para = 0;
		_counter_of_text = 0;
	}
	public static void plus_para_counter(){
		_counter_of_para ++;
	}
	public static void plus_text_counter(){
		_counter_of_text ++;
	}

	private static void clear(){
		_chs = "";
		_result = "";
		_outline_level = "";
		_pr_style_name = "";
	}

	//If the tag is set, <text:table-of-content>
	//element should be added. It contains a template
	//for table of contents.
	private static String toc_start(){
		String toc = "";

		if(_toc_start){
			String tocTemp = "";
			tocTemp = "<text:table-of-content text:style-name=\"Sect1\"" +
						" text:protected=\"true\" text:name=\"内容目录1\">";
			//tocTemp += Default_Styles.get_table_of_content_source();
			tocTemp += "<text:index-body>";
			//tocTemp += "<text:index-title>";
			//tocTemp += "</text:index-title>";

			toc += tocTemp;
			//reset the tag.
			_toc_start = false;
		}


		return toc;
	}

	private static String toc_end(){
		String toc = "";

		//Add end elements needed.
		if(_toc_end){
			toc += "</text:index-body>";
			toc += "</text:table-of-content>";

			//reset the tag.
			_toc_end = false;
		}

		return toc;
	}

	//<text:list> should be treated
	//specially in <presentation>
	private static String pr_list_ele(int level){
		String list = "";
		int lev = level;

		if(lev-- > 0){
			list += "<text:list text:style-name=\""
				+ Presentation_Style.get_list_style_name() + "\">";
			list += "<text:list-item>";
		}
		while(lev-- > 0){
			list += "<text:list><text:list-item>";
		}

		list += _result;

		lev = level;
		while(lev-- > 0){
			list += "</text:list-item></text:list>";
		}

		return list;
	}

	public static String get_result(){
		String rst = "";

		rst += toc_start();

		Integer psLev = Presentation_Style.get_ps_level(_pr_style_name);
		if(psLev != null){
			rst += pr_list_ele(psLev);
		}
		//If the outline level isn't empty, the name
		//of this paragraph should be changed to
		//<text:h> and @text:outline-level be added.
		else if(!_outline_level.equals("") &&
				!Common_Data.get_file_type().equals("presentation")){
			int level = Integer.parseInt(_outline_level) + 1;
			int position1 = 7;	//length of "<text:p" or "text:p>"
			int position2 = _result.length() - position1;

			//Get rid of "<text:p" and "text:p>"
			_result = _result.substring(position1,position2);

			rst += "<text:h text:outline-level=\"" + level + "\"";
			rst += _result;
			rst += "text:h>";
		}
		else {
			rst += _result;
		}
		rst += toc_end();

		//Add the start and end element name for
		// <text:list> if it needs to.
		if(_outline_level.equals("")){
			rst = List_Para.get_list_ele(_para_counter)
				+ rst + List_Para.get_list_end(_para_counter);
		}

		clear();
		return rst;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		String result = "";

		if(_filter_tag)	return;

		if(qName.equals("字:段落")){
			_para_counter ++;

			if(!_field_code){
				result = "<text:p>";
			}
		}
		else if(qName.equals("字:段落属性")){
			//the content inside this elment should be filtrated
			_filter_tag = true;

			attVal = atts.getValue("字:式样引用");
			_style_name = (attVal==null) ? "" : attVal;
		}
		else if(qName.equals("字:句")){
			//here wo don't know the _span_id.
			_span_id_stack.push("");
		}
		else if(qName.equals("字:文本串")){
			_is_chars = true;
		}
		else if(qName.equals("字:句属性")){
			//the content inside this element should be filtrated
			_filter_tag = true;
			attVal = atts.getValue("字:式样引用");
			_style_name = (attVal==null) ? "" : attVal;
		}
		else if(qName.equals("字:区域开始")){
			String id = atts.getValue("字:标识符");
			String secType = atts.getValue("字:类型");

			secType = (secType==null) ? "" : secType;
			_sec_type_map.put(id,secType);

			if(!_span_id.equals("")){
				result += "</text:span>";
				_span_id_stack.pop();
				_span_id_stack.push("");
			}

			if(secType.equals("hyperlink")){
				//return the attributes for <text:a>
				String att = HyperLink.get_link_atts(id);
				if(att != null){
					result += "<text:a" + att + ">";
				}
			}
			//If outline level is present, it means this
			//element is needed only in UOF(TOC).
			else if(secType.equals("bookmark")){
				result += "<text:bookmark-start text:name=\"" + id + "\"/>";
			}
			else if(secType.equals("annotation")){
				//return the <office:annotation> element
				result += Annotation.get_anno(id);
			}
		}
		else if(qName.equals("字:区域结束")){
			String id = atts.getValue("字:标识符引用");
			String secType = _sec_type_map.get(id);

			if(secType.equals("hyperlink")){
				result = "</text:a>";
			}
			else if(secType.equals("bookmark")){
				result = "<text:bookmark-end text:name=\"" + id + "\"/>";
			}
		}
		else if(qName.equals("字:尾注")){
			result = "<text:note text:note-class=\"endnote\">";
			if((attVal=atts.getValue("字:引文体")) != null){
				result += "<text:note-citation>" + attVal + "</text:note-citation>";
			}
			result += "<text:note-body>";
		}
		else if(qName.equals("字:脚注")){
			result = "<text:note text:note-class=\"footnote\">";
			if((attVal=atts.getValue("字:引文体")) != null){
				result += "<text:note-citation>" + attVal + "</text:note-citation>";
			}
			result += "<text:note-body>";
		}
		else if(qName.equals("字:图形")){
			attVal = atts.getValue("字:图形引用");
			result += Object_Set_Data.getDrawing(attVal);
		}

		else if(qName.equals("字:域开始")){
			_field_stack.push("field_");
		}
		else if(qName.equals("字:域代码")){
			_field_code = true;
		}
		else if(qName.equals("字:域结束")){
			String field = _field_stack.pop();

			if(field.equals("field_TOC")){
				_toc_end = true;
			}
		}

		else if(qName.equals("字:删除")){
			_filter_tag = true;
			attVal = atts.getValue("字:修订信息引用");

			result += "<text:change text:change-id=\"" + attVal + "\">";
			result += "</text:change>";
		}
		else if(qName.equals("字:插入开始")){
			_change_id = atts.getValue("字:修订信息引用");
			result += "<text:change-start text:change-id=\"" + _change_id + "\">";
		}
		else if(qName.equals("字:插入结束")){
			result += "<text:change-end text:change-id=\"" + _change_id + "\">";
		}
		else if(qName.equals("字:格式修订")){
			_change_id = atts.getValue("字:修订信息引用");
		}
		else if(qName.equals("字:制表符") && !_field_code){
			result += "<text:tab/>";
		}
		else if(qName.equals("字:空格符") && !_field_code){
			result += "<text:s";
			if((attVal=atts.getValue("字:个数"))!=null){
				result += " text:c=\"" + attVal + "\"";
			}
			result += "/>";
		}
		else if(qName.equals("字:换行符") && !_field_code){
			result += "<text:line-break/>";
		}

		_result += result;
	}

	public static void process_chars(String chs){
		if(_filter_tag)	return;

		if(_is_chars){
			if(_field_code){
				if(chs.contains("TOC")){
					_field_stack.pop();
					_field_stack.push("field_TOC");
					_toc_start = true;
				}
				else {
					_chs += chs;
				}
			}
			else {
				_result += chs;
			}
		}
	}

	public static void process_end(String qName){
		String result = "";

		if(_filter_tag){
			if(qName.equals("字:删除")){
				_filter_tag = false;
			}
			else if(qName.equals("字:句属性")){
				_filter_tag = false;

				_span_id = Text_Pro.get_text_name(_counter_of_text);
				_span_id = (_span_id==null) ? _style_name : _span_id;

				if(!_span_id.equals("")){
					_result += "<text:span text:style-name=\"" + _span_id + "\">";
					//replace the top of the stack
					_span_id_stack.pop();
					_span_id_stack.push(_span_id);
				}
				_style_name = "";
			}
			else if(qName.equals("字:段落属性")){
				_filter_tag = false;
				//treated specially in <presentation>
				if(Common_Data.get_file_type().equals("presentation")){
					_pr_style_name = _style_name;
				}
				else {
					String styleName = "";

					styleName = Para_Pro.get_para_name(_counter_of_para);
					styleName = (styleName==null) ? _style_name : styleName;

					if(!styleName.equals("") && _result.endsWith("<text:p>")){
						int len = _result.length();
						//get rid of ">"
						_result = _result.substring(0,len-1);
						_result += " text:style-name=\"" + styleName + "\"";
						//re-add ">"
						_result += ">";
					}
				}
				_style_name = "";
			}
			return;
		}

		else if(qName.equals("字:段落") && !_field_code){
			result += "</text:p>";
		}
		else if(qName.equals("字:大纲级别")){
			_outline_level = _chs;
		}
		else if(qName.equals("字:句")){
			_span_id = _span_id_stack.pop();
			if(!_span_id.equals("")){
				result += "</text:span>";
			}
		}
		else if(qName.equals("字:文本串")){
			_is_chars = false;
		}
		else if(qName.equals("字:域代码")){
			_field_code = false;
			result += Text_Field.process_field(_chs);
		}
		else if(qName.equals("字:域结束")){
			result += Text_Field.get_end_ele();
		}
		else if(qName.equals("字:尾注")){
			result += "</text:note-body></text:note>";
		}
		else if(qName.equals("字:脚注")){
			result += "</text:note-body></text:note>";
		}
		else if(qName.equals("字:插入开始")){
			result += "</text:change-start>";
		}
		else if(qName.equals("字:插入结束")){
			result += "</text:change-end>";
		}
		else if(qName.equals("字:格式修订")){
			result += "<text:change-start text:change-id=\"" + _change_id + "\"/>";
			result += _chs;
			result += "<text:change-end text:change-id=\"" + _change_id + "\"/>";

			_change_id = "";
		}

		if(!_field_code){
			_chs = "";
		}

		_result += result;
	}
}
