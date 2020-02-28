package convertor;

import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 * �ض���һ����׼�����(System.out/System.err)��һ��JTextArea��.
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
