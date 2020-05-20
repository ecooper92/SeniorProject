/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Exceptions.InvalidScaleException;
import Gradebook.Assignment;
import Gradebook.Category;
import Gradebook.Course;
import Gradebook.Grade;
import Gradebook.Gradebook;
import Gradebook.Scale;
import Gradebook.Student;
import Utilities.ChangeCourse;
import Utilities.Utilities;
import com.sun.java.accessibility.util.EventID;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Eric
 */
public class EditCourse extends javax.swing.JDialog {
    private ArrayList<Student> tempStudentAddList = new ArrayList();
    private ArrayList<Student> tempStudentRemoveList = new ArrayList();
    private HashMap<String, String> tempAssignmentAddList = new HashMap();
    private HashMap<String, String> tempAssignmentRemoveList = new HashMap();
    private ArrayList<String> tempCategoryRemoveList = new ArrayList();
    private ArrayList<String> categoryNames = new ArrayList();
    private Category tempCategory;
    
    /**
     * Creates new form EditCourse
     * @param parent
     * @param modal
     */
    public EditCourse(java.awt.Frame parent, boolean modal, Gradebook gradebook, Course course, Boolean add) {
        super(parent, modal);
        initComponents();        
        addCourse = add;
        SpinnerNumberModel model = new SpinnerNumberModel(100.0,1.0,Double.MAX_VALUE,5);
        assignmentPointSpinner.setModel(model);
        assignmentPointSpinner.setEnabled(false);
        assignmentWeightSpinner.setEnabled(false);
        dropSpinner.setEnabled(false);
        if (course != null)
            m_course = course;
        m_gradebook = gradebook;

        
        
        courseNameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                okButton.setEnabled(isValidCourse());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                okButton.setEnabled(isValidCourse());
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        courseNameField.focusLost(null);
        
        categoryNameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = categoryNameField.getText();
                acceptCategoryButton.setEnabled(m_modifyingCategoryName.length() > 0 && !categoryNameField.getText().equals(m_modifyingCategoryName));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = categoryNameField.getText();
                acceptCategoryButton.setEnabled(m_modifyingCategoryName.length() > 0 && !categoryNameField.getText().equals(m_modifyingCategoryName));
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        categoryNameField.focusLost(null);
        
        assignmentNameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                acceptAssignmentButton.setEnabled(m_modifyingAssignmentName.length() > 0 && !assignmentNameField.getText().equals(m_modifyingAssignmentName));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                acceptAssignmentButton.setEnabled(m_modifyingAssignmentName.length() > 0 && !assignmentNameField.getText().equals(m_modifyingAssignmentName));
            }

