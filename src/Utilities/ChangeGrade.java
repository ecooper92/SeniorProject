package Utilities;
import Gradebook.Grade;

/**
 * Represents the reversible action of changing a grade
 * @author Eric
 */
public class ChangeGrade extends Reversible {
    
    Grade m_grade = null;
    Double m_oldValue = 0.0;
    Double m_newValue = 0.0;
    
    /**
     * Creates a new instances of the ChangeGrade reversible action
     * @param grade The grade object to manage
     * @param oldValue The previous value of the grade
     * @param newValue The new value of the grade
     */
    public ChangeGrade(Grade grade, Double oldValue, Double newValue) {
        if (grade == null)
            throw new IllegalArgumentException("Grade cannot be null");
        
        m_grade = grade;
        m_oldValue = oldValue;
        m_newValue = newValue;
    }
    
    /**
     * Return the grade to its old value
     */
    @Override
    public void undo() {
        if (m_oldValue != null)
            m_grade.setValue(m_oldValue);
        else
            m_grade.clearValue();
    }
    
    /**
     * Return the grade to its new value
     */
    @Override
    public void redo() {
        if (m_newValue != null)
            m_grade.setValue(m_newValue);
        else
            m_grade.clearValue();
    }
    
}
