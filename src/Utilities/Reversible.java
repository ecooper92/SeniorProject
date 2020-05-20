package Utilities;

/**
 * An abstract class that represents any reversible action in the system
 * @author Eric
 */
public abstract class Reversible {
    
    /**
     * Undo the reversible action
     */
    public abstract void undo();
    
    /**
     * Redo the reversible action
     */
    public abstract void redo();
}
