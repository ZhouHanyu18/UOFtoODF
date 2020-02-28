package spreadsheet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.util.*;

import style_set.Sent_Style;
import temp_structs.Common_Data;


public class Chart {

	private static String _ID = "";
	private static String _text_node = "";   //���ڴ���ı��ڵ������
	private static boolean _need_to_store_text = false;   //��ʶ�Ƿ���Ҫȡ���ı��ڵ��ֵ
	
	private static String _content = "";
	
	private static final String _content_begin = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<office:document-content" +
			" xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"" +
			" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"" +
			" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\"" +
			" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\"" +
			" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"" +
			" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"" +
			" xmlns:xlink=\"http://www.w3.org/1999/xlink\"" +
			" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
			" xmlns:meta=\"urn:oasis:names:tc:opendocument:xmlns:meta:1.0\"" +
			" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\"" +
			" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"" +
			" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\"" +
			" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\"" +
			" xmlns:math=\"http://www.w3.org/1998/Math/MathML\"" +
			" xmlns:form=\"urn:oasis:names:tc:opendocument:xmlns:form:1.0\"" +
			" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\"" +
			" xmlns:ooo=\"http://openoffice.org/2004/office\"" +
			" xmlns:ooow=\"http://openoffice.org/2004/writer\"" +
			" xmlns:oooc=\"http://openoffice.org/2004/calc\"" +
			" xmlns:dom=\"http://www.w3.org/2001/xml-events\"" +
			" xmlns:xforms=\"http://www.w3.org/2002/xforms\"" +
			" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" +
			" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
			" office:version=\"1.0\">";
	
	private static String _auto_style = "";
	private static String _body = "<office:body><office:chart>";
	private static String _main_title = "";   
	private static String _x_title = "";
	private static String _y_title = "";
	private static String _legend = "";
	private static String _plot_area_begin = "<chart:plot-area";
	private static String _x_axis = "";
	private static String _y_axis = "";
	private static String _x_grid = "";
	private static String _y_grid = "";
	private static String _x_cates = "";
	private static String _y_cates = "";
	private static Map<String,String> _data_series_map = new HashMap<String,String>();
	private static String _series_source = "";
	
	//����<draw:frame>
	private static float _svgX = 0;   
	private static float _svgY = 0;
	private static float _width = 0;   
	private static float _height = 0;
	private static String _cellRange = "";
	
	//�������ڴ���style
	private static final String _style_prefix = "ch";
	private static int _style_num = 0;
	private static String _current_chart_pro = "";
	private static String _current_graphic_pro = "";
	private static String _current_para_pro = "";
	private static String _current_text_pro = "";
	
	private static String _plot_chart_pro = "";
	
	private static boolean _is_in_text = false;   //��ʶ�Ƿ�λ��<����>Ԫ����
	
	private static double _axis_interval_major = 0;
	
	private static boolean _3D = false;
	private static boolean _vertical = false;
	private static boolean _pie_offset = false;
	private static boolean _stacked = false;
	private static boolean _percentage = false;
	private static boolean _symbol = false;
	private static boolean _lines = false;
	
	private static boolean _serie_proc_tag = false;
	
	private static String _plotstyle = "";
	
/*  Do not need style.xml
 	private static final String _style_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
			"<office:document-styles" +
			" xmlns:office=\"urn:oasis:names:tc:opendocument:xmlns:office:1.0\"" +
			" xmlns:style=\"urn:oasis:names:tc:opendocument:xmlns:style:1.0\"" +
			" xmlns:text=\"urn:oasis:names:tc:opendocument:xmlns:text:1.0\"" +
			" xmlns:table=\"urn:oasis:names:tc:opendocument:xmlns:table:1.0\"" +
			" xmlns:draw=\"urn:oasis:names:tc:opendocument:xmlns:drawing:1.0\"" +
			" xmlns:fo=\"urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0\"" +
			" xmlns:xlink=\"http://www.w3.org/1999/xlink\"" +
			" xmlns:dc=\"http://purl.org/dc/elements/1.1/\"" +
			" xmlns:number=\"urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0\"" +
			" xmlns:svg=\"urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0\"" +
			" xmlns:chart=\"urn:oasis:names:tc:opendocument:xmlns:chart:1.0\"" +
			" xmlns:dr3d=\"urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0\"" +
			" xmlns:script=\"urn:oasis:names:tc:opendocument:xmlns:script:1.0\"" +
			" xmlns:ooo=\"http://openoffice.org/2004/office\"" +
			" xmlns:ooow=\"http://openoffice.org/2004/writer\"" +
			" xmlns:oooc=\"http://openoffice.org/2004/calc\"" +
			" xmlns:dom=\"http://www.w3.org/2001/xml-events\"" +
			" office:version=\"1.0\">" +
			"<office:styles/>" +
			"</office:document-styles>";*/
	
