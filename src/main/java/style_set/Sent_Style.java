package style_set;

import org.xml.sax.Attributes;
import temp_structs.Common_Data;
import temp_structs.Stored_Data;

/**
 * Create a <style:style> which style:family 
 * is "text" for every <uof:��ʽ��> in <uof:ʽ����>
 * or <��:������> in body.
 * @author xie
 *
 */
public class Sent_Style {
	private static String _chs = "";
	//attributes of <style:style>
	private static String _ele_atts = "";
	//attributes of <style:text-properties>
	private static String _text_pro = "";
	//font size (default value is 12)
	private static float _fontsize = 12;
	//Type of current text-style
	private static boolean _is_default = false;

	
	public static float get_fontsize(){
		return _fontsize;
	}
	
	private static void clear(){
		_chs = "";
		_ele_atts = "";
		_text_pro = "";
		_is_default = false;
	}
	
	//Return attributes of <style:text-properties>
	public static String get_text_pro(){
		String str = _text_pro;
		clear();
		return str;
	}
	
	//Store the result
	private static void commit_result(){
		String rst = "";

		rst = "<style:style style:family=\"text\"" + _ele_atts + ">";
		rst += "<style:text-properties " + _text_pro + "/>";
		rst += "</style:style>";
		
		if(_is_default){
			Stored_Data.addStylesInStylesXml(rst);
		}else {
			Stored_Data.addAutoStylesInContentXml(rst);
		}
		
		clear();
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if (qName.equals("uof:��ʽ��")){
			attVal = atts.getValue("��:����");
			attVal = (attVal==null) ? "" : attVal;
			//The default style goes to styles.xml
			if (attVal.equals("default") || attVal.equals("custom")){
				_is_default = true;
			}
						
			if ((attVal = atts.getValue("��:��ʶ��")) != null){
				_ele_atts += " style:name=\"" + attVal + "\"";
			}
			if ((attVal = atts.getValue("��:��ʽ������")) != null){
				_ele_atts += " style:parent-style-name=\"" + attVal + "\"";
			}
			if((attVal = atts.getValue("��:����")) != null){
				_ele_atts += " style:display-name=\"" + attVal + "\"";				
			}						
		}
		
		else if (qName.equals("��:����")){
			if((attVal = atts.getValue("��:������������")) != null){
				_text_pro += " style:font-name=\"" + attVal + "\"";
			}
			if ((attVal = atts.getValue("��:������������")) != null){
				_text_pro += " style:font-name-asian=\"" + attVal + "\"";
			}
			if((attVal = atts.getValue("��:������������")) != null){
				_text_pro += " style:font-name-complex=\"" + attVal + "\"";
			}
			if ((attVal = atts.getValue("��:�ֺ�")) != null){
				_fontsize = Float.parseFloat(attVal);
				_text_pro += " fo:font-size=\"" + attVal + "pt" + "\""
						   + " style:font-size-asian=\"" + attVal + "pt" + "\""
						   + " style:font-size-complex=\"" +attVal + "pt" + "\""
						   + " style:language-asian=\"zh\" style:country-asian=\"CN\"";
			}
			if ((attVal = atts.getValue("��:��ɫ")) != null){
				_text_pro += " fo:color=\"" + attVal + "\"";
			}
		}
		
		else if (qName.equals("��:����")){
			attVal = atts.getValue("��:ֵ");
			
			if (attVal != null && attVal.equals("true")){
				_text_pro += " fo:font-weight=\"bold\"";
				_text_pro += " style:font-weight-asian=\"bold\"";
				_text_pro += " style:font-weight-complex=\"bold\"";
			}
		}
		else if (qName.equals("��:б��")){
			attVal = atts.getValue("��:ֵ");
			
			if (attVal != null && attVal.equals("true")){
				_text_pro += " fo:font-style=\"italic\"";
				_text_pro += " style:font-style-asian=\"italic\"";
				_text_pro += " style:font-style-complex=\"italic\"";
			}
		}

		//uof: none,single,double
		else if (qName.equals("��:ɾ����")){
			if ((attVal = atts.getValue("��:����")) != null){
				_text_pro += " style:text-line-through-type=\"" + attVal + "\"";
			}
		}
		
		else if (qName.equals("��:�»���")){
			attVal = atts.getValue("��:����");
			if(attVal != null && !attVal.equals("none")){
				_text_pro += get_under_line(attVal);
			}

			if ((attVal = atts.getValue("��:��ɫ")) != null){
				attVal = (attVal.equals("auto")) ? "font-color" : attVal;
				_text_pro += " style:text-underline-color=\""+ attVal + "\"";
			}

			if ((attVal = atts.getValue("��:���»���")) != null){
				if (attVal.equals("true")){
					_text_pro += " style:text-underline-mode=\"skip-white-space\"";
				}
				else if (attVal.equals("false")){
					_text_pro += " style:text-underline-mode=\"continuous\"";
				}
			}
		}
		
		else if (qName.equals("��:���غ�")){
			if ((attVal = atts.getValue("��:����")) != null){
				if (attVal.equals("none")){
					_text_pro += " style:text-emphasize=\"none\"";
				}
				else if (attVal.equals("dot")){
					_text_pro += " style:text-emphasize=\"dot below\"";
				}
			}
		}
		
		else if (qName.equals("��:��������")){
			if ((attVal = atts.getValue("��:ֵ"))!= null){
				if (attVal.equals("true")){
					_text_pro += " text:display=\"true\"";
				}
				else if (attVal.equals("false")){
					_text_pro += " text:display=\"none\"";
				}
			}
		}
		
		else if (qName.equals("��:����")){
			if ((attVal = atts.getValue("��:����")) != null){
				if (attVal.equals("none")){
					_text_pro += " style:font-relief=\"none\"";
				}
				else if (attVal.equals("emboss")){
					_text_pro += " style:font-relief=\"embossed\"";
				}
				else if (attVal.equals("engrave")){
					_text_pro += " style:font-relief=\"engraved\"";
				}
			}
		}
		
		else if (qName.equals("��:��Ӱ")){
			if ((attVal = atts.getValue("��:ֵ")) != null){
				if (attVal.equals("true")){
					_text_pro += " fo:text-shadow=\"1pt 1pt\"";
				}
				else if (attVal.equals("false")){
					_text_pro += " fo:text-shadow=\"none\"";
				}
			}
		}
		
		else if (qName.equals("��:���±�")){
			if ((attVal = atts.getValue("��:ֵ")) != null){
				if (attVal.equals("sup")){
					_text_pro += " style:text-position=\"super 58%\"";
				}
				else if(attVal.equals("sub")){
					_text_pro += " style:text-position=\"sub 58%\"";
				}
			}
		}
		
		else if (qName.equals("��:����")){
			if ((attVal = atts.getValue("��:ֵ")) != null){
				_text_pro += " style:text-outline=\"" + attVal + "\"";
			}
		}
		
		else if (qName.equals("��:��Ŀ����")){
			if ((attVal = atts.getValue("��:����")) !=null){
				if(attVal.equals("small-caps")){
					_text_pro += " fo:font-variant=\"small-caps\"";
				}
				else{			
					if (attVal.equals("lowecase")){
						attVal = "lowercase";
					}
					else if(attVal.equals("uppercase")){
						attVal = "uppercase";
					}
					else if(attVal.equals("capital")){
						attVal = "capitalize";
					}
					_text_pro += " style:text-transform=\"" + attVal + "\"";
				}
			}
		}
		else if (qName.equals("��:�����ּ��")){
			_text_pro += " style:text-kerning=\"true\"";
		}
		
	}
	