            @Override
            public void changedUpdate(DocumentEvent e) { }
        });
        assignmentNameField.focusLost(null);
        
        cancelButton.requestFocus();
        
        categoriesList.setModel(m_categoriesListModel);
        assignmentsList.setModel(m_assignmentsListModel);
        availableStudentsList.setModel(m_availableStudentListModel);
        studentsList.setModel(m_studentListModel);
        enableAssignments(false);
        
        courseNameField.setText(m_course.getName());
        yearSpinner.setValue(Integer.parseInt(m_course.getYear()));
        termComboBox.setSelectedItem(m_course.getTerm());
        m_scale = m_course.getScale();
        
        m_categoriesListModel.clear();
        
        ArrayList<Category> categories = m_course.getCategories();
        
        Collections.sort(categories, new Comparator<Category> () {
            public int compare(Category o1, Category o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        
        for (Category category : categories) {
            m_categoriesListModel.addElement(category.getName());
        }
        
        ArrayList<Student> currentStudents = m_course.getStudents();
        for (Student student : m_gradebook.getStudents()) {
            if (!currentStudents.contains(student)) {
                m_availableStudentListModel.addElement(student);
            }
        }
        
        for (Student student : currentStudents) {
            m_studentListModel.addElement(student);
        }
        
        okButton.setEnabled(isValidCourse());

        courseNameField.addValidator(new Validator() {
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    return false;
                }else{
                    return true;
                }
            }
            @Override
            public String getMessage() {
                 return  "Missing Name";   
            }
        });
        categoryNameField.addValidator(new Validator() {
            private int errorID;
            private boolean found = false;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
                    for(Category category : m_course.getCategories()){
                        if(category.getName().equals(text)){
                            for(String categoryName : tempCategoryRemoveList){
                                if(categoryName.equals(text)){
                                    found = true;
                                    break;
                                }
                            }
                            if(found){
                                return true;
                            }else{
                                return false;
                            }
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
        assignmentNameField.addValidator(new Validator() {
            private int errorID;
            @Override
            public boolean isValid(String text) {
                if(text.isEmpty() || text.matches("^ *$")){
                    errorID = 1;
                    return false;
                }else{
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
        courseNameField.requestFocus();
    }
    
    private final DefaultListModel<Student> m_availableStudentListModel = new DefaultListModel<>();
    private final DefaultListModel<Student> m_studentListModel = new DefaultListModel<>();
    private final DefaultListModel m_categoriesListModel = new DefaultListModel();
    private final DefaultListModel m_assignmentsListModel = new DefaultListModel();
    private int m_newCategoryCounter = 1;
    private String m_modifyingCategoryName = "";
    private String m_modifyingAssignmentName = "";
    
    private Gradebook m_gradebook = null;
    private Course m_course = new Course("", "2014", "Fall");
    public boolean addCourse;
    private Scale m_scale;
    public Course getCourse() {
        return m_course;
    }
    
    public boolean isValidCourse() {
        return courseNameField.getText().length() > 0;
    }
    
    private void enableAssignments(boolean enable) {
        
        assignmentsList.setEnabled(enable);
        assignmentNameField.setEnabled(enable);
        addAssignmentButton.setEnabled(enable);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel3 = new javax.swing.JPanel();
        okButton1 = new GUI.ImageButton();
        cancelButton1 = new GUI.ImageButton();
        categoryPanel = new javax.swing.JPanel();
        categoryNameField = new GUI.HintTextField("Category Name");
        addCategoryButton = new javax.swing.JButton();
        removeCategoryButton = new javax.swing.JButton();
        acceptCategoryButton = new javax.swing.JButton();
        previousCategoriesButton = new javax.swing.JButton();
        dropSpinner = new javax.swing.JSpinner();
        dropOkBox = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        categoriesList = new javax.swing.JList();
        assignmentPanel = new javax.swing.JPanel();
        addAssignmentButton = new javax.swing.JButton();
        removeAssignmentButton = new javax.swing.JButton();
        acceptAssignmentButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        assignmentWeightSpinner = new javax.swing.JSpinner();
        pointErrorMessage = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        assignmentPointSpinner = new javax.swing.JSpinner();
        assignmentScale = new javax.swing.JButton();
        assignmentNameField = new GUI.HintTextField("Assignment Name");
        jPanel9 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        assignmentsList = new javax.swing.JList();
        courseErrorField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        addStudentButton = new javax.swing.JButton();
        removeStudentButton = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        availableStudentsList = new javax.swing.JList();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        studentsList = new javax.swing.JList();
        courseScale = new javax.swing.JButton();
        yearSpinner = new javax.swing.JSpinner();
        termComboBox = new javax.swing.JComboBox();
        courseNameField = new GUI.HintTextField();
        okButton = new GUI.ImageButton();
        cancelButton = new GUI.ImageButton();
        jPanel2 = new javax.swing.JPanel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Course");
        setMaximumSize(new java.awt.Dimension(445, 630));
        setMinimumSize(new java.awt.Dimension(445, 630));
        setPreferredSize(new java.awt.Dimension(445, 630));
        setResizable(false);

        categoryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Categories"));
        categoryPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        categoryNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                categoryNameFieldActionPerformed(evt);
            }
        });
        categoryPanel.add(categoryNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 148, -1));

        addCategoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Add.png"))); // NOI18N
        addCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCategoryButtonActionPerformed(evt);
            }
        });
        categoryPanel.add(addCategoryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 21, 20));

        removeCategoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Remove.png"))); // NOI18N
        removeCategoryButton.setEnabled(false);
        removeCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeCategoryButtonActionPerformed(evt);
            }
        });
        categoryPanel.add(removeCategoryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 21, 20));

        acceptCategoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Accept.png"))); // NOI18N
        acceptCategoryButton.setEnabled(false);
        acceptCategoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptCategoryButtonActionPerformed(evt);
            }
        });
        categoryPanel.add(acceptCategoryButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 21, 22));

        previousCategoriesButton.setText("Import Categories..");
        previousCategoriesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousCategoriesButtonActionPerformed(evt);
            }
        });
        categoryPanel.add(previousCategoriesButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 267, -1, -1));

        dropSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 0, 1));
        dropSpinner.setEnabled(false);
        categoryPanel.add(dropSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(145, 225, 41, -1));

        dropOkBox.setText("Drop N Lowest");
        dropOkBox.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        dropOkBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                dropOkBoxItemStateChanged(evt);
            }
        });
        categoryPanel.add(dropOkBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(26, 224, -1, -1));

        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        categoriesList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                categoriesListValueChanged(evt);
            }
        });
        categoriesList.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                categoriesListFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(categoriesList);

        jPanel8.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 140));

        categoryPanel.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 140));

        assignmentPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), "Assignments"));
        assignmentPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addAssignmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Add.png"))); // NOI18N
        addAssignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAssignmentButtonActionPerformed(evt);
            }
        });
        assignmentPanel.add(addAssignmentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 21, 20));

        removeAssignmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Remove.png"))); // NOI18N
        removeAssignmentButton.setEnabled(false);
        removeAssignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAssignmentButtonActionPerformed(evt);
            }
        });
        assignmentPanel.add(removeAssignmentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 21, 20));

        acceptAssignmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Accept.png"))); // NOI18N
        acceptAssignmentButton.setEnabled(false);
        acceptAssignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptAssignmentButtonActionPerformed(evt);
            }
        });
        assignmentPanel.add(acceptAssignmentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 21, 22));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Weight"));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        assignmentWeightSpinner.setModel(new javax.swing.SpinnerNumberModel(1.0d, 0.0d, 20.0d, 0.1d));
        assignmentWeightSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(assignmentWeightSpinner, "#%"));
        assignmentWeightSpinner.setEnabled(false);
        assignmentWeightSpinner.setVerifyInputWhenFocusTarget(false);
        assignmentWeightSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                assignmentWeightSpinnerStateChanged(evt);
            }
        });
        jPanel4.add(assignmentWeightSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 18, -1, -1));

        assignmentPanel.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 100, 50));

        pointErrorMessage.setEditable(false);
        pointErrorMessage.setForeground(new java.awt.Color(255, 0, 0));
        pointErrorMessage.setBorder(null);
        assignmentPanel.add(pointErrorMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 271, 105, -1));

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Points"));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        assignmentPointSpinner.setEnabled(false);
        assignmentPointSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                assignmentPointSpinnerStateChanged(evt);
            }
        });
        jPanel5.add(assignmentPointSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        assignmentPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 90, 50));

        assignmentScale.setText("Scale");
        assignmentScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignmentScaleActionPerformed(evt);
            }
        });
        assignmentPanel.add(assignmentScale, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, -1, -1));

        assignmentNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignmentNameFieldActionPerformed(evt);
            }
        });
        assignmentPanel.add(assignmentNameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 150, -1));

        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        assignmentsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                assignmentsListMouseClicked(evt);
            }
        });
        assignmentsList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                assignmentsListValueChanged(evt);
            }
        });
        jScrollPane5.setViewportView(assignmentsList);

        jPanel9.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 140));

        assignmentPanel.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        courseErrorField.setEditable(false);
        courseErrorField.setForeground(new java.awt.Color(255, 0, 0));
        courseErrorField.setBorder(null);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Students"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addStudentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Right.png"))); // NOI18N
        addStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addStudentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 63, -1, 20));

        removeStudentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/resources/Left.png"))); // NOI18N
        removeStudentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeStudentButtonActionPerformed(evt);
            }
        });
        jPanel1.add(removeStudentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(178, 90, -1, 20));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        availableStudentsList.setBorder(javax.swing.BorderFactory.createTitledBorder("Students"));
        jScrollPane2.setViewportView(availableStudentsList);

        jPanel6.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 148, 150));

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, -1));

        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        studentsList.setBorder(javax.swing.BorderFactory.createTitledBorder("Enrolled Students"));
        jScrollPane3.setViewportView(studentsList);

        jPanel7.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 148, 150));

        jPanel1.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 20, 150, -1));

        courseScale.setText("Scale");
        courseScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseScaleActionPerformed(evt);
            }
        });

        yearSpinner.setModel(new javax.swing.SpinnerNumberModel(2014, 1950, 3000, 1));
        yearSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(yearSpinner, "#"));

        termComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Fall", "Spring", "Summer" }));

        courseNameField.setHint("Course Name");
        courseNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                courseNameFieldActionPerformed(evt);
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

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(courseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(termComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(courseScale))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(categoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(assignmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(courseErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseScale)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(courseNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(termComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(7, 7, 7)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(categoryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(assignmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseErrorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCategoryButtonActionPerformed
        String categoryName = "Category " + m_newCategoryCounter++;
        while (!m_course.addCategory(categoryName)) {
            categoryName = "Category " + m_newCategoryCounter++;
        }
        m_categoriesListModel.insertElementAt(categoryName, m_categoriesListModel.size());
        categoriesList.setSelectedIndex(m_categoriesListModel.size()-1);
        categoryNameField.requestFocus();
        categoryNameField.selectAll();
    }//GEN-LAST:event_addCategoryButtonActionPerformed

    private void categoriesListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_categoriesListValueChanged
        if (!evt.getValueIsAdjusting()) {
            if(assignmentsList.isSelectionEmpty()){
                assignmentPointSpinner.setEnabled(false);
                assignmentWeightSpinner.setEnabled(false);
            }
            int index = categoriesList.getSelectedIndex();
            if (index >= 0) {
                dropSpinner.setEnabled(true);
                assignmentPointSpinner.setEnabled(false);
                assignmentWeightSpinner.setEnabled(false);
                removeCategoryButton.setEnabled(true);
                String categoryName = categoriesList.getSelectedValue().toString();
                m_modifyingCategoryName = categoryName;
                categoryNameField.setText(categoryName);
                enableAssignments(true);
                ArrayList<Assignment> assignments = m_course.getCategory(categoryName).getAssignments();
                int size = assignments.size();
                int startValue = m_course.getCategory(categoryName).getDropAssignment();
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(startValue,0,size,1);
                dropSpinner.setModel(spinnerModel);
                Collections.sort(assignments, new Comparator<Assignment> () {
                    public int compare(Assignment o1, Assignment o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                m_assignmentsListModel.clear();
                for (Assignment assignment : assignments) {
                    m_assignmentsListModel.addElement(assignment.getName());
                }
            }
            else {
                removeCategoryButton.setEnabled(false);
                m_modifyingCategoryName = "";
                categoryNameField.setText("");
                m_assignmentsListModel.clear();
                
                enableAssignments(false);
            }
        }
    }//GEN-LAST:event_categoriesListValueChanged

    private void removeCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeCategoryButtonActionPerformed
        int index = categoriesList.getSelectedIndex();
        if (index >= 0) {
            String removedName = (String)m_categoriesListModel.remove(index);
            tempCategoryRemoveList.add(removedName);
            ArrayList<String> tempList = new ArrayList();
            for(String assignmentKey : tempAssignmentAddList.keySet()){
                String category = tempAssignmentAddList.get(assignmentKey);
                if(category.equals(removedName)){
                    tempList.add(assignmentKey);
                }
            }
            for(String assignment : tempList){
                tempAssignmentAddList.remove(assignment);
            }
        }
    }//GEN-LAST:event_removeCategoryButtonActionPerformed

    private void categoryNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_categoryNameFieldActionPerformed
        acceptCategoryButtonActionPerformed(evt);
        addAssignmentButton.requestFocus();
    }//GEN-LAST:event_categoryNameFieldActionPerformed

    private void acceptCategoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptCategoryButtonActionPerformed
        int index = categoriesList.getSelectedIndex();
        if (index >= 0) {
            if(categoryNameField.runValidation()){
                String categoryName = (String)m_categoriesListModel.get(index);
                Category category = m_course.getCategory(categoryName);
                category.setName(categoryNameField.getText());
                m_modifyingCategoryName = categoryName;
                acceptCategoryButton.setEnabled(false);
                m_categoriesListModel.set(index, categoryNameField.getText());
            }
        }
    }//GEN-LAST:event_acceptCategoryButtonActionPerformed

    private void addStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentButtonActionPerformed
   
        if (availableStudentsList.getSelectedIndex() >= 0) {
            Student student = (Student)availableStudentsList.getSelectedValue();
            m_availableStudentListModel.removeElement(student);
            m_studentListModel.addElement(student);
            tempStudentAddList.add(student);
            tempStudentRemoveList.remove(student);
        }
    }//GEN-LAST:event_addStudentButtonActionPerformed

    private void removeStudentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeStudentButtonActionPerformed
        
        if (studentsList.getSelectedIndex() >= 0) {
            Student student = (Student)studentsList.getSelectedValue();
            m_studentListModel.removeElement(student);
            m_availableStudentListModel.addElement(student);
            tempStudentRemoveList.add(student);
            tempStudentAddList.remove(student);
        }
    }//GEN-LAST:event_removeStudentButtonActionPerformed

    private void acceptAssignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptAssignmentButtonActionPerformed
        if (categoriesList.getSelectedIndex() >= 0) {
            String categoryName = (String)categoriesList.getSelectedValue();
            Category category = m_course.getCategory(categoryName);
            int index = assignmentsList.getSelectedIndex();
            if(tempAssignmentAddList.containsKey((String)assignmentsList.getSelectedValue())){
                tempAssignmentAddList.remove((String)assignmentsList.getSelectedValue());
                tempAssignmentAddList.put(assignmentNameField.getText(), categoryName);
            }
            if (index >= 0) {
                String assignmentName = (String)assignmentsList.getSelectedValue();
                Assignment assignment = category.getAssignment(assignmentName);
                if(assignment != null){
                    assignment.setName(assignmentNameField.getText());
                    assignment.setMaxValue((double)assignmentPointSpinner.getValue());
                    assignment.setWeight((double)assignmentWeightSpinner.getValue());

                }
                m_modifyingAssignmentName = assignmentName;
                acceptAssignmentButton.setEnabled(false);
                m_assignmentsListModel.set(index, assignmentNameField.getText());
            }
        }
    }//GEN-LAST:event_acceptAssignmentButtonActionPerformed

    private void removeAssignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAssignmentButtonActionPerformed
        if (categoriesList.getSelectedIndex() >= 0) {
            String categoryName = (String)categoriesList.getSelectedValue();
            int index = assignmentsList.getSelectedIndex();
            if (index >= 0) {
                String removedName = (String)m_assignmentsListModel.remove(index);
                tempAssignmentRemoveList.put(removedName, categoryName);
                tempAssignmentAddList.remove(removedName);
            }
        }
    }//GEN-LAST:event_removeAssignmentButtonActionPerformed

    private void addAssignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAssignmentButtonActionPerformed
        if (categoriesList.getSelectedIndex() >= 0) {
            String categoryName = (String)categoriesList.getSelectedValue();
            int counter = 1;
            for(Assignment assignment : m_course.getCategory(categoryName).getAssignments()){
                categoryNames.add(assignment.getName());
            }
            String assignmentName = categoryName + " " + counter++;
            while(!categoryName.isEmpty() && categoryNames.contains(assignmentName)){
                assignmentName = categoryName + " " + counter++;
            }
            categoryNames.add(assignmentName);
            assignmentPointSpinner.setValue(100.0);
            tempAssignmentAddList.put(assignmentName, categoryName);
            tempAssignmentRemoveList.remove(assignmentName);
            m_assignmentsListModel.insertElementAt(assignmentName, m_assignmentsListModel.size());
            Category category = m_course.getCategory(categoryName);
            category.addAssignment(assignmentName, 100.0, m_course.getStudents());
        }
    }//GEN-LAST:event_addAssignmentButtonActionPerformed

    private void assignmentsListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_assignmentsListValueChanged
        if (!evt.getValueIsAdjusting()) {
            if (assignmentsList.getSelectedIndex() >= 0) {
                removeAssignmentButton.setEnabled(true);
                String categoryName = (String)categoriesList.getSelectedValue();
                String assignmentName = (String)assignmentsList.getSelectedValue();
                m_modifyingAssignmentName = assignmentName;
                assignmentNameField.setText(assignmentName);
                Category category = m_course.getCategory(categoryName);
                Assignment assignment = category.getAssignment(assignmentName);
            }
            else {
                removeAssignmentButton.setEnabled(false);
                m_modifyingAssignmentName = "";
                assignmentNameField.setText("");
            }
        }
    }//GEN-LAST:event_assignmentsListValueChanged

    private void assignmentsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_assignmentsListMouseClicked
        if(!assignmentsList.isSelectionEmpty()){
            assignmentPointSpinner.setEnabled(true);
            assignmentWeightSpinner.setEnabled(true);
            String name = (String)assignmentsList.getSelectedValue();
            String categoryName = (String)categoriesList.getSelectedValue();
            Category category = m_course.getCategory(categoryName);
            if(category.getAssignment(name) != null){
                Assignment assignment = category.getAssignment(name);
                assignmentPointSpinner.setValue(assignment.getMaxValue());
                assignmentWeightSpinner.setValue(assignment.getWeight().getValue());
            }
        }else{
            assignmentPointSpinner.setEnabled(false);
            assignmentWeightSpinner.setEnabled(false);
        }
    }//GEN-LAST:event_assignmentsListMouseClicked

    private void courseScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseScaleActionPerformed
      EditScale courseScaleDialog = new EditScale(null, true, m_course, m_course, m_gradebook);
      courseScaleDialog.setLocationRelativeTo(null);
      courseScaleDialog.setName("Course Scale");
      courseScaleDialog.setVisible(true);
    }//GEN-LAST:event_courseScaleActionPerformed

    private void assignmentScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignmentScaleActionPerformed

      if (categoriesList.getSelectedIndex() >= 0) {
            String categoryName = (String)categoriesList.getSelectedValue();
            if (assignmentsList.getSelectedIndex() >= 0) {
                String assignmentName = (String)assignmentsList.getSelectedValue();
                Category category = m_course.getCategory(categoryName);
                Assignment assignment = category.getAssignment(assignmentName);
                EditScale courseScaleDialog = new EditScale(null, true, assignment, m_course, m_gradebook);
                courseScaleDialog.setLocationRelativeTo(null);
                courseScaleDialog.setName("Assignment Scale");
                courseScaleDialog.setVisible(true);
            }
      }
    }//GEN-LAST:event_assignmentScaleActionPerformed

    private void dropOkBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_dropOkBoxItemStateChanged
        if (categoriesList.getSelectedIndex() >= 0) {
            if(evt.getStateChange() == ItemEvent.SELECTED){
                String categoryName = categoriesList.getSelectedValue().toString();
                Category category = m_course.getCategory(categoryName);
                category.setDropAssignment((int)dropSpinner.getValue());             
            }
        }
    }//GEN-LAST:event_dropOkBoxItemStateChanged

    private void courseNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_courseNameFieldActionPerformed
        okButtonActionPerformed(evt);
    }//GEN-LAST:event_courseNameFieldActionPerformed

    private void previousCategoriesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousCategoriesButtonActionPerformed
        PreviousCategories prevCategory = new PreviousCategories(null, true, m_gradebook.getCourses());
        prevCategory.setLocationRelativeTo(null);
        prevCategory.setVisible(true);
        tempCategory = prevCategory.getCategory();
        //Adding categories on OK click
        if(tempCategory != null){
            if(m_course.getCategory(tempCategory.getName()) != null){
                m_course.removeCategory(tempCategory.getName());   
                m_categoriesListModel.removeElement(tempCategory.getName());
            }
            m_course.addCategory(tempCategory);
            m_categoriesListModel.insertElementAt(tempCategory.getName(), m_categoriesListModel.size());
        }
    }//GEN-LAST:event_previousCategoriesButtonActionPerformed

    private void okButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton1ActionPerformed

    }//GEN-LAST:event_okButton1ActionPerformed

    private void cancelButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton1ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButton1ActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        courseErrorField.setText("");
        if(courseNameField.runValidation()){
            m_course.setName(courseNameField.getText());
            m_course.setYear(yearSpinner.getValue().toString());
            m_course.setTerm(termComboBox.getSelectedItem().toString()); 
            
            ChangeCourse reversible = new ChangeCourse(m_course, 
                                        m_course.getName(), 
                                        m_course.getTerm(), 
                                        m_course.getYear(),
                                        m_course.getScale().getValues(), 
                                        m_course.getWeight().getValue(), 
                                        m_course.getStudents(), 
                                        m_course.getCategories(),
                                        m_course.getScales());

            if(addCourse){
                if(!m_gradebook.addCourse(m_course)){
                    courseErrorField.setText("This course already exist.");
                    return;
                }
            }
            //add students on ok click
            for(Student student : tempStudentAddList){
                m_course.addStudent(student.getName(),student.getID());
            }
            //Remove Students on ok click
            for(Student student : tempStudentRemoveList){
                m_course.removeStudent(student.getID());
            }
            //Remove categories on OK click
            for (String category : tempCategoryRemoveList){
                m_course.removeCategory(category);
            }
            //Add Assignments to categories on OK click
            /*for (String assignment : tempAssignmentAddList.keySet()){
                Category category = m_course.getCategory(tempAssignmentAddList.get(assignment));
                double points = (double)assignmentPointSpinner.getValue();
                category.addAssignment(assignment, points, m_course.getStudents());
            }*/
            //Remove Assignments from categories on OK click
            for (String assignment : tempAssignmentRemoveList.keySet()){
                Category category = m_course.getCategory(tempAssignmentRemoveList.get(assignment));
                category.removeAssignment(assignment);
            }
            
            Utilities.addReversible(reversible);
            this.dispose();
        }else{
            return;
        }   

    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        m_course = null;
        this.dispose();        
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void assignmentWeightSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_assignmentWeightSpinnerStateChanged
        String categoryName = (String)categoriesList.getSelectedValue();
        String assignmentName = (String)assignmentsList.getSelectedValue();
        Category category = m_course.getCategory(categoryName);
        Assignment assignment = category.getAssignment(assignmentName);
        assignment.setWeight((Double)assignmentWeightSpinner.getValue());
    }//GEN-LAST:event_assignmentWeightSpinnerStateChanged

    private void categoriesListFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_categoriesListFocusGained
        if(assignmentsList.isSelectionEmpty()){
            assignmentPointSpinner.setEnabled(false);
            assignmentWeightSpinner.setEnabled(false);
        }
    }//GEN-LAST:event_categoriesListFocusGained

    private void assignmentNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignmentNameFieldActionPerformed
        acceptAssignmentButtonActionPerformed(evt);
    }//GEN-LAST:event_assignmentNameFieldActionPerformed

    private void assignmentPointSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_assignmentPointSpinnerStateChanged
        String categoryName = (String)categoriesList.getSelectedValue();
        String assignmentName = (String)assignmentsList.getSelectedValue();
        Category category = m_course.getCategory(categoryName);
        Assignment assignment = category.getAssignment(assignmentName);
        if(assignment != null){
            double value = (double)assignmentPointSpinner.getValue();
            assignment.setMaxValue(value);
            HashMap<String, Grade> student_grade = assignment.getGrade();
            for(Grade grade : student_grade.values()){
                grade.setMaxValue(value);
            }
        }
    }//GEN-LAST:event_assignmentPointSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptAssignmentButton;
    private javax.swing.JButton acceptCategoryButton;
    private javax.swing.JButton addAssignmentButton;
    private javax.swing.JButton addCategoryButton;
    private javax.swing.JButton addStudentButton;
    private GUI.HintTextField assignmentNameField;
    private javax.swing.JPanel assignmentPanel;
    private javax.swing.JSpinner assignmentPointSpinner;
    private javax.swing.JButton assignmentScale;
    private javax.swing.JSpinner assignmentWeightSpinner;
    private javax.swing.JList assignmentsList;
    private javax.swing.JList availableStudentsList;
    private GUI.ImageButton cancelButton;
    private GUI.ImageButton cancelButton1;
    private javax.swing.JList categoriesList;
    private GUI.HintTextField categoryNameField;
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JTextField courseErrorField;
    private GUI.HintTextField courseNameField;
    private javax.swing.JButton courseScale;
    private javax.swing.JCheckBox dropOkBox;
    private javax.swing.JSpinner dropSpinner;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private GUI.ImageButton okButton;
    private GUI.ImageButton okButton1;
    private javax.swing.JTextField pointErrorMessage;
    private javax.swing.JButton previousCategoriesButton;
    private javax.swing.JButton removeAssignmentButton;
    private javax.swing.JButton removeCategoryButton;
    private javax.swing.JButton removeStudentButton;
    private javax.swing.JList studentsList;
    private javax.swing.JComboBox termComboBox;
    private javax.swing.JSpinner yearSpinner;
    // End of variables declaration//GEN-END:variables
}
