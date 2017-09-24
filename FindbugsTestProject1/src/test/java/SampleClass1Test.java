/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import junit.framework.TestCase;

/**
 *
 * @author Dad
 */
public class SampleClass1Test extends TestCase {
    
    public SampleClass1Test(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of method1 method, of class SampleClass1.
     */
    public void testMethod1() {
        System.out.println("method1");
        SampleClass1 instance = new SampleClass1();
        instance.method1();
    
    }

    
}
