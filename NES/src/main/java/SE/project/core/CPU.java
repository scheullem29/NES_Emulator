package SE.project.core;

/*
CPU Frequency 1.789773 MHz (~559 ns per cycyle)

CPU Memory

$0000 - $00FF	Zero Page part of internal RAM
$0100 - $07FF	NES internal RAM
$0800 - $0FFF	Mirror of $0000 - $07FF
$1000 - $17FF	Mirror of $0000 - $07FF
$1800 - $1FFF	Mirror of $0000 - $07FF
$2000 - $3FFF	PPU Controls
$4000 - $401F	APU Controls
$4020 - $FFFF	Cartridge address space for PRG RAM and PRG ROM

Breakdown of $2000 - $3FFF

$2000			PPU CONTROL- NMI enable (V), PPU master/slave (P), sprite height (H), background tile select (B), sprite tile select (S), increment mode (I), nametable select (NN). Form of: VPHB SINN
	V:	Generate an NMI at the start of the vertical blanking interval (0: off; 1: on)
	P:	PPU master/slave select (0: read backdrop from EXT pins; 1: output color on EXT pins)
	H:	Sprite size (0: 8x8; 1: 8x16)
	B:	Background pattern table PPU memory address (0: $0000; 1: $1000)
	S:	Sprite pattern table PPU memory address for 8x8 sprites (0: $0000; 1: $1000; ignored with 8x16 sprites)
	I:	VRAM address increment per CPU read/write of PPU DATA (0: add 1, going across; 1: add 32, going down)
	NN:	Base nametable address (00: $2000; 01: $2400; 10: $2800; 11: $2C00)
$2001			PPU MASK - color emphasis (BGR), sprite enable (S), background enable (b), sprite left column enable (M), background left column enable (m), greyscale (G). Form of: BFRs bMmG
	B:	Emphasize blue (1: emphasize; 0: don't emphasize)
	G: 	Emphasize green (1: emphasize; 0: don't emphasize)
	R:	Emphasize red (1: emphasize; 0: don't emphasize)
	s:	show sprites (1: show; 0: hide)
	b:	show background (1: show; 0: hide)
	M:	show sprites in leftmost 8 pixles of the screen (1: show ; 0: hide)
	m:	Show background is leftmost 8 pixels (1: show; 0: hide)
	G:	Grayscale (0: normal color; 1: greyscale)
$2002			PPU STATUS - reads status. vblank (v), sprite 0 hit (S), sprite overflow (O), read resets write pair for $2005/2006. Form of: VSO- ----
	V:	Vertical blank has started (0: not in vblank; 1: in vblank). Clears back to 0 after reading.
	S:	Sprite 0 Hit. Set when a nonzero pixel of sprite 0 overlaps a nonzero background pixel; used for raster timing.
	O:	Sprite overflow. Sprite overflow. It is supposed to set this to 1 if there are too many sprites on a scanline, but creates both false positives and false negatives
$2003			OAM ADDRESS - OAM read/write address (a). Form of: aaaa aaaa
	aaaa aaaa: write the address of OAM you want to access. Usually just $00 is written here and OAMDMA is used.
$2004			OAM DATA - OAM data read/write (d). Form of: dddd dddd
	dddd dddd: write the OAM data here
$2005			PPU SCROLL - fine scroll position (two writes: X, Y)
	Sets the position of the upper left corner pixel in the nametable. First write $00-$FF for X offset. Second write $00-$EF for Y offset. $F0-$FF are treated as -16 to -1.
$2006			PPU ADDRESS - PPU read/write address (two writes: Most significant byte, least significant byte)
$2007			PPU DATA - PPU data read/write

$2008 - $3FFF	Mirrors $2000 - $2007, repeats every 8 bytes

Breakdown of $4000 - $401F

$4000			APU SQUARE WAVE 1 - Duty (D), envelope loop/length counter halt (L), constant volume (C), Volume/envelope(V). Form of: DDLC VVVV
$4001			APU SQUARE WAVE 1 - Sweep unit: enabled (E), period (P), negate (N), shift (S). Form of: EPPP NSSS
$4002			APU SQUARE WAVE 1 - Timer Low (T). Form of: TTTT TTTT
$4003			APU SQUARE WAVE 1 - Length counter Load (L), timer high (T). Form of: LLLL LTTT

$4004			APU SQUARE WAVE 2 - Duty (D), envelope loop/length counter halt (L), constant volume (C), Volume/envelope(V). Form of: DDLC VVVV
$4005			APU SQUARE WAVE 2 - Sweep unit: enabled (E), period (P), negate (N), shift (S). Form of: EPPP NSSS
$4006			APU SQUARE WAVE 2 - Timer Low (T). Form of: TTTT TTTT
$4007			APU SQUARE WAVE 2 - Length counter Load (L), timer high (T). Form of: LLLL LTTT

$4008			APU TRIANGLE WAVE - Length counter halt/linear counter control (C), linear counter load (R). Form of: CRRR RRRR
$4009			APU TRIANGLE WAVE - UNUSED
$400A			APU TRIANGLE WAVE - Timer Low (T). Form of: TTTT TTTT
$400B			APU TRIANGLE WAVE - Length counter load (L), Timer hight (T). Form of: LLLL LTTT

$400C			APU NOISE - Envelope loop/length counter halt (L), constant volume (C), volume/envelope (V). Form of: --LC VVVV
$400D			APU NOISE - UNUSED
$400E			APU NOISE - Loop noise (L), noise period (P). Form of: L--- PPPP
$400F			APU NOISE - Length counter load (L). Form of: LLLL L---

$4010			APU DMC SAMPLE - IRQ enable(I), Loop (L), frequency (R). Form of: IL-- RRRR
$4011			APU DMC SAMPLE - Load counter (D). Form of: -DDD DDDD
$4012			APU DMC SAMPLE - Sample address (A). Form of: AAAA AAAA
$4013			APU DMC SAMPLE - Sample length (L). Form of: LLLL LLLL

$4014			OAM DMA high address

$4015			APU CONTROL - enable/disable sound channels. DMC (D), noise (N), triangle (T), square 1 (1), square 2 (2). Form of: ---D NT21

$4016			CONTROLLER 1 data when reading. CONTROLLER strobe for writing. Write 1 to strobe controllers, write 0 to stop strobe. (in other words, when you do lda #$01, sta $4016, lda #$00, sta $4016. $4016 will have the button presses for CONTROLLER 1, $4017 will have the button presses for CONTROLLER 2)
				BIT:		7	6	5		4		3	2		1		0
				Button:		A	B	Select	Start	Up	Down	Left	Right

$4017			CONTROLLER 2 data when reading. APU FRAME COUNTER  when writing- Mode (M), IRQ inhibit flag (I). Form of: MI-- ----
				BIT:		7	6	5		4		3	2		1		0
				Button:		A	B	Select	Start	Up	Down	Left	Right

$4018 - $401F	APU and I/O testing functionality, usually disabled



PPU Memory

This is the usual mapping (Mapper 0 and 1 both stick to this). Really $2000-$3FFF are the only parts of the NES itself ($3000-$3FFF just a mirror section), the rest is on the cartridge.
All of this accessed by the CPU only through the CPU memory $2006 and $2007 and controlled with CPU memory $2000, $2001, $2002, and $2005.

$0000 - $0FFF	Pattern table 0
$1000 - $1FFF	Pattern table 1
$2000 - $23FF	Nametable 0
$2400 - $27FF	Nametable 1
$2800 - $2BFF	Nametable 2
$2C00 - $2FFF	Nametable 3
$3000 - $3EFF	Mirrors of $2000 - $2EFF
$3F00 - $3F1F	Palette RAM indexes
$3F20 - $3FFF	Mirrors of $3F00 - $3F1F



OAM Memory NEEDS WORK!!!

This is the Object Attribute Memory, which handles the sprite rendering. This is separate from the rest of the PPU memory. It is controlled through CPU memory $2003, $2004, and $4014.

$00 - $0C		Sprite Y coordinate
$01 - $0D		Sprite tile #
$02 - $0E		Sprite attribute
$03 - $0F 		Sprite X coordinate
*/

