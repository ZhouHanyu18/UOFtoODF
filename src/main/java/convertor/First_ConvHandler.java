package convertor;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import text.*;
import presentation.*;
import spreadsheet.*;
import temp_structs.*;
import object_set.*;
import style_set.*;


public class First_ConvHandler extends DefaultHandler {
	private String _file_type = Common_Data.get_file_type();

	private boolean _object_set_tag = false;			//uof:对象集
	private boolean _style_set_tag = false;				//uof:式样集

	//text
	private boolean _tracked_changes_tag = false;		//字:修订信息集
	private boolean _hyperlink_tag = false;				//uof:链接集
	private boolean _annotation_tag = false;			//字:批注集
	private boolean _deletion_tag = false;				//字:删除
	private boolean _sectype_tag = false;				//节属性
	private boolean _cell_pro_tag = false;				//字:单元格属性
	private boolean _row_pro_tag = false;				//字:表行属性
	private boolean _para_tag = false;					//字:段落
	private boolean _text_table_style_tag = false;		//字:文字表属性
	private boolean _para_pro_tag = false;				//字:段落属性
	private boolean _text_pro_tag = false;				//字:句属性
	private boolean _field_tag = false;					//字:域代码

	//spreadsheet
	private boolean _bookmark_tag = false;				//uof:书签（命名表达式）
	private boolean _validation_tag = false;			//表:数据有效性集
	private boolean _map_style_tag = false;				//表:条件格式化集
	private boolean _group_set_tag = false;				//表:分组集
	private boolean _page_layout_s_tag = false;			//表:页面设置

	//Added by CY.
	private boolean _chart_tag = false;
	private String _chartID = "";

	//presentation
	private boolean _master_pane_tag = false;			//演:母版
	private boolean _page_layout_p_tag = false;			//演:页面设置
	private boolean _present_setting_tag = false; 		//演:放映设置
	private boolean _dp_tag = false;					//演:切换/演:背景
	private boolean _present_style_tag = false;			//演:文本式样集
	private boolean _present_pl_tag = false;			//演:页面版式集

	private String _current_deletionID = "";
	private Stack<String> _stack = new Stack<String>(); 	//处理<字:段落>嵌套



