/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Tom
 */
public class Category implements Serializable {
    private String m_name;
    private ArrayList<Assignment> m_assignments;
    private Course m_course = null;
    private int m_dropAssignments;
    
    public Category(String name, Course course){
        m_name = name;
        m_assignments = new ArrayList();
        m_course = course;
        m_dropAssignments = 0;
    }
    
    /**
     * Copy Constructor
     * @param category 
     * @param course 
     */
    public Category(Category category, Course course) {
        m_dropAssignments = 0;
        m_name = category.m_name;
        m_course = course;
        
        m_assignments = new ArrayList<>();
        for (Assignment assignment : category.m_assignments)
            m_assignments.add(new Assignment(assignment, this));
    }
    public void setDropAssignment(int count){
        m_dropAssignments = count;
    }
    public int getDropAssignment(){
        return m_dropAssignments;
    }
    public String getName(){
        return m_name;
    }
    public void setName(String name){
        m_name = name;
    }
    
    public Course getCourse() {
        return m_course;
    }
    public void setCourse(Course course) {
        m_course = course;
    }
    
    public ArrayList<Assignment> getAssignments(){
        return new ArrayList<>(m_assignments);
    }
    public Assignment getAssignment(String name){
        for (Assignment assignment : m_assignments) {
            if (assignment.getName().equals(name)) {
                return assignment;
            }
        }
        return null;
    }
    public boolean addAssignment(String name, double value, ArrayList<Student> students){
        for (Assignment assignment : m_assignments) {
            if (assignment.getName().equals(name)) {
                return false;
            }
        }
        Assignment temp = new Assignment(name, value, this);
        for(Student student : students){
            temp.addStudent(student.getID());
        }
        m_assignments.add(temp);
        fireAssignmentAdded(temp);
        return true;
    }
    public boolean removeAssignment(String name){
        for (int i = 0; i < m_assignments.size(); i++) {
            if (m_assignments.get(i).getName().equals(name)) {
                Assignment assignment = m_assignments.remove(i);
                fireAssignmentRemoved(assignment);
                return true;
            }
        }
        return false;
    }
    public boolean hasAssignment(String name){
        for(Assignment assignment : m_assignments){
            if(assignment.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public ArrayList<Grade> getStudentGradeList(String id){
        ArrayList<Grade> studentGrades = new ArrayList();
        for(Assignment assignment : m_assignments){
            studentGrades.add(assignment.getStudentGrade(id));
        }
        return studentGrades;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Category))return false;
        Category other = (Category)obj;
        return this.m_name.equals(other.m_name) && this.m_course.equals(other.m_course);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return m_name;
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeUTF(m_name);
        stream.writeObject(m_assignments);
        stream.writeObject(m_course);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_name = stream.readUTF();
        m_assignments = (ArrayList<Assignment>)stream.readObject();
        m_course = (Course)stream.readObject();
        
        m_assignmentListerners = new ArrayList<>();
    }
    
    /* --- Action Listeners for Assignments added and removed --- */
    private ArrayList<AssignmentListener> m_assignmentListerners = new ArrayList<>();
    public interface AssignmentListener { 
        void assignmentAdded(Assignment assignment);
        void assignmentRemoved(Assignment assignment);
    }
    
    /**
     * Adds a listener.
     * @param listener The listener to add.
     */
    public void addAssignmentListener(AssignmentListener listener) {
        m_assignmentListerners.add(listener);
    }
    
    /**
     * Removes a listener.
     * @param listener The listener to remove.
     */
    public void removeAssignmentListner(AssignmentListener listener) {
        m_assignmentListerners.remove(listener);
    }
    
    /**
     * Fire added event.
     * @param parent
     * @param item 
     */
    public void fireAssignmentAdded(Assignment item) {
        for (AssignmentListener listener : m_assignmentListerners) {
            listener.assignmentAdded(item);
        }
    }
    
    /**
     * Fire removed event.
     * @param parent
     * @param item 
     */
    public void fireAssignmentRemoved(Assignment item) {
        for (AssignmentListener listener : m_assignmentListerners) {
            listener.assignmentRemoved(item);
        }
    }
}
