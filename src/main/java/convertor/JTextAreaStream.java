package convertor;

import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * 重定向一个标准输出流(System.out/System.err)到一个JTextArea中.
 *
 * @author xie
 */
public class JTextAreaStream extends OutputStream {
	  private JTextArea ta;


	  public JTextAreaStream(JTextArea ta) {
	    this.ta = ta;
	  }

	  public void write(int b) {
	    ta.append(new String(new byte[]{(byte)b}));
	  }
}
