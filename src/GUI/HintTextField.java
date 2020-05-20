package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.imageio.ImageIO;
import javax.swing.JTextField;

/**
 *
 * @author Eric
 */
public class HintTextField extends ValidationField implements FocusListener {

    private String m_hint;
    private boolean m_showingHint;
    
    public HintTextField() {
        super.addFocusListener(this);
    }

    public HintTextField(String hint) {
        m_hint = hint;
        m_showingHint = true;
        super.addFocusListener(this);
    }

    public void setHint(String hint) {
        m_hint = hint;
        m_showingHint = true;
        focusLost(null);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText("");
            m_showingHint = false;
            
            setForeground(Color.BLACK);
        }
    }
    @Override
    public void focusLost(FocusEvent e) {
        if(this.getText().isEmpty()) {
            super.setText(m_hint);
            m_showingHint = true;
            
            setForeground(Color.GRAY);
        }
    }

    @Override
    public String getText() {
        return m_showingHint ? "" : super.getText();
    }
    
    @Override
    public void setText(String text) {
        if (text.length() > 0) {
            m_showingHint = false;
            setForeground(Color.BLACK);
        }
        else {
            m_showingHint = true;
            super.setText(m_hint);
            setForeground(Color.GRAY);
        }
        super.setText(text);
    }
}
