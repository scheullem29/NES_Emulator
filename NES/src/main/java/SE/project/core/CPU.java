package SE.project.core;
hello

public class CPU 
{
    	private byte accumulator, indexRegX, indexRegY, stackPtr, statusReg;
	private int pgrmCtr;
        private int cycleCtr = 0;
	private byte[] CPUmemory;
	private byte[] PPUmemory;
	private byte[] OAMmemory;
        private Interpreter Inter;
        private boolean pageBoundryCrossed = false;
        private boolean interruptState = false;
	
	public CPU()
	{
		this.accumulator = 0x00;
		this.indexRegX = 0x00;
		this.indexRegY = 0x00;
		this.stackPtr = 0x00;
		this.pgrmCtr = 0x00;
		this.statusReg = 0x00;
		this.CPUmemory = new byte[0x10000];
		this.PPUmemory = new byte[0x4000];
		this.OAMmemory = new byte[0x100];
                Inter = new Interpreter();
                this.CPUmemory[0x2002] = (byte) 0x80;
                //this.CPUmemory[0x07ff] = (byte) 0xA5;
		
	}
        
        public void setInterruptState(boolean in)
        {
            this.interruptState = in;
        }
        
        public boolean getInterruptState()
        {
            return this.interruptState;
        }
        
        public void increasePgrmCtr(int amt)
        {
            this.pgrmCtr = pgrmCtr + amt;
        }
        
        public void increaseCycleCtr(int amt)
        {
            this.cycleCtr = cycleCtr + amt;
        }   
        
        public byte getStackPointerByte()
        {
            return this.stackPtr;
        }
        
        public byte getStatusReg()
        {
            return this.statusReg;
        }
        
        public void setStatusReg(byte val)
        {
            this.statusReg = val;
        }
        
        public boolean pageBoundryCrossed()
        {
            return this.pageBoundryCrossed;
        }
        
        public void setcycleCtr(int cycles){
            cycleCtr = cycles;
        }
        
        public int getcycleCtr(){
            return cycleCtr;
        }
        
        public void printMemory(){
            Inter.readInstructions(this);
        }
        
        public void setAccumulator(byte val)
        {
            this.accumulator = val;
        }
        
        public byte getAccumulator()
        {
            return this.accumulator;
        }
        

        public void setIndexRegX(byte x) 
        {
            this.indexRegX = x;
        }

        public byte getIndexRegX()
        {
            return this.indexRegX;
        }

        public void setIndexRegY(byte y)
        {
            this.indexRegY = y;
        }

        public byte getIndexRegY()
        {
            return this.indexRegY;
        }

        public void setStackPointer(byte val)
        {
            this.stackPtr = val;
        }

        public short getStackPointer()
        {
            return (short)(0x0100 | (this.stackPtr & 0xff));
        }

        public byte[] getCPUmemory(){
            return CPUmemory;
        }
        
        public byte[] getPPUmemory(){
            return PPUmemory;
        }
	
        public void setpgrmCtr(int pgrmCtr){
            this.pgrmCtr = pgrmCtr;
        }
        
        public int getpgrmCtr(){
            return pgrmCtr;
        }
		
	public boolean decimalModeFlag()
	{
		return( (statusReg&0x08) == 0x08);
	}
        
        public void decimalModeFlagClear()
	{
		statusReg = (byte)(statusReg&0xf7);
	}
        
        public void decimalModeFlagSet()
	{
		statusReg = (byte)(statusReg|0x08);
	}
        
        /**
	 * Shows the status of the sign flag.
	 * @return true = set, false = clear
	 */
	public boolean signFlag()
	{
		return( (statusReg&0x80) == 0x80);
	}
	
        public void signFlagClear()
	{
		statusReg = (byte)(statusReg&0x7f);
	}
        
        public void signFlagSet()
	{
            statusReg = (byte)(statusReg|0x80);
	}
	/**
	 * Shows the status of the overflow flag.
	 * @return true = set, false = clear
	 */
	public boolean overflowFlag()
	{
		return( (statusReg&0x40) == 0x40);
	}
	
