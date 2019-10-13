package aramokiicompiler;

import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 * @author Aramok
 */
public class AramokIICompiler extends JFrame {

	static AramokIICompiler R;
	CodeWindow code;
	Build build;
	JSplitPane layout, mainlayout;
	NavMenu navmenu;

	Parameters P;
	Tools tools;
	Tabs tab;
	ArrayList<String> b_procs;
	Navigator navigator;

	public AramokIICompiler() {
		b_procs = new ArrayList<>();
		JFrame.setDefaultLookAndFeelDecorated(true);
		setSize(1280, 739);
		setDefaultLookAndFeelDecorated(true);
		setTitle("Aramok MicroProcessor Programmer");
		setLocation(0, 0);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		navmenu = new NavMenu();
		setMenuBar(navmenu);

		P = new Parameters();
		setLayout(new BorderLayout());
		add(tools = new Tools(navmenu), BorderLayout.NORTH);

		tab = new Tabs();
		mainlayout = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		tab.add(mainlayout);
		tab.addTab("Source Code", new ImageIcon(getClass().getClassLoader().getResource("images/icon.png")), mainlayout,
				"AASM Source Code");
		add(tab);
		mainlayout.add(layout = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT), JSplitPane.RIGHT);
		mainlayout.add(navigator = new Navigator(), JSplitPane.LEFT);
		layout.add(code = new CodeWindow(), JSplitPane.LEFT);
		layout.add(build = new Build(), JSplitPane.RIGHT);
		layout.setDividerLocation(850);
		mainlayout.setDividerLocation(200);

		tab.addTab("Hex Viewer",
				new ImageIcon(getClass().getClassLoader().getResource("images/toolbar/book_addresses.png")),
				new ContentShower(), "AASM Source Code");

		setVisible(true);

	}

	public NavMenu getNavmenu() {
		return navmenu;
	}

	public static void main(String[] args) {
		MetalLookAndFeel laf = null;
		MetalLookAndFeel.setCurrentTheme(new AramokTheme());
		try {
			UIManager.setLookAndFeel(laf = new MetalLookAndFeel());

		} catch (UnsupportedLookAndFeelException ex) {
			JOptionPane.showMessageDialog(AramokIICompiler.R, "Unsupported UI", "Error", JOptionPane.ERROR_MESSAGE);
		}

		JDialog.setDefaultLookAndFeelDecorated(true);
		JFrame.setDefaultLookAndFeelDecorated(true);
		R = new AramokIICompiler();
	}

	static class AramokTheme extends DefaultMetalTheme {

		public String getName() {
			return "Aramok";
		}

		int a = 30;
		private final ColorUIResource primary1 = new ColorUIResource(161 - 50, 170 - 50, 177);
		private final ColorUIResource primary3 = new ColorUIResource(161 - 100, 170 - 100, 177);
		private final ColorUIResource primary2 = new ColorUIResource(161 - 100, 170 - 100, 177);
		private final ColorUIResource secondary1 = new ColorUIResource(179 - 100, 183 - 100, 187 - 100);
		private final ColorUIResource secondary2 = new ColorUIResource(179, 183, 187);
		private final ColorUIResource secondary3 = new ColorUIResource(179 + 25, 183 + 25, 187 + 25);

		@Override
		protected ColorUIResource getPrimary1() {
			return primary1;
		}

		@Override
		protected ColorUIResource getPrimary2() {
			return primary2;
		}

		@Override
		protected ColorUIResource getPrimary3() {
			return primary3;
		}

		@Override
		protected ColorUIResource getSecondary1() {
			return secondary1;
		}

		@Override
		protected ColorUIResource getSecondary2() {
			return secondary2;
		}

		@Override
		protected ColorUIResource getSecondary3() {
			return secondary3;
		}

	}

	public CodeWindow getCode() {
		return code;
	}

}
