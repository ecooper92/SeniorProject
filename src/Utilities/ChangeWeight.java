package Utilities;
import Gradebook.Weight;

/**
 * Represents the reversible action of changing a weight
 * @author Eric
 */
public class ChangeWeight extends Reversible {

    Weight m_weight = null;
    double m_oldValue = 0.0;
    double m_newValue = 0.0;
    
    /**
     * Creates a new instances of the ChangeWeight reversible action
     * @param weight The weight object to manage
     * @param oldValue The previous value of the weight
     * @param newValue The new value of the weight
     */
    public ChangeWeight(Weight weight, double oldValue, double newValue) {
        if (weight == null)
            throw new IllegalArgumentException("Weight cannot be null");
        
        m_weight = weight;
        m_oldValue = oldValue;
        m_newValue = newValue;
    }
    
    /**
     * Return the weight to its old value
     */
    @Override
    public void undo() {
        m_weight.setValue(m_oldValue);
    }

    /**
     * Return the weight to its new value
     */
    @Override
    public void redo() {
        m_weight.setValue(m_newValue);
    }
    
}