	private static void addStyle() {
		_auto_style += "<style:style style:name=\"" + _style_prefix + _style_num + "\" style:family=\"chart\">";
		
		_auto_style += "<style:chart-properties" + _current_chart_pro + "/>";
		
		if (_current_graphic_pro.length() != 0)
			_auto_style += "<style:graphic-properties" + _current_graphic_pro + "/>";
		if (_current_para_pro.length() != 0)
			_auto_style += "<style:paragraph-properties" + _current_para_pro + "/>";
		if (_current_text_pro.length() != 0)
			_auto_style += "<style:text-properties" + _current_text_pro + "/>";
		_auto_style += "</style:style>";
		
		_current_chart_pro = "";
		_current_graphic_pro = "";
		_current_para_pro = "";
		_current_text_pro = "";	
	}
	
	public static void processStart(String qName,Attributes atts){
		String value = "";
		
		if (_is_in_text)
			Sent_Style.process_start(qName,atts);
		
		else if (qName.equals("��:ͼ��")) {
			_style_num ++;
			
			_svgX = Float.valueOf(atts.getValue("��:x����"));
			_svgY = Float.valueOf(atts.getValue("��:y����"));
			_width = Float.valueOf(atts.getValue("��:���"));
			_height = Float.valueOf(atts.getValue("��:�߶�")); 
			
			String type = atts.getValue("��:����");
			String subtype = atts.getValue("��:������");
			String chartClass = getClass(type, subtype);
			
			_body += "<chart:chart svg:width=\"" + measure_floatval(_width) + "\" svg:height=\""
			+ measure_floatval(_height) + "\" chart:class=\"" + chartClass
			+ "\" chart:style-name=\"" + _style_prefix + _style_num + "\">";
		}
		else if (qName.equals("��:�߿�")) {
			_current_para_pro += " fo:border=\"";
			if (atts.getValue("��:���") != null)
				_current_para_pro += atts.getValue("��:���") + " ";
			_current_para_pro += atts.getValue("uof:����");   //ODF�б߿��Ƿ����ֻ�����ͣ�
			if (atts.getValue("��:��ɫ") != null)
				_current_para_pro += " " + atts.getValue("��:��ɫ");
			_current_para_pro += "\"";
		}
		else if (qName.equals("��:���")) {
			//δ�ҵ���Ӧ
		}
		else if (qName.equals("��:����")) {
			if (atts.getValue("��:ʽ������") != null) {
				/* _current_text_pro += */    //���õ�Ӧ����һ����ʽ������˾�ʽ������Ҫ�浽style�У�ҲҪ�����浽һ��Map��
			}
			_is_in_text = true;
		}
		else if (qName.equals("��:��ͼ��")) {
			_style_num ++;
			_plotstyle = "style:name=\"" + _style_prefix + _style_num + "\"";
			_plot_area_begin += " chart:style-name=\"" + _style_prefix + _style_num + "\"";;
			_current_chart_pro += _plot_chart_pro;
			_plot_chart_pro = "";
		}
		else if (qName.equals("��:������")) {
			_style_num ++;
			_x_axis = "<chart:axis chart:dimension=\"x\" chart:name=\"primary-x\" chart:style-name=\"" + _style_prefix + _style_num + "\">";
			
			if (atts.getValue("��:���̶�����") != null) {
				if (atts.getValue("��:���̶�����").equals("inside"))
					_current_chart_pro += " chart:tick-marks-major-inner=\"true\"";
				else
					_current_chart_pro += " chart:tick-marks-major-outer=\"true\"";
			}
			if (atts.getValue("��:�ο̶�����") != null) {
				if (atts.getValue("��:�ο̶�����").equals("inside"))
					_current_chart_pro += " chart:tick-marks-minor-inner=\"true\"";
				else
					_current_chart_pro += " chart:tick-marks-minor-outer=\"true\"";
			}
		}
		else if (qName.equals("��:��ֵ��")) {
			_style_num ++;
			_y_axis = "<chart:axis chart:dimension=\"y\"  chart:name=\"primary-y\" chart:style-name=\"" + _style_prefix + _style_num + "\">";
			
			if (atts.getValue("��:���̶�����") != null) {
				if (atts.getValue("��:���̶�����").equals("inside"))
					_current_chart_pro += " chart:tick-marks-major-inner=\"true\"";
				else
					_current_chart_pro += " chart:tick-marks-major-outer=\"true\"";
			}
			if (atts.getValue("��:�ο̶�����") != null) {
				if (atts.getValue("��:�ο̶�����").equals("inside"))
					_current_chart_pro += " chart:tick-marks-minor-inner=\"true\"";
				else
					_current_chart_pro += " chart:tick-marks-minor-outer=\"true\"";
			}
		}
		else if (qName.equals("��:����")) {
			value = atts.getValue("��:����");
			if (value.equals("single"))
				_current_graphic_pro += " draw:stroke=\"solid\"";
			else if (value.equals("dash") /*|| value.equals("")*/)   //To do :������һЩֵҲ���Զ�Ӧ��dash
				_current_graphic_pro += " draw:stroke=\"dash\"";
			else
				_current_graphic_pro += " draw:stroke=\"none\"";
			
			if ((value = atts.getValue("��:��ɫ")) != null)
				_current_graphic_pro += " svg:stroke-color=\"" + value + "\"";
			if ((value = atts.getValue("��:���")) != null)
				_current_graphic_pro += " svg:stroke-width=\"" + value + "\"";
		}
		else if (qName.equals("��:��ֵ")) {
			if ((value = atts.getValue("��:���ӵ�Դ")) != null)
				_current_chart_pro += " chart:link-data-style-to-source=\"" + value+ "\"";
			if ((value = atts.getValue("��:��������")) != null) {
				//To do.�ϸ���
			}
		}
		else if (qName.equals("��:������")) {
			_style_num ++;
			
			if ((value = atts.getValue("��:����")) != null) {
				if (value.equals("single"))
					_current_graphic_pro += " draw:stroke=\"solid\"";
				else if (value.equals("dash") /*|| value.equals("")*/)   //To do :������һЩֵҲ���Զ�Ӧ��dash
					_current_graphic_pro += " draw:stroke=\"dash\"";
				else
					_current_graphic_pro += " draw:stroke=\"none\"";
			}
			
			if ((value = atts.getValue("��:��ɫ")) != null)
				_current_graphic_pro += " svg:stroke-color=\"" + value + "\"";
			if ((value = atts.getValue("��:���")) != null)
				_current_graphic_pro += " svg:stroke-width=\"" + value + "\"";
			if ((value = atts.getValue("��:λ��")) != null) {
				if (value.equals("category axis"))
					_x_grid = "<chart:grid chart:class=\"major\" chart:style-name=\"" + _style_prefix + _style_num + "\"/>";
				if (value.equals("value axis"))
					_y_grid = "<chart:grid chart:class=\"major\" chart:style-name=\"" + _style_prefix + _style_num + "\"/>";
			}
		}
		else if (qName.equals("��:��Сֵ") || qName.equals("��:���ֵ") || qName.equals("��:����λ")
				|| qName.equals("��:�ε�λ") || qName.equals("��:���ַ���") ||qName.equals("��:��ת�Ƕ�")
				|| qName.equals("��:ˮƽ���뷽ʽ") || qName.equals("��:��ֱ���뷽ʽ") || qName.equals("��:����")
				|| qName.equals("��:������ת�Ƕ�")) {
			_need_to_store_text = true;
		}
		else if (qName.equals("��:�Զ�����")) {
			if (atts.getValue("��:ֵ").equals("true"))
				_current_graphic_pro += " fo:wrap-option=\"wrap\"";
			else
				_current_graphic_pro += " fo:wrap-option=\"no-wrap\"";
		}
		else if (qName.equals("��:����")) {
			if (atts.getValue("��:ֵ").equals("true"))
				_current_chart_pro += " chart:axis-logarithmic=\"true\"";
			else
				_current_chart_pro += " chart:axis-logarithmic=\"false\"";
		}
		else if (qName.equals("��:��ʾ��־")) {
			if ((value = atts.getValue("��:ϵ����")) != null) {
				if (value.equals("1"))
					_current_chart_pro += " chart:data-label-text=\"true\"";
				else
					_current_chart_pro += " chart:data-label-text=\"false\"";
			}
			if (atts.getValue("��:��ֵ") != null && atts.getValue("��:��ֵ").equals("1"))
				_current_chart_pro += " chart:data-label-number=\"value\"";
			if (atts.getValue("��:�ٷ���") != null && atts.getValue("��:�ٷ���").equals("1"))
				_current_chart_pro += " chart:data-label-number=\"percentage\"";
			if ((value = atts.getValue("��:ͼ����־")) != null) {
				if (value.equals("1"))
					_current_chart_pro += " chart:data-label-symbol=\"true\"";
				else
					_current_chart_pro += " chart:data-label-symbol=\"false\"";
			}
		}
		else if (qName.equals("��:ͼ��")) {
			_style_num ++;
			_legend += "<chart:legend";
			if ((value = atts.getValue("��:λ��")) != null) {
				_legend += " chart:legend-position=\"";
				if (value.equals("left"))
					_legend += "start\"";
				else if (value.equals("right"))
					_legend += "end\"";
				else if (value.equals("top"))
					_legend += "top\"";
				else if (value.equals("bottom"))
					_legend += "bottom\"";
				/*corner���ܶ�Ӧ���ĸ�ֵ���޷�����
				else if (value.equals("corner"))*/
			}
			_legend +=  " chart:style-name=\"" + _style_prefix + _style_num + "\"/>";
		}
		else if (qName.equals("��:����ϵ��")) {
			_style_num ++;
			String serie = "<chart:series chart:style-name=\"" + _style_prefix + _style_num + "\">";
			String serie_id = atts.getValue("��:ϵ��");
			_data_series_map.put(serie_id,serie);
		}
		else if (qName.equals("��:���ݵ�")) {   //"���"�����ò���
			_style_num ++;
			String data_point = "<chart:data-point chart:style-name=\"" + _style_prefix + _style_num + "\"/>";
			String serie_id = atts.getValue("��:ϵ��");
			_data_series_map.get(serie_id).concat(data_point);
		}
		else if (qName.equals("��:����Դ")) {
			if (atts.getValue("��:��������") != null) {
				String area = atts.getValue("��:��������");
				int i = area.indexOf("'", 2);
				area = area.substring(1, i) + "." + area.substring(i+2).replace(":", ":.");
				_plot_area_begin += " table:cell-range-address=\"" + area + "\"";
			}
			if ((value = atts.getValue("��:ϵ�в���")) != null) {
				_series_source = value;
				String series = "";
				if(value.equals("col")) 
					series += " chart:series-source=\"columns\"";
				else 
					series += " chart:series-source=\"rows\"";
				
				int i = _auto_style.indexOf(_plotstyle);
				i = _auto_style.indexOf("style:chart-properties", i);
				i += 22;
				_auto_style = _auto_style.substring(0, i) + series + _auto_style.substring(i);
			}
		}
		else if (qName.equals("��:ϵ��")) {
			if (_serie_proc_tag)
				return;
			String labels = "";
			boolean xLabel = false, yLabel = false;
			if ((value = atts.getValue("��:ϵ����")) != null && value.length() > 0) {
				if(_series_source.equals("col"))
					yLabel = true;
				else
					xLabel = true;
			}
			if ((value = atts.getValue("��:������")) != null && value.length() > 0) {
				if(_series_source.equals("col"))
					xLabel = true;
				else
					yLabel = true;
			}
			if(xLabel) {
				if(yLabel)
					labels = "both";
				else
					labels = "row";
			}
			else {
				if(yLabel)
					labels = "column";
				else
					labels = "none";
			}
			
			_plot_area_begin += " chart:data-source-has-labels=\"" + labels + "\""; 	
			_series_source = "";
			_serie_proc_tag = true;
		}
		else if (qName.equals("��:����")) {
			_style_num ++;
			
			String title = "<chart:title chart:style-name=\"" + _style_prefix + _style_num + "\">";
			if (atts.getValue("��:����") != null)
				title += "<text:p>" + atts.getValue("��:����") + "</text:p>";
			title += "</chart:title>";
			
			String pos = atts.getValue("��:λ��");
			if (pos.equals("chart"))
				_main_title = title;
			else if (pos.equals("category axis"))
				_x_title = title;
			else if (pos.equals("value axis"))
				_y_title = title;
		}
	}
	
