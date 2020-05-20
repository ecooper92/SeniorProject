/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import Exceptions.InvalidScaleException;
import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author Tom
 */
public class Scale implements Serializable {
    private String m_name;
    private double m_a;
    private double m_b;
    private double m_c;
    private double m_d;
    
    Scale(){
        m_name = "Default Scale";
        m_a = 90.0;
        m_b = 80.0;
        m_c = 70.0;
        m_d = 60.0;
    }
    public Scale(double[] values){
        m_name = "Default Scale";
        m_a = values[0];
        m_b = values[1];
        m_c = values[2];
        m_d = values[3];
    }
    
    /**
     * Copy Constructor
     * @param scale 
     */
    public Scale(Scale scale) {
        m_a = scale.m_a;
        m_b = scale.m_b;
        m_c = scale.m_c;
        m_d = scale.m_d;
        m_name = scale.m_name;
    }
    public void setName(String name){
        m_name = name;
    }
    public String getName(){
        return m_name;
    }
    public double getA(){
        return m_a;
    }
    public void setA(double value){
        if(value < 0){
            throw new InvalidScaleException("A must be greater than 0");
        }else{
            if(value > m_b){
                m_a = value;
            }else{
                throw new InvalidScaleException("A must be greater than B");
            }
        }
    }
    public double getB(){
        return m_b;
    }
    public void setB(double value){
        if(value < 0){
            throw new InvalidScaleException("A must be greater than 0");
        }else{
            if(value > m_c){
                m_b = value;
            }else{
                throw new InvalidScaleException("B must be greater than C");
            }
        }
    }
    public double getC(){
        return m_c;
    }
    public void setC(double value){
        if(value < 0){
            throw new InvalidScaleException("A must be greater than 0");
        }else{
            if(value > m_d){
                m_c = value;
            }else{
                throw new InvalidScaleException("C must be greater than D");
            }
        }
    }
    public double getD(){
        return m_d;
    }
    public void setD(double value){
        if(value < 0){
            throw new InvalidScaleException("D must be greater than 0");
        }else
            if(value < m_d){
                m_d = value;
            }else{
                throw new InvalidScaleException("D must be less than C");
            }
    }
    public double[] getValues(){
        double[] temp = {m_a,m_b,m_c,m_d};
        return temp;
    }
    public void setValues(double[] value){
        if(value[0] > value[1]){
            if(value[1] > value[2]){
                if(value[2] > value[3]){
                    if(value[3] > 0){
                        m_a = value[0];
                        m_b = value[1];
                        m_c = value[2];
                        m_d = value[3];
                    }else{
                        throw new InvalidScaleException("D must be greater than 0");
                    }
                }else{
                    throw new InvalidScaleException("C must be greater than D");
                }
            }else{
                throw new InvalidScaleException("B must be greater than C");
            }
        }else{
            throw new InvalidScaleException("A must be greater than B");
        }
    }
    public String calculate(double value){
        if(value >= m_a){
            return "A";
        }else if((value < m_a) && (value >= m_b)){
            return "B";
        }else if((value < m_b) && (value >= m_c)){
            return "C";
        }else if((value < m_c) && (value >= m_d)){
            return "D";
        }else if (value < m_d){
            return "F";
        }else {
            return "-";
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeUTF(m_name);
        stream.writeDouble(m_a);
        stream.writeDouble(m_b);
        stream.writeDouble(m_c);
        stream.writeDouble(m_d);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_name = stream.readUTF();
        m_a = stream.readDouble();
        m_b = stream.readDouble();
        m_c = stream.readDouble();
        m_d = stream.readDouble();
    }
    public String toString() {
        return m_name;
    }
}
