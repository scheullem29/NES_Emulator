package SE.project.core;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class InterpreterTest {
    
    public InterpreterTest() {
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
     * Test of readInstructions method, of class Interpreter.
     */
    @org.junit.Test
    public void testReadInstructions() {
        System.out.println("readInstructions");
        CPU nes = null;
        Interpreter instance = new Interpreter();
        instance.readInstructions(nes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
