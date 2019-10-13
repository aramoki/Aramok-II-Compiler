package aramokiicompiler;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JSplitPane;

/**
 *
 * @author Aramok
 */
public class NavMenu extends MenuBar {

	Menu files, builder, editor;

	ActionListener save, build, undo, redo, inc_font, dec_font, ascii, recover, backup;

	public NavMenu() {
		add(editor = new Menu("Editor"));
		editor.add(new MenuItem("Undo", new MenuShortcut(KeyEvent.VK_Z)))
				.addActionListener(undo = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AramokIICompiler.R.getCode().undo();
					}
				});
		editor.add(new MenuItem("Redo", new MenuShortcut(KeyEvent.VK_Z)))
				.addActionListener(redo = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AramokIICompiler.R.getCode().redo();
					}
				});
		editor.add(new MenuItem("Increase font")).addActionListener(inc_font = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AramokIICompiler.R.getCode().setfontsize(+1);
			}
		});
		editor.add(new MenuItem("Decrease Font")).addActionListener(dec_font = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AramokIICompiler.R.code.setfontsize(-1);
			}
		});
		add(files = new Menu("Edit"));
		files.add(new MenuItem("Backup", new MenuShortcut(KeyEvent.VK_M, true)))
				.addActionListener(backup = new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						AramokIICompiler.R.code.backupDocument();
					}
				});
		files.add(new MenuItem("Recover", new MenuShortcut(KeyEvent.VK_N, true)))
				.addActionListener(recover = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AramokIICompiler.R.code.recoverDocument();
					}
				});
		files.add(new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S))).addActionListener(save = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AramokIICompiler.R.code.saveDocument();
			}
		});
		add(builder = new Menu("Build"));
		builder.add(new MenuItem("Build Project", new MenuShortcut(KeyEvent.VK_R)))
				.addActionListener(build = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						AramokIICompiler.R.code.saveDocument();
						AramokIICompiler.R.build.compile();
						AramokIICompiler.R.build.saveDocument();
					}
				});
		ascii = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ASCII asci = new ASCII();
			}
		};

	}

}