	public First_ConvHandler() {

	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		Convertor_UOF_To_ODF.write_source_ta("parsing <" + qName + ">...\n");

		if(_para_pro_tag){
			Para_Pro.process_start(qName,atts);
		}
		else if(qName.equals("字:段落属性")){
			Para_Pro.plus_para_counter();
			Text_p.plus_para_counter();

			_para_pro_tag = true;
			Para_Pro.process_start(qName,atts);
		}

		if(_text_pro_tag){
			Text_Pro.process_start(qName,atts);
		}
		else if(qName.equals("字:句属性")){
			Text_p.plus_text_counter();
			Text_Pro.plus_text_counter();

			if(!_style_set_tag && !_present_style_tag){
				_text_pro_tag = true;
				Text_Pro.process_start(qName,atts);
			}
		}

		if (_chart_tag) {
			Chart.processStart(qName, atts);
		}
		else if(_style_set_tag){
			Style_Set.process_start(qName,atts);
		}
		else if(qName.equals("uof:式样集")){
			_style_set_tag = true;
		}

		else if(_object_set_tag){
			Object_Set.process_start(qName,atts);
		}
		else if(qName.equals("uof:对象集")){
			_object_set_tag = true;
		}

		else if(_hyperlink_tag){
			HyperLink.process(qName,atts);
		}
		else if(qName.equals("uof:链接集")){
			_hyperlink_tag = true;
		}

		else if(_bookmark_tag){
			Name_Expression.process_start(qName,atts);
		}
		else if(qName.equals("uof:书签集")){
			_bookmark_tag = true;
		}
		else if(_file_type.equals("text")){
			if(_annotation_tag){
				Annotation.process_start(qName,atts);
			}
			else if(qName.equals("字:批注集")){
				_annotation_tag = true;
			}

			else if(_sectype_tag){
				Page_Layout.process_start(qName,atts);
			}
			else if(qName.equals("字:节属性")){
				Para_Pro.in_sec_type(true);
				Text_Pro.in_sec_type(true);
				_sectype_tag = true;
				Page_Layout.process_start(qName,atts);
			}

			else if(_deletion_tag){
				Text_p.process_start(qName,atts);
			}
			else if(qName.equals("字:删除")){
				_deletion_tag = true;
				_current_deletionID = atts.getValue("字:修订信息引用");
				Text_Data.addChangeType(_current_deletionID, "deletion");
			}

			else if(_para_tag){
				_stack.push(qName);

				if(qName.equals("字:域代码")){
					_field_tag = true;
				}
				List_Para.process_start(qName,atts);
				Text_Anchor.process_start(qName,atts);
			}
			else if(qName.equals("字:段落")){
				_para_tag = true;
				_stack.push(qName);
				List_Para.process_start(qName,atts);
			}

			else if(_cell_pro_tag){
				Cell_Pro.process_start(qName,atts);
			}
			else if(qName.equals("字:单元格属性")){
				_cell_pro_tag = true;
				Cell_Pro.process_start(qName,atts);
			}

			else if(_text_table_style_tag){
				Table_Style.process_start(qName,atts);
			}
			else if(qName.equals("字:文字表属性")){
				_text_table_style_tag = true;
				Table_Style.process_start(qName,atts);
			}

			else if(_row_pro_tag){
				Table_Row.process_text_atts(qName,atts);
			}
			else if(qName.equals("字:表行属性")){
				_row_pro_tag = true;
				Table_Row.process_text_atts(qName,atts);
			}

			else if(qName.equals("字:插入开始")){
				Text_Data.addChangeType(atts.getValue("字:修订信息引用"),"insertion");
			}
			else if(qName.equals("字:格式修订")){
				Text_Data.addChangeType(atts.getValue("字:修订信息引用"),"format-change");
			}

			else if(_tracked_changes_tag){
				Tracked_changes.process(qName,atts);
			}
			else if(qName.equals("字:修订信息集")){
				_tracked_changes_tag = true;
			}
			else if(qName.equals("字:用户")){
				String id = atts.getValue("字:标识符");
				String name = atts.getValue("字:姓名");
				Text_Data.addUser(id,name);
			}
		}
		else if(_file_type.equals("spreadsheet")){
			if(qName.equals("表:工作表")){
				Table_Style.process_sheet_atts(qName,atts);

				Table_Shapes.set_table_id(atts.getValue("表:名称"));
			}
			else if(qName.equals("表:列")){
				Sheet_Table.set_has_col(true);
				Table_Column.process_sheet_col(qName,atts);
			}
			else if(qName.equals("表:行")){
				Table_Row.process_sheet_row(qName,atts);
			}
			else if(qName.equals("表:工作表内容")){
				Table_Column.process_sheet_col(qName,atts);
				Table_Row.process_sheet_row(qName,atts);
			}

			else if(qName.equals("表:计算设置")
					||qName.equals("表:日期系统-1904")
					||qName.equals("表:精确度以显示值为准")){
				Calculation_Setting.process_start(qName,atts);
			}

			else if(_validation_tag){
				Content_Validation.process_start(qName,atts);
			}
			else if(qName.equals("表:数据有效性集")){
				_validation_tag = true;
				Content_Validation.process_start(qName,atts);
			}

			else if(_map_style_tag){
				Style_Map.process_start(qName,atts);
			}
			else if(qName.equals("表:条件格式化集")){
				_map_style_tag = true;
			}

			else if(_group_set_tag){
				Table_Group.process_start(qName,atts);
			}
			else if(qName.equals("表:分组集")){
				_group_set_tag = true;
			}

			else if(_page_layout_s_tag){
				Page_Layout_S.process_start(qName,atts);
			}
			else if(qName.equals("表:页面设置")){
				_page_layout_s_tag = true;
				Page_Layout_S.process_start(qName,atts);
			}

			//Added by CY.
			else if(qName.equals("uof:锚点") || qName.equals("表:批注")){
				Table_Shapes.process_start(qName,atts);
			}
			else if(qName.equals("表:图表")){
				_chart_tag = true;
				_chartID = IDGenerator.get_chart_id();
				Chart.set_ID(_chartID);
				ZipCompress.addFolder(_chartID);
				Spreadsheet_Data.add_table_chart(_chartID);

				Chart.processStart(qName, atts);
			}
		}

		else if(_file_type.equals("presentation")){
			if(_dp_tag){
				Draw_Page_Style.process_start(qName,atts);
			}
			else if(qName.equals("演:幻灯片")){
				_dp_tag = true;
				Draw_Page_Style.process_start(qName,atts);
			}

			else if(_present_style_tag){
				Presentation_Style.process_start(qName,atts);
			}
			else if(qName.equals("演:文本式样集")){
				_present_style_tag = true;
			}

			else if(_present_pl_tag){
				Presentation_Page_Layout.process_start(qName,atts);
			}
			else if(qName.equals("演:页面版式集")){
				_present_pl_tag = true;
			}

			else if(_master_pane_tag){
				Master_Pane.process_start(qName,atts);
			}
			else if(qName.equals("演:母版集")){
				_master_pane_tag = true;
			}

			else if(_page_layout_p_tag){
				Page_Layout_p.process_start(qName,atts);
			}
			else if(qName.equals("演:页面设置集")){
				_page_layout_p_tag = true;
			}

			else if(_present_setting_tag){
				Presentation_Setting.process_start(qName,atts);
			}
			else if(qName.equals("演:放映设置")){
				_present_setting_tag = true;
			}
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
		Convertor_UOF_To_ODF.write_source_ta("parsing </" + qName + ">...\n");

		if(_para_pro_tag){
			Para_Pro.process_end(qName);
			if(qName.equals("字:段落属性")){
				_para_pro_tag = false;
			}
		}

		else if(_text_pro_tag){
			Text_Pro.process_end(qName);
			if(qName.equals("字:句属性")){
				_text_pro_tag = false;
			}
		}

		if(_style_set_tag){
			Style_Set.process_end(qName);
			if(qName.equals("uof:式样集")){
				_style_set_tag = false;
			}
		}

		else if(_object_set_tag){
			Object_Set.process_end(qName);
			if(qName.equals("uof:对象集")){
				_object_set_tag = false;
			}
		}

		else if(qName.equals("uof:链接集")){
			_hyperlink_tag = false;
		}
		else if(qName.equals("uof:书签集")){
			_bookmark_tag = false;
		}

		else if(_file_type.equals("text")){
			if(_para_tag){
				if(qName.equals("字:域代码")){
					 _field_tag = false;
				}

				Text_Anchor.process_end(qName);
				_stack.pop();
				if(_stack.empty()){
					_para_tag = false;
				}
			}

			else if(qName.equals("字:修订信息集")){
				_tracked_changes_tag = false;
			}

			else if(qName.equals("字:批注集")){
				_annotation_tag = false;
				Annotation.process_end(qName);
			}
			else if(_annotation_tag){
				Annotation.process_end(qName);
			}

			else if(qName.equals("字:删除")){
				_deletion_tag = false;
				Text_Data.addDeletionData(_current_deletionID,Text_p.get_result());
			}
			else if(_deletion_tag){
				Text_p.process_end(qName);
			}

			else if(_cell_pro_tag){
				Cell_Pro.process_end(qName);
				if(qName.equals("字:单元格属性")){
					_cell_pro_tag = false;
					Stored_Data.addCellStyle(Cell_Pro.get_result());
				}
			}

			else if(_text_table_style_tag){
				Table_Style.process_end(qName);
				if(qName.equals("字:文字表属性")){
					_text_table_style_tag = false;
				}
			}

			else if(qName.equals("字:表行属性")){
				Table_Row.add_text_atts();
				_row_pro_tag = false;
			}

			else if(_sectype_tag){
				Page_Layout.process_end(qName);
				if(qName.equals("字:节属性")){
					Para_Pro.in_sec_type(false);
					Text_Pro.in_sec_type(false);
					_sectype_tag = false;
				}
			}
		}

		else if(_file_type.equals("spreadsheet")){
			//===Added by CY.===
			if (_chart_tag){
				Chart.processEnd(qName);
			}

			if(qName.equals("uof:锚点") || qName.equals("表:批注")){
				Table_Shapes.process_end(qName);
			}
			else if(qName.equals("表:图表")){
				_chart_tag = false;
				String chartFrame = Chart.getChartFrame();
				Table_Shapes.add_one_shape(chartFrame);
				ZipCompress.addFile(_chartID, "content.xml");
				String chartContent = Chart.get_content();
				ZipCompress.writeFile(_chartID, "content.xml", chartContent);
				_chartID = "";
			}
			//==================

			else if(qName.equals("表:分组集")){
				_group_set_tag = false;
			}

			else if(_page_layout_s_tag){
				Page_Layout_S.process_end(qName);
				if(qName.equals("表:页面设置")){
					_page_layout_s_tag = false;
				}
			}

			else if(_validation_tag){
				Content_Validation.process_end(qName);
				if(qName.equals("表:数据有效性集")){
					_validation_tag = false;
				}
			}

			else if(_map_style_tag){
				Style_Map.process_end(qName);
				if(qName.equals("表:条件格式化集")){
					_map_style_tag = false;
				}
			}
		}

		else if(_file_type.equals("presentation")){
			if(_dp_tag){
				Draw_Page_Style.process_end(qName);
				if(qName.equals("演:幻灯片")){
					_dp_tag = false;
				}
			}

			else if(_present_style_tag){
				Presentation_Style.process_end(qName);
				if(qName.equals("演:文本式样集")){
					_present_style_tag = false;
				}
			}

			else if(_present_pl_tag){
				Presentation_Page_Layout.process_end(qName);
				if(qName.equals("演:页面版式集")){
					_present_pl_tag = false;
				}
			}

			else if(_master_pane_tag){
				Master_Pane.process_end(qName);
				if(qName.equals("演:母版集")){
					_master_pane_tag = false;
				}
			}

			else if(_page_layout_p_tag){
				Page_Layout_p.process_end(qName);
				if(qName.equals("演:页面设置集")){
					_page_layout_p_tag = false;
				}
			}

			else if(_present_setting_tag){
				Presentation_Setting.process_end(qName);
				if(qName.equals("演:放映设置")){
					_present_setting_tag = false;
				}
			}
		}
	}

	public void characters(char[] ch, int start, int length)
	throws SAXException  {
		String chs = new String(ch, start, length);

		if(chs.equals(""))		return ;
		chs = chs.replaceAll("&", Common_Data.ANDTAG);
		chs = chs.replaceAll("<", Common_Data.LTAG);


		if(_para_pro_tag){
			Para_Pro.process_chars(chs);
		}
		else if(_text_pro_tag){
			Text_Pro.process_chars(chs);
		}
		else if(_object_set_tag){
			Object_Set.process_chars(chs);
		}
		else if(_style_set_tag){
			Style_Set.process_chars(chs);
		}

		else if(_file_type.equals("text")){
			if(_field_tag && chs.toUpperCase().contains("SEQ")){
				Text_Field.add_seq_vari(chs);
			}

			if(_para_tag){
				Text_Anchor.process_chars(chs);
			}
			else if(_annotation_tag){
				Annotation.process_chars(chs);
			}
			else if(_cell_pro_tag){
				Cell_Pro.process_chars(chs);
			}
			else if(_text_table_style_tag){
				Table_Style.process_chars(chs);
			}
			else if(_sectype_tag){
				Page_Layout.process_chars(chs);
			}
			else if(_deletion_tag){
				Text_p.process_chars(chs);
			}
		}

		else if(_file_type.equals("spreadsheet")){
			if(_validation_tag){
				Content_Validation.process_chars(chs);
			}
			else if(_map_style_tag){
				Style_Map.process_chars(chs);
			}
			else if(_page_layout_s_tag){
				Page_Layout_S.process_chars(chs);
			}
			else if (_chart_tag) {
				Chart.process_chars(chs);
			}
		}

		else if(_file_type.equals("presentation")){
			if(_present_setting_tag){
				Presentation_Setting.process_chars(chs);
			}
			else if(_dp_tag){
				Draw_Page_Style.process_chars(chs);
			}
			else if(_present_style_tag){
				Presentation_Style.process_chars(chs);
			}
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