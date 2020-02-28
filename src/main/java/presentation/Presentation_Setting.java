package presentation;

import org.xml.sax.Attributes;

/**
 * Process the conversion from 
 * <��:��ӳ����> to <presentation:settings> 
 * @author xie
 *
 */
public class Presentation_Setting {
	//
	private static String _chs = "";
	//the result
	private static String _rst = ""; 
	
	
	public static String get_result(){
		String rst = "";
		
		rst = "<presentation:settings" + _rst + "/>";
		_chs = "";
		_rst = "";
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){	
		if(qName.equals("��:�õ�Ƭ����")){
			String startPage = "";
			
			if(_chs.contains(" ")){
				int ind = _chs.indexOf(" ");
				startPage = _chs.substring(0,ind);
			}else {
				startPage = _chs;
			}
			_rst += add_att("presentation:start-page",startPage);
		}
		else if(qName.equals("��:������ת")){
			_rst += add_att("presentation:transition-on-click",convVal(_chs));
		}
		else if(qName.equals("��:��ӳ����")){
			_rst += add_att("presentation:animations",convVal(_chs));
		}
		else if(qName.equals("��:ȫ����ӳ")){
			_rst += add_att("presentation:full-screen",_chs);
		}
		else if(qName.equals("��:ѭ����ӳ")){
			_rst += add_att("presentation:endless",_chs);
		}
		else if(qName.equals("��:��ӳ���")){
			_rst += add_att("presentation:pause",Draw_Page_Style.conv_time(_chs));
		}
		else if(qName.equals("��:�ֶ���ʽ")){
			_rst += add_att("presentation:force-manual",_chs);
		}
		else if(qName.equals("��:��������")){
			_rst += add_att("presentation:start-with-navigator",_chs);
		}
		else if(qName.equals("��:ǰ����ʾ")){
			_rst += add_att("presentation:stay-on-top",_chs);
		}
	}
	
	private static String add_att(String attName,String val){
		return " " + attName + "=\"" + val + "\"";
	}
	
	private static String convVal(String val){
		String str = "";
		
		if(val.equals("true")){
			str = "enabled";
		}
		else{
			str = "disabled";
		}
		
		return str;
	}
}