	public static void process_chars(String chs){
		_chs = chs;	
	}
	
	public static void process_end(String qName){
		if (qName.equals("uof:��ʽ��")){
			commit_result();
		}
		
		else if (qName.equals("ͼ:��ɫ") && !_chs.equals("Auto")){
			_text_pro += " fo:background-color=\"" + _chs + "\"";
		}
		
		else if (qName.equals("��:λ��")){

		}
		
		else if (qName.equals("��:����")){
			_text_pro += " style:text-scale=\"" + _chs + "\"";
		}
		
		else if (qName.equals("��:�ַ����")){
			_text_pro += " fo:letter-spacing=\"" + _chs + Common_Data.get_unit() + "\"";
		}
		
		_chs = "";

	}
	
	private static String get_under_line(String val){
		String line = "";
		
		if (val.equals("single"))
			line = " style:text-underline-type=\"single\"";
		else if (val.equals("double"))
			line = " style:text-underline-type=\"double\"";
		else if (val.equals("thick"))
			line = " style:text-underline-style=\"solid\"";
		else if (val.equals("dotted"))
			line = " style:text-underline-style=\"dotted\"";
		else if (val.equals("dotted-heavy"))
			line = " style:text-underline-style=\"dotted\" style:text-underline-width=\"bold\"";
		else if (val.equals("dash"))
			line = " style:text-underline-style=\"dash\"";
		else if (val.equals("dash-heavy"))
			line = " style:text-underline-style=\"dash\" style:text-underline-width=\"bold\"";
		else if (val.equals("dash-long"))
			line = " style:text-underline-style=\"long-dash\"";
		else if (val.equals("dash-long-heavy"))
			line = " style:text-underline-style=\"long-dash\" style:text-underline-width=\"bold\"";
		else if (val.equals("dot-dash"))
			line = " style:text-underline-style=\"dot-dash\"";
		else if (val.equals("dot-dash-heavy"))
			line = " style:text-underline-style=\"dot-dash\" style:text-underline-width=\"bold\"";
		else if (val.equals("wave"))
			line = " style:text-underline-style=\"wave\"";
		else if (val.equals("wave-heavy"))
			line = " style:text-underline-style=\"wave\" style:text-underline-width=\"bold\"";
		else if (val.equals("wavy-double"))
			line = " style:text-underline-type=\"double\" style:text-underline-style=\"wave\"";
		else
			line = " style:text-underline-style=\"solid\"";
		
		return line;
	}
}
