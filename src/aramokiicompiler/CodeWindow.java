package aramokiicompiler;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.PlainDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Aramok
 */
public class CodeWindow extends JPanel {
	JPopupMenu menu;
	private HighlightLineTextPane textpane;
	private File file;
	private JScrollPane scroll;

	public int font_size = 13;
	final UndoManager undoMgr = new UndoManager();
	StyledDocument doc;
	Font font;
	codeStatus status;

	public CodeWindow() {
		BorderLayout bl;
		TextLineNumber tln;
		setLayout(bl = new BorderLayout());
		bl.setHgap(0);
		add(scroll = new JScrollPane(textpane = new HighlightLineTextPane()), BorderLayout.CENTER);
		add(status = new codeStatus(), BorderLayout.PAGE_END);
		scroll.setRowHeaderView(tln = new TextLineNumber(textpane, 4));
		tln.setBackground(new Color(233, 232, 226));
		doc = new DefaultStyledDocument();
		doc.putProperty(PlainDocument.tabSizeAttribute, 10);
		textpane.setStyledDocument(doc);
		textpane.setFont(font = new Font("Courier new", Font.PLAIN, font_size));

		try {
			BufferedReader reader;
			file = new File("program.aasm");
			String content = "";
			String line;

			if (!file.exists()) {
				file.createNewFile();
			}
			reader = new BufferedReader(new FileReader(file));
			line = reader.readLine();
			while (line != null) {
				content += line + System.lineSeparator();
				line = reader.readLine();
			}
			textpane.setText(content);
			Colorize();

		} catch (FileNotFoundException ex) {
			System.out.println("err ");
		} catch (IOException ex) {
			System.out.println("ers");
		}

		textpane.getDocument().addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent pEvt) {
				undoMgr.addEdit(pEvt.getEdit());
			}
		});

		textpane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				super.keyPressed(e); // To change body of generated methods, choose Tools | Templates.
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_TAB) {
					Colorize();
				}
			}
		});

		menu = new JPopupMenu("Menu");

		textpane.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					menu.show(textpane, e.getX(), e.getY());
				}
			}
		});

	}

	public void setfontsize(int i) {
		font_size += i;
		font = new Font("Courier new", Font.PLAIN, font_size);
		textpane.setFont(font);
		AramokIICompiler.R.build.getTextpane().setFont(font);
	}

	public void addProc(String procname) {
		menu.add(new JMenuItem(procname));
	}

	public void Colorize() {
		clearTextColors();
		String textmatch = textpane.getText().replaceAll("\r", "");

		if (!status.opcode.isSelected()) {

			Matcher match = Pattern.compile(Parameters.OPCODE_REGEX, Pattern.CASE_INSENSITIVE).matcher(textmatch);
			while (match.find()) {
				updateTextColor(match.start(), match.end() - match.start(), new Color(0, 0, 255));
				boldtext(match.start(), match.end() - match.start());
			}
		}

		if (!status.reg.isSelected()) {
			Matcher match2 = Pattern.compile(Parameters.REGISTER_REGEX, Pattern.CASE_INSENSITIVE).matcher(textmatch);

			while (match2.find()) {
				updateTextColor(match2.start(), match2.end() - match2.start(), new Color(128, 128, 255));
				updateForeColor(match2.start(), match2.end() - match2.start(), new Color(255, 255, 204));
				boldtext(match2.start(), match2.end() - match2.start());
			}
		}
		if (!status.ptr.isSelected()) {
			Matcher match3 = Pattern.compile(Parameters.POINTER_REGEX, Pattern.CASE_INSENSITIVE).matcher(textmatch);

			while (match3.find()) {
				updateTextColor(match3.start(), match3.end() - match3.start(), new Color(0, 128, 255));
			}
		}

		if (!status.num.isSelected()) {
			Matcher match4 = Pattern
					.compile("\\b[0-9]..*d\\b|\\b[0-1]..*b\\b|\\b0x[a-aA-F_0-9]..*\\h", Pattern.CASE_INSENSITIVE)
					.matcher(textmatch);
			while (match4.find()) {
				updateTextColor(match4.start(), match4.end() - match4.start(), new Color(255, 128, 0));
			}
		}

		Matcher match5 = Pattern.compile(";(.*)", Pattern.CASE_INSENSITIVE).matcher(textmatch);
		while (match5.find()) {
			updateTextColor(match5.start(), match5.end() - match5.start(), new Color(0, 128, 0));
		}

		Matcher match6 = Pattern.compile(";PROC(.*)", Pattern.CASE_INSENSITIVE).matcher(textmatch);
		while (match6.find()) {
			updateTextColor(match6.start(), match6.end() - match6.start(), Color.red);
			boldtext(match6.start(), match6.end() - match6.start());
		}

		Matcher match7 = Pattern.compile("offset|byte|#|,|:", Pattern.CASE_INSENSITIVE).matcher(textmatch);
		while (match7.find()) {

			updateTextColor(match7.start(), match7.end() - match7.start(), new Color(0, 0, 128));
			boldtext(match7.start(), match7.end() - match7.start());
		}

		Matcher match8 = Pattern.compile("\\\".*\\\"|ptr|;;(.*)", Pattern.CASE_INSENSITIVE).matcher(textmatch);
		while (match8.find()) {
			updateTextColor(match8.start(), match8.end() - match8.start(), new Color(128, 128, 128));
		}

	}

	public void updateTextColor(int offset, int length, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
		textpane.getStyledDocument().setCharacterAttributes(offset, length, aset, true);
	}

	public void clearTextColors() {
		updateTextColor(0, textpane.getText().length(), Color.black);
	}

	public void updateTextColor(int offset, int length) {
		updateTextColor(offset, length, Color.GREEN);
	}

	public void boldtext(int offset, int length) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Bold, true);
		textpane.getStyledDocument().setCharacterAttributes(offset, length, aset, false);
	}

	public void updateForeColor(int offset, int length, Color c) {
		StyleContext sc = StyleContext.getDefaultStyleContext();
		AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, c);
		textpane.getStyledDocument().setCharacterAttributes(offset, length, aset, false);
	}

	public void undo() {
		try {
			undoMgr.undo();
		} catch (Exception e) {

		}
	}

	public void redo() {
		try {
			undoMgr.redo();
		} catch (Exception e) {

		}
	}

	public void saveDocument() {
		Date date = new Date();

		try {
			FileWriter fw = new FileWriter(new File("program.aasm"), false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(textpane.getText());
			bw.close();
			AramokIICompiler.R.build.notify("Document Saved" + "\n");

		} catch (Exception ex) {
			System.out.println(file + "");
			AramokIICompiler.R.build.showerror("Saving Document error\n" + "\n");
		}
	}

	public HighlightLineTextPane getTextpane() {
		return textpane;
	}

	public void backupDocument() {

		Date date = new Date();

		try {
			FileWriter fw = new FileWriter(new File("backups/" + date.getTime() + ".aasm"), false);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(textpane.getText());
			bw.close();
			AramokIICompiler.R.build.notify("Backup Document done" + "\n" + System.getProperty("user.home") + "backups/"
					+ date.getTime() + ".aasm\n\n");

		} catch (Exception ex) {
			AramokIICompiler.R.build.showerror("Document backup error: \t" + file + "\n");
		}
	}

	public void recoverDocument() {
		try {
			BufferedReader reader;
			JFileChooser path = new JFileChooser("backups");
			path.setFileSelectionMode(JFileChooser.FILES_ONLY);

			if (path.showOpenDialog(AramokIICompiler.R) == JFileChooser.APPROVE_OPTION) {
				System.out.println(path.getSelectedFile() + "");
				file = path.getSelectedFile();
				String content = "";
				String line;

				if (!file.exists()) {
					file.createNewFile();
				}
				reader = new BufferedReader(new FileReader(file));
				line = reader.readLine();
				while (line != null) {
					content += line + System.lineSeparator();
					line = reader.readLine();
				}
				textpane.setText(content);
				Colorize();
				AramokIICompiler.R.build.notify("File Recovered \n");
			}

		} catch (FileNotFoundException ex) {
			AramokIICompiler.R.build.showerror("Recover backup error: \t" + file + "\n");
		} catch (IOException ex) {
			AramokIICompiler.R.build.showerror("Recover backup error: \t" + file + "\n");
		}

	}

	private String getCurrentEditLine(HighlightLineTextPane scriptEditor) {
		int readBackChars = 100;
		int caretPosition = scriptEditor.getCaretPosition();

		if (caretPosition == 0) {
			return null;
		}
		StyledDocument doc = scriptEditor.getStyledDocument();
		int offset = caretPosition <= readBackChars ? 0 : caretPosition - readBackChars;

		String text = null;
		try {
			text = doc.getText(offset, caretPosition);
		} catch (BadLocationException e) {
		}
		if (text != null) {
			int idx = text.lastIndexOf("\n");
			if (idx != -1) {
				return text.substring(idx);
			} else {
				return text;
			}
		}
		return null;
	}

	public void resetprocs() {
		menu = new JPopupMenu();
	}
}
