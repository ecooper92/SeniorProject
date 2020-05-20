/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import Exceptions.InvalidScaleException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Represents an a graded assignment in a course.
 * @author Tom
 */
public class Assignment implements IGradeable, Serializable{
    private String                  m_name;
    private HashMap<String,Grade>   m_studentGrade;
    private Weight                  m_weight;
    private Scale                   m_scale;
    private double                  m_maxValue;
    private Category                m_category;
    
    /**
     * Initializes a new instance of the Assignment object.
     * @param name The name of the assignment
     * @param value The maximum number of points in the assignment.
     */
    Assignment(String name, double maxValue, Category category){
        m_name = name;
        m_studentGrade = new HashMap();
        m_weight = new Weight();
        m_scale = new Scale();
        m_maxValue = maxValue;
        m_category = category;
    }
    
    /**
     * Copy Constructor
     * @param assignment 
     * @param category 
     */
    public Assignment(Assignment assignment, Category category) {
        m_name = assignment.m_name;
        m_maxValue = assignment.m_maxValue;
        m_weight = new Weight(assignment.m_weight);
        m_scale = new Scale(assignment.m_scale);
        m_category = category;
        
        m_studentGrade = new HashMap<>();
        for (Entry<String,Grade> entry : assignment.m_studentGrade.entrySet()) {
            String studentName = entry.getKey();
            m_studentGrade.put(entry.getKey(), new Grade(studentName, this, assignment.getMaxValue()));
        }
    }
    
    /**
     * Gets the name of the assignment.
     * @return The name of the assignment.
     */
    public String getName(){
        return m_name;
    }
    
    /**
     * Sets the name of the assignment.
     * @param name The name of the assignment.
     */
    public void setName(String name){
        String oldValue = m_name;
        m_name = name;
        fireNameChanged(oldValue, name);
    }
    
    /**
     * Gets the max value of the assignment.
     * @return The max value of the assignment.
     */
    public double getMaxValue(){
        return m_maxValue;
    }
    
    /**
     * Sets the max value of the assignment
     * @param value The new max value, must be greater than zero.
     * @throws IllegalArgumentException 
     */
    public void setMaxValue(double value){
        if (value < 0)
            throw new IllegalArgumentException("Value must be greater than zero");
        
        m_maxValue = value;
    }
    
    public Category getCategory() {
        return m_category;
    }
    public void setCategory(Category category) {
        m_category = category;
    }
    
    /**
     * Get the grade of an individual student.
     * @param id The student id for which to search.
     * @return The student's grade or null if it does not exist.
     */
    public Grade getStudentGrade(String id) {
        if(m_studentGrade.containsKey(id))
            return m_studentGrade.get(id);
        else
            return null;
    }
    
    /**
     * Sets the grade of an individual student if they are enrolled in the course.
     * @param id The student id to set.
     * @param value The grade value to set.
     */
    public void setStudentGrade(String id, double value){
        if(m_studentGrade.containsKey(id))
            m_studentGrade.get(id).setValue(value);
    }
    
    /**
     * Adds a student to the assignment.
     * @param id The id of the student to add.
     * @return 
     */
    public void addStudent(String id){
        if(!m_studentGrade.containsKey(id)){
            Grade grade = new Grade(id, this, m_maxValue);
            m_studentGrade.put(id, grade);
            fireGradeAdded(grade);
        }
    }
    
    /**
     * Remove a student from the assignment.
     * @param id The id of the student to remove.
     */
    public void removeStudent(String id){
        if(m_studentGrade.containsKey(id)) {
            Grade grade = m_studentGrade.remove(id);
            fireGradeRemoved(grade);
        }
    }
    
    /**
     * Gets the assignment weight.
     * @return The assignment weight.
     */
    @Override
    public Weight getWeight(){
        return m_weight;
    }
    
    /**
     * Sets the assignment weight.
     * @param value The weight value of the assignment, 1.0 = 100%
     * @return 
     */
    public boolean setWeight(double value){
        return m_weight.setValue(value);
    }
    
