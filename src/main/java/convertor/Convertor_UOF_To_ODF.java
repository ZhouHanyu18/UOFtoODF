package convertor;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.io.PrintStream;

import presentation.*;
import spreadsheet.*;
import style_set.*;
import temp_structs.*;
import text.*;


public class Convertor_UOF_To_ODF  extends JFrame implements ActionListener{
	private static final long serialVersionUID = 200611111101L;
	private static JTextField _src_path_field;
	private static JTextField _rst_path_field;
	private static JTextArea _source_area;
	private static JTextArea _result_area;
	private static JButton convertButton;
	private static JTextField _state_field;


	private JPanel create_src_panel(JComponent comp1, JComponent comp2){
		JPanel srcPl = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

	    srcPl.add(comp1, c);

	    c.gridwidth = 2;
	    srcPl.add(comp2, c);

	    return srcPl;
	}

	private void add_content(JComponent comp,Insets is, int gridx, int gridy, int gridwidth){
		GridBagConstraints c = new GridBagConstraints();

		c.insets = is;
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;

		getContentPane().add(comp,c);
	}

	public Convertor_UOF_To_ODF(String title){
		super(title);
		getContentPane().setLayout(new GridBagLayout());

	    JLabel pathLb = new JLabel("请输入UOF源文件名: ");
	    pathLb.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
	    pathLb.setFont(new Font(" ", Font.BOLD, 14));
	    _src_path_field = new JTextField(System.getProperty("user.dir"));
	    _src_path_field.setFont(new Font(" ", Font.PLAIN, 13));
	    _src_path_field.setColumns(37);
	    add_content(create_src_panel(pathLb, _src_path_field),new Insets(25,5,5,5), 0, 0, 2);

	    convertButton = new JButton("转换到");
	    convertButton.setBorder(BorderFactory.createEmptyBorder(3,4,3,4));
	    convertButton.setActionCommand("CONVERT");
	    convertButton.addActionListener(this);
	    JPanel butPn = new JPanel();
	    butPn.add(convertButton);
	    butPn.setBorder(BorderFactory.createEmptyBorder(0,0,0,37));
	    _rst_path_field = new JTextField(System.getProperty("user.dir") + "\\");
	    _rst_path_field.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));
	    _rst_path_field.setBackground(Color.LIGHT_GRAY);
	    _rst_path_field.setColumns(32);
	    add_content(create_src_panel(butPn, _rst_path_field),new Insets(5,5,5,5), 0, 1, 2);

	    add_content(new JLabel("解析源文件: "),new Insets(5,5,0,35), 0, 2, 1);

	    add_content(new JLabel("结果输出:   "),new Insets(5,0,0,55), 1, 2, 1);

	    _source_area = new JTextArea("",20,25);
	    _source_area.setEditable(false);
	    _source_area.setLineWrap(true);
	    _source_area.setForeground(Color.gray);
	    add_content(new JScrollPane(_source_area),new Insets(0,25,5,0), 0, 3, 1);

	    _result_area = new JTextArea("",20,25);
	    _result_area.setEditable(false);
	    _result_area.setLineWrap(true);
	    _result_area.setForeground(Color.blue);
	    add_content(new JScrollPane(_result_area),new Insets(0,0,5,25), 1, 3, 1);

	    _state_field = new JTextField("");
	    _state_field.setColumns(30);
	    _state_field.setBorder(BorderFactory.createEmptyBorder());
	    _state_field.setForeground(Color.RED);
	    _state_field.setBackground(Color.LIGHT_GRAY);
	    _state_field.setFont(new Font("",Font.BOLD,14));
	    add_content(_state_field, new Insets(5,5,10,5),0,4,2);
	}

	public void actionPerformed(ActionEvent event){
		if (event.getActionCommand().equals("CONVERT")){
			String srcFile = _src_path_field.getText().trim();
			String rstFile = _rst_path_field.getText().trim();
			String userDir = System.getProperty("user.dir");

			if(!srcFile.endsWith(".uof")){
				_source_area.setText("错误! 源文件必须是uof类型，请重新输入！");
			}
			else {
				try{
					int inds = srcFile.lastIndexOf("\\");
					int inde = srcFile.lastIndexOf(".");
					String srcName = srcFile.substring(inds+1, inde);

					if(!rstFile.contains(".")){
						new File(rstFile).mkdirs();
						if(!rstFile.endsWith("\\")){
							rstFile += "\\";
						}
						rstFile += srcName + "_result.uof";;
					}
					else if(!rstFile.endsWith(".odt") && !rstFile.endsWith(".ods") && !rstFile.endsWith(".odp")){
						rstFile = userDir + "\\" + srcName + "_result.odt";
					}

					_source_area.setText("");
					_result_area.setText("");
					do_convert(srcFile, rstFile);

					initialize();
					//convertButton.setEnabled(false);
				}catch (Exception e){
					System.err.println(e.getMessage());
				}
			}
		}
	}

	private void initialize(){
		Annotation.init();
		Cell_Pro.init();
		HyperLink.init();
		Master_Page.init();
		List_Para.init();
		Para_Pro.init();
		Text_Field.init();
		Text_p.init();
		Text_Pro.init();
		Text_Table.init();
		Table_Cell.init();
		Table_Column.init();
		Table_Row.init();
		Table_Shapes.init();
		Table_Style.init();
		Sheet_Table.init();
		Page_Layout_S.init();

		Draw_Page_Style.init();
		Draw_Page.init();
		Presentation_Style.init();

		IDGenerator.init();
		Font_Set.init();
		Object_Set_Data.init();
		Stored_Data.init();
		Text_Data.init();
		Spreadsheet_Data.init();
	}

	private void do_convert(String srcFileName, String rstFileName){
		Draw_Page_Style.init_effect_table();
		XMLReader xmlReader = null;
		String state = "";

		try {
			ZipCompress.init();
	        Results_Writer.initialize();

			SAXParserFactory spfactory = SAXParserFactory.newInstance();
			spfactory.setValidating(false);    //非验证解析器，用于格式良好的文档。
			SAXParser saxParser = spfactory.newSAXParser();
			xmlReader = saxParser.getXMLReader();
			srcFileName = "file:///" + srcFileName;
			InputSource source = new InputSource(srcFileName);

			//提前扫描源文档,提取度量单位、文件类型
			DefaultHandler preConvertHandler = new Pre_ConvHandler();
			xmlReader.setContentHandler(preConvertHandler);
			xmlReader.setErrorHandler(preConvertHandler);
			xmlReader.parse(source);

			//第一次扫描源文档
			DefaultHandler firstConvertHandler = new First_ConvHandler();
			xmlReader.setContentHandler(firstConvertHandler);
			xmlReader.setErrorHandler(firstConvertHandler);
			xmlReader.parse(source);

			//第二次扫描源文档
			IDGenerator.restart();
			DefaultHandler secondConvertHandler = new Second_ConvHandler();
			xmlReader.setContentHandler(secondConvertHandler);
			xmlReader.setErrorHandler(secondConvertHandler);
			xmlReader.parse(source);

			Results_Writer.close();
			Settings_Writer settingsWriter = new Settings_Writer(Common_Data.get_file_type());
			settingsWriter.writeFiles();
			_rst_path_field.setText(ZipCompress.compress(rstFileName));		//压缩

			state = "          Convert successfully!!!";
		} catch (Exception exception) {
			System.err.println(exception);
			exception.printStackTrace();

			if(state.equals("")){
				state = "Something wrong happened in this conversion!!!";
			}
		}

		_state_field.setText(state);
	}

	public static void write_source_ta(String str){
		_source_area.append(str);
	}

	public static void write_result_ta(String element){
		_result_area.append(element);
	}

	public static void main (String args[]) {
		Convertor_UOF_To_ODF convApp = new Convertor_UOF_To_ODF("UOF-ODF Converter");

		convApp.pack();
		convApp.setLocation(450,230);
		convApp.setVisible(true);
		convApp.setResizable(false);
		convApp.setDefaultCloseOperation(EXIT_ON_CLOSE);

		System.setErr(new PrintStream(new JTextAreaStream(_result_area)));
	}
}