public class CPU 
{
	private byte accumulator, indexRegX, indexRegY, stackPtr, statusReg;
	private short pgrmCtr;
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
		this.statusReg = 0x00;
		this.CPUmemory = new byte[0x10000];
		this.PPUmemory = new byte[0x4000];
		this.OAMmemory = new byte[0x100];
		
		indexRegY = 0x01;
		indexRegX = 0x01;
		CPUmemory[20] = 0x01;
		CPUmemory[21] = 0x10;
		
	}
        
        public byte[] getCPUmemory(){
            return CPUmemory;
        }
        
        public byte[] getPPUmemory(){
            return PPUmemory;
        }
		
	/**
	 * Shows the status of the sign flag.
	 * @return true = set, false = clear
	 */
	public boolean signFlag()
	{
		return( (statusReg&0x80) == 0x80);
	}
	
	/**
	 * Shows the status of the overflow flag.
	 * @return true = set, false = clear
	 */
	public boolean overflowFlag()
	{
		return( (statusReg&0x40) == 0x40);
	}
	
	/**
	 * Shows break status
	 * @return true = set, false = clear
	 */
	public boolean breakStatus()
	{
		return( (statusReg&0x10) == 0x10);
	}
	
	/**
	 * Shows the interrupt disabled status
	 * @return true = interrupts disabled, false = interrupts enabled
	 */
	public boolean interruptDisableStatus()
	{
		return( (statusReg&0x04) == 0x04);
	}
	
	/**
	 * Shows the zero flag status
	 * @return true = set, false = clear
	 */
	public boolean zeroFlag()
	{
		return( (statusReg&0x02) == 0x02);
	}
	
	/**
	 * Shows the carry flag status
	 * @return true = set, false = clear
	 */
	public boolean carryFlag()
	{
		return( (statusReg&0x01) == 0x01);
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
