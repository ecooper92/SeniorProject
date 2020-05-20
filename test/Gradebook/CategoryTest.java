/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Gradebook;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tom
 */
public class CategoryTest {
    
    public CategoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class Category.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Category instance = new Category("cat1", null);
        String expResult = "cat1";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAssignment method, of class Category.
     */
    @Test
    public void testGetAssignment() {
        System.out.println("getAssignment");
        String name = "test1";
        ArrayList<Student> students = new ArrayList();
        students.add(new Student("Tom Nguyen","a123"));
        students.add(new Student("Tommy Nguyen","a1234"));
        
        Category instance = new Category("Test", null);
        Assignment expResult = new Assignment(name,100, null);
        String temp1 = expResult.getName();
        instance.addAssignment(name, 100, students);
        Assignment result = instance.getAssignment(name);
        String temp2 = result.getName();
        assertEquals(temp1, temp2);
    }

    /**
     * Test of removeAssignment method, of class Category.
     */
    @Test
    public void testRemoveAssignment() {
        System.out.println("removeAssignment");
        String name = "Test1";
        Category instance = new Category("Tests", null);
        ArrayList<Student> students = new ArrayList();
        instance.addAssignment(name, 100, students);
        boolean expResult = true;
        boolean result = instance.removeAssignment(name);
        assertEquals(expResult, result);
        expResult = false;
        result = instance.removeAssignment(name);
        assertEquals(expResult, result);
    }
    
}
