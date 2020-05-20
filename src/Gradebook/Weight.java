package Gradebook;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Tom
 */
public class Weight implements Serializable {
    
    private double m_weight;
    Weight(){
        m_weight = 1.0;
    }
    
    public Weight(Weight weight) {
        m_weight = weight.m_weight;
    }
    
    public double getValue(){
        return m_weight;
    }
    
    public boolean setValue(double weight){
        if(weight > 0){
            m_weight = weight;
            return true;
        }else{
            return false;
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeDouble(m_weight);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_weight = stream.readDouble();
    }
}
