package aramokiicompiler;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.PlainDocument;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Aramoki
 */
public class ContentShower extends JPanel {
	JTextPane textpane;
	StyledDocument doc;

	public ContentShower() {
		setLayout(new GridLayout(1, 1));
		textpane = new JTextPane(doc = new DefaultStyledDocument());
		doc.putProperty(PlainDocument.tabSizeAttribute, 2);
		add(textpane);

	}

}