        public void overflowFlagClear()
	{
		statusReg =  (byte)(statusReg&0xbf);
	}
        
        public void overflowFlagSet()
	{
		statusReg = (byte)(statusReg|0x40);
	}
        
	/**
	 * Shows break status
	 * @return true = set, false = clear
	 */
	public boolean breakStatus()
	{
		return( (statusReg&0x10) == 0x10);
	}
	
        public void breakStatusClear()
	{
		statusReg = (byte)(statusReg&0xef);
	}
        
        public void breakStatusSet()
	{
		statusReg = (byte)(statusReg|0x10);
	}
        
	/**
	 * Shows the interrupt disabled status
	 * @return true = interrupts disabled, false = interrupts enabled
	 */
	public boolean interruptDisableStatus()
	{
		return( (statusReg&0x04) == 0x04);
	}
	
        public void interruptDisableStatusSet()
        {
            statusReg = (byte)(statusReg|0x04);
        }
        
        public void interruptDisableStatusClear()
        {
            statusReg = (byte)(statusReg&0x0fb);
        }
        
	/**
	 * Shows the zero flag status
	 * @return true = set, false = clear
	 */
	public boolean zeroFlag()
	{
		return( (statusReg&0x02) == 0x02);
	}
	
        public void zeroFlagClear()
	{
		statusReg = (byte)(statusReg&0xfd);
	}
        
        public void zeroFlagSet()
	{
		statusReg = (byte)(statusReg|0x02);
	}
        
	/**
	 * Shows the carry flag status
	 * @return true = set, false = clear
	 */
	public boolean carryFlag()
	{
            return( (statusReg&0x01) == 0x01);
	}
	
        public void carryFlagSet()
	{
            statusReg = (byte)(statusReg&0xfe);
	}
        
        public void carryFlagClear()
	{
            statusReg = (byte)(statusReg|0x01);
	}
        
	/**
	 * Calculates the address for pre-indexed indirect addressing. ASM ex: AND ($20, X)
	 * Takes the value passed in and adds it to the value of index register X, goes to the memory location for the low byte
	 * and that location + 1 for the high byte.
	 * Ignores any carryover.
	 * @param value contents of register X will have this value added to it for the low byte and this value + 1 for the high byte
	 * @return the pre-indexed indirect address.
	 */
	protected int preIndexedIndirectAddressing(byte value)
	{
                int place = (indexRegX + value) & 0xff;
		return (((CPUmemory[place+1]  <<8)|CPUmemory[place]) & 0xffff); //masking reg X and memory contents to prevent any issues with the two's complement representations padding the front bits with 1
	}
	
