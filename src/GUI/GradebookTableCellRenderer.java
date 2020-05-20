/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Eric
 */
public class GradebookTableCellRenderer implements TableCellRenderer {

    private DefaultTableCellRenderer m_renderer = null;
    
    public GradebookTableCellRenderer(DefaultTableCellRenderer renderer) {
        if (renderer == null)
            throw new IllegalArgumentException("Renderer cannot be null!");
        
        m_renderer = renderer;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                
        DefaultTableCellRenderer c = (DefaultTableCellRenderer)m_renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setHorizontalAlignment(JLabel.CENTER);

        /*if (!isSelected) {
            if (row % 2 == 1)
                c.setBackground(new Color(0,73,105,20));
            else
                c.setBackground(new Color(255, 255, 255));
        }*/

        return c;
    }
    
}
