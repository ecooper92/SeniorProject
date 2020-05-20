package GUI;

import Gradebook.Gradebook;
import Gradebook.Student;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.stream.XMLStreamConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cmh0034
 */
public class EditStudent extends javax.swing.JDialog {
    private Gradebook m_gradebook;
    DefaultListModel m_studentListModel = new DefaultListModel();
    private ArrayList<Student> m_newStudents = new ArrayList<>();
    /**
     * Creates new form EditStudent
     */
    public EditStudent(java.awt.Frame parent, boolean modal, Gradebook gradebook) {
        super(parent, modal);
        initComponents();
        
        studentNameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                acceptStudentButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                acceptStudentButton.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        
        m_gradebook = gradebook;
        for (Student student : gradebook.getStudents()) {
            m_studentListModel.addElement(student);
        }
        
        studentList.setModel(m_studentListModel);
        
        studentIDField.focusLost(null);
        studentNameField.focusLost(null);
        acceptStudentButton.setEnabled(false);
        cancelButton.requestFocus();
        
        /********************************************************************/
        studentIDField.addValidator(new Validator() {
            private int errorID;
            ArrayList<Student> students = m_gradebook.getStudents();
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty()){
                    errorID = 1;
                    return false;
                }else if(m_gradebook.getStudent(text) != null){
                    errorID = 2;
                    return false;
                }else{
                    return text.matches("^[a-zA-Z][0-9]*");
                }
            }
            

            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing A#";
                    case 2: return  "ID is Taken";
                    default: return "Invalid ID. Format: A#";
                }
            }
        });
        studentNameField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                String text2 = text.trim();
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                
                }else if(text2.matches(".*\\d.*")){
                    return false;
                }else{
                    return (text.matches("^[a-zA-Z]* [a-zA-Z]*$") || text.matches("^[a-zA-Z]* [a-zA-Z]* [a-zA-Z]*$"));
                }
            }
            

            @Override
            public String getMessage() {
                switch(errorID){
                    case 1: return  "Missing Name.";
                    default: return "Illegal Name";
                }
            }
        });
        /********************************************************************/
        addStudentButton.requestFocus();
    }
    

    public ArrayList<Student> getNewStudents() {
        return m_newStudents;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        studentNameField = new GUI.HintTextField("Student Name");
        studentIDField = new GUI.HintTextField("Student ID");
        jScrollPane1 = new javax.swing.JScrollPane();
        studentList = new javax.swing.JList();
        acceptStudentButton = new javax.swing.JButton();
        addStudentButton = new javax.swing.JButton();
        okButton = new GUI.ImageButton();
        cancelButton = new GUI.ImageButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gradebook Students");
        setResizable(false);

        studentNameField.setToolTipText("");
        studentNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentNameFieldActionPerformed(evt);
            }
        });

        studentIDField.setToolTipText("");
        studentIDField.setEnabled(false);

        studentList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                studentListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(studentList);

        acceptStudentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Accept.png"))); // NOI18N
        acceptStudentButton.setEnabled(false);
        acceptStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptStudentButtonActionPerformed(evt);
            }
        });

        addStudentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Add.png"))); // NOI18N
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });

        okButton.setText("Ok");
        okButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Manage Students");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel2.setText("<html>  Create or Modify students in the Gradebook");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addGap(0, 6, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(studentNameField, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(studentIDField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acceptStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentIDField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(acceptStudentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private Student m_tempStudent = null;
    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
        m_tempStudent = new Student("New Student","");
        studentIDField.setEnabled(true);
        studentIDField.setText("");
        studentIDField.focusLost(null);
        studentNameField.setText("");
        studentNameField.focusLost(null);
        acceptStudentButton.setEnabled(true);
        studentIDField.requestFocus();
    }//GEN-LAST:event_addStudentButtonActionPerformed

    private void acceptStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptStudentButtonActionPerformed
        if (m_tempStudent != null) { // A new student is being added
            if(studentIDField.runValidation() && studentNameField.runValidation()){
                String newID = studentIDField.getText();
                String tempName = studentNameField.getText();
                    boolean found = false;
                    ArrayList<Student> students = m_gradebook.getStudents();
                    students.addAll(m_newStudents);

                    for (Student student : students) {
                        if (student.getID().equals(newID)) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        m_tempStudent.setID(newID);
                        m_tempStudent.setName(studentNameField.getText());
                        m_studentListModel.addElement(m_tempStudent);
                        m_newStudents.add(m_tempStudent);
                        m_gradebook.addStudent(tempName, newID);
                        m_tempStudent = null;
                        studentIDField.setEnabled(false);
                    }
                }
        }else { // An existing student is being modified
            if (studentList.getSelectedIndex() >= 0) {
                Student student = (Student)studentList.getSelectedValue();
                student.setName(studentNameField.getText());
            }
        }
        addStudentButton.requestFocus();
    }//GEN-LAST:event_acceptStudentButtonActionPerformed

    private void studentListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_studentListValueChanged
        if (!evt.getValueIsAdjusting()) {
            m_tempStudent = null;
            if (studentList.getSelectedIndex() >= 0) {
                Student student = (Student)studentList.getSelectedValue();
                studentIDField.setEnabled(false);
                studentIDField.setText(student.getID());
                studentNameField.setText(student.getName());
            }
            else {
                studentIDField.setText("");
                studentNameField.setText("");
            }
            acceptStudentButton.setEnabled(false);
        }
    }//GEN-LAST:event_studentListValueChanged

    private void studentNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentNameFieldActionPerformed
        acceptStudentButtonActionPerformed(evt);
    }//GEN-LAST:event_studentNameFieldActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptStudentButton;
    private javax.swing.JButton addStudentButton;
    private GUI.ImageButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private GUI.ImageButton okButton;
    private GUI.HintTextField studentIDField;
    private javax.swing.JList studentList;
    private GUI.HintTextField studentNameField;
    // End of variables declaration//GEN-END:variables
}
