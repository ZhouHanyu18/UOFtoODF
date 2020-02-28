package text;

import java.util.*;
import org.xml.sax.Attributes;

import temp_structs.Common_Data;

/**
 * 
 * @author xie
 *
 */
public class HyperLink {
	//a list of table names for <spreadsheet>
	private static ArrayList<String>
		_tableID_list = new ArrayList<String>();
	//store the mapping of hyperlink's ID and
	//attributes of <text:a>
	private static Map<String,String>
		_hyper_link_map = new HashMap<String,String>();
	
	
	//initialize
	public static void init(){
		_tableID_list.clear();
		_hyper_link_map.clear();
	}
	
	public static void add_table_name(String name){
		_tableID_list.add(name);
	}
	
	public static String get_link_atts(String id){
		return _hyper_link_map.get(id);
	}
	
	public static void process(String qName,Attributes atts){
		String oneLink = "";
		String attVal = "";
		
		if(qName.equals("uof:超级链接")){
			String id = "";
			
			if((attVal=atts.getValue("uof:标识符"))!=null){
				oneLink += " office:name=\"" + attVal + "\"";
				id = attVal;
			}
			
			if((attVal=atts.getValue("uof:目标"))!=null){
				oneLink += " xlink:href=\"" + link_href(attVal) + "\"";
			}
			
			if((attVal=atts.getValue("uof:式样引用"))!=null){
				oneLink += " text:style-name=\"" + attVal + "\"";
			}
			
			if((attVal=atts.getValue("uof:已访问式样引用"))!=null){
				oneLink += " text:visited-style-name=\"" + attVal + "\"";
			}
			
			if((attVal=atts.getValue("uof:链源"))!=null){
				id = attVal;
			}
			_hyper_link_map.put(id,oneLink);
		}
	}
	
	private static String link_href(String rawHref){
		String href = "";
		boolean isTbAddr = false;
		
		if(Common_Data.get_file_type().equals("spreadsheet")){
			for(int i=0; i<_tableID_list.size()&&!isTbAddr; i++){
				isTbAddr = rawHref.startsWith(_tableID_list.get(i) + "!");
			}
		}
		
		if(isTbAddr){
			href = "#" + rawHref.replace("!",".");
		}
		else if(!(rawHref.startsWith("http:") || rawHref.startsWith("ftp:") 
			|| rawHref.startsWith("telnet:") || rawHref.startsWith("mailto:")
			|| rawHref.startsWith("news:"))){
			
			if(rawHref.length() > 3){
				char c1 = rawHref.charAt(1);
				char c2 = rawHref.charAt(2);
				if(c1==':' && c2=='\\'){
					href = "file:///" + rawHref.replace("\\","/");
				}				
			}
			
			if(href.equals("")){
				href += "../" + rawHref;
			}
		}
		
		href = (href.equals("")) ? rawHref : href;
		
		return href;
	}
}
