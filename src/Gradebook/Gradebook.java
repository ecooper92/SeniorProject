/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import Exceptions.InvalidPasswordException;
import Exceptions.SerializationException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.sql.rowset.serial.SerialException;

/**
 *
 * @author Tom
 */
public class Gradebook implements java.io.Serializable {
    
    private ArrayList<Course>       m_courses;
    private HashMap<String,Student> m_students;
    private String                  m_password;
    private ArrayList<Category>     m_categories;
    
    public Gradebook(){
        m_courses = new ArrayList();
        m_students = new HashMap();
        m_password = "";
        m_categories = new ArrayList();
    }
    
    public static void save(Gradebook gradebook, String filePath) {
        try {
            if (gradebook == null)
                throw new IllegalArgumentException("Gradebook cannot be null");
            if (filePath == null)
                throw new IllegalArgumentException("The file path cannot be null");
            
            // Hash the password
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            byte[] hash = digest.digest(gradebook.m_password.getBytes("UTF-8"));
            
            FileOutputStream fileOut = new FileOutputStream(filePath);
            //Write hash to the front (always first 32 bytes)
            fileOut.write(hash);
            
            // Write the serialized object
            CryptoOutputStream crypto = new CryptoOutputStream(fileOut, gradebook.m_password);
            ObjectOutputStream out = new ObjectOutputStream(crypto);
            out.writeObject(gradebook);
            out.close();
            fileOut.close();
        }
        catch(Exception e) 
        { 
            Exception ex = e;
            Exception ax = ex;
        }
    }
    
    private static boolean validatePassword(String password, String filePath) throws IOException {
        if (filePath == null)
            throw new IllegalArgumentException("The file path cannot be null");
        
        FileInputStream inputStream = new FileInputStream(filePath); 
        boolean result = validatePassword(password, inputStream);
        inputStream.close();
        return result;
    }
    
    private static boolean validatePassword(String password, InputStream stream) throws IOException {
        try {
            if (password == null)
                throw new IllegalArgumentException("Password cannot be null");
            if (stream == null || stream.available() < 32)
                throw new IllegalArgumentException("The input stream is not a gradebook");
            
            // Hash the password
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            
            byte[] compareHash = new byte[32];
            stream.read(compareHash);

            // Compare hashes
            return Arrays.equals(hash, compareHash);
        }
        catch(IOException | NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }
    
    /**
     * Attempts to decrypt and deserialize a gradebook from a file.
     * @param password The password to use in decryption.
     * @param filePath The path of the file to load.
     * @return The saved gradebook
     * @throws InvalidPasswordException Occurs when the password given does not 
     * correctly decrypt the file.
     * @throws SerializationException Occurs when the password was correct, but
     * an unknown error is preventing the gradebook from being deserialized.
     * @throws IOException Occurs when there is an error is preventing access to
     * the file.
     */
    public static Gradebook load(String password, String filePath) throws InvalidPasswordException, SerializationException, IOException {
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            if (validatePassword(password, fileIn)) {
                // Deserialize remaining bytes
                CryptoInputStream crypto = new CryptoInputStream(fileIn, password);
                ObjectInputStream in = new ObjectInputStream(crypto);
                Object o = in.readObject();
                Gradebook gradebook = (Gradebook)o;
                fileIn.close();
                return gradebook;
            }
            else {
                throw new InvalidPasswordException("Password incorrect");
            }
        }
        catch(ClassNotFoundException | InvalidClassException e) { 
            throw new SerializationException("Gradebook could not be opened");
        }
        catch(IOException e) { 
            throw new IOException(e);
        }
    }
    
    public void setPassword(String password) {
        m_password = password;
    }
    
    public Course getCourse(String name, String year, String term){
        if(m_courses.isEmpty()){
            return null;
        }else{
            for(Course course : m_courses){
                if(course.getName().equals(name) && 
                        course.getYear().equals(year) &&
                        course.getTerm().equals(term)){
                    return course;
                }
            }
            return null;
        }
    }
    
    public ArrayList<Course> getCourses(){
        return m_courses;
    }
    
    public boolean addCourse(String name, String year, String term){
        return m_courses.add(new Course(name,year,term));
    }
    
    public boolean addCourse(Course course) {
        if (!m_courses.contains(course)) {
            m_courses.add(course);
            return true;
        }
        return false;
    }
    public boolean addCategory(Category category){
        if(m_categories != null || !m_categories.contains(category)){
            m_categories.add(category);
            return true;
        }
        return false;
    }
    public ArrayList<Category> getCategories(){
        return m_categories;
    }
    
    public boolean removeCourse(String name, String year, String term){
        for(Course course : m_courses){
            if(course.getName().equals(name) && 
                        course.getYear().equals(year) &&
                        course.getTerm().equals(term)){
                    m_courses.remove(course);
                    return true;
            }
        }
        return false;
    }
    public Student getStudent(String id){
        if(m_students.containsKey(id)){
            return m_students.get(id);
        }else{
            return null;
        }
    }
    public ArrayList<Student> getStudents(){
        return new ArrayList<>(m_students.values());
    }
    public boolean addStudent(String name, String id){
        if(m_students.containsKey(id)){
            return false;
        }else{
            Student tempStudent = new Student(name, id);
            m_students.put(id, tempStudent);
            return true;
        }
    }
    public boolean removeStudent(String id){
        if(m_students.containsKey(id)){
            m_students.remove(id);
            return true;
        }else{
            return false;
        }
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeObject(m_courses);
        stream.writeObject(m_students);
        stream.writeUTF(m_password);
        stream.writeObject(m_categories);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_courses = (ArrayList<Course>)stream.readObject();
        m_students = (HashMap<String, Student>)stream.readObject();
        m_password = stream.readUTF();
        m_categories = (ArrayList<Category>)stream.readObject();
    }
}
