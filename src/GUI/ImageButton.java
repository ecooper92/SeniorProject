/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JButton;

/**
 *
 * @author Eric
 */
public class ImageButton extends JButton {
    private static final long serialVersionUID = 1L;

    private boolean m_mouseDown = false;
    private boolean m_mouseOver = false;
    private Image m_Image = null;
    private GradientPaint m_normalPaint = null;
    private GradientPaint m_overPaint = null;
    private GradientPaint m_downPaint = null;
    private GradientPaint m_disabledPaint = null;
    private GradientPaint m_paint = null;
    
    public ImageButton() {
        super();

        setContentAreaFilled(false);
        int height = getHeight();
        
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) { }

            @Override
            public void mousePressed(MouseEvent e) { 
                m_mouseDown = true;
                m_paint = m_downPaint;
            }

            @Override
            public void mouseReleased(MouseEvent e) { 
                m_mouseDown = false;
                if (m_mouseOver)
                    m_paint = m_overPaint;
                else
                    m_paint = m_normalPaint;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                m_mouseOver = true;
                if (!m_mouseDown)
                    m_paint = m_overPaint;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                m_mouseOver = false;
                if (!m_mouseDown)
                    m_paint = m_normalPaint;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        if (m_paint == null) {
            m_normalPaint = new GradientPaint( 0, 0, new Color(0,150,190), 0, getHeight(), new Color(0,73,105) );
            m_overPaint = new GradientPaint( 0, 0, new Color(0,150,190), 0, getHeight(), new Color(30,110,143) );
            m_downPaint = new GradientPaint( 0, 0, new Color(0,92,120), 0, getHeight(), new Color(30,110,143) );
            m_disabledPaint = new GradientPaint( 0, 0, new Color(159,159,169), 0, getHeight(), new Color(89,89,105) );
            m_paint = m_normalPaint;
        }
        
        
        GradientPaint brush = m_paint;
        if (!isEnabled())
            brush = m_disabledPaint;
        
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint( brush );
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10 );
        
        int xPadding = 10;
        int imageWidth = 0;
        if (m_Image != null) {
            imageWidth += m_Image.getWidth(this) + xPadding;
            g2d.drawImage(m_Image, xPadding, getHeight() / 2 - (m_Image.getHeight(this) / 2), this);
        }
        
        String text = getText();
        FontMetrics fm = g.getFontMetrics(getFont());
        Rectangle2D rect = fm.getStringBounds(text, g);
        
        g2d.setColor(Color.white);
        g2d.setFont(getFont());
        g2d.drawString(getText(), (float)((((getWidth() - imageWidth) / 2.0) + imageWidth) - rect.getWidth() / 2.0), (float)((getHeight() / 2.0) + (rect.getHeight() / 4.0)));
    }
    
    public void setImage(Image image) {
        m_Image = image;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += size.height;
        return size;
    }
}
