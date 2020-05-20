/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import Gradebook.Category;
import Gradebook.Course;
import Gradebook.Scale;
import Gradebook.Student;
import Gradebook.Weight;
import java.util.ArrayList;

/**
 *
 * @author Eric
 */
public class ChangeCourse extends Reversible {

    Course m_course = null;
    String m_oldName = "";
    String m_oldTerm = "";
    String m_oldYear = "";
    double[] m_oldScale = null;
    double m_oldWeight = 0.0;
    ArrayList<Student> m_oldStudents = new ArrayList<>();
    ArrayList<Category> m_oldCategories = new ArrayList<>();
    ArrayList<Scale> m_oldScales = new ArrayList<>();
    
    String m_newName = "";
    String m_newTerm = "";
    String m_newYear = "";
    double[] m_newScale = null;
    double m_newWeight = 0.0;
    ArrayList<Student> m_newStudents = new ArrayList<>();
    ArrayList<Category> m_newCategories = new ArrayList<>();
    ArrayList<Scale> m_newScales = new ArrayList<>();
    
    public ChangeCourse(Course course,
                        String oldName, 
                        String oldTerm, 
                        String oldYear,
                        double[] oldScale,
                        double oldWeight,
                        ArrayList<Student> oldStudents,
                        ArrayList<Category> oldCategories,
                        ArrayList<Scale> oldScales) {
        
        m_course = course;
        m_oldName = oldName;
        m_oldTerm = oldTerm;
        m_oldYear = oldYear;
        m_oldScale = oldScale;
        m_oldWeight = oldWeight;
        m_oldStudents = new ArrayList<>(oldStudents);
        m_oldCategories = new ArrayList<>(oldCategories);
        
        if (oldScales != null)
            m_oldScales = new ArrayList<>(oldScales);
        
        m_newName = course.getName();
        m_newTerm = course.getTerm();
        m_newYear = course.getYear();
        m_newScale = course.getScale().getValues();
        m_newWeight = course.getWeight().getValue();
        m_newStudents = new ArrayList<>(course.getStudents());
        m_newCategories = new ArrayList<>(course.getCategories());
        
        if (course.getScales() != null)
            m_newScales = new ArrayList<>(course.getScales());
    }
    
    @Override
    public void undo() {
        m_course.setName(m_oldName);
        m_course.setYear(m_oldYear);
        m_course.setTerm(m_oldTerm);
        
        for (Category newCategory : m_course.getCategories()) {
            m_course.removeCategory(newCategory.getName());
        }
        
        for (Category oldCategory : m_oldCategories) {
            m_course.addCategory(oldCategory);
        }
        
        
        for (Student student : m_course.getStudents()) {
            m_course.removeStudent(student.getID());
        }
        
        for (Student oldStudent : m_oldStudents) {
            m_course.addStudent(oldStudent);
        }
        
        if (m_course.getScales() != null) {
            m_course.getScales().clear();
            m_course.getScales().addAll(m_oldScales);
        }
        m_course.getScale().setValues(m_oldScale);
        m_course.getWeight().setValue(m_oldWeight);
    }

    @Override
    public void redo() {
        m_course.setName(m_newName);
        m_course.setYear(m_newYear);
        m_course.setTerm(m_newTerm);
        
        for (Category category : m_course.getCategories()) {
            m_course.removeCategory(category.getName());
        }
        
        for (Category newCategory : m_newCategories) {
            m_course.addCategory(newCategory);
        }
        
        
        for (Student student : m_course.getStudents()) {
            m_course.removeStudent(student.getID());
        }
        
        for (Student newStudent : m_newStudents) {
            m_course.addStudent(newStudent);
        }
        
        if (m_course.getScales() != null) {
            m_course.getScales().clear();
            m_course.getScales().addAll(m_newScales);
        }
        m_course.getScale().setValues(m_newScale);
        m_course.getWeight().setValue(m_newWeight);
    }
    
}
