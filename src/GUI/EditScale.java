/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Data.Chart;
import Data.Data;
import static Data.Data.Type.Chart;
import Data.DataFactory;
import Gradebook.Course;
import Gradebook.Gradebook;
import Gradebook.IGradeable;
import Gradebook.Scale;
import java.awt.BorderLayout;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Tom
 */
public class EditScale extends javax.swing.JDialog {
    private double[] m_scale = {0,0,0,0};
    private IGradeable m_gradeable;
    private ChartPanel m_chartPanel;
    private Gradebook m_gradebook;
    private Scale m_scaleObj;
    private Course m_course;
    
    /**
     * Creates new form EditScale
     */
    public EditScale(java.awt.Frame parent, boolean modal, IGradeable gradeable, Course course, Gradebook gradebook) {
        super(parent, modal);
        initComponents();
        m_gradeable = gradeable;
        m_gradebook = gradebook;
        m_course = course;
        m_scaleObj = gradeable.getScale();
        Scale tempScale = course.getScale();
        if (tempScale != null) {
            courseAField.setText(Double.toString(tempScale.getA()));
            courseBField.setText(Double.toString(tempScale.getB()));
            courseCField.setText(Double.toString(tempScale.getC()));
            courseDField.setText(Double.toString(tempScale.getD()));
            scaleNameField.setText(tempScale.getName());
        }
    /*************************************************************************/
    /*                       Text Box Validators                             */
    /*************************************************************************/
        courseAField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    try{
                        double value = Double.parseDouble(text);
                        m_scale[0] = value;
                        if(value > m_scale[1]){
                            return true;
                        }else{
                            return false;
                        }
                    }catch(NumberFormatException e){
                        errorID = 2;
                        return false;
                    }
                }
            }
            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing A";
                    case 2: return  "Invalid A";
                    default: return "A <= B";
                }
            }
        });
        courseBField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    try{
                        double value = Double.parseDouble(text);
                        m_scale[1] = value;
                        if(value > m_scale[2]){
                            return true;
                        }else{
                            return false;
                        }
                    }catch(NumberFormatException e){
                        errorID = 2;
                        return false;
                    }
                }
            }
            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing B";
                    case 2: return  "Invalid B";
                    default: return "B <= C";
                }
            }
        });
        courseCField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    try{ 
                        double value = Double.parseDouble(text);
                        m_scale[2] = value;
                        if(value > m_scale[3]){
                            return true;
                        }else{
                            return false;
                        }
                    }catch(NumberFormatException e){
                        errorID = 2;
                        return false;
                    }
                }
            }
            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing C";
                    case 2: return  "Invalid C";
                    default: return "C <= D";
                }
            }
        });
        courseDField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    try{ 
                        double value = Double.parseDouble(text);
                        m_scale[3] = value;
                        if(value > 0){
                            return true;
                        }else{
                            return false;
                        }
                    }catch(NumberFormatException e){
                        errorID = 2;
                        return false;
                    }
                }
            }
            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing D";
                    case 2: return  "Invalid D";
                    default: return "D < 0";
                }
            }
        });
        scaleNameField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    for(Scale scale : m_course.getScales()){
                        if(scale.getName().equals(text)){
                            return false;
                        }
                    }
                    return true;
                }
            }
            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing Name";
                    default: return "Name Taken";
                }
            }
        });
        
        Data data = DataFactory.getGradeDistributionChart(gradeable);
        displayPanel.add(data.getComponent(), BorderLayout.CENTER);
    }
    
    public void setData(IGradeable gradeable) {
        
        displayPanel.removeAll();
        displayPanel.revalidate();
        Data data = DataFactory.getGradeDistributionChart(gradeable);
        displayPanel.add(data.getComponent(), BorderLayout.CENTER);
        displayPanel.repaint();
    }
    
    private boolean validateFields() {
        boolean result = (courseDField.runValidation() && courseCField.runValidation() && 
                courseBField.runValidation() && courseAField.runValidation());
        
        if (result) {
            IGradeable gradeable = m_gradeable.getCopy();
            gradeable.setScale(m_scale);
            setData(gradeable);
        }
        
        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        courseAField = new GUI.HintTextField();
        courseBField = new GUI.HintTextField();
        courseCField = new GUI.HintTextField();
        courseDField = new GUI.HintTextField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();
        displayPanel = new javax.swing.JPanel();
        prevScaleButton = new javax.swing.JButton();
        addScaleButton = new javax.swing.JButton();
        scaleNameField = new GUI.HintTextField();
        scaleMessageField = new javax.swing.JTextField();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Scale");
        setResizable(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Scale", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, java.awt.Color.black));

        jLabel1.setText("A");

        jLabel2.setText("B");

        jLabel5.setText("%");

        jLabel6.setText("%");

        jLabel7.setText("%");

        jLabel8.setText("%");

        jLabel3.setText("C");

        jLabel4.setText("D");

        courseAField.setText("90.0");
        courseAField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseAFieldActionPerformed(evt);
            }
        });

        courseBField.setText("80.0");
        courseBField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseBFieldActionPerformed(evt);
            }
        });

        courseCField.setText("70.0");
        courseCField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseCFieldActionPerformed(evt);
            }
        });

        courseDField.setText("60.0");
        courseDField.setToolTipText("");
        courseDField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseDFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(courseAField, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(courseBField, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(courseCField, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jLabel7))
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(courseDField, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addComponent(jLabel4))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(courseAField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseBField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseCField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(courseDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(13, 13, 13))
        );

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        displayPanel.setLayout(new java.awt.BorderLayout());

        prevScaleButton.setText("Import Scale..");
        prevScaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prevScaleButtonActionPerformed(evt);
            }
        });

        addScaleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Add.png"))); // NOI18N
        addScaleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addScaleButtonActionPerformed(evt);
            }
        });

        scaleNameField.setToolTipText("");

        scaleMessageField.setEditable(false);
        scaleMessageField.setForeground(new java.awt.Color(0, 204, 0));
        scaleMessageField.setBorder(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(prevScaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scaleNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addScaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scaleMessageField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(displayPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prevScaleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addScaleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(scaleMessageField)
                    .addComponent(scaleNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        if(validateFields()){
            this.dispose();
        }
    }//GEN-LAST:event_okButtonActionPerformed

    private void courseAFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseAFieldActionPerformed
        boolean result = validateFields();
        m_gradeable.setScale(m_scale);
        setData(m_gradeable);
    }//GEN-LAST:event_courseAFieldActionPerformed

    private void courseBFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseBFieldActionPerformed
        boolean result = validateFields();
        m_gradeable.setScale(m_scale);
        setData(m_gradeable);
    }//GEN-LAST:event_courseBFieldActionPerformed

    private void courseCFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseCFieldActionPerformed
        boolean result = validateFields();
        m_gradeable.setScale(m_scale);
        setData(m_gradeable);
    }//GEN-LAST:event_courseCFieldActionPerformed

    private void courseDFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseDFieldActionPerformed
        boolean result = validateFields();
        m_gradeable.setScale(m_scale);
        setData(m_gradeable);
    }//GEN-LAST:event_courseDFieldActionPerformed

    private void prevScaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prevScaleButtonActionPerformed
        PreviousScale prevScale = new PreviousScale(null, true, m_gradeable, m_gradebook);
        prevScale.setLocationRelativeTo(null);
        prevScale.setVisible(true);
        if(prevScale.getScale() != null){
            Scale tempScale = prevScale.getScale();
            courseAField.setText(Double.toString(tempScale.getA()));
            courseBField.setText(Double.toString(tempScale.getB()));
            courseCField.setText(Double.toString(tempScale.getC()));
            courseDField.setText(Double.toString(tempScale.getD()));
            scaleNameField.setText(tempScale.getName());
        }
    }//GEN-LAST:event_prevScaleButtonActionPerformed

    private void addScaleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addScaleButtonActionPerformed
        if(courseAField.runValidation() && courseBField.runValidation() && courseCField.runValidation() && courseDField.runValidation() & scaleNameField.runValidation()){
            double[] values = {Double.parseDouble(courseAField.getText()),
                                Double.parseDouble(courseBField.getText()),
                                Double.parseDouble(courseCField.getText()),
                                Double.parseDouble(courseDField.getText())
                                };
            Scale newScale = new Scale(values);
            newScale.setName(scaleNameField.getText());
            m_course.addScale(newScale);
            scaleMessageField.setText("Scale Added");
        }else{
            return;
        }
    }//GEN-LAST:event_addScaleButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addScaleButton;
    private javax.swing.JButton cancelButton;
    private GUI.HintTextField courseAField;
    private GUI.HintTextField courseBField;
    private GUI.HintTextField courseCField;
    private GUI.HintTextField courseDField;
    private javax.swing.JPanel displayPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton prevScaleButton;
    private javax.swing.JTextField scaleMessageField;
    private GUI.HintTextField scaleNameField;
    // End of variables declaration//GEN-END:variables
}
