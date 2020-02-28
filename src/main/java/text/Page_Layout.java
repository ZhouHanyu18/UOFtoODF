package text;

import org.xml.sax.Attributes;
import java.util.ArrayList;
import temp_structs.Common_Data;
import style_set.Common_Pro;
/**
 * 
 * @author xie
 *
 */
public class Page_Layout extends Common_Pro{
	private static String _chs = "";
	//the result
	private static String _result = "";
	//attributes of <style:page-layout-properties>
	private static String _page_layout_pro = ""; 
	//
	private static String _header_style = "";
	//
	private static String _footer_style = "";
	//
	private static String _column_count = "";			//����
	private static String _columns = "";				//<��:����>��ת�����
	private static int _page_layout_id = 0;				//
	private static ArrayList<Float> column_gap_list = new ArrayList<Float>(10);	
	//
	private static boolean _master_page_tag = false;
	
	
	protected static void clear(){
		_chs = "";
		_page_layout_pro = "";	
		_header_style = "";
		_footer_style = "";
		_column_count = "";
		_columns = "";
	}
	
	//store one <style:page-layout> to the result
	public static void commit_result(){
		float averOfGap = 0.0f;
		float sumOfGap = 0.0f;
		
		String str = "<style:page-layout style:name=\"" + gen_PL_id() + "\">";
		str += "<style:page-layout-properties" + _page_layout_pro + ">";	
		
		if(_columns.length()!= 0){
			str += "<style:columns";
			if(_column_count.length() != 0){
				str += " fo:column-count=\"" + _column_count + "\"";
			}
			
			if(column_gap_list.size() != 0){
				for(int i=0; i<column_gap_list.size(); i++){
					sumOfGap += column_gap_list.get(i).floatValue();
				}			
				averOfGap = sumOfGap/column_gap_list.size();
			}

			str += " fo:column-gap=\"" + averOfGap + Common_Data.get_unit() + "\"";
			str += ">";
			
			str += _columns;
			str += "</style:columns>";
		}
		str += "</style:page-layout-properties>";
		str += _header_style;
		str += _footer_style;
		str += "</style:page-layout>";
		
		_result += str;
		clear(); 
	} 
	
	public static String get_result(){
		String s = _result;
		
		_result = "";
		_page_layout_id = 0;
		
		return s;
	}
	
	public static void process_start(String qName,Attributes atts){
		String str = "";
		String attVal = "";
		
		if(_master_page_tag){
			Master_Page.process_start(qName,atts);
		}
		else if(qName.equals("��:ҳü")||qName.equals("��:ҳ��")){
			_master_page_tag = true;
			Master_Page.process_start(qName,atts);
		}
		
		else if(qName.equals("��:ֽ��")){
			_page_layout_pro += get_page(atts);
		}
		else if(qName.equals("��:ҳ�߾�")){
			_page_layout_pro += get_margins(atts);
		}
		else if(qName.equals("uof:��")||qName.equals("uof:��")
			  ||qName.equals("uof:��")||qName.equals("uof:��")){
			_page_layout_pro += get_borders(qName, atts);	  
		}
		else if(qName.equals("��:��������")){
			if((attVal=atts.getValue("��:��������"))!=null){
				str += " style:layout-grid-mode=\"" 
					+ conv_grid_mode(attVal) + "\"";
			}
			if((attVal=atts.getValue("��:�߶�"))!=null){
				str += " style:layout-grid-base-height=\"" 
					+ attVal + Common_Data.get_unit() + "\"";
			}
			if((attVal=atts.getValue("��:��ʾ����"))!=null){
				str += " style:layout-grid-display=\"" + attVal + "\"";
			}
			if((attVal=atts.getValue("��:��ӡ����"))!=null){
				str += " style:layout-grid-print=\"" + attVal + "\"";
			}
			_page_layout_pro += str;
		}
		
		else if(qName.equals("��:ҳ������")){
			if((attVal=atts.getValue("��:��ʽ"))!=null){
				str += " style:num-format=\"" + conv_num_format(attVal) + "\"";
			}
			if((attVal=atts.getValue("��:��ʼ���"))!=null){
				str += " style:first-page-number=\"" + attVal + "\"";
			}
			
			_page_layout_pro += str;
		}
		
		else if(qName.equals("��:����")){
			_column_count = atts.getValue("��:����");     //����_column_count
			
			str += "<style:column-sep";
			if((attVal=atts.getValue("��:�ָ���"))!=null){
				str += " style:style=\"" + conv_line_style(attVal) + "\"";
			}
			if((attVal=atts.getValue("��:�ָ��߿��"))!=null){
				str += " style:width=\"" + attVal + Common_Data.get_unit() +  "\"";
			}
			if((attVal=atts.getValue("��:�ָ�����ɫ"))!=null){
				str += " style:color=\"" + attVal + "\"";
			}
			str += "/>";
			
			_columns += str;
		}
		
		else if(qName.equals("��:��")){			
			if((attVal=atts.getValue("��:���"))!=null){
				column_gap_list.add(Float.parseFloat(attVal));
			}
			
			str += "<style:column";
			if((attVal=atts.getValue("��:���"))!=null){
				float relF = Float.parseFloat(attVal) * 100;
				int relI = new Float(relF).intValue();
				str += " style:rel-width=\"" + relI + "*\"";		//��*��
			}
			str += "/>";
			_columns += str;
		}
		
		else if(qName.equals("��:ҳüλ��")){
			if((attVal=atts.getValue("��:���о")) != null){
				_header_style += "<style:header-style>";
				_header_style += "<style:header-footer-properties";
				_header_style += " fo:margin-bottom=\"" + attVal 
						+ Common_Data.get_unit() + "\"/>";
				_header_style += "</style:header-style>";
			}
		}
		
		else if(qName.equals("��:ҳ��λ��")){
			if((attVal=atts.getValue("��:���о")) != null){
				_footer_style += "<style:footer-style>";
				_footer_style += "<style:header-footer-properties";
				_footer_style += " fo:margin-top=\"" + attVal
						+ Common_Data.get_unit() + "\"/>";
				_footer_style += "</style:footer-style>";
			}
		}
		
		else if(qName.equals("��:βע����")){
			Text_Config.process(qName,atts);			
		}
		else if(qName.equals("��:��ע����")){
			Text_Config.process(qName,atts);
		}
		else if(qName.equals("��:�к�����")){
			Text_Config.process(qName,atts);
		}
	}
	
