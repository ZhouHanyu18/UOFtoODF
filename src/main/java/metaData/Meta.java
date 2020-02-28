package metaData;

import org.xml.sax.Attributes;
import convertor.*;

public class Meta{
	public static String _chs = "";
	public static String value="";
	public static String  documentStatistics="<meta:document-statistics";

	public static void process_start(String qName,Attributes atts){

		String result = "";
		if(qName.equals("uof:元数据")){
			result +="<office:meta>";
		}
		if (qName.equals("meta:generator")){
			result +="<uof:创建应用程序>";
		}
		if(qName.equals("uof:标题")){
			result +="<dc:title>";
		}
		if(qName.equals("uof:摘要")){
			result +="<dc:description>";
		}
		if(qName.equals("uof:主题")){
			result +="<dc:subject>";
		}
		if(qName.equals("uof:关键字")){
			result +="<meta:keyword>";
		}
		if(qName.equals("uof:创建者")){
			result +="<meta:initial-creator>";
		}
		if(qName.equals("uof:作者")){
			result +="<dc:creator>";
		}
		if(qName.equals("uof:最后作者")){
			result +="<meta:printed-by>";
		}
		if(qName.equals("uof:创建日期")){
			result +="<meta:creation-date>";
		}
		if(qName.equals("uof:编辑次数")){
			result +="<meta:editing-cycles>";
		}
		if(qName.equals("uof:编辑时间")){
			result +="<meta:editing-duration>";
		}
		if(qName.equals("uof:文档模版")){//meta:template还有一些属性不知道怎样对应
			result +="<meta:template>";
		}

		//"<meta:document-statistics"怎样处理
		if(qName.equals("uof:用户自定义元数据")){
			result +="<meta:user-defined";
			if((value=atts.getValue("uof:名称"))!=null){
				result+=" meta:name=\"" + value +"\"";
			}
			if((value=atts.getValue("uof:类型"))!=null){
				result+=" meta:type=\"" + value +"\"";
			}
			result+=">";

		}
		if(qName.equals("uof:页数")){
			documentStatistics +=" meta:page-coun=\"";
		}
		if(qName.equals("uof:段落数")){
			//documentStatistics +=" meta:paragraph-count=\"";
			documentStatistics +=" meta:paragraph-count=\"";
		}
		if(qName.equals("uof:字数")){
			documentStatistics +=" meta:word-count=\"";
		}
		if(qName.equals("uof:英文字符数")){
			documentStatistics +=" meta:character-count=\"";
		}
		if(qName.equals("uof:行数")){
			documentStatistics +=" meta:row-count=\"";
		}
		if(qName.equals("uof:对象数")){
			documentStatistics +=" meta:object-count=\"";
		}

		Results_Writer.processMetaResult(result);
	}

	public static void process_chars(String chs){
		_chs = chs;
	}
	public static void process_end(String qName){
		String result = "";
		if(qName.equals("uof:元数据")){
			Results_Writer.processMetaResult(documentStatistics + "/>");
			result += _chs +"</office:meta>";
		}
		if (qName .equals("meta:generator")){
			result += _chs +"</uof:创建应用程序>";
		}
		if(qName.equals("uof:标题")){
			result += _chs +"</dc:title>";
		}
		if(qName.equals("uof:摘要")){
			result += _chs +"</dc:description>";
		}
		if(qName.equals("uof:主题")){
			result += _chs +"</dc:subject>";
		}
		if(qName.equals("uof:关键字")){
			result += _chs +"</meta:keyword>";
		}
		if(qName.equals("uof:创建者")){
			result += _chs +"</meta:initial-creator>";
		}
		if(qName.equals("uof:作者")){
			result +="</dc:creator>";
		}
		if(qName.equals("uof:最后作者")){
			result += _chs +"</meta:printed-by>";
		}
		if(qName.equals("uof:创建日期")){
			result += _chs +"</meta:creation-date>";
		}
		if(qName.equals("uof:编辑次数")){
			result += _chs +"</meta:editing-cycles>";
		}
		if(qName.equals("uof:编辑时间")){
			result += _chs +"</meta:editing-duration>";
		}
		if(qName.equals("uof:文档模版")){//meta:template还有一些属性不知道怎样对应
			result +="</meta:template>";
		}

		if(qName.equals("uof:页数")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:段落数")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName=="uof:对象数"){
			documentStatistics  +=( _chs +"\"");
		}
		if(qName.equals("uof:字数")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:英文字符数")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:行数")){
			documentStatistics +=( _chs +"\"");
		}
		if(qName.equals("uof:用户自定义元数据")){
			result +=(_chs+"</meta:user-defined>");
		}
		_chs = "";
		Results_Writer.processMetaResult(result);
	}

}
