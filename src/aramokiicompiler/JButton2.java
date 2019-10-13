package aramokiicompiler;

import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author Aramok
 */
public class JButton2 extends JButton {

    public JButton2(ImageIcon icon, ActionListener a) {
        super(icon);
        addActionListener(a);
    }

    public JButton2(String name, ImageIcon icon, ActionListener a) {
        super(name, icon);
        addActionListener(a);
    }

}
