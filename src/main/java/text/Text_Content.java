package text;

import java.util.Stack;
import org.xml.sax.Attributes;

public class Text_Content {
	//the result
	private static String _result = "";
	//tag for <text:p>
	private static boolean _para_tag = false;
	//tag for <table:table>
	private static boolean _table_tag = false;
	//stack for dealing with nesting
	private static Stack<String> _stack = new Stack<String>();

	private static void clear(){
		_result = "";
	}

	public static String get_result(){
		String str = _result;
		clear();

		return str;
	}

	public static void process_start(String qName,Attributes atts){
		if(_para_tag){
			_stack.push(qName);
			Text_p.process_start(qName,atts);
		}
		else if(_table_tag){
			_stack.push(qName);
			Text_Table.process_start(qName,atts);
		}
		else if(qName.equals("字:段落")){
			_stack.push(qName);
			_para_tag = true;
			Text_p.process_start(qName,atts);
		}
		else if(qName.equals("字:文字表")){
			_stack.push(qName);
			_table_tag = true;
			Text_Table.process_start(qName,atts);
		}
	}

	public static void process_chars(String chs){
		if(_para_tag){
			Text_p.process_chars(chs);
		}
		else if(_table_tag){
			Text_Table.process_chars(chs);
		}
	}

	//处理元素结束标签
	public static void process_end(String qName){
		if(_table_tag){
			Text_Table.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_table_tag = false;
				_result += Text_Table.get_result();
			}
		}
		else if(_para_tag){
			Text_p.process_end(qName);
			_stack.pop();
			if(_stack.empty()){
				_para_tag = false;
				_result += Text_p.get_result();
			}
		}
	}
}