	/**
	 * Calculates the address for post-index indirect addressing. ASM ex: AND ($20),Y
	 * Takes the value passed and finds that location and the next location in the zero page.
	 * takes the memory[value+1] as the high byte for the address and the memory[value] as
	 * the low byte for the address. It then adds the contents of index register Y to this
	 * address to get the address. Ignores any carryover.
	 * @param value address location in memory for the low bytes of the address to be added to Y. Value +1 is the high bytes of the address.
	 * @return the post-indexed indirect address.
	 */
	protected int postIndexedIndirectAddressing(byte value)
	{
                pageBoundryCrossed = false;
                if(((CPUmemory[value & 0xff] & 0xff) + (indexRegY & 0xff)) > 0xff)
                {
                    pageBoundryCrossed = true;
                }
		return ((((CPUmemory[(value & 0xff)+1] << 8)|(CPUmemory[value & 0xff] & 0xff)) + (indexRegY & 0xff)) & 0xffff); //masking reg Y and CPUmemory[value] to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address for indexed addressing with zero page. ASM ex: AND $20, X
	 * Takes the value passed in and adds it to the value of index register X. Only returns a one byte number.
	 * Ignores any carryover.
	 * @param value value to be added to contents of register X
	 * @return the indexed address.
	 */
	protected int indexedAddressingZeroPageX(byte value)
	{
		return (value + (indexRegX & 0xff))&(0xff); //masking reg X to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
        /**
	 * Calculates the address for indexed addressing with zero page. ASM ex: LDX $20, Y
	 * Takes the value passed in and adds it to the value of index register Y. Only returns a one byte number.
	 * Ignores any carryover.
	 * @param value value to be added to contents of register T
	 * @return the indexed address.
	 */
	protected int indexedAddressingZeroPageY(byte value)
	{
		return (value + (indexRegY & 0xff))&(0xff); //masking reg X to prevent any issues with the two's complement representations padding the front bits with 1s
	}
        
	/**
	 * Calculates the address for indexed addressing with absolute addressing and the X register. ASM ex: AND $3232,X
	 * Takes the value passed and adds it the the value of index register X. Ignores any carryover.
	 * @param value address that register X shall be added to
	 * @return the indexed address
	 */
	protected int indexedAddressingAbsoluteX(byte low, byte high)
	{
                pageBoundryCrossed = false;
                if(((((int)low) & 0xff)+(indexRegX & 0xff)) > 0xff)
                {
                    pageBoundryCrossed = true;
                }
		return ( ((((int)high)<<8)| (((int)low)&0xff)) + (indexRegX & 0xff) )&(0xffff); //masking reg X to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address for indexed addressing with absolute addressing and the Y register. ASM ex: AND $3232,Y
	 * Takes the value passed and adds it the the value of index register Y. Ignores any carryover.
	 * @param value address that register Y shall be added to
	 * @return the indexed address
	 */
	protected int indexedAddressingAbsoluteY(byte low, byte high)
	{
                pageBoundryCrossed = false;
                if(((((int)low) & 0xff)+(indexRegY & 0xff)) > 0xff)
                {
                    pageBoundryCrossed = true;
                }
		return (((((int)high)<<8)| (((int)low)&0xff)) + (indexRegY & 0xff))&(0xffff); //masking reg Y to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address where the address is stored in memory of the value passed in and the next place in memory. ASM ex: JMP ($3323)
	 * @param value Location in memory of desired address where value is the low byte and value + 1 is the high byte.
	 * @return the indirect address
	 */
	protected int indirectAddressing(byte low, byte high)
	{
		return ( ((CPUmemory[((high & 0xff)<<8)|((low&0xff)+1)] &0xff) << 8) | (CPUmemory[((high & 0xff)<<8)|(low&0xff)] & 0xff) ); //masking the low byte memory to prevent issues with two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address that branch operations refer to. Takes the program counter, adds 2 
	 * (to move past the branch instruction in memory) and then applies the relative value, giving the
	 * new place in memory the program counter should be going to.
	 * @param value The byte given with the branch instruction
	 * @return The offset to adjust the program counter by.
	 */
	protected int relativeAddressing(byte value)
	{
		int adjust = value;
		if( (value & 0x80) == 0x80) //if negative
		{
			adjust = (((~value)&0xff)+1) * -1; //convert from 2's complements but only the byte we care about
		}
		return (pgrmCtr + 1 + adjust);
	}
        
        protected int absoluteAddressing(byte low, byte high)
        {
            int tmp = high & 0xff;
            tmp = tmp << 8;
            int tmp2 = low & 0xff;
            return( (tmp|tmp2)& 0xffff);
        }
        
        public void printInfo(){
            System.out.println("Cycle counter: " + this.cycleCtr);
            System.out.println("Program Counter: " + String.format("%02X", this.pgrmCtr));
            System.out.println("Accumulator: " + String.format("%02X", this.accumulator));
            System.out.println("IndexRegX: " + String.format("%02X", this.indexRegX));
            System.out.println("IndexRegY: " + String.format("%02X", this.indexRegY));
            System.out.println("Stack Pointer: " + String.format("%02X", this.stackPtr));
            System.out.println("Status Register: " + String.format("%02X", this.statusReg));
        }
}
