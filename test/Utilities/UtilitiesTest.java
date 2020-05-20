/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Admin
 */
public class UtilitiesTest {
    
    private class TestObject {
        
        private int m_value = 0;
        public TestObject(int value) {
            
        }
        
        public int getValue() {
            return m_value;
        }
        
        public void setValue(int value) {
            m_value = value;
        }
    }
    
    private class TestReversible extends Reversible {

        private TestObject m_testObject = null;
        private int m_oldValue = 0;
        private int m_newValue = 0;
        public TestReversible(TestObject testObject, int oldValue, int newValue) {
            m_testObject = testObject;
            m_oldValue = oldValue;
            m_newValue = newValue;
        }
        
        @Override
        public void undo() {
            m_testObject.setValue(m_oldValue);
        }

        @Override
        public void redo() {
            m_testObject.setValue(m_newValue);
        }
        
    }
        
    TestObject m_object1 = null;
    TestObject m_object2 = null;
    
    public UtilitiesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        m_object1 = new TestObject(100);
        m_object2 = new TestObject(200);
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testAddReversible() {
        
    }
}
