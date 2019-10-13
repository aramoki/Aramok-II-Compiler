package aramokiicompiler;

import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 *
 * @author Aramoki
 */
public class Navigator extends JTabbedPane {
	Procedures p;

	public Navigator() {
		setOpaque(true);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setTabPlacement(BOTTOM);
		setBackground(new Color(170, 170, 170));
		setBorder(null);
		addTab("Procedures", p = new Procedures());
	}
}