	public static void processEnd(String qName){		
		if (_is_in_text) {
			Sent_Style.process_end(qName);
			if (qName.equals("��:����")) {
				_current_text_pro += Sent_Style.get_text_pro();      //ȡ��text-pro�������б�
				_is_in_text = false;
			}
		}
		else if (qName.equals("��:ͼ��")) {
			String data_series = "";
			Collection<String> dataSeries = _data_series_map.values();
			for (Iterator iterator = dataSeries.iterator(); iterator.hasNext(); ) {
				data_series += (String)iterator.next() + "</chart:series>";
			}
			
			if (_x_axis.length() == 0)
				_x_axis = "<chart:axis chart:dimension=\"x\" chart:name=\"primary-x\">";
			if (_y_axis.length() == 0)
				_y_axis = "<chart:axis chart:dimension=\"y\" chart:name=\"primary-y\">";
			
			_body += _main_title + _legend + _plot_area_begin + ">" 
			+ _x_axis + _x_title + _x_cates + _x_grid + "</chart:axis>" 
			+ _y_axis + _y_title + _y_cates + _y_grid + "</chart:axis>"
			+ data_series + "</chart:plot-area>" + "</chart:chart></office:chart></office:body>";
			
			_content = _content_begin 
			+ "<office:automatic-styles>" +_auto_style + "</office:automatic-styles>" 
			+ _body + "</office:document-content>";
			
			_auto_style = "";
			_body = "<office:body><office:chart>";
			_main_title = "";
			_legend = "";
			_plot_area_begin = "<chart:plot-area";
			_x_axis = "";
			_y_axis = "";
			_data_series_map = new HashMap<String,String>();
			_style_num = 0;
		}
		else if (qName.equals("��:ͼ����") || qName.equals("��:��ͼ��") || qName.equals("��:������")
				|| qName.equals("��:��ֵ��") || qName.equals("��:ͼ��") || qName.equals("��:����ϵ��")
				|| qName.equals("��:���ݵ�") || qName.equals("��:������") || qName.equals("��:����")) {
			addStyle();
		}
		else if (qName.equals("��:��Сֵ")) {
			_current_chart_pro += " chart:minimun=\"" + _text_node + "\"";
		}
		else if (qName.equals("��:���ֵ")) {
			_current_chart_pro += " chart:maximun=\"" + _text_node + "\"";
		}
		else if (qName.equals("��:����λ")) {
			_current_chart_pro += " chart:axis-interval-major=\"" + _text_node + "\"";
			_axis_interval_major = Double.valueOf(_text_node);
		}
		else if (qName.equals("��:�ε�λ")) {
			int divisor = (int)Math.floor(_axis_interval_major/Double.valueOf(_text_node));
			_current_chart_pro += " chart:axis-interval-minor-divisor=\"" + divisor + "\"";
		}
		else if (qName.equals("��:���ַ���")) {
			if (_text_node.equals("horizontal"))
				_current_chart_pro += " style:direction=\"ltr\"";
			else
				_current_chart_pro += " style:direction=\"ttb\"";;	
		}
		else if (qName.equals("��:��ת�Ƕ�") || qName.equals("��:������ת�Ƕ�")) {  //��ת�Ƕ���ͬ��������
			_current_text_pro += " style:rotation-angle=\"" + _text_node + "\"";
		}
		else if (qName.equals("��:ˮƽ���뷽ʽ")) {
			_current_para_pro += " fo:text-align=\"";
			if (_text_node.equals("left") || _text_node.equals("right")
					|| _text_node.equals("center"))
				_current_para_pro += _text_node;
			else if (_text_node.equals("justified"))
				_current_para_pro += "justify";
			else
				_current_para_pro += "auto";   //�������ȫ��Ϊauto
			_current_para_pro += "\"";
		}
		else if (qName.equals("��:��ֱ���뷽ʽ")) {
			_current_para_pro += " fo:vertical-align=\"";
			if (_text_node.equals("top") || _text_node.equals("bottom"))
				_current_para_pro += _text_node;
			else if (_text_node.equals("center"))
				_current_para_pro += "middle";
			else
				_current_para_pro += "auto";   //�������ȫ��Ϊauto
			_current_para_pro += "\"";
		}
		else if (qName.equals("��:����")) {   //To do:������ȡֵҪ��һ������
			_current_para_pro += " fo:text-indent=\"" + _text_node + "\"";;
		}
		else if (qName.equals("��:����Դ")) {
			_serie_proc_tag = false;
		}
		
		//ÿ��Ԫ�ؽ���ʱ��Ҫ���_text_node������_need_to_store_text
		_text_node = "";   
		_need_to_store_text = false;
	}
	
