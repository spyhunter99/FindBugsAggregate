/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.spyhunter99.jacodotestproject2;

import junit.framework.TestCase;

/**
 *
 * @author Dad
 */
public class SampleClass2Test extends TestCase {
    
    public SampleClass2Test(String testName) {
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
     * Test of method1 method, of class SampleClass2.
     */
    public void testMethod1() {
        System.out.println("method1");
        SampleClass2 instance = new SampleClass2();
        instance.method1();
      
    }

    /**
     * Test of method2 method, of class SampleClass2.
     */
    public void testMethod2() {
        System.out.println("method2");
        SampleClass2 instance = new SampleClass2();
        instance.method2();
      
    }
    
}
