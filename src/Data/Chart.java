/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.jfree.util.UnitType;

/**
 *
 * @author Eric
 */
public class Chart extends Data {

    private ArrayList<Object> m_xValues = new ArrayList<>();
    private ArrayList<Number> m_yValues = new ArrayList<>();
    private ChartPanel m_chartPanel = null;

    public Chart(ArrayList<Object> xValues, ArrayList<Number> yValues) {
	m_xValues = xValues;
	m_yValues = yValues;
	m_Type = Type.Chart;
    }

    public ArrayList<Object> getXValues() {
	return m_xValues;
    }

    public ArrayList<Number> getYValues() {
	return m_yValues;
    }

    @Override
    public void export(String filePath) throws IOException {
        if (m_chartPanel == null)
            getComponent();
        
        String path = filePath;
        if (!path.endsWith(".png")) {
            path += ".png";
        }
        
        ChartUtilities.saveChartAsPNG(new File(path), m_chartPanel.getChart(), 400, 400);
    }

    @Override
//    public void print(BufferedImage image) {
    public void print() {
        if (m_chartPanel == null)
            getComponent();
        
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setJobName("Gradebook Report");
        printJob.setPrintable(m_chartPanel);    
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
        return new FileNameExtensionFilter("PNG Image", ".png");
    }

    @Override
    public Component getComponent() {
        
        if (m_xValues.size() == m_yValues.size()) {

            DefaultCategoryDataset ds = new DefaultCategoryDataset();

            for (int i = 0; i < m_xValues.size(); i++) {
                ds.addValue(m_yValues.get(i), "Grade", m_xValues.get(i).toString());
            }

            JFreeChart c = ChartFactory.createBarChart("", "", "", ds, PlotOrientation.VERTICAL, false, true, false);
            c.setBackgroundPaint(new Color(0, 0, 0, 0));

            CategoryPlot plot = c.getCategoryPlot();
            plot.setBackgroundPaint(Color.white);
            
            plot.getRenderer().setSeriesPaint(0, new Color(0,73,105));
            plot.getRenderer().setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
            plot.getRenderer().setBaseItemLabelPaint(Color.WHITE);
            plot.getRenderer().setBasePositiveItemLabelPosition(new ItemLabelPosition( ItemLabelAnchor.CENTER, TextAnchor.BOTTOM_CENTER));
            plot.getRenderer().setBaseItemLabelsVisible(true);
            plot.getRangeAxis().setVisible(false);
            //plot.getRangeAxis().setInsets(new RectangleInsets(UnitType.ABSOLUTE, 0, 0, 25, 25));

            m_chartPanel = new ChartPanel(c);
            
            return m_chartPanel;
        }
        return null;
    }
}
