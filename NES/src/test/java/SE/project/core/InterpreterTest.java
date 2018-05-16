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
    public static void setUpClass() 
    {
        
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
    public void testprocessByte() {
        System.out.println("processByte");
        CPU nes = new CPU();
        Interpreter instance = new Interpreter();
        
        
        System.out.println("SEI");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = 0x78;
        String temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(true, nes.interruptDisableStatus());
        assertEquals(0x8001,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        
        System.out.println("CLD");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xd8;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(false, nes.decimalModeFlag());
        assertEquals(0x8001,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        
        System.out.println("LDA Immediate");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa9;
        nes.getCPUmemory()[0x8001] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x01, nes.getAccumulator());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA zero page"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa5;
        nes.getCPUmemory()[0x8001] = 0x02;
        nes.getCPUmemory()[0x0002] = (byte)0x80;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        int val = nes.getAccumulator() & 0xff;
        assertEquals(0x80, val);
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(3,nes.getcycleCtr());
        assertEquals(true, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA zero page,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xb5;
        nes.getCPUmemory()[0x8001] = 0x01;
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x0000] = 0x01;
        nes.getCPUmemory()[0x0001] = 0x01;
        nes.getCPUmemory()[0x0002] = (byte)0x00;
        nes.getCPUmemory()[0x0003] = 0x01;
        nes.getCPUmemory()[0x0004] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0, nes.getAccumulator());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(true, nes.zeroFlag());
        
        System.out.println("LDA absolute"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xad;
        nes.getCPUmemory()[0x8001] = (byte)0x02;
        nes.getCPUmemory()[0x8002] = (byte)0x09;
        nes.getCPUmemory()[0x0902] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getAccumulator());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA absolute,x boundry cross"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbd;
        nes.getCPUmemory()[0x8001] = (byte)0xff;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0200] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getAccumulator());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA absolute,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbd;
        nes.getCPUmemory()[0x8001] = (byte)0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0101] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getAccumulator());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA absolute,y boundry cross"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xb9;
        nes.getCPUmemory()[0x8001] = (byte)0xff;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0200] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getAccumulator());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA absolute,y"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xb9;
        nes.getCPUmemory()[0x8001] = (byte)0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0101] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getAccumulator());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());

        System.out.println("LDA pre indexed indirect"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xa1;
        nes.getCPUmemory()[0x8001] = (byte)0x01;
        nes.getCPUmemory()[0x0002] = (byte)0x40;
        nes.getCPUmemory()[0x0003] = (byte)0x09;
        nes.getCPUmemory()[0x0940] = (byte)0x42;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x42, nes.getAccumulator());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(6,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA post indexed indirect"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xb1;
        nes.getCPUmemory()[0x8001] = (byte)0x01;
        nes.getCPUmemory()[0x0001] = (byte)0x40;
        nes.getCPUmemory()[0x0002] = (byte)0x09;
        nes.getCPUmemory()[0x0941] = (byte)0x42;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x42, nes.getAccumulator());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDA post indexed indirect boundry cross"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xb1;
        nes.getCPUmemory()[0x8001] = (byte)0x01;
        nes.getCPUmemory()[0x0001] = (byte)0xff;
        nes.getCPUmemory()[0x0002] = (byte)0x09;
        nes.getCPUmemory()[0x0a00] = (byte)0x42;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x42, nes.getAccumulator());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(6,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("SDA absolute");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0x8d;
        nes.getCPUmemory()[0x8001] = 0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x90;
        nes.setAccumulator((byte)0x01);
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(nes.getCPUmemory()[0x9000], 0x01);
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        
        //"A2" //ldx immediate
        System.out.println("LDX Immediate");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa2;
        nes.getCPUmemory()[0x8001] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x01, nes.getIndexRegX());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"A6" //ldx zeropage
        System.out.println("LDX zero page"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa6;
        nes.getCPUmemory()[0x8001] = 0x02;
        nes.getCPUmemory()[0x0002] = (byte)0x80;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        val = nes.getIndexRegX()& 0xff;
        assertEquals(0x80, val);
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(3,nes.getcycleCtr());
        assertEquals(true, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"B6" //ldx indexed Addressing Zero Page
        System.out.println("LDX zero page,y"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xb6;
        nes.getCPUmemory()[0x8001] = 0x01;
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x0000] = 0x01;
        nes.getCPUmemory()[0x0001] = 0x01;
        nes.getCPUmemory()[0x0002] = (byte)0x00;
        nes.getCPUmemory()[0x0003] = 0x01;
        nes.getCPUmemory()[0x0004] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0, nes.getIndexRegX());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(true, nes.zeroFlag());
        
        //"AE" //ldx absolute addressing
        System.out.println("LDX absolute"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xae;
        nes.getCPUmemory()[0x8001] = (byte)0x02;
        nes.getCPUmemory()[0x8002] = (byte)0x09;
        nes.getCPUmemory()[0x0902] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegX());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"BE" //ldx indexed addressing absolute y
        System.out.println("LDX absolute,y"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbe;
        nes.getCPUmemory()[0x8001] = (byte)0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0101] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegX());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDX absolute,y boundry cross"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegY((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbe;
        nes.getCPUmemory()[0x8001] = (byte)0xff;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0200] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegX());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        
        //"A0": //immediate
        System.out.println("LDY Immediate");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa0;
        nes.getCPUmemory()[0x8001] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x01, nes.getIndexRegY());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"A4": //zero page
        System.out.println("LDY zero page"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xa4;
        nes.getCPUmemory()[0x8001] = 0x02;
        nes.getCPUmemory()[0x0002] = (byte)0x80;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        val = nes.getIndexRegY()& 0xff;
        assertEquals(0x80, val);
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(3,nes.getcycleCtr());
        assertEquals(true, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"B4": // indexed Addressing Zero Page
        System.out.println("LDY zero page,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xb4;
        nes.getCPUmemory()[0x8001] = 0x01;
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x0000] = 0x01;
        nes.getCPUmemory()[0x0001] = 0x01;
        nes.getCPUmemory()[0x0002] = (byte)0x00;
        nes.getCPUmemory()[0x0003] = 0x01;
        nes.getCPUmemory()[0x0004] = 0x01;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0, nes.getIndexRegY());
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(true, nes.zeroFlag());
        
        //"AC": // absolute addressing
        System.out.println("LDY absolute"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xac;
        nes.getCPUmemory()[0x8001] = (byte)0x02;
        nes.getCPUmemory()[0x8002] = (byte)0x09;
        nes.getCPUmemory()[0x0902] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegY());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"BC": // indexed addressing absolute X
        System.out.println("LDY absolute,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbc;
        nes.getCPUmemory()[0x8001] = (byte)0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0101] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegY());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(4,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        System.out.println("LDY absolute,x boundry cross"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0xbc;
        nes.getCPUmemory()[0x8001] = (byte)0xff;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0200] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getIndexRegY());
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        //"9A":  //txs
        System.out.println("TXS");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0x9a;
        nes.setIndexRegX((byte)0x40);
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40, nes.getStackPointerByte());
        assertEquals(0x8001,nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        
        
        //"20": //jsr
        System.out.println("JSR");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setStackPointer((byte)0xff);
        nes.getCPUmemory()[0x8000] = (byte)0x20;
        nes.getCPUmemory()[0x8001] = (byte)0x40;
        nes.getCPUmemory()[0x8002] = (byte)0x80;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x01fd, nes.getStackPointer());
        assertEquals(0x8040,nes.getpgrmCtr());
        assertEquals(6,nes.getcycleCtr());
        assertEquals((byte)0x80,nes.getCPUmemory()[0x01ff]); //not sure if this is the correct order
        assertEquals((byte)0x03,nes.getCPUmemory()[0x01fe]);
        
        //lsr "4A" accumulator
        System.out.println("LSR Accumulator");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setAccumulator((byte)0x02);
        nes.getCPUmemory()[0x8000] = (byte)0x4a;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x01, nes.getAccumulator());
        assertEquals(0x8001, nes.getpgrmCtr());
	assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        assertEquals(false, nes.carryFlag());
        
        System.out.println("LSR Accumulator zero carry");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setAccumulator((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0x4a;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x00, nes.getAccumulator());
        assertEquals(0x8001, nes.getpgrmCtr());
	assertEquals(2,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(true, nes.zeroFlag());
        assertEquals(true, nes.carryFlag());
        
        //lsr "46" zero page
        System.out.println("LSR zero page"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0x46;
        nes.getCPUmemory()[0x8001] = 0x02;
		
        nes.getCPUmemory()[0x0002] = (byte)0x80;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x40,nes.getCPUmemory()[0x0002]);
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(5,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        assertEquals(false, nes.carryFlag());
        
        //lsr "56" zero page,x
        System.out.println("LSR zero page,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0x56;
        nes.getCPUmemory()[0x8001] = 0x01;
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x0002] = (byte)0x04;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x02, nes.getCPUmemory()[0x0002]);
        assertEquals(0x8002,nes.getpgrmCtr());
        assertEquals(6,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        assertEquals(false, nes.carryFlag());
        
        //lsr "4E" absolute
        System.out.println("LSR absolute"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0x4e;
        nes.getCPUmemory()[0x8001] = (byte)0x02;
        nes.getCPUmemory()[0x8002] = (byte)0x09;
		
        nes.getCPUmemory()[0x0902] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x20, nes.getCPUmemory()[0x0902]);
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(6,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        assertEquals(false, nes.carryFlag());
        
        //lsr "5E" absolute, x
        System.out.println("LSR absolute,x"); 
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.setIndexRegX((byte)0x01);
        nes.getCPUmemory()[0x8000] = (byte)0x5e;
        nes.getCPUmemory()[0x8001] = (byte)0x00;
        nes.getCPUmemory()[0x8002] = (byte)0x01;
        nes.getCPUmemory()[0x0101] = (byte)0x40;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x20,nes.getCPUmemory()[0x0101]);
        assertEquals(0x8003,nes.getpgrmCtr());
        assertEquals(7,nes.getcycleCtr());
        assertEquals(false, nes.signFlag());
        assertEquals(false, nes.zeroFlag());
        assertEquals(false, nes.carryFlag());
        
        //"EA" nop
        System.out.println("No-op");
        nes.setpgrmCtr(0x8000);
        nes.setcycleCtr(0);
        nes.getCPUmemory()[0x8000] = (byte)0xea;
        temp = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
        
        instance.processByte(temp, nes);
        assertEquals(0x8001, nes.getpgrmCtr());
        assertEquals(2,nes.getcycleCtr());
    }
    
}
