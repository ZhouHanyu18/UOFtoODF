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
	//stack for <��:�����>'s nesting
	private static Stack<String> _field_stack = new Stack<String>();
	//<text:table-of-content>'s tag.
	private static boolean _toc_start = false;
	//<text:table-of-content>'s end-tag.
	// It's set in <��:�����>
	private static boolean _toc_end = false;	
	//tag for <��:�����>
	private static boolean _field_code = false;
	//<��:��ټ���>
	private static String _outline_level = "";
	//map for <��:����ʼ>����
	private static Map<String,String> _sec_type_map = new TreeMap<String,String>();
	//tag for <��:�ı���>
	private static boolean _is_chars = false;
	//<text:span>'s style name
	private static String _span_id = "";
	//��:ʽ������
	private static String _style_name = "";
	//stack for <text:span>'s style name in case of <��:��>'s nesting
	private static Stack<String> _span_id_stack = new Stack<String>();
	//�޶���Ϣ����
	private static String _change_id = "";
	//counter of paragraph,corresponding to List_Para's
	private static int _para_counter = 0;
	//counter of <��:������>,corresponding to Para_Pro's
	private static int _counter_of_text = 0;
	//counter of <��:��������>,corresponding to Para_Pro's
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
						" text:protected=\"true\" text:name=\"����Ŀ¼1\">";
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
		
		if(qName.equals("��:����")){
			_para_counter ++;
			
			if(!_field_code){
				result = "<text:p>";
			}
		}
		else if(qName.equals("��:��������")){
			//the content inside this elment should be filtrated
			_filter_tag = true;
			
			attVal = atts.getValue("��:ʽ������");
			_style_name = (attVal==null) ? "" : attVal;
		}
		else if(qName.equals("��:��")){
			//here wo don't know the _span_id.
			_span_id_stack.push("");
		}
		else if(qName.equals("��:�ı���")){
			_is_chars = true;
		}
		else if(qName.equals("��:������")){
			//the content inside this element should be filtrated
			_filter_tag = true;	
			attVal = atts.getValue("��:ʽ������");
			_style_name = (attVal==null) ? "" : attVal;
		}
		else if(qName.equals("��:����ʼ")){		
			String id = atts.getValue("��:��ʶ��");
			String secType = atts.getValue("��:����");
			
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
		else if(qName.equals("��:�������")){	
			String id = atts.getValue("��:��ʶ������");
			String secType = _sec_type_map.get(id);
			
			if(secType.equals("hyperlink")){
				result = "</text:a>";
			}
			else if(secType.equals("bookmark")){
				result = "<text:bookmark-end text:name=\"" + id + "\"/>";
			}
		}
		else if(qName.equals("��:βע")){			
			result = "<text:note text:note-class=\"endnote\">";	
			if((attVal=atts.getValue("��:������")) != null){
				result += "<text:note-citation>" + attVal + "</text:note-citation>";
			}
			result += "<text:note-body>";		
		}
		else if(qName.equals("��:��ע")){
			result = "<text:note text:note-class=\"footnote\">";	
			if((attVal=atts.getValue("��:������")) != null){
				result += "<text:note-citation>" + attVal + "</text:note-citation>";
			}
			result += "<text:note-body>";	
		}
		else if(qName.equals("��:ͼ��")){
			attVal = atts.getValue("��:ͼ������");
			result += Object_Set_Data.getDrawing(attVal);
		}
		
		else if(qName.equals("��:��ʼ")){
			_field_stack.push("field_");
		}
		else if(qName.equals("��:�����")){
			_field_code = true;
		}
		else if(qName.equals("��:�����")){
			String field = _field_stack.pop();
			
			if(field.equals("field_TOC")){
				_toc_end = true;
			}
		}
		
		else if(qName.equals("��:ɾ��")){
			_filter_tag = true;
			attVal = atts.getValue("��:�޶���Ϣ����");
			
			result += "<text:change text:change-id=\"" + attVal + "\">";
			result += "</text:change>";
		}
		else if(qName.equals("��:���뿪ʼ")){
			_change_id = atts.getValue("��:�޶���Ϣ����");
			result += "<text:change-start text:change-id=\"" + _change_id + "\">";
		}
		else if(qName.equals("��:�������")){
			result += "<text:change-end text:change-id=\"" + _change_id + "\">";
		}
		else if(qName.equals("��:��ʽ�޶�")){
			_change_id = atts.getValue("��:�޶���Ϣ����");
		}
		else if(qName.equals("��:�Ʊ��") && !_field_code){
			result += "<text:tab/>";
		}
		else if(qName.equals("��:�ո��") && !_field_code){
			result += "<text:s";
			if((attVal=atts.getValue("��:����"))!=null){
				result += " text:c=\"" + attVal + "\"";
			}			
			result += "/>";
		}
		else if(qName.equals("��:���з�") && !_field_code){
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
			if(qName.equals("��:ɾ��")){
				_filter_tag = false;
			}
			else if(qName.equals("��:������")){
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
			else if(qName.equals("��:��������")){
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
		
		else if(qName.equals("��:����") && !_field_code){
			result += "</text:p>";
		}
		else if(qName.equals("��:��ټ���")){
			_outline_level = _chs;
		}
		else if(qName.equals("��:��")){	
			_span_id = _span_id_stack.pop();
			if(!_span_id.equals("")){
				result += "</text:span>";
			}
		}
		else if(qName.equals("��:�ı���")){
			_is_chars = false;
		}
		else if(qName.equals("��:�����")){
			_field_code = false;
			result += Text_Field.process_field(_chs);
		}
		else if(qName.equals("��:�����")){
			result += Text_Field.get_end_ele();
		}
		else if(qName.equals("��:βע")){
			result += "</text:note-body></text:note>";
		}
		else if(qName.equals("��:��ע")){
			result += "</text:note-body></text:note>";
		}
		else if(qName.equals("��:���뿪ʼ")){
			result += "</text:change-start>";
		}
		else if(qName.equals("��:�������")){
			result += "</text:change-end>";
		}
		else if(qName.equals("��:��ʽ�޶�")){
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
