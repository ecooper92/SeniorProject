/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import GUI.GradebookTableCellRenderer;
import GUI.ReportTableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.DefaultRowSorter;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author Eric
 */
public class Report extends Data implements Printable{

    private Object[] m_Headers = new Object[0];
    private Object[][] m_Data = new Object[0][0];
    private JTable m_Table = null;
    private boolean m_selectable = false;

    public Report(Object[] headers, Object[][] data, boolean selectable) {
	m_Headers = headers;
	m_Data = data;
	m_Type = Type.Report;
        m_selectable = selectable;
    }

    /**
     *
     * @param filePath
     * @throws java.io.IOException
     */
    @Override
    public void export(String filePath) throws IOException {
	try {

	    String path = filePath;
	    if (!path.endsWith(".csv")) {
		path += ".csv";
	    }
            
            RowSorter r = m_Table.getRowSorter();
            TableModel model = m_Table.getModel();
            int columns = m_Headers.length;
            ArrayList<Object[]> sortedData = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                
                boolean includeRow = true;
                if (m_selectable)
                    includeRow = (boolean)model.getValueAt(r.convertRowIndexToView(i), columns);
                
                if (includeRow) {
                    Object[] row = new Object[columns];
                    for (int j = 0; j < columns; j++) {
                        row[j] = model.getValueAt(r.convertRowIndexToView(i), j);
                    }
                    sortedData.add(row);
                }
            }

	    FileWriter writer = new FileWriter(path);
	    for (int i = 0; i < sortedData.size(); i++) {
		for (int j = 0; j < sortedData.get(i).length; j++) {
		    writer.write(sortedData.get(i)[j].toString());
		    if (j < sortedData.get(i).length - 1) {
			writer.write(",");
		    }
		}
		writer.write("\n");
	    }
            writer.flush();
            writer.close();

	} catch (IOException ex) {
	    throw new IOException(ex);
	}
    }

    public Object[] getHeaders() {
	return m_Headers;
    }

    public Object[][] getData() {
	return m_Data;
    }

    @Override
    public void print() {
        
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setJobName("Gradebook Report");
        printJob.setPrintable(this);    
        if(printJob.printDialog()) {
            try
            {
                printJob.print(new HashPrintRequestAttributeSet());
            }catch(PrinterException pe)
            {
            }
        }
    }

    @Override
    public FileNameExtensionFilter getExportFilter() {
        return new FileNameExtensionFilter("CSV", ".csv");
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        
        if (m_Table == null)
            getComponent();
        
        float x = (float)pageFormat.getImageableX();
        float y = (float)pageFormat.getImageableY();
        float width = (float)pageFormat.getImageableWidth();
        float height = (float)pageFormat.getImageableHeight();
        
        RowSorter r = m_Table.getRowSorter();
        TableModel model = m_Table.getModel();
        int columns = m_Headers.length;
        if (columns > 0) {
            
            float columnWidth = width / columns;
            float rowHeight = 20;
            float headerHeight = rowHeight + 5;
            Graphics2D g2d = (Graphics2D)g;

            Font titleFont = new Font("Segoe UI", Font.PLAIN, 14);
            g2d.setFont(titleFont);
            
            FontMetrics fm = g2d.getFontMetrics();
            
            g2d.setColor(Color.black);
            for (int i = 0; i < columns; i++) {
                
                String text = m_Headers[i].toString();
                Rectangle2D rect = fm.getStringBounds(text, g);
                
                float newX = (float)((columnWidth / 2) - rect.getWidth() / 2) + columnWidth * i + x;
                float newY = y + rowHeight;
                g2d.drawString(text, newX, newY);
            }
            g2d.drawLine((int)x, (int)(y + headerHeight), (int)(x + width), (int)(y + headerHeight));
            
            ArrayList<Object[]> sortedData = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                
                boolean includeRow = true;
                if (m_selectable)
                    includeRow = (boolean)model.getValueAt(r.convertRowIndexToView(i), columns);
                
                if (includeRow) {
                    Object[] row = new Object[columns];
                    for (int j = 0; j < columns; j++) {
                        row[j] = model.getValueAt(r.convertRowIndexToView(i), j);
                    }
                    sortedData.add(row);
                }
            }
            
            Font font = new Font("Segoe UI", Font.PLAIN, 12);
            g2d.setFont(font);
            for (int i = 0; i < sortedData.size(); i++) {
                for (int j = 0; j < sortedData.get(i).length; j++) {
                    
                    String text = sortedData.get(i)[j].toString();
                    Rectangle2D rect = fm.getStringBounds(text, g);
                    
                    float newX = (float)((columnWidth / 2) - rect.getWidth() / 2) + columnWidth * j + x;
                    float newY = y + rowHeight * (i + 1) + headerHeight;
                    g2d.drawString(text, newX, newY);
                }
            }
        }

        return Printable.PAGE_EXISTS;
    }

    @Override
    public Component getComponent() {
            
        Object[][] data = getData();
        Object[] headers = getHeaders();
        
        ReportTableModel model = new ReportTableModel(data, headers, m_selectable);
        m_Table = new JTable(model);
        m_Table.setAutoCreateRowSorter(true);

        JTableHeader header = m_Table.getTableHeader();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        m_Table.setGridColor(new Color(0,73,105,20));
        GradebookTableCellRenderer cell = new GradebookTableCellRenderer((DefaultTableCellRenderer)m_Table.getDefaultRenderer(Object.class));
        m_Table.getColumnModel().getColumn(0).setCellRenderer(cell);
        
        if (m_selectable) {
            m_Table.getColumnModel().getColumn(m_Table.getColumnCount()-1).setPreferredWidth(10);
            m_Table.moveColumn(m_Table.getColumnCount()-1, 0);
        }

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(m_Table);
        return scrollPane;
    }
}
