package aramokiicompiler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.TextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Aramoki
 */
public class ASCII extends JFrame {

	ImageIcon tile;

	public ASCII() throws HeadlessException {
		setLocationByPlatform(true);
		setSize(512 + 150, 512 + 32);
		add(new inside());
		tile = new ImageIcon(getClass().getClassLoader().getResource("images/asc.png"));
		setVisible(true);
		setTitle("ASC Code viewer");
	}

	public class inside extends JPanel {
		int x;
		int y;
		JTextField dec, hex, bin;

		public inside() {

			setLayout(null);
			add(new JLabel("HEX:")).setBounds(512 + 10, 45, 130, 20);
			add(hex = new JTextField()).setBounds(512 + 10, 70, 130, 20);

			add(new JLabel("BIN:")).setBounds(512 + 10, 90, 130, 20);
			add(bin = new JTextField()).setBounds(512 + 10, 115, 130, 20);

			add(new JLabel("DEC:")).setBounds(512 + 10, 135, 130, 20);
			add(dec = new JTextField()).setBounds(512 + 10, 155, 130, 20);

			repaint();
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					super.mouseClicked(e);
					if (e.getX() < 512 & e.getY() < 512) {
						x = (e.getX() / 32);
						y = (e.getY() / 32);
						dec.setText(x + y * 16 + ".d");
						bin.setText(Integer.toBinaryString(x + y * 16) + ".b");
						hex.setText(Integer.toHexString(x + y * 16) + ".h");
						repaint();
					}
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.black);
			g.fillRect(0, 0, 512, 512);
			g.setColor(Color.red);
			g.fillRect(x * 32, y * 32, 32, 32);
			g.drawImage(tile.getImage(), 0, 0, 512, 512, this);
		}
	}
}
