package presentation;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.xml.sax.Attributes;

import convertor.IDGenerator;


/**
 *
 * @author xie
 *
 */
public class Draw_Page_Style {
	//
	private static String _chs = "";
	//tag for <演:幻灯片备注>
	private static boolean _notes_tag = false;
	//counter for <演:幻灯片>
	private static int _dp_counter = 0;
	//attributes of <style:drawing-page-properties>
	private static String _dp_pro = "";
	//the result
	private static String _styles = "";
	//attributes of <presentation:sound>
	private static String _sound_atts = "";
	//
	private static Map<String,String>
		_effect_table = new HashMap<String,String>();
	//
	private static Map<Integer,String>
		_styleID_map = new TreeMap<Integer,String>();


	//initialize
	public static void init(){
		_dp_counter = 0;
		_dp_pro = "";
		_styles = "";
		_sound_atts = "";
		_effect_table.clear();
		_styleID_map.clear();
	}

	//
	public static String get_style_name(int counter){
		return _styleID_map.get(counter);
	}

	private static void clear(){
		_dp_pro = "";
		_sound_atts = "";
	}

	private static String create_style(){
		String style = "";

		if(!_dp_pro.equals("") || !_sound_atts.equals("")){
			String dpID = IDGenerator.get_draw_page_id();

			style = "<style:style style:family=\"drawing-page\"";
			style += " style:name=\"" + dpID + "\">";
			style += "<style:drawing-page-properties " + _dp_pro + "";
			if(!_sound_atts.equals("")){
				style += "<presentation:sound" + _sound_atts + "/>";
				style += "</style:drawing-page-properties>";
			}
			else {
				style += "/>";
			}
			style += "</style:style>";

			_styleID_map.put(_dp_counter,dpID);
		}

		clear();
		return style;
	}

	public static String get_result(){
		String str = "";

		str = _styles;
		_styles = "";

		return str;
	}

	private static String add_att(String attName,String attVal){
		return " " + attName + "=\"" + attVal + "\"";
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(_notes_tag){
			return;
		}
		else if(qName.equals("演:幻灯片备注")){
			_notes_tag = true;
		}

		else if(qName.equals("演:幻灯片")){
			_dp_counter ++;
		}

		else if(qName.equals("演:切换")){
			attVal = atts.getValue("演:效果");
			if(attVal != null){
				_dp_pro += get_effect_atts(attVal);
			}

			attVal = atts.getValue("演:速度");
			if(attVal != null){
				_dp_pro += add_att("presentation:transition-speed",attVal);
			}
		}

		else if(qName.equals("演:声音")){
			//todo
		}

		else if(qName.equals("图:渐变")){
			String gradID = Draw_Padding.create_gradient(atts);

			_dp_pro += add_att("presentation:background-visible","true");
			_dp_pro += add_att("draw:fill","gradient");
			_dp_pro += add_att("draw:fill-gradient-name",gradID);

		}

		else if(qName.equals("图:图片")){
			String imageID = Draw_Padding.create_fill_image(atts);

			_dp_pro += add_att("presentation:background-visible","true");
			_dp_pro += add_att("draw:fill","bitmap");
			_dp_pro += add_att("draw:fill-image-name",imageID);

			attVal = atts.getValue("图:位置");
			_dp_pro += add_att("draw:repeat",conv_position(attVal));
		}
	}

	public static void process_chars(String chs){
		_chs = chs;
	}

	public static void process_end(String qName){
		if(_notes_tag){
			if(qName.equals("演:幻灯片备注")){
				_notes_tag = false;
			}
			return ;
		}

		else if(qName.equals("演:幻灯片")){
			_styles += create_style();
		}

		else if(qName.equals("演:时间间隔")){
			_dp_pro += add_att("presentation:duration",conv_time(_chs));
		}

		else if(qName.equals("图:颜色")){
			_dp_pro += add_att("draw:fill","solid");
			_dp_pro += add_att("draw:fill-color",_chs);
			_dp_pro += add_att("presentation:background-visible","true");
		}

		else if(qName.equals("图:图案")){
			//todo
		}

		_chs = "";
	}

	private static String conv_position(String val){
		String posi = "center";

		if(val.equals("no-repeat")){
			posi = "center";
		}
		else if(val.equals("tile")){
			posi = "repeat";
		}
		else if(val.equals("stretch")){
			posi = "stretch";
		}

		return posi;
	}

	//convert the duaration value from uof to odf
	//for example: 2000 => PT00H00M02S
	public static String conv_time(String val){
		String dur = "";

		try{
			float secs = Float.parseFloat(val);
			secs = secs / 1000;

			int sec = new Float(secs).intValue();
			int hour = sec / 3600;
			int minute = (sec % 3600) / 60;
			sec = sec % 60;

			dur = "PT";
			dur += (hour < 10) ? ("0" + hour + "H") : (hour + "H");
			dur += (minute < 10) ? ("0" + minute + "H") : (minute + "H");
			dur += (sec < 10) ? ("0" + sec + "S") : (sec + "S");
		}catch (Exception e){
			e.printStackTrace();
		}

		return dur;
	}

