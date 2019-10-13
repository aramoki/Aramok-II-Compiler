package aramokiicompiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author Aramoki
 */
public class codeStatus extends JPanel {

	JToggleButton num;
	JToggleButton ptr;
	JToggleButton reg;
	JToggleButton opcode;
	JPanel jp;
	ActionListener al;

	public codeStatus() {
		setBorder(null);
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
		add(opcode = new JToggleButton("Opcode >",
				new ImageIcon(getClass().getClassLoader().getResource("images/sbar/proc.gif"))));
		add(reg = new JToggleButton("Register >",
				new ImageIcon(getClass().getClassLoader().getResource("images/sbar/reg.gif"))));
		add(ptr = new JToggleButton("Pointer >",
				new ImageIcon(getClass().getClassLoader().getResource("images/sbar/data.gif"))));
		add(num = new JToggleButton("Data >",
				new ImageIcon(getClass().getClassLoader().getResource("images/sbar/data.gif"))));
		al = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				AramokIICompiler.R.code.Colorize();
			}
		};
		num.setBorder(null);
		ptr.setBorder(null);
		reg.setBorder(null);
		opcode.setBorder(null);
		num.addActionListener(al);
		ptr.addActionListener(al);
		reg.addActionListener(al);
		opcode.addActionListener(al);
	}

}