	public static void process_chars(String chs){
		if(_master_page_tag){
			Master_Page.process_chars(chs);
		}
		else{
			_chs = chs;
		}
	}
	
	public static void process_end(String qName){

		if(qName.equals("��:ҳü")||qName.equals("��:ҳ��")){
			_master_page_tag = false;
			Master_Page.process_end(qName);
		}
		else if(_master_page_tag){
			Master_Page.process_end(qName);
		}
		
		else if(qName.equals("��:ֽ�ŷ���")){
			_page_layout_pro += " style:print-orientation=\"" + _chs + "\"";
		}
		else if(qName.equals("uof:��ɫ")){      //��������ɫ
			_page_layout_pro += " fo:background-color\"" + _chs + "\"";
		}
		else if(qName.equals("��:ҳ���ʽ")){   //??
			_page_layout_pro += " style:num-format\"" + _chs + "\"";
		}
		else if(qName.equals("��:�������з���")){
			_page_layout_pro += " style:writing-mode=\"" 
				+ conv_writing_mode(_chs.trim()) + "\"";
		}
		else if(qName.equals("��:������")){
			commit_result();
			Master_Page.commit_result();
		}
	}
	
	private static String conv_line_style(String val){
		String str = "solid";	//Ĭ�ϵ���������
		
		if(val.equals("none")){
			str = "none";
		}
		else if(val.equals("single")){
			str = "solid";
		}
		else if(val.equals("dotted")){
			str = "dotted";
		}
		else if(val.equals("dash")){
			str = "dashed";
		}
		else if(val.equals("dot-dash")){
			str = "dot-dashed";
		}
		
		return str;
	}
	
	private static String conv_writing_mode(String val){
		//default value is lr-tb
		String str = "lr-tb";		
		
		//lr-tb����left-to-right and top-to-bottom
		if(val.equals("hori-l2r")){       
			str = "lr-tb";
		}
		else if(val.equals("hori-r2l")){
			str = "rl-tb";
		}
		else if(val.equals("vert-r2l")){
			str = "tb-rl";
		}
		else if(val.equals("vert-l2r")){
			str = "tb-lr";
		}
		
		return str;
	}
	
	//generate a style name for <style:page-layout>
	private static String gen_PL_id(){
		_page_layout_id++;
		return "pm" + _page_layout_id;
	}

	//convert the grid mode from UOF to ODF
	private static String conv_grid_mode(String val){
		String str = "none";
		/*�����������ʾ��һ�������¶�Ӧ��������
		if(val.equals("none")){
			str = "none";
		}
		else if(val.equals("line")){
			str = "line";
		}
		else if(val.equals("line-char")){
			str = "both";
		}
		*/
		return str;
	}
}
