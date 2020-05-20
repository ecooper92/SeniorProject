/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Data;

import Gradebook.Grade;
import Gradebook.IGradeable;
import Gradebook.Scale;
import Gradebook.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Eric
 */
public class DataFactory {
    
    private DataFactory() { }
    
    public static Data getGradeDistributionChart(IGradeable gradeable) {
        
        HashMap<String, Grade> grades = gradeable.getGrade();
        Scale scale = gradeable.getScale();
        double weight = gradeable.getWeight().getValue();
        
        HashMap<String, Integer> letters = new HashMap<>();
        letters.put("A", 0);
        letters.put("B", 0);
        letters.put("C", 0);
        letters.put("D", 0);
        letters.put("F", 0);
        letters.put("-", 0);
        
        if (grades != null) {
            for (Grade grade : grades.values()) {
                if(!grade.isNull()){
                    String letter = scale.calculate(((grade.getValue() / grade.getMaxValue()) * weight) * 100);
                    letters.put(letter, letters.get(letter) + 1);
                }
            }
        }
        
        letters.remove("-");
        
        ArrayList<Entry<String, Integer>> sets = new ArrayList<>(letters.entrySet());
        Collections.sort(sets, new Comparator<Entry<String, Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        
        ArrayList<Object> xValues = new ArrayList<>();
        ArrayList<Number> yValues = new ArrayList<>();
        for (Entry<String, Integer> entry : sets) {
            xValues.add(entry.getKey());
            yValues.add(entry.getValue());
        }
        
        return new Chart(xValues, yValues);
    }
    
    public static Data getEnrolledStudentReport(ArrayList<Student> students) {
        
        String[][] data = new String[students.size()][];
        for (int i = 0; i < students.size(); i++) {
            
            String[] row = new String[2];
            row[0] = students.get(i).getID();
            row[1] = students.get(i).getName();

            data[i] = row;
        }
        return new Report(new String[] { "ID", "Name" }, data, false);
    }
    
    public static Data getGradeReport(IGradeable gradeable, ArrayList<Student> students) {
        
       HashMap<String, Grade> grades = gradeable.getGrade();
        Scale scale = gradeable.getScale();
        double weight = gradeable.getWeight().getValue();
        
        ArrayList<String[]> data = new ArrayList<>();
        for (Student student : students) {
            
            if (grades.containsKey(student.getID())) {
                
                Grade grade = grades.get(student.getID());
                Double number;
                String letter;
                if(!grade.isNull()){
                    number = ((grade.getValue() / grade.getMaxValue()) * weight) * 100;
                    letter = scale.calculate(number);
                }else{
                    number = null;
                    letter = "-";
                }
                
                String[] row = new String[4];
                row[0] = student.getID();
                row[1] = student.getName();
                
                if ( number != null &&!Double.isNaN(number))
                    row[2] = number.toString();
                else
                    row[2] = "-";
                
                row[3] = letter;
                
                data.add(row);
            }
        }
        
        String[][] cells = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            cells[i] = data.get(i);
        }
        
        return new Report(new String[] { "ID", "Name", "Grade", "Letter" }, cells, true);
    }
}
