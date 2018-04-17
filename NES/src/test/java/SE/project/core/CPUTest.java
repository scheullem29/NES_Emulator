/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SE.project.core;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Tony
 */
public class CPUTest {
    
    public CPUTest() {
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
     * Test of preIndexedIndirectAddressing method, of class CPU.
     */
    @Test
    public void testPreIndexedIndirectAddressing() {    
        System.out.println("preIndexedIndirectAddressing"); 
        CPU instance = new CPU();
        byte value = 0x00;
        instance.setIndexRegX((byte)0x00);
        instance.getCPUmemory()[0]=0x00;
        instance.getCPUmemory()[1]=(byte)0xff;
        int expResult = 0xff00;
        int result = instance.preIndexedIndirectAddressing(value);
        assertEquals(expResult, result);                            //zero test
        
        value = (byte)0x40;
        instance.setIndexRegX((byte)0x40);
        instance.getCPUmemory()[0x80]=0x00;
        instance.getCPUmemory()[0x81]=(byte)0xff;
        expResult = 0xff00;
        result = instance.preIndexedIndirectAddressing(value);
        assertEquals(expResult, result);                            //normal test
        
        value = (byte)0x80;
        instance.setIndexRegX((byte)0x00);
        instance.getCPUmemory()[0x80]=0x00;
        instance.getCPUmemory()[0x81]=(byte)0xff;
        expResult = 0xff00;
        result = instance.preIndexedIndirectAddressing(value);
        assertEquals(expResult, result);                            //leading 1 test
        
        value = 0x00;
        instance.setIndexRegX((byte)0x80);
        instance.getCPUmemory()[0x80]=0x00;
        instance.getCPUmemory()[0x81]=(byte)0xff;
        expResult = 0xff00;
        result = instance.preIndexedIndirectAddressing(value);
        assertEquals(expResult, result);                            //leading 1 test
        
        value = (byte)0xff;
        instance.setIndexRegX((byte)0x01);
        instance.getCPUmemory()[0]=0x00;
        instance.getCPUmemory()[1]=(byte)0xff;
        expResult = 0xff00;
        result = instance.preIndexedIndirectAddressing(value);
        assertEquals(expResult, result);                            //carryover test
    }

    /**
     * Test of postIndexedIndirectAddressing method, of class CPU.
     */
    @Test
    public void testPostIndexedIndirectAddressing() {
        System.out.println("postIndexedIndirectAddressing");
        CPU instance = new CPU();
        byte value = 0;
        instance.setIndexRegY((byte)0x00);
        instance.getCPUmemory()[0]=0x00;
        instance.getCPUmemory()[1]=(byte)0xff;
        int expResult = 0xff00;
        int result = instance.postIndexedIndirectAddressing(value);
        assertEquals(expResult, result);
        
        value = 0x40;
        instance.setIndexRegY((byte)0x40);
        instance.getCPUmemory()[0x40]=0x00;
        instance.getCPUmemory()[0x41]=(byte)0xff;
        expResult = 0xff40;
        result = instance.postIndexedIndirectAddressing(value);
        assertEquals(expResult, result);
        
        value = 0;
        instance.setIndexRegY((byte)0x80);
        instance.getCPUmemory()[0]=0x00;
        instance.getCPUmemory()[1]=(byte)0xff;
        expResult = 0xff80;
        result = instance.postIndexedIndirectAddressing(value);
        assertEquals(expResult, result);
        
        value = (byte)0x80;
        instance.setIndexRegY((byte)0x00);
        instance.getCPUmemory()[0x80]=0x00;
        instance.getCPUmemory()[0x81]=(byte)0xff;
        expResult = 0xff00;
        result = instance.postIndexedIndirectAddressing(value);
        assertEquals(expResult, result);
        
        value = 1;
        instance.setIndexRegY((byte)0x00);
        instance.getCPUmemory()[0]=(byte)0xff;
        instance.getCPUmemory()[1]=(byte)0xff;
        expResult = 0x0000;
        result = instance.postIndexedIndirectAddressing(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of indexedAddressingZeroPageX method, of class CPU.
     */
    @Test
    public void testIndexedAddressingZeroPageX() {
        System.out.println("indexedAddressingZeroPage");
        CPU instance = new CPU();
        byte value = 0;
        instance.setIndexRegX((byte)0x00);
        int expResult = 0;
        int result = instance.indexedAddressingZeroPageX(value);
        assertEquals(expResult, result);
        
        value = 0x40;
        instance.setIndexRegX((byte)0x40);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageX(value);
        assertEquals(expResult, result);
        
        value = (byte)0x80;
        instance.setIndexRegX((byte)0x00);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageX(value);
        assertEquals(expResult, result);
        
        value = 0x00;
        instance.setIndexRegX((byte)0x80);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageX(value);
        assertEquals(expResult, result);
        
        value = 0x01;
        instance.setIndexRegX((byte)0xff);
        expResult = 0x00;
        result = instance.indexedAddressingZeroPageX(value);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of indexedAddressingZeroPageY method, of class CPU.
     */
    @Test
    public void testIndexedAddressingZeroPageY() {
        System.out.println("indexedAddressingZeroPage");
        CPU instance = new CPU();
        byte value = 0;
        instance.setIndexRegY((byte)0x00);
        int expResult = 0;
        int result = instance.indexedAddressingZeroPageY(value);
        assertEquals(expResult, result);
        
        value = 0x40;
        instance.setIndexRegY((byte)0x40);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageY(value);
        assertEquals(expResult, result);
        
        value = (byte)0x80;
        instance.setIndexRegY((byte)0x00);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageY(value);
        assertEquals(expResult, result);
        
        value = 0x00;
        instance.setIndexRegY((byte)0x80);
        expResult = 0x80;
        result = instance.indexedAddressingZeroPageY(value);
        assertEquals(expResult, result);
        
        value = 0x01;
        instance.setIndexRegY((byte)0xff);
        expResult = 0x00;
        result = instance.indexedAddressingZeroPageY(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of indexedAddressingAbsoluteX method, of class CPU.
     */
    @Test
    public void testIndexedAddressingAbsoluteX() {
        System.out.println("indexedAddressingAbsoluteX");
        CPU instance = new CPU();
        byte low = 0;
        byte high = 0;
        instance.setIndexRegX((byte)0x00);
        int expResult = 0;
        int result = instance.indexedAddressingAbsoluteX(low, high);
        assertEquals(expResult, result);
  
        low = 0x40;
        high = 0x40;
        instance.setIndexRegX((byte)0x40);
        expResult = 0x4080;
        result = instance.indexedAddressingAbsoluteX(low, high);
        assertEquals(expResult, result);
 
        low = (byte)0x80;
        high = 0x00;
        instance.setIndexRegX((byte)0x00);
        expResult = 0x0080;
        result = instance.indexedAddressingAbsoluteX(low, high);
        assertEquals(expResult, result);
        
        low = 0x00;
        high = 0x00;
        instance.setIndexRegX((byte)0x80);
        expResult = 0x0080;
        result = instance.indexedAddressingAbsoluteX(low, high);
        assertEquals(expResult, result);
        
        low = (byte)0xff;
        high = (byte)0xff;
        instance.setIndexRegX((byte)0x01);
        expResult = 0x0000;
        result = instance.indexedAddressingAbsoluteX(low, high);
        assertEquals(expResult, result);
    }

    /**
     * Test of indexedAddressingAbsoluteY method, of class CPU.
     */
    @Test
    public void testIndexedAddressingAbsoluteY() {
        System.out.println("indexedAddressingAbsoluteX");
        CPU instance = new CPU();
        byte low = 0;
        byte high = 0;
        instance.setIndexRegY((byte)0x00);
        int expResult = 0;
        int result = instance.indexedAddressingAbsoluteY(low, high);
        assertEquals(expResult, result);
  
        low = 0x40;
        high = 0x40;
        instance.setIndexRegY((byte)0x40);
        expResult = 0x4080;
        result = instance.indexedAddressingAbsoluteY(low, high);
        assertEquals(expResult, result);
 
        low = (byte)0x80;
        high = 0x00;
        instance.setIndexRegY((byte)0x00);
        expResult = 0x0080;
        result = instance.indexedAddressingAbsoluteY(low, high);
        assertEquals(expResult, result);
        
        low = 0x00;
        high = 0x00;
        instance.setIndexRegY((byte)0x80);
        expResult = 0x0080;
        result = instance.indexedAddressingAbsoluteY(low, high);
        assertEquals(expResult, result);
        
        low = (byte)0xff;
        high = (byte)0xff;
        instance.setIndexRegY((byte)0x01);
        expResult = 0x0000;
        result = instance.indexedAddressingAbsoluteY(low, high);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of indirectAddressing method, of class CPU.
     */
    @Test
    public void testIndirectAddressing() {
        System.out.println("indirectAddressing");
        CPU instance = new CPU();
        byte low = 0;
        byte high = 0;
        instance.getCPUmemory()[0] = 0x00;
        instance.getCPUmemory()[1] = (byte)0xff;
        int expResult = 0xff00;
        int result = instance.indirectAddressing(low, high);
        assertEquals(expResult, result);

        low = 0x00;
        high = 0x40;
        instance.getCPUmemory()[0x4000] = 0x00;
        instance.getCPUmemory()[0x4001] = (byte)0xff;
        expResult = 0xff00;
        result = instance.indirectAddressing(low, high);
        assertEquals(expResult, result);
        
        low = 0x00;
        high = (byte)0x80;
        instance.getCPUmemory()[0x8000] = 0x00;
        instance.getCPUmemory()[0x8001] = (byte)0xff;
        expResult = 0xff00;
        result = instance.indirectAddressing(low, high);
        assertEquals(expResult, result);
    }

    /**
     * Test of relativeAddressing method, of class CPU.
     */
    @Test
    public void testRelativeAddressing() {
        System.out.println("relativeAddressing");
        CPU instance = new CPU();
        byte value = 0x44;
        int expResult = 0x46;
        instance.setpgrmCtr(0);
        int result = instance.relativeAddressing(value);
        assertEquals(expResult, result);
        
        value = (byte)0xf8;
        expResult = 0x18;
        instance.setpgrmCtr(0x20);
        result = instance.relativeAddressing(value);
        assertEquals(expResult, result);
        
        value = (byte)0x00;
        expResult = 0x2;
        instance.setpgrmCtr(0x00);
        result = instance.relativeAddressing(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of absoluteAddressing method, of class CPU.
     */
    @Test
    public void testAbsoluteAddressing() {
        System.out.println("absoluteAddressing");
        CPU instance = new CPU();
        byte low = 0;
        byte high = 0;
        int expResult = 0;
        int result = instance.absoluteAddressing(low, high);
        assertEquals(expResult, result);
        
        low = 0x40;
        high = 0x00;
        expResult = 0x0040;
        result = instance.absoluteAddressing(low, high);
        assertEquals(expResult, result);
        
        low = 0x40;
        high = (byte)0x80;
        expResult = 0x8040;
        result = instance.absoluteAddressing(low, high);
        assertEquals(expResult, result);
    }
    
}
