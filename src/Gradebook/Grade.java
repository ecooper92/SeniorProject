package Gradebook;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a Student's grade that is stored in the Gradebook.
 * @author Tom
 */
public class Grade implements Serializable, Comparable {
    private double  m_value;
    private double  m_maxValue;
    private boolean m_input;
    private String m_studentID;
    private Assignment m_assignment;
    
    /**
     * Initialize a new instance of the Grade object.
     */
    Grade(String studentID, Assignment assignment){
        this(studentID, assignment, 0.0);
    }
    
    /**
     * Initialize a new instance of the Grade object.
     * @param value The value of the grade.
     */
    Grade(String studentID, Assignment assignment, double value){
        m_studentID = studentID;
        m_value = 0;
        m_maxValue = value;
        m_input = false;
        m_assignment = assignment;
    }
    
    /**
     * Copy Constructor
     * @param grade
     * @param assignment
     */
    public Grade(Grade grade, Assignment assignment) {
        m_studentID = grade.m_studentID;
        m_value = grade.m_value;
        m_maxValue = grade.m_maxValue;
        m_input = grade.m_input;
        m_assignment = assignment;
    }
    
    /**
     * Gets the value of the grade.
     * @return The value of the grade.
     */
    public double getValue(){
        return m_value;
    }
    
    public Assignment getAssignment() {
        return m_assignment;
    }
    
    /**
     * Sets the value of the grade
     * @param value The value of the grade, must be greater than zero.
     * @throws IllegalArgumentException Occurs if value is less than zero.
     */
    public void setValue(double value){
        // Throw and exception if the value is less than zero.
        if (value < 0) 
            throw new IllegalArgumentException("Value must be greater than zero");
        
        // Set the value.
        double old = m_value;
        m_value = value;
        m_input = true;
        
        // Fire changed event.
        fireValueChanged(old, value);
        
    }
    
    /**
     * Gets the max value of the grade.
     * @return The max value of the grade.
     */
    public double getMaxValue(){
        return m_maxValue;
    }
    
    /**
     * Sets the max value of the grade.
     * @param value The max value of the grade, must be greater than zero.
     * @throws IllegalArgumentException Occurs if value is less than zero.
     */
    public void setMaxValue(double value){
        // Throw and exception if the value is less than zero.
        if (value < 0) 
            throw new IllegalArgumentException("Value must be greater than zero");
        
        // Set the value.
        double old = m_maxValue;
        m_maxValue = value;  
        
        // Fire changed event.
        fireMaxValueChanged(old, value);
    }
    
    /**
     * Reset the grade to an empty value.
     */
    public void clearValue(){
        Double oldValue = null;
        if (!isNull())
            oldValue = m_value;
        
        m_value = 0;
        m_input = false;
        
        fireValueChanged(oldValue, null);
    }
    
    /**
     * Check if the grade is empty.
     * @return True if the grade is empty, otherwise false.
     */
    public boolean isNull(){
        return !m_input;
    }
    
    /**
     * Convert the grade into its string representation.
     * @return 
     */
    public String toString() {
        if (isNull()) {
            return "";
        }
        return Double.toString(m_value);
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Grade))return false;
        Grade other = (Grade)obj;
        return this.m_studentID.equals(other.m_studentID) && this.m_assignment.equals(other.m_assignment);
    }
    
    /**
     * Output the object to a stream.
     * @param stream
     * @throws IOException 
     */
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeDouble(m_value);
        stream.writeDouble(m_maxValue);
        stream.writeBoolean(m_input);
        stream.writeObject(m_assignment);
    }

    /**
     * Re-create the object from a stream.
     * @param stream
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_value = stream.readDouble();
        m_maxValue = stream.readDouble();
        m_input = stream.readBoolean();
        m_assignment = (Assignment)stream.readObject();
        
        m_valueChangedListerners = new ArrayList<>();
        m_maxValueChangedListerners = new ArrayList<>();
    }
    
    /* --- Action Listeners for Value Changed --- */
    private ArrayList<ValueChangedListener> m_valueChangedListerners = new ArrayList<>();


    @Override
    public int compareTo(Object g) {
        Grade gr = (Grade)g;
        if(this.m_maxValue == 0 && gr.getValue() != 0){
            return -1; 
        }
        else if(this.m_maxValue != 0 && gr.getValue() == 0){
            return 1;
        }else if(this.m_maxValue == 0 && gr.getValue() == 0){
            return 0;
        }else{
            double grade = this.m_value/this.m_maxValue;
            double grade2 = gr.m_value/gr.m_maxValue;
            if(grade == grade2){
                return 0;
            }else if(grade > grade2){
                return 1;
            }else{
                return -1;
            }
        }
    }
    public interface ValueChangedListener {
        void valueChanged(Grade grade, Double oldValue, Double newValue);
    }
    
    /**
     * Adds a listener for value changed.
     * @param listener The listener to add.
     */
    public void addValueChangedListener(ValueChangedListener listener) {
        m_valueChangedListerners.add(listener);
    }
    
    /**
     * Removes a listener for value changed.
     * @param listener The listener to remove.
     */
    public void removeValueChangedListner(ValueChangedListener listener) {
        m_valueChangedListerners.remove(listener);
    }
    
    /**
     * Notify all subscribers that the value has changed.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    private void fireValueChanged(Double oldValue, Double newValue) {
        for (ValueChangedListener listener : m_valueChangedListerners) {
            listener.valueChanged(this, oldValue, newValue);
        }
    }
    
    /* --- Action Listeners for Max Value Changed --- */
    private ArrayList<MaxValueChangedListener> m_maxValueChangedListerners = new ArrayList<>();
    public interface MaxValueChangedListener {
        void maxValueChanged(Grade grade, double oldValue, double newValue);
    }
    
    /**
     * Adds a listener for max value changed.
     * @param listener The listener to add.
     */
    public void addMaxValueChangedListener(MaxValueChangedListener listener) {
        m_maxValueChangedListerners.add(listener);
    }
    
    /**
     * Removes a listener for max value changed.
     * @param listener The listener to remove.
     */
    public void removeMaxValueChangedListner(MaxValueChangedListener listener) {
        m_maxValueChangedListerners.remove(listener);
    }
    
    /**
     * Notify all subscribers that the max value has changed.
     * @param oldValue The old max value.
     * @param newValue The new max value.
     */
    private void fireMaxValueChanged(Double oldValue, Double newValue) {
        for (MaxValueChangedListener listener : m_maxValueChangedListerners) {
            listener.maxValueChanged(this, oldValue, newValue);
        }
    }
}
