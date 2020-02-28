package presentation;

import org.xml.sax.Attributes;

//处理动画类型
public class Animation {
	private static String _qName = "";					//当前处理的元素名
	private static Attributes _atts = null;				//当前处理元素的属性列表	
	private static String _chs = "";					//存放字符节点的内容
	
	private static String _shape_id = "";				//动画对象
	private static String _action_type = "";  			//用来判断进入或退出
	private static String _direction = "";				//进入、退出的方向
	private static String _delay = "";					//持续时间
	private static String _speed = "";					//进入或退出速度
	
	private static boolean _duration_tag = false;
	private static boolean _direction_tag = false;
	
	public static void process_start(String qName,Attributes atts){
		_qName = qName;
		_atts = atts;
		
		if(_qName.equals("演:动画序列")){
			_shape_id = _atts.getValue("演:动画对象");
		}
		if(_qName.equals("演:持续时间")){
			_duration_tag = true;
		}
		else if(_qName.equals("演:动画方向")){
			_direction_tag = true;
		}
	}
	
	public static void process_chars(String chs){
		_chs = chs;
	}
	
	public static void process_end(String qName){
		_qName = qName;
		
		if(_qName.equals("演:持续时间")){
			_duration_tag = false;
		}
		else if(_qName.equals("演:动画方向")){
			_direction_tag = false;
		}
		
		if(_duration_tag){
			if(_qName.equals("演:进入")){
				_speed = _chs.trim();
			}
			else if(_qName.equals("演:停留")){
				_delay = _chs.trim();
			}
			else if(_qName.equals("演:推出")){
				_speed = _chs.trim();
			}
		}
		else if(_direction_tag){
			if(_qName.equals("演:进入")){
				_direction = convertDirection(_chs.trim(),"show-shape");
				_action_type = "show-shape";
			}
			else if(_qName.equals("演:退出")){
				_direction = convertDirection(_chs.trim(),"hide-shape");
				_action_type = "hide-shape";
			}
		}
	}
	
	//uof与odf对 方向 的定义不一致，需要转换 
	private static String convertDirection(String string,String action_type){
		String direction = "";
		String pre = "";
		
		if(action_type.equals("hide-shape")){
			pre = "to-";
		}
		else{
			pre = "from-";
		}
		
		if(string.equals("top")){
			direction = pre + "top";
		}
		else if(string.equals("bottom")){
			direction = pre + "bottom";
		}
		else if(string.equals("left")){
			direction = pre + "left";
		}
		else if(string.equals("right")){
			direction = pre + "right";
		}
		else if(string.equals("top left")){
			direction = pre + "upper-left";
		}
		else if(string.equals("top right")){
			direction = pre + "upper-right";
		}
		else if(string.equals("lower left")){
			direction = pre + "lower-left";
		}
		else if(string.equals("lower right")){
			direction = pre + "lower-right";
		}
		
		return direction;
	}
	
	public static String getResult(){
		String str = "<presentation:animations>";
		String action = "";
		
		if(_action_type.equals("show-shape")){
			action = "presentation:show-shape";
		}
		else{ 
			action = "presentation:hide-shape";
		}
		
		str += "<" + action;
		str += " draw:shape-id=\"" + _shape_id + "\"";
		str += " presentation:direction=\"" + _direction + "\"";
		str += " presentation:speed=\"" + _speed + "\"";
		str += " presentation:delay=\"" + _delay + "\"";
		str += ">";
		str += "</presentation:animations>";
		
		clear();           
		
		return str;
	}
	
	private static void clear(){
		_qName = "";
		_atts = null;
		_chs = "";
		_shape_id = "";
		_action_type = "";
		_direction = "";
		_speed = "";
		_delay = "";
	}
}
