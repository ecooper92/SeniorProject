package Utilities;
import Gradebook.Scale;

/**
 * Represents the reversible action of changing a scale
 * @author Eric
 */
public class ChangeScale extends Reversible {

    Scale m_scale = null;
    double[] m_oldValues = new double[4];
    double[] m_newValues = new double[4];
    
    /**
     * Creates a new instances of the ChangeScale reversible action
     * @param scale The scale object to manage
     * @param oldValues The previous values of the scale
     * @param newValues The new values of the scale
     */
    public ChangeScale(Scale scale, double[] oldValues, double[] newValues) {
        if (scale == null)
            throw new IllegalArgumentException("Scale cannot be null");
        if (oldValues == null || oldValues.length != 4)
            throw new IllegalArgumentException("Old Values must be an array of size 4");
        if (newValues == null || newValues.length != 4)
            throw new IllegalArgumentException("New Values must be an array of size 4");
        
        m_scale = scale;
        m_oldValues = oldValues;
        m_newValues = newValues;
    }
    
    /**
     * Return the scale to its old values
     */
    @Override
    public void undo() {
        m_scale.setValues(m_oldValues);
    }

    /**
     * Return the scale to its new values
     */
    @Override
    public void redo() {
        m_scale.setValues(m_newValues);
    }
    
}
