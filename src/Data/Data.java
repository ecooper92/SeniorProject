/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.awt.Component;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Eric
 */
public abstract class Data {

    public enum Type {

	Chart, Report
    };
    protected Data.Type m_Type;

    public Data.Type getType() {
	return m_Type;
    }
    
    public abstract Component getComponent();
    
    public abstract FileNameExtensionFilter getExportFilter();

    public abstract void export(String filePath) throws IOException;

//    public abstract void print(BufferedImage image);
    public abstract void print();
}
