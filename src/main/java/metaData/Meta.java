package metaData;

import org.xml.sax.Attributes;
import convertor.*;

public class Meta{
	public static String _chs = "";
	public static String value="";
	public static String  documentStatistics="<meta:document-statistics";
	
	public static void process_start(String qName,Attributes atts){

		String result = "";
		if(qName.equals("uof:Ԫ����")){
			result +="<office:meta>";
		}
		if (qName.equals("meta:generator")){
			result +="<uof:����Ӧ�ó���>";
		}
		if(qName.equals("uof:����")){
			result +="<dc:title>";
		}
		if(qName.equals("uof:ժҪ")){
			result +="<dc:description>";
		}
		if(qName.equals("uof:����")){
			result +="<dc:subject>";
		}
		if(qName.equals("uof:�ؼ���")){
			result +="<meta:keyword>";
		}
		if(qName.equals("uof:������")){
			result +="<meta:initial-creator>";
		}
		if(qName.equals("uof:����")){
			result +="<dc:creator>";
		}
		if(qName.equals("uof:�������")){
			result +="<meta:printed-by>";
		}
		if(qName.equals("uof:��������")){
			result +="<meta:creation-date>";
		}
		if(qName.equals("uof:�༭����")){
			result +="<meta:editing-cycles>";
		}
		if(qName.equals("uof:�༭ʱ��")){
			result +="<meta:editing-duration>";
		}
		if(qName.equals("uof:�ĵ�ģ��")){//meta:template����һЩ���Բ�֪��������Ӧ
			result +="<meta:template>";
		}
		
		//"<meta:document-statistics"��������
		if(qName.equals("uof:�û��Զ���Ԫ����")){
			result +="<meta:user-defined";
			if((value=atts.getValue("uof:����"))!=null){
				result+=" meta:name=\"" + value +"\"";
			}
			if((value=atts.getValue("uof:����"))!=null){
				result+=" meta:type=\"" + value +"\"";
			}
			result+=">";
			
		}
		if(qName.equals("uof:ҳ��")){
			documentStatistics +=" meta:page-coun=\"";
		}
		if(qName.equals("uof:������")){
			//documentStatistics +=" meta:paragraph-count=\"";
			documentStatistics +=" meta:paragraph-count=\"";
		}
		if(qName.equals("uof:����")){
			documentStatistics +=" meta:word-count=\"";
		}
		if(qName.equals("uof:Ӣ���ַ���")){
			documentStatistics +=" meta:character-count=\"";
		}
		if(qName.equals("uof:����")){
			documentStatistics +=" meta:row-count=\"";
		}
		if(qName.equals("uof:������")){
			documentStatistics +=" meta:object-count=\"";
		}
	
		Results_Writer.processMetaResult(result);
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	public static void process_end(String qName){
		String result = "";
		if(qName.equals("uof:Ԫ����")){
			Results_Writer.processMetaResult(documentStatistics + "/>");
			result += _chs +"</office:meta>";
		}
		if (qName .equals("meta:generator")){ 
			result += _chs +"</uof:����Ӧ�ó���>";
		}
		if(qName.equals("uof:����")){
			result += _chs +"</dc:title>";
		}
		if(qName.equals("uof:ժҪ")){
			result += _chs +"</dc:description>";
		}
		if(qName.equals("uof:����")){
			result += _chs +"</dc:subject>";
		}
		if(qName.equals("uof:�ؼ���")){
			result += _chs +"</meta:keyword>";
		}
		if(qName.equals("uof:������")){
			result += _chs +"</meta:initial-creator>";
		}
		if(qName.equals("uof:����")){
			result +="</dc:creator>";
		}
		if(qName.equals("uof:�������")){
			result += _chs +"</meta:printed-by>";
		}
		if(qName.equals("uof:��������")){
			result += _chs +"</meta:creation-date>";
		}
		if(qName.equals("uof:�༭����")){
			result += _chs +"</meta:editing-cycles>";
		}
		if(qName.equals("uof:�༭ʱ��")){
			result += _chs +"</meta:editing-duration>";
		}
		if(qName.equals("uof:�ĵ�ģ��")){//meta:template����һЩ���Բ�֪��������Ӧ
			result +="</meta:template>";
		}
		
		if(qName.equals("uof:ҳ��")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:������")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName=="uof:������"){
			documentStatistics  +=( _chs +"\"");
		}
		if(qName.equals("uof:����")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:Ӣ���ַ���")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:����")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:�û��Զ���Ԫ����")){
			result +=(_chs+"</meta:user-defined>");
		}
		_chs = "";
		Results_Writer.processMetaResult(result);
	}
	
}
