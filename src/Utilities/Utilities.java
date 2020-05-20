package Utilities;
import java.util.Stack;

/**
 * Provides different support functions to all areas of the application
 * @author Eric
 */
public class Utilities {
    
    private static final Stack<Reversible> m_undoStack = new Stack<>();
    private static final Stack<Reversible> m_redoStack = new Stack<>();
    
    /**
     * Private constructor to prevent creating an instance of the static class
     */
    private Utilities() { }
    
    /**
     * Execute the next undo reversible
     */
    public static void undo() {
        if (!m_undoStack.empty()) {
            Reversible reversible = m_undoStack.pop();
            reversible.undo();
            m_redoStack.push(reversible);
        }
    }
    
    /**
     * Execute the next redo reversible
     */
    public static void redo() {
        if (!m_redoStack.empty()) {
            Reversible reversible = m_redoStack.pop();
            reversible.redo();
            m_undoStack.push(reversible);
        }
    }
    
    /**
     * Get the number of undo actions
     * @return The number of undo actions
     */
    public static int getUndoCount() {
        return m_undoStack.size();
    }
    
    /**
     * Get the number of redo actions
     * @return The number of redo actions
     */
    public static int getRedoCount() {
        return m_redoStack.size();
    }
    
    /**
     * Add a new undo reversible
     * @param reversible The reversible to add to the stack
     */
    public static void addReversible(Reversible reversible) {
        if (reversible != null) {
            m_undoStack.push(reversible);
            m_redoStack.clear();
        }
        else {
            throw new IllegalArgumentException("Reversible cannot be null");
        }
    }
}
