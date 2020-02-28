package presentation;

import org.xml.sax.Attributes;

import temp_structs.Object_Set_Data;
import temp_structs.Stored_Data;
import convertor.IDGenerator;

public class Draw_Padding {

	private static String add_att(String attName,String attVal){
		return " " + attName + "=\"" + attVal + "\"";
	}
	
	//create a <draw:fill-image> element (to be
	//saved in styles.xml), and return its name
	public static String create_fill_image(Attributes atts){
		String attVal = "";
		String imagePro = "";
		String imageID = "";
		
		attVal = atts.getValue("ͼ:ͼ������");
		attVal = (attVal==null) ? "" : attVal;

		String href = Object_Set_Data.getOtherObj(attVal);
		if(href != null){
			imageID = IDGenerator.get_fill_image_id();
			
			imagePro += add_att("draw:name",imageID);
			imagePro += add_att("xlink:href",href);
			imagePro += add_att("xlink:type","simple");
			imagePro += add_att("xlink:show","embed");
			imagePro += add_att("xlink:actuate","onLoad");
			
			String image = "<draw:fill-image" + imagePro + "/>";
			Stored_Data.addStylesInStylesXml(image);
		}
		
		return imageID;
	}
	
	//create a <draw:gradient> element (to be 
	//saved in styles.xml), and return its name
	public static String create_gradient(Attributes atts){
		String attVal = "";
		String gradPro = "";
		String gradID = "";
		
		gradID = IDGenerator.get_gradient_id();
		gradPro += add_att("draw:name",gradID);
		
		if((attVal=atts.getValue("ͼ:��ʼɫ")) != null){
			gradPro += add_att("draw:start-color",attVal);
		}
		if((attVal=atts.getValue("ͼ:��ֹɫ")) != null){
			gradPro += add_att("draw:end-color",attVal);
		}
		if((attVal=atts.getValue("ͼ:��������")) != null){
			gradPro += add_att("draw:style",conv_style(attVal));
		}
		if((attVal=atts.getValue("ͼ:��ʼŨ��")) != null){
			gradPro += add_att("draw:start-intensity",to_percent(attVal));
		}
		if((attVal=atts.getValue("ͼ:��ֹŨ��")) != null){
			gradPro += add_att("draw:end-intensity",to_percent(attVal));
		}
		if((attVal=atts.getValue("ͼ:���䷽��")) != null){
			gradPro += add_att("draw:angle",Float.parseFloat(attVal)+"");
		}
		if((attVal=atts.getValue("ͼ:�߽�")) != null){
			gradPro += add_att("draw:border",attVal+"%");
		}
		if((attVal=atts.getValue("ͼ:����Xλ��")) != null){
			gradPro += add_att("draw:cX",attVal+"%");
		}
		if((attVal=atts.getValue("ͼ:����Yλ��")) != null){
			gradPro += add_att("draw:cY",attVal+"%");
		}
		
		String style = "<draw:gradient" + gradPro + "/>";
		Stored_Data.addStylesInStylesXml(style);
		
		return gradID;
	}
	
	private static String to_percent(String val){
		return Float.parseFloat(val) * 100 + "%";
	}
	
	private static String conv_style(String val){
		String convVal = "linear";
		
		if(val.equals("linear")){
			convVal = "linear";
		}
		else if(val.equals("radar")){
			convVal = "axial";
		}
		else if(val.equals("oval")){
			convVal = "ellipsoid";
		}
		else if(val.equals("square")){
			convVal = "square";
		}
		else if(val.equals("rectangular")){
			convVal = "rectangle";
		}
		
		return convVal;
	}
}
