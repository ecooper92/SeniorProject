/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GUI;

import Gradebook.Assignment;
import Gradebook.Category;
import Gradebook.Course;
import Gradebook.Grade;
import Gradebook.Gradebook;
import Gradebook.Student;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Admin
 */
public class GradebookTableModel extends DefaultTableModel implements Course.StudentListener, 
                                                            Course.CategoryListener,
                                                            Category.AssignmentListener,
                                                            Assignment.NameChangedListener,
                                                            Assignment.GradeListener,
                                                            Grade.ValueChangedListener{
    
    private Course m_course = null;
    
    public GradebookTableModel(Object[] columnNames, int rows) {
        super(columnNames, rows);
    }
    
    public GradebookTableModel(Course course) {
        setCourse(course);
    }
    
    public Course getCourse() {
        return m_course;
    }
    
    public void setCourse(Course course) {
        
        m_course = course;
        
        course.addStudentListener(this);
        course.addCategoryListener(this);

        addColumn("Student");
        
        for (Category category : course.getCategories()) {
            category.addAssignmentListener(this);
            for (Assignment assignment : category.getAssignments()) {
                assignment.addNameChangedListener(this);
                assignment.addGradeListener(this);
                addColumn(assignment);
            }
        }

	for (Student student : course.getStudents()) {
	    ArrayList<Object> rowData = new ArrayList<>();
	    rowData.add(student);

	    for (Category category : course.getCategories()) {
		for (Assignment assignment : category.getAssignments()) {
                    
                    Grade grade = assignment.getStudentGrade(student.getID());
                    grade.addValueChangedListener(this);
		    rowData.add(grade);
                }
	    }
	    addRow(rowData.toArray());
	}
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {  
        return col != 0;  
    }

    @Override
    public void assignmentAdded(Assignment assignment) {
        assignment.addNameChangedListener(this);
        assignment.addGradeListener(this);
        
        Object[] data = new Object[getRowCount()];
        for (int i = 0; i < getRowCount(); i++) {
            Student student = (Student)getValueAt(i, 0);
            data[i] = assignment.getStudentGrade(student.getID());
        }
        
        addColumn(assignment, data);
    }

    @Override
    public void assignmentRemoved(Assignment assignment) {
        assignment.removeNameChangedListner(this);
        assignment.removeGradeListner(this);
        
        for (int i = 1; i < columnIdentifiers.size(); i++) {
            Assignment headerAssignment = (Assignment)columnIdentifiers.get(i);
            if (headerAssignment == assignment) {
                columnIdentifiers.remove(i);
                for (Object row: dataVector) {
                    ((Vector)row).remove(i);
                }
                fireTableStructureChanged();
            }
        }
    }

    @Override
    public void studentAdded(Course course, Student student) {
        
        ArrayList<Object> grades = new ArrayList<>();
        grades.add(student);
        for (Category category : course.getCategories()) {
            for (Assignment assignment : category.getAssignments()) {
                grades.add(assignment.getStudentGrade(student.getID()));
            }
        }

        addRow(grades.toArray());
    }

    @Override
    public void studentRemoved(Course course, Student student) {
        
        for (int j = 0; j < getRowCount(); j++) {
            Student rowStudent = (Student) getValueAt(j, 0);
            if (rowStudent == student) {
                removeRow(j);
                break;
            }
        }
    }

    @Override
    public void categoryAdded(Category category) {
        category.addAssignmentListener(this);
        
        for (Assignment assignment : category.getAssignments()) {
            assignmentAdded(assignment);
        }
    }

    @Override
    public void categoryRemoved(Category category) {
        category.removeAssignmentListner(this);
        
        for (Assignment assignment : category.getAssignments()) {
            assignmentRemoved(assignment);
        }
        /*boolean removed = false;
        for (int i = 1; i < columnIdentifiers.size(); i++) {
            Assignment headerAssignment = (Assignment)columnIdentifiers.get(i);
            if (headerAssignment.getCategory() == category) {
                
                columnIdentifiers.remove(i);
                for (Object row: dataVector) {
                    ((Vector)row).remove(i);
                }
                i--;
                
                removed = true;
            }
        }
        
        if (removed)
            fireTableStructureChanged();*/
    }

    @Override
    public void nameChanged(Assignment assignment, String oldValue, String newValue) {
        fireTableStructureChanged();
    }

    @Override
    public void gradeAdded(Grade grade) {
        grade.addValueChangedListener(this);
    }

    @Override
    public void gradeRemoved(Grade grade) {
        grade.removeValueChangedListner(this);
    }

    @Override
    public void valueChanged(Grade grade, Double oldValue, Double newValue) {
        
        /*for (int i = 1; i < columnIdentifiers.size(); i++) {
            Assignment headerAssignment = (Assignment)columnIdentifiers.get(i);
            if (headerAssignment == grade.getAssignment()) {
                
                for (Object row: dataVector) {
                    Vector rowVector = (Vector)row;
                    Grade columnGrade = (Grade)rowVector.get(i);
                    if (columnGrade == grade) {
                        rowVector.set(i, newValue);
                        break;
                    }
                }
                break;
            }
        }*/
        
        fireTableStructureChanged();
    }
}