    /**
     * Gets the assignment scale.
     * @return The assignment scale
     */
    @Override
    public Scale getScale(){
        return m_scale;
    }
    
    /**
     * Sets the assignment scale.
     * @param value The values for the assignment scale, must be an
     * array of length 4.
     */
    public void setScale(double[] value){
        try{
            m_scale.setValues(value);
        }catch(InvalidScaleException e){
            throw e;
        }
    }

    /**
     * Gets a HashMap of the grades in the assignment.
     * @return 
     */
    @Override
    public HashMap<String,Grade> getGrade() {
        return m_studentGrade;
    }
    
    @Override
    public String toString() {
        return m_name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Assignment))return false;
        Assignment other = (Assignment)obj;
        return this.m_name.equals(other.m_name) && this.m_category.equals(other.m_category);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    /**
     * Output the object to a stream.
     * @param stream
     * @throws IOException 
     */
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeUTF(m_name);
        stream.writeDouble(m_maxValue);
        stream.writeObject(m_scale);
        stream.writeObject(m_weight);
        stream.writeObject(m_studentGrade);
        stream.writeObject(m_category);
    }

    /**
     * Re-create the object from a stream.
     * @param stream
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_name = stream.readUTF();
        m_maxValue = stream.readDouble();
        m_scale = (Scale)stream.readObject();
        m_weight = (Weight)stream.readObject();
        m_studentGrade = (HashMap<String,Grade>)stream.readObject();
        m_category = (Category)stream.readObject();
        
        m_nameChangedListerners = new ArrayList<>();
        m_gradeListerners = new ArrayList<>();
    }
    
    @Override
    public IGradeable getCopy() {
        return new Assignment(this, m_category);
    }
    
    /* --- Action Listeners for Name Changed --- */
    private ArrayList<NameChangedListener> m_nameChangedListerners = new ArrayList<>();

    @Override
    public void setScale(Scale scale) {
        m_scale = new Scale(scale);
    }
    public interface NameChangedListener {
        void nameChanged(Assignment assignment, String oldValue, String newValue);
    }
    
    /**
     * Adds a listener for name changed.
     * @param listener The listener to add.
     */
    public void addNameChangedListener(NameChangedListener listener) {
        m_nameChangedListerners.add(listener);
    }
    
    /**
     * Removes a listener for name changed.
     * @param listener The listener to remove.
     */
    public void removeNameChangedListner(NameChangedListener listener) {
        m_nameChangedListerners.remove(listener);
    }
    
    /**
     * Notify all subscribers that the name has changed.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    private void fireNameChanged(String oldValue, String newValue) {
        for (NameChangedListener listener : m_nameChangedListerners) {
            listener.nameChanged(this, oldValue, newValue);
        }
    }
    
    /* --- Action Listeners for Assignments added and removed --- */
    private ArrayList<GradeListener> m_gradeListerners = new ArrayList<>();
    public interface GradeListener { 
        void gradeAdded(Grade grade);
        void gradeRemoved(Grade grade);
    }
    
    /**
     * Adds a listener.
     * @param listener The listener to add.
     */
    public void addGradeListener(GradeListener listener) {
        m_gradeListerners.add(listener);
    }
    
    /**
     * Removes a listener.
     * @param listener The listener to remove.
     */
    public void removeGradeListner(GradeListener listener) {
        m_gradeListerners.remove(listener);
    }
    
    /**
     * Fire added event.
     * @param parent
     * @param item 
     */
    private void fireGradeAdded(Grade item) {
        for (GradeListener listener : m_gradeListerners) {
            listener.gradeAdded(item);
        }
    }
    
    /**
     * Fire removed event.
     * @param parent
     * @param item 
     */
    private void fireGradeRemoved(Grade item) {
        for (GradeListener listener : m_gradeListerners) {
            listener.gradeRemoved(item);
        }
    }
}
