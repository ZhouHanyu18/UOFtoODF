package convertor;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import presentation.Presentation_Style;
import temp_structs.Common_Data;
import text.HyperLink;


public class Pre_ConvHandler extends DefaultHandler {
	private String _qName = "";
	private boolean _present_style_tag = false;
	private String _style_name = "";


	public void startElement(String namespaceURI, String localName, 
			String qName, Attributes atts)throws SAXException {		
		_qName = qName;
		
		if(qName.equals("uof:���ִ���")){
			Common_Data.set_file_type("text");
		}
		else if(qName.equals("uof:���ӱ��")){
			Common_Data.set_file_type("spreadsheet");
		}
		else if(qName.equals("uof:��ʾ�ĸ�")){
			Common_Data.set_file_type("presentation");
		}
		else if(qName.equals("��:������")){
			HyperLink.add_table_name(atts.getValue("��:����"));
		}
		
		if(Common_Data.get_file_type().equals("presentation")){
			if(qName.equals("��:ĸ��")){
				String type = atts.getValue("��:����");
				if(type != null && type.equals("slide")){
					Presentation_Style.set_master_name(atts.getValue("��:��ʶ��"));
				}
			}
			else if(qName.equals("��:�ı�ʽ����")){
				_present_style_tag = true;
			}
			else if(_present_style_tag){
				if(qName.equals("uof:����ʽ��")){
					_style_name = atts.getValue("��:��ʶ��");
				}
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  {	
		String chs = new String(ch, start, length).trim();
		
		if(chs.equals(""))		return ;
		
		if(_qName.endsWith("������λ")){		
			Common_Data.set_unit(chs);
		}
		else if(_present_style_tag && _qName.equals("��:��ټ���")){
			Presentation_Style.put_ps_level(_style_name,Integer.parseInt(chs));
		}
	}
	
	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
		
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
