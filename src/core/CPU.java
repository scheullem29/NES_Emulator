package core;


public class CPU 
{
	private byte accumulator, indexRegX, indexRegY, stackPtr;
	private short pgrmCtr;
	private boolean signFlag, overflowFlag, breakFlag, decimalModeFlag, interruptFlag, zeroFlag, carryFlag;
	private byte[] CPUmemory;
	private byte[] PPUmemory;
	private byte[] OAMmemory;
	
	public CPU()
	{
		this.accumulator = 0x00;
		this.indexRegX = 0x00;
		this.indexRegY = 0x00;
		this.stackPtr = 0x00;
		this.pgrmCtr = 0x00;
		this.signFlag = false;
		this.overflowFlag = false;
		this.breakFlag = false;
		this.decimalModeFlag = false; //not used by NES
		this.interruptFlag = false;
		this.carryFlag = false;
		this.CPUmemory = new byte[0x10000];
		this.PPUmemory = new byte[0x4000];
		this.OAMmemory = new byte[0x100];
		
		indexRegY = 0x01;
		indexRegX = 0x01;
		CPUmemory[20] = 0x01;
		CPUmemory[21] = 0x10;
		
	}
	
	/**
	 * Calculates the address for pre-indexed indirect addressing. ASM ex: AND ($20, X)
	 * Takes the value passed in and adds it to the value of index register X, goes to the memory location for the low byte
	 * and that location + 1 for the high byte.
	 * Ignores any carryover.
	 * @param value contents of register X will have this value added to it for the low byte and this value + 1 for the high byte
	 * @return the pre-indexed indirect address.
	 */
	protected int preIndexedIndirectAddressing(int value)
	{
		return ((CPUmemory[(indexRegX & 0xff)+(value+1)]<<8|(CPUmemory[(indexRegX & 0xff)+value] & 0xff)) & 0xffff); //masking reg X and memory contents to prevent any issues with the two's complement representations padding the front bits with 1
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
	protected int postIndexedIndirectAddressing(int value)
	{
		return ((((CPUmemory[value+1] << 8)|(CPUmemory[value] & 0xff)) + (indexRegY & 0xff)) & 0xffff); //masking reg Y and CPUmemory[value] to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address for indexed addressing with zero page. ASM ex: AND $20, X
	 * Takes the value passed in and adds it to the value of index register X. Only returns a one byte number.
	 * Ignores any carryover.
	 * @param value value to be added to contents of register X
	 * @return the indexed address.
	 */
	protected int indexedAddressingZeroPage(int value)
	{
		return (value + (indexRegX & 0xff))&(0xff); //masking reg X to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address for indexed addressing with absolute addressing and the X register. ASM ex: AND $3232,X
	 * Takes the value passed and adds it the the value of index register X. Ignores any carryover.
	 * @param value address that register X shall be added to
	 * @return the indexed address
	 */
	protected int indexedAddressingAbsoluteX(int value)
	{
		
		return ((((value&0xff)<<8)|((value&0xff00)>>8)) + (indexRegX & 0xff))&(0xffff); //masking reg X to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address for indexed addressing with absolute addressing and the Y register. ASM ex: AND $3232,Y
	 * Takes the value passed and adds it the the value of index register Y. Ignores any carryover.
	 * @param value address that register Y shall be added to
	 * @return the indexed address
	 */
	protected int indexedAddressingAbsoluteY(int value)
	{
		return ((((value&0xff)<<8)|((value&0xff00)>>8)) + (indexRegY & 0xff))&(0xffff); //masking reg Y to prevent any issues with the two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address where the address is stored in memory of the value passed in and the next place in memory. ASM ex: JMP ($3323)
	 * @param value Location in memory of desired address where value is the low byte and value + 1 is the high byte.
	 * @return the indirect address
	 */
	protected int indirectAddressing(int value)
	{
		return ( (CPUmemory[(((value&0xff)<<8)|((value&0xff00)>>8))+1] << 8) | (CPUmemory[(((value&0xff)<<8)|((value&0xff00)>>8))] & 0xff) ); //masking the low byte memory to prevent issues with two's complement representations padding the front bits with 1s
	}
	
	/**
	 * Calculates the address that branch operations refer to. Takes the program counter, adds 2 
	 * (to move past the branch instruction in memory) and then applies the relative value, giving the
	 * new place in memory the program counter should be going to.
	 * @param value The byte given with the branch instruction
	 * @return The offset to adjust the program counter by.
	 */
	protected int relativeAddressing(int value)
	{
		int adjust = value;
		if( (value & 0x80) == 0x80) //if negative
		{
			adjust = (((~value)&0xff)+1) * -1; //convert from 2's complements but only the byte we care about
		}
		return (pgrmCtr + 2 + adjust);
	}
}