	private static String get_effect_atts(String eff){
		String atts = "";
		String str = _effect_table.get(eff);

		if(str != null){
			int ind1 = str.indexOf("|");
			int ind2 = str.lastIndexOf("|");

			if(ind1 == -1){
				atts += " smil:type=\"" + str + "\"";
			}
			else if(ind1 != -1 && ind2 == ind1){
				atts += " smil:type=\"" + str.substring(0,ind1) + "\"";
				atts += " smil:subtype=\"" + str.substring(ind1+1) + "\"";
			}
			else if(ind1 != -1 && ind2 != ind1){
				atts += " smil:type=\"" + str.substring(0,ind1) + "\"";
				atts += " smil:subtype=\"" + str.substring(ind1+1,ind2) + "\"";
				atts += " smil:direction=\"" + str.substring(ind2+1) + "\"";
			}
		}

		return atts;
	}

	public static void init_effect_table(){

		_effect_table.put("wipe up","barWipe|topToBottom|reverse");

		_effect_table.put("wipe down","barWipe|topToBottom");

		_effect_table.put("wipe right","barWipe|leftToRight");

		_effect_table.put("wipe left","barWipe|leftToRight|reverse");

		_effect_table.put("wheel clockwise – 1 spoke","pinWheelWipe|oneBlade");

		_effect_table.put("wheel clockwise – 2 spoke","pinWheelWipe|twoBladeVertical");

		_effect_table.put("wheel clockwise – 3 spoke","pinWheelWipe|threeBlade");

		_effect_table.put("wheel clockwise – 4 spoke","pinWheelWipe|fourBlade");

		_effect_table.put("wheel clockwise – 8 spoke","pinWheelWipe|eightBlade");

		_effect_table.put("uncover down","slideWipe|fromTop|reverse");

		_effect_table.put("uncover left","slideWipe|fromRight|reverse");

		_effect_table.put("uncover right","slideWipe|fromLeft|reverse");

		_effect_table.put("uncover up","slideWipe|fromBottom|reverse");

		_effect_table.put("uncover left-down","slideWipe|fromTopRight|reverse");

		_effect_table.put("uncover left-up","slideWipe|fromBottomRight|reverse");

		_effect_table.put("uncover right-down","slideWipe|fromTopLeft|reverse");

		_effect_table.put("uncover right-up","slideWipe|fromBottomLeft|reverse");

		_effect_table.put("random bars vertical","randomBarWipe|vertical");

		_effect_table.put("random bars horizontal","randomBarWipe|horizontal");

		_effect_table.put("checkerboard down","checkerBoardWipe|down");

		_effect_table.put("checkerboard across","checkerBoardWipe|across");

		_effect_table.put("shape plus","fourBoxWipe|cornersOut");

		_effect_table.put("shape diamond","irisWipe|diamond");

		_effect_table.put("shape circle","ellipseWipe|circle");

		_effect_table.put("box out","irisWipe|rectangle");

		_effect_table.put("box in","irisWipe|rectangle|reverse");

		_effect_table.put("wedge","fanWipe|centerTop");

		_effect_table.put("blinds vertical","blindsWipe|vertical");

		_effect_table.put("blinds horizontal","blindsWipe|horizontal");

		_effect_table.put("fade through black","fade|fadeOverColor");

		_effect_table.put("cover down","slideWipe|fromTop");

		_effect_table.put("cover left","slideWipe|fromRight");

		_effect_table.put("cover right","slideWipe|fromLeft");

		_effect_table.put("cover up","slideWipe|fromBottom");

		_effect_table.put("cover left-down","slideWipe|fromTopRight");

		_effect_table.put("cover left-up","slideWipe|fromBottomRight");

		_effect_table.put("cover right-down","slideWipe|fromTopLeft");

		_effect_table.put("cover right-up","slideWipe|fromBottomLeft");

		_effect_table.put("dissolve","dissolve");

		_effect_table.put("random transition","random");

		_effect_table.put("comb horizontal","pushWipe|combHorizontal");

		_effect_table.put("comb vertical","pushWipe|combVertical");

		_effect_table.put("fade smoothly","fade|crossfade");

		_effect_table.put("push down","pushWipe|fromTop");

		_effect_table.put("push left","pushWipe|fromRight");

		_effect_table.put("push right","pushWipe|fromLeft");

		_effect_table.put("push up","pushWipe|fromBottom");

		_effect_table.put("split horizontal in","barnDoorWipe|horizontal|reverse");

		_effect_table.put("split horizontal out","barnDoorWipe|horizontal");

		_effect_table.put("split vertical in","barnDoorWipe|vertical|reverse");

		_effect_table.put("split vertical out","barnDoorWipe|vertical");

		_effect_table.put("strips left-down","waterfallWipe|horizontalRight");

		_effect_table.put("strips left-up","waterfallWipe|horizontalLeft|reverse");

		_effect_table.put("strips right-down","waterfallWipe|horizontalLeft");

		_effect_table.put("strips right-up","waterfallWipe|horizontalRight|reverse");
	}
}
