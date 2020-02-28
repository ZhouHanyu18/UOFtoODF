package style_set;

import org.xml.sax.Attributes;

import presentation.Presentation_Style;
import temp_structs.Common_Data;

public class Style_Set {
	//uof:���弯
	private static boolean _font_set_tag = false;
	//uof:�Զ���ż�
	private static boolean _autonum_set_tag = false;
	//uof:��ʽ��
	private static boolean _sent_style_tag = false;
	//uof:����ʽ��
	private static boolean _para_style_tag = false;
	//uof:���ֱ�ʽ��
	private static boolean _table_style_tag = false;
	//uof:��Ԫ��ʽ��
	private static boolean _cell_style_tag = false;
	//uof:����ʽ��(presentation)
	private static boolean _present_style_tag = false;
	
	
	public Style_Set() {	
	}
	
	public static void process_start(String qName,Attributes atts) {
		if (_font_set_tag){
			Font_Set.process_start(qName,atts);
		}
		else if (_autonum_set_tag){
			AutoNum_Set.process_start(qName,atts);
		}
		else if (_sent_style_tag){
			Sent_Style.process_start(qName,atts);
		}
		else if (_para_style_tag){
			Para_Style.process_start(qName,atts);
		}
		else if (_table_style_tag){
			Table_Style.process_start(qName,atts);
		}
		else if (_cell_style_tag){
			Cell_Style.process_start(qName,atts);
		}
		else if (_present_style_tag){
			Presentation_Style.process_start(qName,atts);
		}
		
		else if(qName.equals("uof:���弯")) {
			_font_set_tag = true;
			Font_Set.process_start(qName,atts);
		}
		else if(qName.equals("uof:�Զ���ż�")) {
			_autonum_set_tag = true;
			AutoNum_Set.process_start(qName,atts);
		}
		else if(qName.equals("uof:��ʽ��")) {
			_sent_style_tag = true;
			Sent_Style.process_start(qName,atts);
		}
		else if(qName.equals("uof:����ʽ��")) {
			String type = atts.getValue("��:����");
			
			if(Common_Data.get_file_type().equals("presentation") 
					&& type != null && type.equals("notes")){
				_present_style_tag = true;
				Presentation_Style.process_start(qName,atts);
			}
			else {
				_para_style_tag = true;
				Para_Style.process_start(qName,atts);
			}
		}
		else if(qName.equals("uof:���ֱ�ʽ��")) {
			_table_style_tag = true;
			Table_Style.process_start(qName,atts);
		}
		else if(qName.equals("uof:��Ԫ��ʽ��")) {
			_cell_style_tag = true;
			Cell_Style.process_start(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		if (_autonum_set_tag){
			AutoNum_Set.process_chars(chs);
		}
		else if (_sent_style_tag){
			Sent_Style.process_chars(chs);
		}
		else if (_para_style_tag){
			Para_Style.process_chars(chs);
		}
		else if (_table_style_tag){
			Table_Style.process_chars(chs);
		}
		else if (_cell_style_tag){
			Cell_Style.process_chars(chs);
		}
		else if (_present_style_tag){
			Presentation_Style.process_chars(chs);
		}
	}
	
	public static void process_end(String qName){
		if (_font_set_tag){
			Font_Set.process_end(qName);
			if(qName.equals("uof:���弯")) {
				_font_set_tag = false;
			}
		}
		else if (_autonum_set_tag){
			AutoNum_Set.process_end(qName);
			if(qName.equals("uof:�Զ���ż�")) {
				_autonum_set_tag = false;
			}
		}
		else if (_sent_style_tag){
			Sent_Style.process_end(qName);
			if(qName.equals("uof:��ʽ��")) {
				_sent_style_tag = false;
			}
		}
		else if (_para_style_tag){
			Para_Style.process_end(qName);
			if(qName.equals("uof:����ʽ��")) {
				_para_style_tag = false;
			}
		}
		else if (_table_style_tag){
			Table_Style.process_end(qName);
			if(qName.equals("uof:���ֱ�ʽ��")) {
				_table_style_tag = false;
			}
		}
		else if (_cell_style_tag){
			Cell_Style.process_end(qName);
			if(qName.equals("uof:��Ԫ��ʽ��")) {
				_cell_style_tag = false;
			}
		}
		else if (_present_style_tag){
			Presentation_Style.process_end(qName);
			if(qName.equals("uof:����ʽ��")) {
				_present_style_tag = false;
			}
		}
	}
}
