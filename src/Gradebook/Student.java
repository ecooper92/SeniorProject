/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Tom
 */
public class Student implements Serializable {
    String m_name;
    String m_ID;
    

    Student(){
    }
    
    public Student(String name, String id){
        m_name = name;
        m_ID = id;
    }
    
    /**
     * Copy Constructor
     * @param course 
     */
    public Student(Student student) {
        m_name = student.m_name;
        m_ID = student.m_ID;
    }
    
    public String getName(){
        return m_name;
    }
    public void setName(String name){
        m_name = name;
    }
    public String getID(){
        return m_ID;
    }
    public void setID(String id){
        m_ID = id;
    }
    
    @Override
    public String toString() {
        return m_name + ", (" + m_ID + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Student))return false;
        Student other = (Student)obj;
        return this.m_ID.equals(other.m_ID);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeUTF(m_name);
        stream.writeUTF(m_ID);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_name = stream.readUTF();
        m_ID = stream.readUTF();
    }
}
