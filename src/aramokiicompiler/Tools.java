package aramokiicompiler;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 *
 * @author Aramok
 */
public class Tools extends JToolBar {

	public Tools(NavMenu nav) {
		System.out.println("Tools created");
		setBorderPainted(true);
		setFloatable(false);
		setOpaque(true);

		add(new JButton2("backup", new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/backup.png")),
				nav.backup));
		add(new JButton2("recover",
				new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/recover.png")), nav.recover));
		addSeparator();
		add(new JButton2("Save", new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/sv.png")),
				nav.save));
		add(new JButton2("Build", new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/run.gif")),
				nav.build));
		addSeparator();
		addSeparator();
		addSeparator();
		add(new JButton2("Undo",
				new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/arrow_undo.png")), nav.undo));
		add(new JButton2("Redo",
				new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/arrow_redo.png")), nav.redo));
		addSeparator();
		add(new JButton2("", new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/zoom_in.png")),
				nav.inc_font));
		add(new JButton2("", new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/zoom_out.png")),
				nav.dec_font));
		addSeparator();
		add(new JButton2("ASCII",
				new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/book_addresses.png")),
				nav.ascii));
		add(new JLabel(" ASM for AramokII Processor v0.2 BLUE alpha"));
	}

}
