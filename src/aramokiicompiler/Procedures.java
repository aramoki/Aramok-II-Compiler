package aramokiicompiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Aramoki
 */
public class Procedures extends JSplitPane {

	JList procedures;
	DefaultListModel dlm;
	JTextPane infopanel;
	StyledDocument doc;

	public Procedures() {
		doc = new DefaultStyledDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, 3);
		procedures = new JList(dlm = new DefaultListModel());
		procedures.setCellRenderer(new ListEntryCellRenderer());
		setDividerLocation(400);
		setOrientation(VERTICAL_SPLIT);
		add(new JScrollPane(procedures), TOP);
		add(new JScrollPane(infopanel = new JTextPane(doc)), BOTTOM);
		infopanel.setBackground(new Color(196, 196, 196));
		infopanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		infopanel.setEditable(false);

		dlm.addElement(new ListEntry("; : Comment Definition",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/orange.png"))));
		dlm.addElement(new ListEntry("# : address",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/orange.png"))));
		dlm.addElement(new ListEntry("byte",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/ruby.png"))));
		dlm.addElement(new ListEntry("word",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/ruby.png"))));
		dlm.addElement(new ListEntry("dword",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/ruby.png"))));

		procedures.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				try {
					infopanel.setText(((ListEntry) dlm.get(procedures.getSelectedIndex())).getInfo());
				} catch (Exception exx) {
					System.out.println("none");
				}
			}
		});

	}

	public void addproc(String s, String info) {
		dlm.addElement(new ListEntry(s + "()",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/bullet_red.png")),
				new Color(128 + 64, 0, 0), info));
	}

	public void addlabel(String s, String info) {
		dlm.addElement(new ListEntry(s + ":",
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/bullet_blue.png")),
				new Color(0, 0, 128), info));
	}

	public void addvariable(String s, String info) {
		dlm.addElement(new ListEntry(s,
				new ImageIcon(getClass().getClassLoader().getResource("images/navigator/bullet_green.png")),
				new Color(0, 128, 0), info));
	}

	public void resetprocs() {
		dlm.clear();
	}

	class ListEntry {
		final int VAR = 0;
		private String value;
		private ImageIcon icon;
		private Color color;
		private String info;

		public String getInfo() {
			return info;
		}

		public ListEntry(String value, ImageIcon icon) {
			this.value = value;
			this.icon = icon;
			this.color = Color.black;
		}

		public ListEntry(String value, ImageIcon icon, Color c, String info) {
			this.value = value;
			this.icon = icon;
			this.color = c;
			this.info = info;
		}

		public Color getColor() {
			return color;
		}

		public String getValue() {
			return value;
		}

		public ImageIcon getIcon() {
			return icon;
		}

		public String toString() {
			return value;
		}
	}

	class ListEntryCellRenderer extends JLabel implements ListCellRenderer {
		private JLabel label;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			ListEntry entry = (ListEntry) value;
			setText(value.toString());
			setIcon(entry.getIcon());
			setBorder(BorderFactory.createEmptyBorder(2, 15, 2, 0));
			setForeground(entry.getColor());
			if (isSelected) {
				setBackground(list.getSelectionBackground());
			} else {
				setBackground(list.getBackground());
			}
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}
}
