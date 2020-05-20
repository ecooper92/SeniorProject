/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;


import Exceptions.InvalidScaleException;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.sort;
import java.util.HashMap;
import java.util.Objects;

/**
 *
 * @author Tom
 */
public class Course implements IGradeable, java.io.Serializable {
    private String m_name;
    private String m_year;
    private String m_term;
    protected ArrayList<Student> m_cStudents;
    private ArrayList<Category> m_categories;
    private HashMap<String,Grade> m_finalGrade;
    private Scale m_scale;
    private Weight m_weight;
    private ArrayList<Scale> m_scales;
    
    public Course(String name, String year, String term){
        m_name = name;
        m_year = year;
        m_term = term;
        m_cStudents = new ArrayList();
        m_categories = new ArrayList();
        m_scale = new Scale();
        m_scales = new ArrayList();
        m_scales.add(m_scale);
        m_weight = new Weight();
    }
    
    /**
     * Copy Constructor
     * @param course 
     */
    public Course(Course course) {
        m_name = course.m_name;
        m_year = course.m_year;
        m_term = course.m_term;
        m_scale = new Scale(course.m_scale);
        m_weight = new Weight(course.m_weight);
        m_cStudents = new ArrayList();
        m_scales = new ArrayList();
        for(Scale scale : course.m_scales){
            m_scales.add(new Scale(scale));
        }
        
        m_cStudents = new ArrayList();
        for (Student student : course.m_cStudents){
            m_cStudents.add(new Student(student));
        }
        
        m_categories = new ArrayList();
        for (Category category : course.m_categories){
            m_categories.add(new Category(category, this));
        }
    }
    public String getName(){
        return m_name;
    }
    public void setName(String name){
        m_name = name;
    }
    public String getYear(){
        return m_year;
    }
    public void setYear(String name){
        m_year = name;
    }
    public String getTerm(){
        return m_term;
    }
    public void setTerm(String term){
        m_term = term;
    }
    public boolean addStudent(String name, String id){
        
        for(Student student : m_cStudents){
            if(student.getID().equals(id)){
                return false;
            }
        }
        Student temp = new Student(name, id);
        m_cStudents.add(temp);
        
        // Add an empty grade for each assignment for the new student
        for (Category category : m_categories) {
            for (Assignment assignment : category.getAssignments()) {
                assignment.addStudent(id);
            }
        }
        
        fireStudentAdded(temp);
        return true;
    }
    public boolean addStudent(Student newStudent){
        
        for(Student student : m_cStudents){
            if(student == newStudent){
                return false;
            }
        }
        m_cStudents.add(newStudent);
        
        // Add an empty grade for each assignment for the new student
        for (Category category : m_categories) {
            for (Assignment assignment : category.getAssignments()) {
                assignment.addStudent(newStudent.getID());
            }
        }
        
        fireStudentAdded(newStudent);
        return true;
    }
    public boolean removeStudent(String id){
        if(m_cStudents.isEmpty()){
            return false;
        }else{
            for(int i = 0; i < m_cStudents.size(); i++){
                Student temp = m_cStudents.get(i);
                if(temp.getID().equals(id)){
                    m_cStudents.remove(i);
                    
                    // Remove the grades associated with this student for each assignment
                    for (Category category : m_categories) {
                        for (Assignment assignment : category.getAssignments()) {
                            assignment.removeStudent(id);
                        }
                    }
                    
                    fireStudentRemoved(temp);
                    return true;
                }
            }
            return false;
        }
    }
    
    public Student getStudent(String id){
        if(m_cStudents.isEmpty()){
            return null;
        }else{
            for(Student student : m_cStudents){
                if(student.getID().equals(id)){
                    return student;
                }
            }
            return null;
        }
    }
    public ArrayList<Student> getStudents() {
        return new ArrayList<>(m_cStudents);
    }
    
    public ArrayList<Category> getCategories() {
        return new ArrayList<>(m_categories);
    }
    
    public Category getCategory(String name) {
        for (Category category : m_categories) {
            if (category.getName().equals(name)) {
                return category;
            }
        }
        return null;
    }
    
