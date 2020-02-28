package presentation;

import org.xml.sax.Attributes;

import convertor.IDGenerator;

import temp_structs.Common_Data;

/**
 *
 * @author xie
 *
 */
public class Master_Pane {
	//
	private static String _chs = "";
	//the result
	private static String _result = "";
	//attributes of <style:drawing-page-properties>
	private static String _dp_pro = "";
	//name of drawing-page style
	private static String _dp_name = "";
	//drawing-page styles used by master-page
	private static String _dp_styles = "";
	//tag for <presentation:notes>
	private static boolean _notes_tag = false;


	public static String get_dp_styles(){
		String dps = "";

		dps = _dp_styles;
		_dp_styles = "";

		return dps;
	}

	private static String one_dp_style(){
		String style = "";

		style = "<style:style";
		style += add_att("style:name",_dp_name);
		style += add_att("style:family","drawing-page");
		style += ">";
		style += "<style:drawing-page-properties " + _dp_pro + "/>";
		style += "</style:style>";

		_dp_pro = "";
		return style;
	}

	private static void clear(){
		_result = "";
	}

	public static String get_result(){
		String rst = "";

		rst = layer_set() + _result;
		clear();

		return rst;
	}

	private static String add_att(String attName,String attVal){
		return " " + attName + "=\"" + attVal + "\"";
	}
	private static String skip_null(String val){
		return (val==null) ? "" : val;
	}

	public static void process_start(String qName,Attributes atts){
		String attVal = "";

		if(qName.equals("演:母版")){
			attVal = skip_null(atts.getValue("演:类型"));
			if(attVal.equals("slide")){
				_dp_name = IDGenerator.get_mpdp_id();

				_result += "<draw:master-page";
				if((attVal=atts.getValue("演:标识符"))!=null){
					_result += add_att("style:name",attVal);
				}
				if((attVal=atts.getValue("演:页面设置引用"))!=null){
					_result += add_att("style:page-layout-name",attVal);
				}
				_result += add_att("draw:style-name",_dp_name);
				_result += ">";
			}
			else if(attVal.equals("notes")){
				_notes_tag = true;

				_result += "<presentation:notes";
				if((attVal=atts.getValue("演:页面设置引用"))!=null){
					_result += add_att("style:page-layout-name",attVal);
				}
				_result += ">";
			}
		}

		else if(qName.equals("uof:锚点")){
			String frameAtts = "";
			String holder = "";

			attVal = skip_null(atts.getValue("uof:占位符"));
			holder = Presentation_Page_Layout.conv_PH_type(attVal);

			if(holder.equals("title")){
				frameAtts += add_att("presentation:style-name","pr_" + Presentation_Style.get_title_name());
			}
			else if(holder.equals("outline")){
				frameAtts += add_att("presentation:style-name","pr_" + Presentation_Style.get_outline_name());
			}
			else if(holder.equals("notes")){
				frameAtts += add_att("presentation:style-name","pr_" + Presentation_Style.get_notes_name());
			}
			else {
				//frameAtts += add_att("presentation:style-name","pr1");
			}
			frameAtts += add_att("presentation:class",holder);
			//frameAtts += add_att("presentation:placeholder","true");
			frameAtts += add_att("draw:layer","backgroundobjects");
			frameAtts += add_att("svg:x",measure_val(atts.getValue("uof:x坐标")));
			frameAtts += add_att("svg:y",measure_val(atts.getValue("uof:y坐标")));
			frameAtts += add_att("svg:width",measure_val(atts.getValue("uof:宽度")));
			frameAtts += add_att("svg:height",measure_val(atts.getValue("uof:高度")));

			String frame = "";
			frame = "<draw:frame" + frameAtts + ">";
			frame += "<draw:text-box/>";
			frame += "</draw:frame>";

			_result += frame;
		}

		else if(qName.equals("图:渐变") && !_notes_tag){
			String gradID = Draw_Padding.create_gradient(atts);

			_dp_pro += add_att("presentation:background-visible","true");
			_dp_pro += add_att("draw:fill","gradient");
			_dp_pro += add_att("draw:fill-gradient-name",gradID);

		}

		else if(qName.equals("图:图片") && !_notes_tag){
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
		if(qName.equals("演:母版集")){
			_result += "</draw:master-page>";
			_dp_styles += one_dp_style();
		}
		else if(qName.equals("演:母版") && _notes_tag){
			_notes_tag = false;

			_result += "</presentation:notes>";
		}
		else if(qName.equals("图:颜色") && !_notes_tag){
			_dp_pro += add_att("draw:fill","solid");
			_dp_pro += add_att("draw:fill-color",_chs);
			_dp_pro += add_att("draw:background-size","border");
		}
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

	//
	private static String measure_val(String val){
		return Float.valueOf(val) * Common_Data.get_graphratio() + Common_Data.get_unit();
	}

	private static String layer_set(){
		String set = "";

		set = "<draw:layer-set>"
			+ "<draw:layer draw:name=\"layout\" />"
			+ "<draw:layer draw:name=\"background\" />"
			+ "<draw:layer draw:name=\"backgroundobjects\" />"
			+ "<draw:layer draw:name=\"controls\" />"
			+ "<draw:layer draw:name=\"measurelines\" />"
			+ "</draw:layer-set>";

		return set;
	}
}
