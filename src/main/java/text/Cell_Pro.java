package text;

import org.xml.sax.Attributes;

import java.util.*;

import style_set.Common_Pro;

//处理<字:单元格属性类型>
//其子元素：宽度 单元格边距 边框 填充 垂直对齐方式 跨行 跨列 自动换行 适应文字
//其中，宽度 跨行 跨列 自动换行 没有对应
public class Cell_Pro extends Common_Pro{
	private static String _chs = "";	
	//
	private static String _cell_pro = ""; 
	//table:number-columns-spanned
	private static String _cols_spanned = "";	
	//table:number-rows-spanned
	private static String _rows_spanned = "";
	//
	private static int _cell_style_id = 0;
	//This counter will be increased by 1
	//when it sees a new <字:单元格属性>
	private static int _cell_counter = 0;
	//Each <字:单元格属性>-ele associates with a style name
	//of table-cell <style:style>.
	private static Map<Integer,String > 
		_style_name_map = new HashMap<Integer,String>();
	//
	private static ArrayList<Cell_Pro_Struct> 
		_cell_pro_list = new ArrayList<Cell_Pro_Struct>();
	
	//initialize
	public static void init(){
		_cell_counter = 0;
		_cell_style_id = 0;
		_style_name_map.clear();
		_cell_pro_list.clear();
	}
	
	private static void clear(){
		_chs = "";
		_cols_spanned = "";
		_rows_spanned = "";
		_cell_pro = "";
	}
	
	//return @style:name/@table:number-columns-spanned/
	//@table:number-rows-spanned attributes if it has	
	public static String get_atts(int proNum){
		String name = _style_name_map.get(proNum);
		
		if(name == null){
			name = "";
		}
		return name;
	}
	
	//If there is a Cell_Pro_Struct of the list has
	//a same content with cellPro, then return its
	//style name, else return "".
	private static String in_list(Cell_Pro_Struct cellPro){
		String styleName = "";
		
		for(Iterator<Cell_Pro_Struct> it=_cell_pro_list.iterator(); it.hasNext();){
			Cell_Pro_Struct cps = it.next();
			if(cps.equals_to(cellPro)){
				styleName = cps.get_style_name();
			}
		}
		
		return styleName;
	}
	
	public static String get_result(){
		String rst = "";
		String styleName = "";
		Cell_Pro_Struct proStr = new Cell_Pro_Struct();
		
		proStr.set_cell_pro(_cell_pro);
		styleName = in_list(proStr);
		
		//Add a new Cell_Pro_Struct to the list
		if(styleName.equals("")){
			styleName = gen_cell_styleID();
			proStr.set_style_name(styleName);
			_cell_pro_list.add(proStr);
			//Create cell-style	
			rst += "<style:style style:family=\"table-cell\"";
			rst += " style:name=\"" + styleName + "\"" + ">";		
			rst += "<style:table-cell-properties" + _cell_pro + "/>";
			rst += "</style:style>";
		}
		
		//put a map entry to the map
		String attsAndCols = " table:style-name=\"" + styleName + "\"";
		if(!_cols_spanned.equals("")){
			attsAndCols += " table:number-columns-spanned=\"" + _cols_spanned + "\"";
		}
		if(!_rows_spanned.equals("")){
			attsAndCols += " table:number-rows-spanned=\"" + _rows_spanned + "\"";
		}
		//columns spanned
		attsAndCols += "|" + _cols_spanned;
		_style_name_map.put(_cell_counter,attsAndCols);
		
		clear();
		return rst;
	}
	
	public static void process_start(String qName,Attributes atts){
		String attVal = "";
		
		if(qName.equals("字:单元格属性")){
			_cell_counter ++;
		}
		else if(qName.equals("字:单元格边距")){
			_cell_pro += get_padding(atts);
		}
		else if(qName.equals("uof:上")||qName.equals("uof:下")    //边框的子元素
			  ||qName.equals("uof:左")||qName.equals("uof:右")){
			_cell_pro += get_borders(qName, atts);	  
		}
		else if(qName.equals("字:跨列")){
			if((attVal=atts.getValue("字:值")) != null){
				_cols_spanned = attVal;
			}
		}
		else if(qName.equals("字:跨行")){
			if((attVal=atts.getValue("字:值")) != null){
				_rows_spanned = attVal;
			}
		}
		else if(qName.equals("字:适应文字")){
			if((attVal=atts.getValue("字:值")) != null){
				_cell_pro += " style:shrink-to-fit=\"" + attVal + "\"";
			}
		}
		else if (qName.equals("字:自动换行")) {	
			attVal = atts.getValue("字:值");
			if(attVal != null){
				String val = (attVal.equals("true"))?"wrap":"no-wrap";
				_cell_pro += " fo:wrap-option=\"" + val + "\"";
			}
		}
	}
	
	public static void process_chars(String chs){	
		_chs = chs;
	}
	
	public static void process_end(String qName){		
		if(qName.equals("字:垂直对齐方式")){
			_cell_pro += " style:vertical-align=\"" 
				+ conv_vertical_align(_chs) + "\"";
		}
		else if(qName.equals("uof:颜色")){
			_cell_pro += " fo:background-color\"" + _chs + "\"";
		}
		else if(qName.equals("字:边框")){
			//@style:border-line-width
			_cell_pro += Common_Pro.get_double_line_width();
		}
	}
	
	//"style:vertical-align"属性值：UOF中both
	//和ODF中automatic分别在对方找不到对应
	private static String conv_vertical_align(String val){
		String convVal = "automatic";
		
		if(val.equals("top")){
			convVal = "top";
		}
		else if(val.equals("center")){
			convVal = "middle";
		}
		else if(val.equals("bottom")){
			convVal = "bottom";
		}
		
		return convVal;
	}
	
	private static String gen_cell_styleID(){
		_cell_style_id ++;
		return "cell_style" + _cell_style_id;
	}
}