    public void addCategory(Category category){
        Category newCategory = new Category(category, this);
        m_categories.add(newCategory);
        fireCategoryAdded(newCategory);
    }
    public boolean addCategory(String name){
        for (Category category : m_categories) {
            if (category.getName().equals(name)) {
                return false;
            }
        }
        Category category = new Category(name, this);
        m_categories.add(category);
        fireCategoryAdded(category);
        return true;
    }
    public boolean removeCategory(String name){
        for (int i = 0; i < m_categories.size(); i++) {
            if (m_categories.get(i).getName().equals(name)) {
                Category category = m_categories.remove(i);
                fireCategoryRemoved(category);
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Scale> getScales(){
        return m_scales;
    }
    public void addScale(Scale scale){
        m_scales.add(scale);
    }
    @Override
    public void setScale(Scale scale){
        m_scale = new Scale(scale);
    }
    @Override
    public HashMap getGrade(){
        if(m_cStudents.isEmpty() || m_categories.isEmpty()){
            return null;
        }else{
            
            HashMap<String, Grade> grades = new HashMap<>();
            for(Student student : m_cStudents){
                String id = student.getID();
                Grade tempGrade = new Grade("", null);
//                ArrayList<Grade> studentGradeList = new ArrayList();
                for(Category category : m_categories){ // for each category
                    ArrayList<Assignment> assign = category.getAssignments();
                    ArrayList<Grade> studentGradeList = new ArrayList();
                    for(Assignment assignment : assign){ // for each assignment in the category
                        studentGradeList.add(assignment.getStudentGrade(id));
                    }
                    sort(studentGradeList);
                    int counter = category.getDropAssignment();
                    for(int i = 0; i < studentGradeList.size(); i++){
                        Grade tGrade = studentGradeList.get(i);
                        if(counter > 0){
                            if(!tGrade.isNull()){
                                counter--;
                            }
                        }else{
                            if(!tGrade.isNull()){
                                double assignmentGrade = studentGradeList.get(i).getValue(); //get the student's assignment grade
                                double assignmentValue = studentGradeList.get(i).getMaxValue(); //get the assignment value
                                assignmentGrade += tempGrade.getValue(); //Add to the student toal points
                                assignmentValue += tempGrade.getMaxValue(); //Add to the total point
                                tempGrade.setValue(assignmentGrade); //Set the student toal point
                                tempGrade.setMaxValue(assignmentValue); //Set the total point
                            }
                        }
                    }
                }
                grades.put(id, tempGrade);
            }
            return grades;
        }
    }
    
    @Override
    public Scale getScale(){
        return m_scale;
    }
    public void setScale(double[] values){
        try{
            m_scale.setValues(values);
        }catch(InvalidScaleException e){
            throw e;
        }
    }
    @Override
    public Weight getWeight(){
        return m_weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Course))return false;
        Course other = (Course)obj;
        return this.m_name.equals(other.m_name) &&
                this.m_year.equals(other.m_year) &&
                this.m_term .equals(other.m_term);
    }
    
        /**
     * Convert the grade into its string representation.
     * @return 
     */
    public String toString() {
        return m_name + " (" + m_term + " " + m_year + ")";
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeUTF(m_name);
        stream.writeUTF(m_year);
        stream.writeUTF(m_term);
        stream.writeObject(m_cStudents);
        stream.writeObject(m_categories);
        stream.writeObject(m_scale);
        stream.writeObject(m_weight);
        stream.writeObject(m_scales);
    }

    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        m_name = stream.readUTF();
        m_year = stream.readUTF();
        m_term = stream.readUTF();
        m_cStudents = (ArrayList<Student>)stream.readObject();
        m_categories = (ArrayList<Category>)stream.readObject();
        m_scale = (Scale)stream.readObject();
        m_weight = (Weight)stream.readObject();
        m_scales =  (ArrayList<Scale>)stream.readObject();
        
        m_studentListerners = new ArrayList<>();
        m_categoryListerners = new ArrayList<>();
    }

    @Override
    public IGradeable getCopy() {
        return new Course(this);
    }
    
    public interface StudentListener { 
        void studentAdded(Course course, Student student);
        void studentRemoved(Course course, Student student);
    }
    
    private ArrayList<StudentListener> m_studentListerners = new ArrayList<>();
    public void addStudentListener(StudentListener listener) {
        m_studentListerners.add(listener);
    }
    
    public void removeStudentListner(StudentListener listener) {
        m_studentListerners.remove(listener);
    }
    
    private void fireStudentAdded(Student student) {
        for (StudentListener listener : m_studentListerners) {
            listener.studentAdded(this, student);
        }
    }
    
    private void fireStudentRemoved(Student student) {
        for (StudentListener listener : m_studentListerners) {
            listener.studentRemoved(this, student);
        }
    }
    
    /* --- Action Listeners for Categories added and removed --- */
    private ArrayList<CategoryListener> m_categoryListerners = new ArrayList<>();
    public interface CategoryListener { 
        void categoryAdded(Category category);
        void categoryRemoved(Category category);
    }
    
    /**
     * Adds a listener.
     * @param listener The listener to add.
     */
    public void addCategoryListener(CategoryListener listener) {
        m_categoryListerners.add(listener);
    }
    
    /**
     * Removes a listener.
     * @param listener The listener to remove.
     */
    public void removeCategoryListner(CategoryListener listener) {
        m_categoryListerners.remove(listener);
    }
    
    /**
     * Fire added event.
     * @param parent
     * @param item 
     */
    private void fireCategoryAdded(Category item) {
        for (CategoryListener listener : m_categoryListerners) {
            listener.categoryAdded(item);
        }
    }
    
    /**
     * Fire removed event.
     * @param parent
     * @param item 
     */
    private void fireCategoryRemoved(Category item) {
        for (CategoryListener listener : m_categoryListerners) {
            listener.categoryRemoved(item);
        }
    }
}
