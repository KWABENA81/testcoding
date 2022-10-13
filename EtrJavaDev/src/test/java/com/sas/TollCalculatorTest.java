package com.sas;

import org.testng.annotations.Test;

@Test
public class TollCalculatorTest {
    @Test
    void setup() {
        System.out.println("Inside setup");
    }

    @Test
    void testSteps() {
        System.out.println("Inside test step");
    }

    @Test
    void tearDown() {
        System.out.println("Inside TearDown");
    }
}
