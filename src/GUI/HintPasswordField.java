package GUI;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPasswordField;
import javax.swing.Timer;

/**
 *
 * @author Eric
 */
public class HintPasswordField extends ValidationField implements FocusListener {
    
    private String m_hint;
    private boolean m_showingHint;
    
    public HintPasswordField() {
        this(null);
    }

    public HintPasswordField(String hint) {
        m_hint = hint;
        m_showingHint = hint != null;
        super.addFocusListener(this);
    }

    public void setHint(String hint) {
        m_hint = hint;
        m_showingHint = hint != null;
        focusLost(null);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            m_showingHint = false;
            setPasswordChar('\u25cf');
            setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(m_hint);
            m_showingHint = true;
            setPasswordChar((char)0);
            setForeground(Color.GRAY);
        }
    }

    @Override
    public String getText() {
        return m_showingHint ? "" : super.getText();
    }
}
