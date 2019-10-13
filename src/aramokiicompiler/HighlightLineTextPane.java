/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aramokiicompiler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class HighlightLineTextPane extends JTextPane {

	public HighlightLineTextPane() {
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {

		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		try {
			Rectangle rect = modelToView(getCaretPosition());
			if (rect != null) {
				g.setColor(new Color(255, 255, 204));
				g.fillRect(0, rect.y, getWidth(), rect.height);
			}
		} catch (BadLocationException e) {
		}
		super.paintComponent(g);
	}

	@Override
	public void repaint(long tm, int x, int y, int width, int height) {
		super.repaint(tm, 0, 0, getWidth(), getHeight());
	}

	public String lineFromTextPane(String data, Caret c) {
		int indiceStart = 0, indiceStop = 0, correction = 0;

		if (data.length() == c.getDot()) {
			correction = 1;
		}

		if (data.charAt(c.getDot() - correction) != '\n') {
			for (int i = 0 + correction; i < c.getDot(); i++) {
				if (data.charAt(c.getDot() - i) == '\n') {
					indiceStart = i;
					break;
				}
			}

			return data.substring(indiceStart, c.getDot() - correction);
		} else {
			for (int i = 0; i < c.getDot(); i++) {
				if (data.charAt(c.getDot() - i) == '\n') {
					indiceStart = i;
					break;
				}
			}

			for (int i = c.getDot(); i < data.length() - correction; i++) {
				if (data.charAt(c.getDot() + i) == '\n') {
					indiceStop = i;
					break;
				}
			}
			return data.substring(indiceStart, indiceStop);
		}
	}

	public static int getLineOfOffset(JTextComponent comp, int offset) throws BadLocationException {
		Document doc = comp.getDocument();
		if (offset < 0) {
			throw new BadLocationException("Can't translate offset to line", -1);
		} else if (offset > doc.getLength()) {
			throw new BadLocationException("Can't translate offset to line", doc.getLength() + 1);
		} else {
			Element map = doc.getDefaultRootElement();
			return map.getElementIndex(offset);
		}
	}

	public static int getLineStartOffset(JTextComponent comp, int line) throws BadLocationException {
		Element map = comp.getDocument().getDefaultRootElement();
		if (line < 0) {
			throw new BadLocationException("Negative line", -1);
		} else if (line >= map.getElementCount()) {
			throw new BadLocationException("No such line", comp.getDocument().getLength() + 1);
		} else {
			Element lineElem = map.getElement(line);
			return lineElem.getStartOffset();
		}
	}
}
