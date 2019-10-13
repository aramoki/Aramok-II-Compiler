package aramokiicompiler;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

/**
 *
 * @author Aramok
 */
public class Tabs extends JTabbedPane {
	public Tabs() {
		setOpaque(true);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBackground(new Color(170, 170, 170));
	}

	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		super.addTab(title, icon, component, tip);
	}

	public class CloseButtonTab extends JPanel {
		private final Component tab;
		private JLabel label;
		private final JLabel close;

		public CloseButtonTab(final Component tab, String title, Icon icon, Tabs res) {
			this.tab = tab;
			add(label = new JLabel(title + "  ", icon, JLabel.LEFT));
			label.setFont(label.getFont().deriveFont((float) 11.5));
			add(close = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("images/close.png"))))
					.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							res.remove(tab);
						}
					});
			setOpaque(false);
		}
	}

}
