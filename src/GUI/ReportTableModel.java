/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class ReportTableModel extends DefaultTableModel {
        
    private boolean m_selectable = false;
    private int m_selectIndex = 0;
    
    public ReportTableModel(Object[][] data, Object[] columnNames, boolean selectable) {
        super(data, columnNames);
        
        m_selectable = selectable;
        if (m_selectable) {
            Object[] selectColumn = new Object[data.length];
            for (int i = 0; i < selectColumn.length; i++)
                selectColumn[i] = true;
            addColumn("", selectColumn);
            m_selectIndex = data[0].length;
        }
        
        m_selectable = selectable;
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (m_selectable && columnIndex == m_selectIndex)
            return Boolean.class;
        return super.getColumnClass(columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return (m_selectable && col == m_selectIndex); 
    }
}
