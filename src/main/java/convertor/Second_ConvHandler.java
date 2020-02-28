package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.Stack;

import text.*;
import metaData.*;
import style_set.*;
import spreadsheet.*;
import presentation.*;
import temp_structs.*;

public class Second_ConvHandler extends DefaultHandler {
	//
	private String _file_type = Common_Data.get_file_type();
	private boolean _meta_tag = false;
	private boolean _paragraph_tag = false;
	private boolean _table_tag = false;
	private Stack<String> _stack = new Stack<String>();
	private boolean _draw_page_tag = false;
	private boolean _filter_tag = false;
	private boolean _sheet_table_tag = false;
	private boolean _master_pag_tag = false;

	public Second_ConvHandler() {

	}

	private String autostyle_in_styles(){
		String styles = "";

		if(_file_type.equals("text")){
			styles += Page_Layout.get_result();
			styles += Stored_Data.getAutoStylesInStylesXml();
		}
		else if(_file_type.equals("spreadsheet")){
			styles += Page_Layout_S.get_page_layout();
		}
		else if(_file_type.equals("presentation")){
			styles = Page_Layout_p.get_result();
			styles += Master_Pane.get_dp_styles();
			styles += Stored_Data.getAutoStylesInStylesXml();
		}

		return styles;
	}

	private String master_styles(){
		String ms = "";

		if (_file_type.equals("presentation")){
			ms += Master_Pane.get_result();
		}
		else if(_file_type.equals("spreadsheet")){
			ms += Page_Layout_S.get_master_page();
		}
		else if(_file_type.equals("text")){
			ms += Master_Page.get_result();
		}

		return ms;
	}

	public void startDocument() throws SAXException {
		Text_p.restart();

		//输出styles.xml
		String styles = Font_Set.get_result();
		styles += "<office:styles>";
		styles += Stored_Data.getStylesInStylesXml();
		styles += Text_Config.get_result();
		styles += "</office:styles>";
		styles += "<office:automatic-styles>";
		styles += autostyle_in_styles();
		styles += "</office:automatic-styles>";
		styles += "<office:master-styles>";
		styles += master_styles();
		styles += "</office:master-styles>";
		styles += "</office:document-styles>";
		Results_Writer.processStyleResult(styles);

		//输出content.xml的style部分
		String content = "";
		content += Font_Set.get_result();
		content += "<office:automatic-styles>";
		content += Draw_Page_Style.get_result();
		content += AutoNum_Set.get_reuslt();
		content += Table_Style.get_result();
		content += Table_Row.get_result();
		content += Table_Column.get_result();
		content += Stored_Data.getAutoStylesInContentXml();
		content += Stored_Data.getCellStyleSet();
		content += "</office:automatic-styles>";
		Results_Writer.processContentResult(content);

		//
		Results_Writer.processContentResult("<office:body>");
	}

	public void endDocument() throws SAXException {
		//
		Results_Writer.processContentResult("</office:body>");
		Results_Writer.processContentResult("</office:document-content>");

		//输出meta.xml
		Results_Writer.processMetaResult("</office:document-meta>");
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		String result = "";

		Convertor_UOF_To_ODF.write_source_ta("parsing <" + qName + ">...\n");

		if(qName.equals("字:段落属性")){
			Text_p.plus_para_counter();
		}
		else if(qName.equals("字:句属性")){
			Text_p.plus_text_counter();
		}

		if(_filter_tag){
			return ;
		}
		else if(qName.equals("uof:链接集")||qName.equals("uof:对象集")
			||qName.equals("uof:式样集")||qName.equals("字:分节")
			||qName.equals("字:公用处理规则")){
			_filter_tag = true;
		}
		else if(_meta_tag){
			Meta.process_start(qName,atts);
		}
		else if(qName.equals("uof:元数据")){
			_meta_tag=true;
			Meta.process_start(qName,atts);
		}
		else if(_file_type.equals("text")){
			if(_table_tag){
				_stack.push(qName);
				Text_Table.process_start(qName,atts);
			}
			else if(_paragraph_tag){
				_stack.push(qName);
				Text_p.process_start(qName,atts);
			}
			else if(qName.equals("字:段落")){
				_stack.push(qName);
				_paragraph_tag = true;
				Text_p.process_start(qName,atts);
			}
			else if(qName.equals("字:文字表")){
				_stack.push(qName);
				_table_tag = true;
				Text_Table.process_start(qName,atts);
			}
			else if(qName.equals("uof:文字处理")){
				result += "<office:text>";				//office-text-content-prelude
				result += Text_Data.getTrackedChanges();
				result += Text_Field.create_seq_decls();
			}
		}

		else if(_file_type.equals("spreadsheet")){
			if(_sheet_table_tag){
				Sheet_Table.process_start(qName,atts);
			}
			else if(qName.equals("表:工作表")){
				_sheet_table_tag = true;
				Sheet_Table.process_start(qName,atts);
			}
			else if(qName.equals("uof:电子表格")){
				result += "<office:spreadsheet>";		//office-spreadsheet-content-prelude
				//<table:calculation-settings>
				result += Calculation_Setting.get_result();
				//<table:content-validations>
				result += Content_Validation.get_result();
			}
		}

		else if(_file_type.equals("presentation")){
			if(_draw_page_tag){
				Draw_Page.process_start(qName,atts);
			}
			else if(qName.equals("演:幻灯片") && !_master_pag_tag){
				_draw_page_tag = true;
				Draw_Page.process_start(qName,atts);
			}
			else if(qName.equals("uof:演示文稿")){
				result += "<office:presentation>";		//office-presentation-content-prelude
			}
			else if (qName.equals("演:母版集")) {
				_master_pag_tag = true;
			}
		}

		if(result.length()!=0) Results_Writer.processContentResult(result);
	}

	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
		String result = "";

