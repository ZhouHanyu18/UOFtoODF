package text;

import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;

/**
 * To deal with <text:list>, we store in a map
 * its style-name and list-level taking from
 * <字:自动编号信息> for every paragraph.
 *
 * @author xie
 *
 */
public class List_Para {
	//counter of paragrpah
	private static int _counter_of_para = 0;
	//store applied list-name and list-level
	//for corresponding paragraph.
	private static Map<Integer,List_Struct>
		ls_map = new TreeMap<Integer,List_Struct>();


	//initialize
	public static void init(){
		_counter_of_para = 0;
		ls_map.clear();
	}

	//Get start element name of <text:list> for corresponding
	//paragraph specified by the counter.
	public static String get_list_ele(int counter){
		String startEle = "";
		List_Struct preLs = ls_map.get(counter-1);
		List_Struct curLs = ls_map.get(counter);

		String curName = (curLs==null) ? "" : curLs.get_list_name();
		String preName = (preLs==null) ? "" : preLs.get_list_name();
		int curLevel = (curLs==null) ? -1 : curLs.get_list_level();
		int preLevel = (preLs==null) ? -1 : preLs.get_list_level();
		int repeat = curLevel - preLevel;

		if(curLs!=null && !curName.equals("")){
			if(preLs==null || !preName.equals(curName)){
				startEle = "<text:list text:style-name=\""
							+ curName + "\"><text:list-item>";
				while(--repeat > 0){
					startEle += "<text:list><text:list-item>";
				}
			}
			else if(preLevel == curLevel){
				startEle = "<text:list-item>";
			}
			else if(preLevel < curLevel){
				while(repeat-- > 0){
					startEle += "<text:list><text:list-item>";
				}
			}
			else if(preLevel > curLevel){
				startEle = "<text:list-item>";
			}
		}

		return startEle;
	}

	//Get end element name of <text:list> for corresponding
	//paragraph specified by the counter.
	public static String get_list_end(int counter){
		String endEle = "";
		List_Struct nextLs = ls_map.get(counter+1);
		List_Struct curLs = ls_map.get(counter);

		String curName = (curLs==null) ? "" : curLs.get_list_name();
		String nextName = (nextLs==null) ? "" : nextLs.get_list_name();
		int curLevel = (curLs==null) ? -1 : curLs.get_list_level();
		int nextLevel = (nextLs==null) ? -1 : nextLs.get_list_level();
		int repeat = curLevel - nextLevel;

		if(curLs!=null && !curName.equals("")){
			if(nextLs==null || !nextName.equals(curName)){
				while(repeat-- > 0){
					endEle += "</text:list-item></text:list>";
				}
			}
			else if(nextLevel == curLevel){
				endEle = "</text:list-item>";
			}
			else if(nextLevel > curLevel){
				endEle = "";
			}
			else if(nextLevel < curLevel){
				while(repeat-- > 0){
					endEle += "</text:list-item></text:list>";
				}
				endEle += "</text:list-item>";
			}
		}

		return endEle;
	}

	//Add infomation of <text:list> for every paragraph
	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("字:段落")){
			_counter_of_para ++;
		}

		else if(qName.equals("字:自动编号信息")){
			List_Struct ls = new List_Struct();

			//Style name of <text:list>
			attVal = atts.getValue("字:编号引用");
			attVal = (attVal==null) ? "" : attVal;
			ls.set_list_name(attVal);

			//text:list-level
			attVal = atts.getValue("字:编号级别");
			attVal = (attVal==null) ? "0" : attVal;
			int level = Integer.parseInt(attVal);
			ls.set_list_level(level);

			ls_map.put(_counter_of_para, ls);
		}
	}
}