	public static void process_chars(String chs)
	throws SAXException  {
		if (_is_in_text)
			Sent_Style.process_chars(chs);
		
		if (_need_to_store_text) {
			_text_node += chs;			
		}		
	}
	
	public static String getChartFrame(){
		String svgX = measure_floatval(_svgX);
		String svgY = measure_floatval(_svgY);
		String width = measure_floatval(_width);
		String height = measure_floatval(_height);
		String chartFrame = "<draw:frame svg:width=\"" + width + "\" svg:height=\"" + height
		+ "\" svg:x=\"" + svgX + "\" svg:y=\"" + svgY + "\"><draw:object draw:notify-on-update-of-ranges=\""
		+ _cellRange + "\" xlink:href=\"./" + _ID + "\" xlink:type=\"simple\" xlink:show=\"embed\" xlink:actuate=\"onLoad\"/></draw:frame>";
		
		_svgX = 0;   
		_svgY = 0;
		_width = 0;   
		_height = 0;
		_cellRange = "";
		_ID = "";
		
		return chartFrame;
	}
	
	private static String measure_floatval(float val){
		return val * Common_Data.get_graphratio() + Common_Data.get_unit();
	}
	
	public static void set_ID(String ID) {
		_ID = ID;
	}
	
	public static String get_content() {
		String content = _content;
		_content = "";
		return content;
	}
	
