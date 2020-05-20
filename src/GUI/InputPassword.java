/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Exceptions.InvalidPasswordException;
import Exceptions.SerializationException;
import Gradebook.Gradebook;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author cmh0034
 */
public class InputPassword extends javax.swing.JDialog {

    /**
     * Creates new form InputPassword
     * @param parent
     * @param modal
     */
    public InputPassword(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        passwordField.addValidator(new Validator() {

            String message = "";
            @Override
            public boolean isValid(String text) {
                try {
                    m_gradebook = Gradebook.load(passwordField.getText(), m_filePath);
                    return true;
                }
                catch (InvalidPasswordException e) {
                    message = "Incorrect Password";
                }
                catch (SerializationException e) {
                    message = "Error reading gradebook";
                    m_gradebook = null;
                }
                catch (IOException e) {
                    message = "Unable to open gradebook";
                    m_gradebook = null;
                }
                return false;
            }

            @Override
            public String getMessage() {
                return message;
            }
        });
        
        SwingUtilities.getRootPane(okButton1).setDefaultButton(okButton1);
        setLocationRelativeTo(null);
    }
    
    private String m_filePath = "";
    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }
    
    private Gradebook m_gradebook;
    public Gradebook getGradebook() {
        return m_gradebook;
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
        passwordField = new GUI.HintPasswordField();
        okButton1 = new GUI.ImageButton();
        cancelButton1 = new GUI.ImageButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Password");

        okButton1.setText("Ok");
        okButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        okButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButton1ActionPerformed(evt);
            }
        });

        cancelButton1.setText("Cancel");
        cancelButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Grade Book Password");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setText("<html>  Enter the password for the selected Grade Book.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(passwordField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton1ActionPerformed
        
        if (passwordField.runValidation()) {
            this.setVisible(false);
        }
    }//GEN-LAST:event_okButton1ActionPerformed

    private void cancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton1ActionPerformed
        m_gradebook = null;
        setVisible(false);
    }//GEN-LAST:event_cancelButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private GUI.ImageButton cancelButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private GUI.ImageButton okButton1;
    private GUI.HintPasswordField passwordField;
    // End of variables declaration//GEN-END:variables
}
