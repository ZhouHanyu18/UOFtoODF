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
		
		if(qName.equals("uof:文字处理")){
			Common_Data.set_file_type("text");
		}
		else if(qName.equals("uof:电子表格")){
			Common_Data.set_file_type("spreadsheet");
		}
		else if(qName.equals("uof:演示文稿")){
			Common_Data.set_file_type("presentation");
		}
		else if(qName.equals("表:工作表")){
			HyperLink.add_table_name(atts.getValue("表:名称"));
		}
		
		if(Common_Data.get_file_type().equals("presentation")){
			if(qName.equals("演:母版")){
				String type = atts.getValue("演:类型");
				if(type != null && type.equals("slide")){
					Presentation_Style.set_master_name(atts.getValue("演:标识符"));
				}
			}
			else if(qName.equals("演:文本式样集")){
				_present_style_tag = true;
			}
			else if(_present_style_tag){
				if(qName.equals("uof:段落式样")){
					_style_name = atts.getValue("字:标识符");
				}
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException  {	
		String chs = new String(ch, start, length).trim();
		
		if(chs.equals(""))		return ;
		
		if(_qName.endsWith("度量单位")){		
			Common_Data.set_unit(chs);
		}
		else if(_present_style_tag && _qName.equals("字:大纲级别")){
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
