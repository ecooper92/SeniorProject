/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author Eric
 */
public class ValidationField extends JPasswordField {
    
    private Image m_validationImage = null;
    private ArrayList<Validator> m_validators = new ArrayList<>();
    private boolean m_isValid = true;
    private Validator m_failedValidator = null;
    
    private enum FadeState { In, Out, Stall, Off };
    private FadeState m_invalidState = FadeState.Off;
    private double m_invalidOpacity = 0.0;
    private Timer m_invalidTimer;
    
    public ValidationField() {
        super();
        
        setEchoChar((char)0);
        
        try {
            m_validationImage = ImageIO.read(this.getClass().getResource("resources/warning.png"));
        }
        catch (Exception e) {}
        
        ActionListener timerListener = new ActionListener() {

            private int m_stallCounter = 0;
            public void actionPerformed(ActionEvent e) {

                if (m_invalidState == FadeState.In) {
                    if (m_invalidOpacity < 100) {
                        m_invalidOpacity += 7.5;
                        if (m_invalidOpacity > 100)
                            m_invalidOpacity = 100;
                    }
                    else {
                        m_invalidState = FadeState.Stall;
                        m_stallCounter = 0;
                    }
                }
                else if (m_invalidState == FadeState.Stall) {
                    if (m_stallCounter++ > 100)
                        m_invalidState = FadeState.Out;
                }
                else if (m_invalidState == FadeState.Out) {
                    if (m_invalidOpacity > 0) {
                        m_invalidOpacity -= 7.5;
                        if (m_invalidOpacity < 0)
                            m_invalidOpacity = 0;
                    }
                    else {
                        m_invalidState = FadeState.Off;
                        m_invalidTimer.stop();
                    }
                }

                repaint();
            }
        };
        
        m_invalidTimer = new Timer(15, timerListener);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D)g;
        if (!m_isValid) {
            if (m_invalidState != FadeState.Off) {
                int width = getWidth();
                int height = getHeight();
                
                float opacity = (float)(m_invalidOpacity / 100);
                g2d.setColor(new Color(0.55f, 0.55f, 0.55f, opacity));
                g2d.fillRect(0, 0, width, height);
                
                g2d.setColor(new Color(0.9f, 0, 0, opacity));
                g2d.drawRect(1, 1, width - 3, height - 3);
                
                if (m_failedValidator != null) {
                    String error = m_failedValidator.getMessage();
                    FontMetrics fm = g2d.getFontMetrics();
                    g2d.setFont(getFont());
                    Rectangle2D rect = fm.getStringBounds(error, g2d);
                    
                    g2d.setColor(new Color(1f, 1f, 1f, opacity));
                    g2d.drawString(error, 5, (float)(getHeight() / 2 + rect.getHeight() / 4));
                }
            }
            
            float imageWidth = m_validationImage.getWidth(this);
            float imageHeight = m_validationImage.getHeight(this);

            g2d.drawImage(m_validationImage, (int)(getWidth() - imageWidth - 5), (int)(getHeight() / 2 - imageHeight / 2),this);
        }
    }
    
    @Override
    public String getToolTipText(MouseEvent evt) {
        if (!m_isValid && m_failedValidator != null) {
            if (evt.getPoint().x >= (getWidth() - m_validationImage.getWidth(this) - 10) && evt.getPoint().x <= getWidth()) {
                return m_failedValidator.getMessage();
            }
        }
        return "";
    }
    
    public void addValidator(Validator v) {
        if (v != null) {
            m_validators.add(v);
        }
    }
    
    public boolean runValidation() {
        for (Validator validator : m_validators) {
            m_isValid = validator.isValid(getText());
            if (!m_isValid) {
                m_failedValidator = validator;
                setMargin(new Insets(0, 2, 0, m_validationImage.getWidth(this) + 7));
                
                m_invalidState = FadeState.In;
                m_invalidTimer.start();
                
                repaint();
                return false;
            }
        }
        
        m_failedValidator = null;
        m_isValid = true;
        setMargin(new Insets(0, 2, 0, 0));
        repaint();
        return true;
    }
    
    public void setPasswordChar(char passwordChar) {
        setEchoChar(passwordChar);
    }
}
