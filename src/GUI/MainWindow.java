/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Data.DataFactory;
import Gradebook.Course;
import Gradebook.Grade;
import Gradebook.Gradebook;
import Gradebook.IGradeable;
import Gradebook.Student;
import Utilities.ChangeGrade;
import Utilities.Utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author cmh0034
 */
public class MainWindow extends javax.swing.JFrame  {

    Gradebook m_gradebook = null;
    JPanel m_addCourseButton = null;
    String m_gradebookPath = "";
    

    public boolean createGradebook() {
	CreateGradebook createGradebook = new CreateGradebook(this, true);
	createGradebook.setVisible(true);
        
        return loadGradebook(createGradebook.getGradebook());
    }

    /**
     * Prompt the user to select a grade book file to load and display the data.
     * @return Returns true if the grade book loaded successfully.
     */
    public boolean loadGradebook() {
	JFileChooser fileChooser = new JFileChooser();
	fileChooser.setAcceptAllFileFilterUsed(false);
	fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Gradebook File", "gb"));
	if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	    m_gradebookPath = fileChooser.getSelectedFile().getAbsolutePath();

	    InputPassword inputPassword = new InputPassword(this, true);
	    inputPassword.setFilePath(m_gradebookPath);
	    inputPassword.setVisible(true);
	    return loadGradebook(inputPassword.getGradebook());
	}
	return false;
    }
    
    /**
     * Display the data in the grade book passed as a parameter
     * @param gradebook The grade book to display
     * @return Returns true if the grade book displayed successfully.
     */
    public boolean loadGradebook(Gradebook gradebook) {
        if (gradebook != null) {
            m_gradebook = gradebook;

            ArrayList<Course> courses =  m_gradebook.getCourses();
            setupPanel.setVisible(courses.size() <= 0);

            jCoursesPane.removeAll();
            for (Course course : m_gradebook.getCourses())
                jCoursesPane.add(course.toString(), getCourseComponent(course));
            
            jCoursesPane.addTab("", null);
            jCoursesPane.setTabComponentAt(jCoursesPane.getTabCount() - 1, m_addCourseButton);
            SetupGradebookLoaded();
            
            if (m_gradebook.getCourses().size() > 0) {
                jCoursesPane.setSelectedIndex(jCoursesPane.getTabCount() - 2);
            }
            return true;
        }
	return false;
    }

    public void saveGradebook() {
	if (m_gradebook != null) {
	    if (m_gradebookPath.length() <= 0) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Gradebook File", "gb"));
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
		    m_gradebookPath = fileChooser.getSelectedFile().getAbsolutePath();
		    if (!m_gradebookPath.endsWith(".gb")) {
			m_gradebookPath += ".gb";
		    }
		}
	    }
	    Gradebook.save(m_gradebook, m_gradebookPath);
	}
    }

    //Creates new Course with Tab
    public void createCourse() {
        
        if (m_gradebook != null) {
            
            boolean newCourse = true;
            if (m_gradebook.getCourses().size() > 0) {
                NewExistingCourse dialog = new NewExistingCourse(this, true);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                if (dialog.getAction() ==  NewExistingCourse.Action.Copy) {
                    newCourse = false;
                }
            }
            
            if (newCourse) {
                EditCourse dialog = new EditCourse(this, true, m_gradebook, null, true);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                Course course = dialog.getCourse();

                if (dialog.isValidCourse() && course != null) {
                    jCoursesPane.insertTab(course.toString(), null, getCourseComponent(course), null, jCoursesPane.getTabCount() - 1);
                    jCoursesPane.setSelectedIndex(jCoursesPane.getTabCount() - 2);
                    setupPanel.setVisible(false);
                }
            }
            else {
                SelectCourse dialog = new SelectCourse(this, true, m_gradebook.getCourses());
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
                
                if (dialog.getCourse() != null) {
                    EditCourse edit = new EditCourse(this, true, m_gradebook, new Course(dialog.getCourse()), true);
                    edit.setLocationRelativeTo(null);
                    edit.setVisible(true);
                    
                    Course course = edit.getCourse();
                    if (edit.isValidCourse() && course != null) {
                        jCoursesPane.insertTab(course.toString(), null, getCourseComponent(course), null, jCoursesPane.getTabCount() - 1);
                        jCoursesPane.setSelectedIndex(jCoursesPane.getTabCount() - 2);
                        setupPanel.setVisible(false);
                    }
                }
            }
        }
    }
    public void about(){
        About aboutProgram = new About();
        aboutProgram.setLocationRelativeTo(null);
        aboutProgram.setVisible(true);
                      
    }
    
    public void editCourse() {
	if (m_gradebook != null) {
	    if (m_gradebook.getCourses().size() > 0) {

		JPanel panel = (JPanel) jCoursesPane.getSelectedComponent();
		Course course = (Course) panel.getClientProperty("course");

		EditCourse edit = new EditCourse(this, true, m_gradebook, course, false);
		edit.setLocationRelativeTo(null);
		edit.setVisible(true);
		if (edit.isValidCourse()) {
		    if (m_gradebook != null) {
			// TODO: Force course data to update inside tab
		    }
		}
	    }
	}
    }

    // NOT YET FINISHED..       m_courses.remove not implemented from gradebook
    public void RemoveCourse() {
	/*EditCourse edit = new EditCourse(this, true);
	 edit.setLocationRelativeTo(null);
	 edit.setVisible(true);
	 if (edit.isValidCourse()) {
	 if (m_gradebook != null) {
	 m_gradebook.removeCourse(edit.getCourseName(), edit.getCourseYear(), edit.getCourseTerm());

	 }
	 }*/
    }

    public void AddStudent() {
	EditStudent editStu = new EditStudent(this, true, m_gradebook);
	editStu.setLocationRelativeTo(null);
	editStu.setVisible(true);
	for (Student student : editStu.getNewStudents()) {
	    m_gradebook.addStudent(student.getName(), student.getID());
	}
    }

    public Component getCourseComponent(Course course) {

	GradebookTableModel model = new GradebookTableModel(course);

	JPanel panel = new JPanel();
	panel.putClientProperty("course", course);
	panel.putClientProperty("model", model);
	panel.setLayout(new BorderLayout());

	JTable table = new JTable(model);
        TableColumn column = table.getColumn("Student");

	TableCellListener listener = new TableCellListener(table, new AbstractAction() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		TableCellListener tcl = (TableCellListener) e.getSource();

		JTable table = tcl.getTable();
		Grade grade = (Grade) tcl.getOldValue();
		String newInput = tcl.getNewValue().toString();
                
                Double oldValue = null;
                if (!grade.isNull())
                    oldValue = grade.getValue();

		try {
		    Double result = Double.parseDouble(newInput);
		    if (result < 0) {
			result = 0.0;
		    }
                    
		    table.setValueAt(grade, tcl.getRow(), tcl.getColumn());
		    grade.setValue(result);

                    Utilities.addReversible(new ChangeGrade(grade, oldValue, result));
                    
		} catch (Exception ex) {
		    table.setValueAt(grade, tcl.getRow(), tcl.getColumn());
		    grade.clearValue();
                    
                    if (oldValue != null)
                        Utilities.addReversible(new ChangeGrade(grade, oldValue, null));
		}
	    }
	});

	JTableHeader header = table.getTableHeader();
	DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
	renderer.setHorizontalAlignment(JLabel.CENTER);
        table.setGridColor(new Color(0,73,105,20));
        
        GradebookTableCellRenderer cell = new GradebookTableCellRenderer((DefaultTableCellRenderer)table.getDefaultRenderer(Object.class));
        table.getColumnModel().getColumn(0).setCellRenderer(cell);
        table.setRowHeight(25);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(table), BorderLayout.CENTER);
        
	panel.add(contentPane);

	return panel;
    }

    public void SetupGradebookLoaded() {
	saveMenuItem.setEnabled(true);
	saveAsMenuItem.setEnabled(true);
	addCouseMenuItem.setEnabled(true);
	editCourseMenuItem.setEnabled(true);
	addStudentMenuItem.setEnabled(true);
    }

    public MainWindow() {

	try {
	    ArrayList<Image> images = new ArrayList<>();
	    images.add(ImageIO.read(this.getClass().getResource("resources/smallicon.png")));
	    images.add(ImageIO.read(this.getClass().getResource("resources/largeicon.png")));

	    setIconImages(images);
	} catch (Exception e) {
	}

	initComponents();
        setupPanel.setVisible(false);

	FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

	// Make a small JPanel with the layout and make it non-opaque
	m_addCourseButton = new JPanel(f);
	m_addCourseButton.setOpaque(false);

        try {
            JLabel picLabel = new JLabel(new ImageIcon(ImageIO.read(this.getClass().getResource("resources/SmallAdd.png"))));
            m_addCourseButton.add(picLabel);
        }
        catch (Exception e)
        {
            m_addCourseButton.add(new JLabel("+"));
        }
	m_addCourseButton.addMouseListener(new MouseListener() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		createCourse();
	    }

	    @Override
	    public void mousePressed(MouseEvent e) {
	    }

	    @Override
	    public void mouseReleased(MouseEvent e) {
		createCourse();
	    }

	    @Override
	    public void mouseEntered(MouseEvent e) {
	    }

	    @Override
	    public void mouseExited(MouseEvent e) {
	    }
	});

	//jCoursesPane.addTab("", null);
	//jCoursesPane.setTabComponentAt (jCoursesPane.getTabCount () - 1, m_addCourseButton);
	jCoursesPane.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        setupPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        imageButton2 = new GUI.ImageButton();
        jCoursesPane = new javax.swing.JTabbedPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        FileMenuSeperator1 = new javax.swing.JPopupMenu.Separator();
        addCouseMenuItem = new javax.swing.JMenuItem();
        editCourseMenuItem = new javax.swing.JMenuItem();
        fileMenuSeperator2 = new javax.swing.JPopupMenu.Separator();
        addStudentMenuItem = new javax.swing.JMenuItem();
        FileMenuSeperator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        gradeDistributionMenuItem = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Grading Program");
        setPreferredSize(new java.awt.Dimension(800, 500));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.OverlayLayout(jPanel1));

        setupPanel.setOpaque(false);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("No Courses in the Grade Book");
        jLabel1.setToolTipText("");
        jLabel1.setAutoscrolls(true);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerifyInputWhenFocusTarget(false);

        imageButton2.setText("Add Course");
        imageButton2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        imageButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout setupPanelLayout = new javax.swing.GroupLayout(setupPanel);
        setupPanel.setLayout(setupPanelLayout);
        setupPanelLayout.setHorizontalGroup(
            setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, setupPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        setupPanelLayout.setVerticalGroup(
            setupPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(setupPanelLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(imageButton2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(262, Short.MAX_VALUE))
        );

        jPanel1.add(setupPanel);

        jCoursesPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jCoursesPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCoursesPaneStateChanged(evt);
            }
        });
        jPanel1.add(jCoursesPane);

        jMenu1.setText("File");

        newMenuItem.setText("New Gradebook..");
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(newMenuItem);

        openMenuItem.setText("Open..");
        openMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openMenuItem);

        saveMenuItem.setText("Save");
        saveMenuItem.setEnabled(false);
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveMenuItem);

        saveAsMenuItem.setText("Save As..");
        saveAsMenuItem.setEnabled(false);
        saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAsMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(saveAsMenuItem);
        jMenu1.add(FileMenuSeperator1);

        addCouseMenuItem.setText("Add Course..");
        addCouseMenuItem.setEnabled(false);
        addCouseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addCouseMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(addCouseMenuItem);

        editCourseMenuItem.setText("Edit Course..");
        editCourseMenuItem.setEnabled(false);
        editCourseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCourseMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(editCourseMenuItem);
        jMenu1.add(fileMenuSeperator2);

        addStudentMenuItem.setText("Manage Students...");
        addStudentMenuItem.setEnabled(false);
        addStudentMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addStudentMenuItemMouseClicked(evt);
            }
        });
        addStudentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStudentMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(addStudentMenuItem);
        jMenu1.add(FileMenuSeperator3);

        jMenuItem5.setText("Exit");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem11.setText("Undo");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setText("Redo");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem12);

        jMenuBar1.add(jMenu2);

        jMenu3.setLabel("View");

        jMenu5.setText("Graphs");

        gradeDistributionMenuItem.setText("Grade Distribution");
        gradeDistributionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gradeDistributionMenuItemActionPerformed(evt);
            }
        });
        jMenu5.add(gradeDistributionMenuItem);

        jMenu3.add(jMenu5);

        jMenu6.setText("Reports");

        jMenuItem6.setText("Enrolled Students");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem6);

        jMenuItem7.setText("Grades");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem7);

        jMenu3.add(jMenu6);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Help");

        jMenuItem8.setText("Online Help");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem10.setText("About");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem10);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMenuItemActionPerformed
	createGradebook();
    }//GEN-LAST:event_newMenuItemActionPerformed

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
	loadGradebook();
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
	// TODO add your handling code here:
	about();
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
	saveGradebook();
    }//GEN-LAST:event_saveMenuItemActionPerformed

    private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAsMenuItemActionPerformed
	m_gradebookPath = "";
	saveGradebook();
    }//GEN-LAST:event_saveAsMenuItemActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
	this.dispose();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void addCouseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addCouseMenuItemActionPerformed
	createCourse();
    }//GEN-LAST:event_addCouseMenuItemActionPerformed

    private void gradeDistributionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gradeDistributionMenuItemActionPerformed

	Object courseObject = ((JPanel) jCoursesPane.getSelectedComponent()).getClientProperty("course");
	GradeDistributionSelection gds = new GradeDistributionSelection(this, true, m_gradebook);
        gds.setLocationRelativeTo(null);
        gds.setVisible(true);
        IGradeable gradeable = gds.getGradable();
        if(gradeable != null){
            Course course = gds.getCourse();
	    DataDialog dialog = new DataDialog(this, false, "Grade Distribution Graph", DataFactory.getGradeDistributionChart(gradeable));
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	}
    }//GEN-LAST:event_gradeDistributionMenuItemActionPerformed

    private void addStudentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStudentMenuItemActionPerformed
	AddStudent();
    }//GEN-LAST:event_addStudentMenuItemActionPerformed

    private void addStudentMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addStudentMenuItemMouseClicked
	// TODO add your handling code here:
    }//GEN-LAST:event_addStudentMenuItemMouseClicked

    private void editCourseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCourseMenuItemActionPerformed
	editCourse();
    }//GEN-LAST:event_editCourseMenuItemActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened

        boolean isLoaded = false;
        OpenCreateGradebook prompt = new OpenCreateGradebook(this, true);
        prompt.setLocationRelativeTo(null);
        
        while (!isLoaded) {
            prompt.setVisible(true);
            
            if (prompt.getAction() == OpenCreateGradebook.Action.Create) {
                isLoaded = createGradebook();
            } else if (prompt.getAction() == OpenCreateGradebook.Action.Open) {
                isLoaded = loadGradebook();
            }
        }
    }//GEN-LAST:event_formWindowOpened

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

	Object courseObject = ((JPanel) jCoursesPane.getSelectedComponent()).getClientProperty("course");
	if (courseObject != null) {
	    Course tabCourse = (Course) courseObject;

	    DataDialog dialog = new DataDialog(this, false, tabCourse.getName() + " -- Enrolled Students" , DataFactory.getEnrolledStudentReport(tabCourse.getStudents()));
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
	}
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

	GradeDistributionSelection gds = new GradeDistributionSelection(this, true, m_gradebook);
        gds.setLocationRelativeTo(null);
        gds.setVisible(true);
        IGradeable gradeable = gds.getGradable();
        if(gradeable != null){
            Course course = gds.getCourse();
	    DataDialog dialog = new DataDialog(this, false, "Grades", DataFactory.getGradeReport(gradeable, course.getStudents()));
	    dialog.setLocationRelativeTo(null);
	    dialog.setVisible(true);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void imageButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageButton2ActionPerformed
        createCourse();
    }//GEN-LAST:event_imageButton2ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        Utilities.undo();
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        Utilities.redo();
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jCoursesPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jCoursesPaneStateChanged
        if (jCoursesPane.getSelectedIndex() == jCoursesPane.getTabCount() - 1) {
            jCoursesPane.setSelectedIndex(jCoursesPane.getTabCount() - 2);
        }
    }//GEN-LAST:event_jCoursesPaneStateChanged

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        if( java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported( java.awt.Desktop.Action.BROWSE )) {
                try {

                    java.net.URI uri = new java.net.URI( "http://cs.uah.edu/~emc0005/help.pdf" );
                    desktop.browse( uri );
                }
                catch ( Exception e ) { }
            }
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

	//WebLookAndFeel.install();
	try {
	    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	} catch (Exception ex) {
	}

	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		MainWindow window = new MainWindow();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	    }
	});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator FileMenuSeperator1;
    private javax.swing.JPopupMenu.Separator FileMenuSeperator3;
    private javax.swing.JMenuItem addCouseMenuItem;
    private javax.swing.JMenuItem addStudentMenuItem;
    private javax.swing.JMenuItem editCourseMenuItem;
    private javax.swing.JPopupMenu.Separator fileMenuSeperator2;
    private javax.swing.JMenuItem gradeDistributionMenuItem;
    private GUI.ImageButton imageButton2;
    private javax.swing.JTabbedPane jCoursesPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JPanel setupPanel;
    // End of variables declaration//GEN-END:variables
}