		Convertor_UOF_To_ODF.write_source_ta("parsing <" + qName + ">...\n");

		if(qName.equals("uof:链接集")||qName.equals("uof:对象集")
		   ||qName.equals("uof:式样集")||qName.equals("字:分节")
		   ||qName.equals("字:公用处理规则")){
			_filter_tag = false;
		}
		else if(_filter_tag){
			return;
		}
		else if(qName.equals("uof:元数据")){
			_meta_tag=false;
			Meta.process_end(qName);
		}
		else if(_meta_tag){
			Meta.process_end(qName);
		}
		else if(_file_type.equals("text")){
			if(_paragraph_tag){
				_stack.pop();
				Text_p.process_end(qName);
				if(_stack.empty()){
					_paragraph_tag = false;
					result = Text_p.get_result();
				}
			}
			else if(_table_tag){
				_stack.pop();
				Text_Table.process_end(qName);
				if(_stack.empty()){
					_table_tag = false;
					result = Text_Table.get_result();
				}
			}
			else if(qName.equals("uof:文字处理")){	//office-text-content-epilogue
				result += "</office:text>";
			}
		}

		else if(_file_type.equals("spreadsheet")){

			if(qName.equals("表:工作表")){
				_sheet_table_tag=false;
				Sheet_Table.process_end(qName);
				result += Sheet_Table.get_result();
			}
			else if(_sheet_table_tag){
				Sheet_Table.process_end(qName);
			}
			else if(qName.equals("uof:电子表格")){	//office-spreadsheet-content-epilogue
				//<table:named-expressions>
				result += Name_Expression.get_result();
				//<table:database-ranges>
				result += Table_Filter.get_result();
				result += "</office:spreadsheet>";
			}
		}

		else if(_file_type.equals("presentation")){
			if (qName.equals("演:母版集")) {
				_master_pag_tag = false;
			}
			else if(qName.equals("演:幻灯片") && !_master_pag_tag){
				_draw_page_tag = false;
				Draw_Page.process_end(qName);
				result += Draw_Page.get_result();
			}
			else if(_draw_page_tag){
				Draw_Page.process_end(qName);
			}
			else if(qName.equals("uof:演示文稿")){	//office-presentation-content-epilogue
				//presentation:settings
					result += Presentation_Setting.get_result();
					result += "</office:presentation>";
			}
		}

		if(result.length()!=0)	Results_Writer.processContentResult(result);
	}

	public void characters(char[] ch, int start, int length)
	throws SAXException  {
		String chs = new String(ch, start, length);

		chs = chs.replaceAll("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);

		if(_filter_tag || chs.equals("")) 	return;

		if(_paragraph_tag){
			Text_p.process_chars(chs);
		}
		else if(_table_tag){
			Text_Table.process_chars(chs);
		}
		else if(_draw_page_tag){
			Draw_Page.process_chars(chs);
		}
		else if(_sheet_table_tag){
			Sheet_Table.process_chars(chs);
		}
		else if(_meta_tag){
			Meta.process_chars(chs);
		}
	}

	public void error(SAXParseException exception)
	{
		System.err.println("Error parsing the file: "+exception.getMessage());
	}

	public void warning(SAXParseException exception)
	{
		System.err.println("Warning parsing the file: "+exception.getMessage());
	}

	public void fatalError(SAXParseException exception)
	{
		System.err.println("Fatal error parsing the file: "+exception.getMessage());
		System.err.println("Cannot continue.");
	}
}
