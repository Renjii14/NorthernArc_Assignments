package org.northernarc.unittestingdemo.service;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CustomerServiceTest {

    private static CalculatorService calculatorService;
    @BeforeAll
    static void init(){
        System.out.println("Initialising test");
        calculatorService=new CalculatorService();
    }

    @BeforeEach
    void setUp(){
        System.err.println("Setting up test");
    }

    @Test
    void testadd(){
        int actualResult=calculatorService.add(10, 20);
        int expectedResult=30;
        assert actualResult==expectedResult;
        assertEquals(6,calculatorService.add(3,3));
    }

    @Test
    void testsubtract(){
        int actualResult= calculatorService.subtract(20, 10);
        int expectedResult=10;
        assert actualResult==expectedResult;
    }

    @Test
    void testmultiply(){
        int actualResult= calculatorService.multiply(10, 20);
        int expectedResult=200;
        assert actualResult==expectedResult;
    }

    @Test
    void testdivide(){
        int actualResult= calculatorService.divide(20, 10);
        int expectedResult=2;
        assert actualResult==expectedResult;
    }

    @AfterAll
    static void tearDown(){
        System.out.println("Cleaning up after all tests");
        calculatorService=null;
    }
    @AfterEach
    void teardown(){
        System.out.println("Cleaning up after each test");
        calculatorService=null;
    }


}