	private static String getClass(String type, String subtype) {
		String chartClass = "";
		
		if (subtype.contains("3D")) {
			_3D = true;	
		}
		
		if (subtype.contains("marker")) {
			_symbol = true;	
		}
		
		if (subtype.contains("scatter_line")) {
			_lines = true;	
		}
		
		if (subtype.contains("100%")) {
			_percentage = true;	
		}
		else if (subtype.contains("stacked") || subtype.contains("stcked")) {
			_stacked = true;	
		}
		
		if (subtype.equals("pie_exploded")) {
			_pie_offset = true;	
		}
		
		if (type.equals("column")) {
			if (subtype.equals("column_stacked_3D") || subtype.equals("column_100%_stacked_3D")
					|| subtype.equals("column_clustered_3D") || subtype.equals("column_stacked")
					|| subtype.equals("column_100%_stacked") || subtype.equals("column_clustered")) {
				chartClass = "chart:bar";
			}		
			else if (subtype.equals("doughnut_standard"))
				chartClass = "chart:ring";
			else if (subtype.contains("area"))
				chartClass = "chart:area";
			else if (subtype.contains("scatter"))
				chartClass = "chart:scatter";
		}
		else if (type.equals("bar")) {
			_vertical = true;
			chartClass = "chart:bar";
		}
		else if (type.equals("pie")) {
			chartClass = "chart:circle";
		}
		else if (type.equals("line")) {
			chartClass = "chart:line";
		}
		//To do. chart:stock. EIOffice bug.
		
		_plot_chart_pro += _3D?" chart:three-dimensional=\"true\"":" chart:three-dimensional=\"false\"";
		_plot_chart_pro += _symbol?" chart:symbol-type=\"automatic\"":" chart:symbol-type=\"none\"";
		_plot_chart_pro += _lines?" chart:lines=\"true\"":" chart:lines=\"false\"";
		_plot_chart_pro += _percentage?" chart:percentage=\"true\"":"";
		_plot_chart_pro += _stacked?" chart:stacked=\"true\"":"";
		_plot_chart_pro += _pie_offset?" chart:pie-offset=\"10\"":"";
		_plot_chart_pro += _vertical?" chart:vertical=\"true\"":" chart:vertical=\"false\"";
		
		_3D = false;
		_vertical = false;
		_pie_offset = false;
		_stacked = false;
		_percentage = false;
		_symbol = false;
		_lines = false;
		
		return chartClass;
	}
	
}
