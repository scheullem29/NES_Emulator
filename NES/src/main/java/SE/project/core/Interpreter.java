package SE.project.core;

public class Interpreter 
{

	/*
	OPCODE	HEX	ADDRESSING		LEN	CPU	S	V	B	D	I	Z	C 	IN ENGLISH
	
	GREG
	
	ADC 	69	Immediate 		2	2	x	x				x	x	Add the immediate data, with carry, to those of the accumulator
	ADC 	65	Zero Page 		2	3	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	75	Zero Page, X            2	4	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	6D	Absolute 		3	4	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	7D	Absolute, X             3	4*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	79	Absolute, Y             3	4*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	61	(Indirect, X)           2	6	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	71	(Indirect), Y           2	5*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	AND 	29	Immediate 		2	2	x					x		Logical AND the contents of the accumulator with the immediate data.
	AND 	25	Zero Page 		2	3	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	35	Zero Page, X            2	4	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	2D	Absolute 		3	4	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	3D	Absolute, X             3	4*	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	39	Absolute, Y             3	4*	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	21	(Indirect, X)           2	6	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	31	(Indirect), Y           2	5*	x					x		Logical AND the contents of the accumulator with those of memory location.
	ASL 	0A	Accumulator             1	2	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	06	Zero Page 		2	5	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	16	Zero Page, X            2	6	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	0E	Absolute 		3	6	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	1E	Absolute, X             3	7	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	BCC 	90	Relative		2	2**								Branch if carry flag is cleared (0).
	BCS 	B0	Relative		2	2**								Branch is carry flag is set (1).
	BEQ 	F0	Relative		2	2**								Branch is zero flag is set (1).
	BIT 	24	Zero Page 		2	3	7	6				x		Logical AND the contents of the accumulator with those of memory location. Only the status bits are affected.
	BIT 	2C	Absolute 		3	4	7	6				x		Logical AND the contents of the accumulator with those of memory location. Only the status bits are affected.
	BMI 	30	Relative		2	2**								Branch if sign flag is set (1).
	BNE 	D0	Relative		2	2**								Branch if zero flag is cleared (0).
	BPL 	10	Relative		2	2**								Branch is sign flag is cleared (0).
	BRK 	00	None			1	7			1		1			Programmed interrupt. BRK cannot be disabled. The program counter is incremented twice before it is saved on the stack.
	BVC 	50	Relative		2	2**								Branch if overflow flag is cleared (0).
	BVS 	70	Relative		2	2**								Branch if overflow flag is set (1).
	CLC 	18	None			1	2							0	Clear the carry flag (0).
	CLD	D8	None			1	2				0				Clear decimal mode (0) NOT USED IN THE NES.
	CLI 	58	None			1	2					0			Enable interrupts by claring the interrupt disabled bit of status register (0).
	CLV 	B8	None			1	2		0						Clear the overflow flag (0)
	
	
	MARY
	
	CMP 	C9	Immediate 		2	2	x					x	x	Compare the contents of the accumulator with the immediate data. Only the status bits are affected.
	CMP 	C5	Zero Page 		2	3	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D5	Zero Page, X            2	4	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	CD	Absolute 		3	4	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	DD	Absolute, X             3	4*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D9	Absolute, Y             3	4*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	C1	(Indirect, X)           2	6	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D1	(Indirect), Y           2	5*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CPX 	E0	Immediate 		2	2	x					x	x	Compare contents of the X register with the immediate. Only the status flags are affected.
	CPX 	E4	Zero Page 		2	3	x					x	x	Compare contents of the X register with those of memory location. Only the status flags are affected.
	CPX 	EC	Absolute 		3	4	x					x	x	Compare contents of the X register with those of memory location. Only the status flags are affected.
	CPY 	C0	Immediate 		2	2	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	CPY 	C4	Zero Page 		2	3	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	CPY 	CC	Absolute 		3	4	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	DEC 	C6 	Zero Page 		2	5	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	D6	Zero Page, X            2	6	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	CE	Absolute 		3	6	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	DE	Absolute, X             3	7	x					x		Decrements contents of memory location. Index through the X register only.
	DEX 	CA	None			1	2	x					x		Decrements contents of the X register.
	DEY 	88	None			1	2	x					x		Decrements contents of the Y register.
	EOR 	49	Immediate 		2	2	x					x		XOR contents of the accumulator with the immediate data.
	EOR 	45	Zero Page 		2	3	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	55	Zero Page, X            2	4	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	4D	Absolute 		3	4	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	5D	Absolute, X             3	4*	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	59	Absolute, Y             3	4*	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	41	(Indirect, X)           2	6	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	51	(Indirect), Y           2	5*	x					x		XOR contents of the accumulator with those of memory location.
	INC 	E6	Zero Page 		2	5	x					x		Increment contents of memory location. Index through the X register only.
	INC 	F6	Zero Page, X            2	6	x					x		Increment contents of memory location. Index through the X register only.
	INC 	EE	Absolute 		3	6	x					x		Increment contents of memory location. Index through the X register only.
	INC 	FE	Absolute, X             3	7	x					x		Increment contents of memory location. Index through the X register only.
	INX 	E8	None			1	2	x					x		Increment contents of the X register.
	INY 	C8	None			1	2	x					x		Increment contents of the Y register.
	JMP 	6C	Indirect 		3	5								Jump to new location, using extended or indirect addressing
	JMP 	4C	Absolute 		3	3								Jump to new location, using extended or indirect addressing
	
	TONY
	
	JSR 	20	None			3	6								Jump to subroutine beginning at address given in bytes 2 and 3 of the inctruction. Note that the stored program counter points to the last byte of the JSR inctruction.
	LDA 	A9	Immediate 		2	2	x					x		Load the accumulator with immediate data.
	LDA 	A5	Zero Page 		2	3	x					x		Load the accumulator for memory.
	LDA 	B5	Zero Page, X            2	4	x					x		Load the accumulator for memory.
	LDA 	AD	Absolute 		3	4	x					x		Load the accumulator for memory.
	LDA 	BD	Absolute, X             3	4*	x					x		Load the accumulator for memory.
	LDA 	B9	Absolute, Y             3	4*	x					x		Load the accumulator for memory.
	LDA 	A1	(Indirect, X)           2	6	x					x		Load the accumulator for memory.
	LDA 	B1	(Indirect), Y           2	5*	x					x		Load the accumulator for memory.
	LDX 	A6	Zero Page 		2	3	x					x		Load the X register for memory.
	LDX 	B6	Zero Page, Y            2	4	x					x		Load the X register for memory.
	LDX 	AE	Absolute 		3	4	x					x		Load the X register for memory.
	LDX 	BE	Absolute, Y             3	4*	x					x		Load the X register for memory.
	LDX 	A2	Immediate 		2	2	x					x		Load the X register with immediate data.
	LDY 	A0	Immediate 		2	2	x					x		Load the Y register with immediate data.
	LDY 	A4	Zero Page 		2	3	x					x		Load the Y register for memory.
	LDY 	B4	Zero Page, X            2	4	x					x		Load the Y register for memory.
	LDY 	AC	Absolute 		3	4	x					x		Load the Y register for memory.
	LDY 	BC	Absolute, X             3	4*	x					x		Load the Y register for memory.
	LSR 	4A	Accumulator             1	2	0					x	x	Logical shift right the contents of the accumulator. Index through the x register only.
	LSR 	46	Zero Page 		2	5	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	56	Zero Page, X            2	6	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	4E	Absolute 		3	6	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	5E	Absolute, X             3	7	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	NOP 	EA	None			1	2								No operation.
	ORA 	09	Immediate 		2	2	x					x		Logical OR the contents of the accumulator wit the immediate data.
	ORA 	05	Zero Page 		2	3	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	15	Zero Page, X 	2	4	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	0D	Absolute 		3	4	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	1D 	Absolute, X 	3	4*	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	19	Absolute, Y 	3	4*	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	01	(Indirect, X) 	2	6	x					x		Logical OR the contents of the accumulator wit those of memory location.
	ORA 	11	(Indirect), Y 	2	5*	x					x		Logical OR the contents of the accumulator wit those of memory location.
	PHA 	48	None			1	3								Push accumulator contents onto the stack.
	PHP 	08	None			1	3								Push the status register contents onto the stack.
	PLA 	68	None			1	4	x					x		Load the accumulator from the top of the stack.
	PLP 	28	None			1	4	x	x	x	x	x	x	x	Load the status register from the top of the stack.
	ROL 	2A	Accumulator             1	2	x					x	x	Rotate contents of the accumulator left through carry.
	ROL 	26	Zero Page 		2	5	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	36	Zero Page, X            2	6	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	2E	Absolute 		3	6	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	3E	Absolute, X             3	7	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	
	ROMAN
	
	ROR 	6A	Accumulator             1	2	x					x	x	Rotate contents of the accumulator right through carry.
	ROR 	66	Zero Page 		2	5	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	76	Zero Page, X            2	6	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	6E	Absolute 		3	6	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	7E	Absolute, X             3	7	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	RTI 	40	None			1	6	x	x	x	x	x	x	x	Return from interrupts, restore status.
	RTS 	60	None			1	6								Return from subroutine, incrementing program counter to point to the instruction after the JSR which called the routine.
	SBC 	E9	Immediate 		2	2	x	x				x	x	Subtract the immediate data, with borrow, from the accumulator.
	SBC 	E5	Zero Page 		2	3	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F5	Zero Page, X            2	4	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	ED	Absolute 		3	4	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	FD	Absolute, X             3	4*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F9	Absolute, Y             3	4*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	E1	(Indirect, X)           2	6	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F1	(Indirect), Y           2	5*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SEC 	38	None			1	2							1	Set carry flag (1).
	SED	F8	None			1	2				1				Set decimal mode (1). NOT USED IN THE NES.
	SEI 	78	None			1	2					1			Disable interrupts (1).
	STA 	85	Zero Page 		2	3								Store the accumulator to memory.
	STA 	95	Zero Page, X            2	4								Store the accumulator to memory.
	STA 	8D	Absolute 		3	4								Store the accumulator to memory.
	STA 	9D	Absolute, X             3	4*								Store the accumulator to memory.
	STA 	99	Absolute, Y             3	4*								Store the accumulator to memory.
	STA 	81	(Indirect, X)           2	6								Store the accumulator to memory.
	STA 	91	(Indirect), Y           2	6								Store the accumulator to memory.
	STX 	86	Zero Page 		2	3								Store the X register to memory.
	STX 	96	Zero Page, Y            2	4								Store the X register to memory.
	STX 	8E	Absolute 		3	4								Store the X register to memory.
	STY 	84	Zero Page 		2	3								Store the Y register to memory.
	STY 	94	Zero Page, X            2	4								Store the Y register to memory.
	STY 	8C	Absolute 		3	4								Store the Y register to memory.
	TAX 	AA	None			1	2	x					x		Set the X register to the accumulator contents.
	TAY 	A8	None			1	2	x					x		Set the Y register to the accumulator contents.
	TSX 	BA	None			1	2	x					x		Set the X register to the stack pointer contents.
	TXA 	8A	None			1	2	x					x		Set the accumulator to the X register contents.
	TXS 	9A	None			1	2								Set the stack pointer to the contents of the X register.
	TYA 	98	None			1	2	x					x		Set the accumulator to the Y register contents.
													
	KEY												
	0	Unconditionally sets to 0											
	1	Unconditionally sets to 1											
	x	Modified to reflect the results of execution											
	7	Bit 7 of tested byte											
	6	Bit 6 of tested byte											
	*	Add 1 clock cycle is a page boundry is crossed.											
	**	Add 1 clock cycle if branch occurs to location in same page. Add 2 clock cycles if branch to another page occurs.											

	**/

    void readInstructions(CPU nes) {
        
        int limit = 0x8010;

        
        while(nes.getpgrmCtr() <= limit){ //;)
            String bite = (String.format("%02X", nes.getCPUmemory()[nes.getpgrmCtr()]));
            System.out.println(bite + " " + nes.getpgrmCtr() + " " + limit);
            processByte(bite, nes);
        }
    }

    private void processByte(String temp, CPU nes) {
        switch (temp) {
            case "78": //sei
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setcycleCtr(nes.getcycleCtr() + 2);
                nes.interruptDisableStatusSet();
                break;
            case "D8": //cld
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setcycleCtr(nes.getcycleCtr() + 2);
                nes.decimalModeFlagClear();  
                break;
            case "A9": case "A5": case "B5": case "AD": 
            case "BD": case "B9": case "A1": case "B1": //lda 
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                byte tmp = 0;
                switch (temp) {
                    case "A9": //immediate
                        tmp = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.setcycleCtr(nes.getcycleCtr() + 2);
                        break;
                    case "A5": //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        nes.setcycleCtr(nes.getcycleCtr() + 3);
                        break;
                    case "B5": // indexed Addressing Zero Page
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.setcycleCtr(nes.getcycleCtr() + 4);
                        break;
                    case "AD": // absolute addressing
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                        nes.setcycleCtr(nes.getcycleCtr() + 4);
                        break;
                    case "BD": // indexed addressing absolute x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                        nes.setcycleCtr(nes.getcycleCtr() + 4);
                        if(nes.pageBoundryCrossed())
                        {
                            nes.setcycleCtr(nes.getcycleCtr() +1);
                        }
                        break;
                    case "B9": // indexed addressing absolute y
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                        nes.setcycleCtr(nes.getcycleCtr() + 4); 
                        if(nes.pageBoundryCrossed())
                        {
                            nes.setcycleCtr(nes.getcycleCtr() +1);
                        }
                        break;
                    case "A1": // pre indexed indirect
                        tmp = nes.getCPUmemory()[nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.setcycleCtr(nes.getcycleCtr() + 6);
                        break;
                    case "B1": // post indexed indirect
                        tmp = nes.getCPUmemory()[nes.postIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.setcycleCtr(nes.getcycleCtr() + 5); 
                        if(nes.pageBoundryCrossed())
                        {
                            nes.setcycleCtr(nes.getcycleCtr() +1);
                        }
                        break;
                    default:
                        System.out.println("LDA error");
                        break;
                }
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setAccumulator(tmp);
                if(tmp < 0)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(tmp == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                break;
            case "8D": //sta
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setcycleCtr(nes.getcycleCtr() + 4);
                byte tmp1 = nes.getCPUmemory()[nes.getpgrmCtr()];
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                byte tmp2 = nes.getCPUmemory()[nes.getpgrmCtr()];
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                int tmp3 = nes.absoluteAddressing(tmp1, tmp2);
                nes.getCPUmemory()[tmp3] = nes.getAccumulator();
                break;
            case "A6": case "B6": case "AE": case "BE": case "A2"://ldx 
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                tmp = 0;
                switch (temp) {
					case "A2": //immediate
                        tmp = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.setcycleCtr(nes.getcycleCtr() + 2);
                        break;
                    case "A6": //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        nes.setcycleCtr(nes.getcycleCtr() + 3);
                        break;
                    case "B6": // indexed Addressing Zero Page
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageY(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.setcycleCtr(nes.getcycleCtr() + 4);
                        break;
                    case "AE": // absolute addressing
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                        nes.setcycleCtr(nes.getcycleCtr() + 4);
                        break;
                    case "BE": // indexed addressing absolute y
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                        nes.setcycleCtr(nes.getcycleCtr() + 4); 
                        if(nes.pageBoundryCrossed())
                        {
                            nes.setcycleCtr(nes.getcycleCtr() +1);
                        }
                        break;
                    default:
                        System.out.println("LDX error");
                        break;
                }
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setIndexRegX(tmp);
                if(tmp < 0)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(tmp == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                break;
            case "9A":  //txs
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                nes.setcycleCtr(nes.getcycleCtr() + 2);
                nes.setStackPointer(nes.getIndexRegX());
                nes.printInfo();
                break;
            case "20": //jsr
                int pc = (nes.getpgrmCtr()+2)&0xffff;
                byte high = (byte) ((pc & 0xff00)>>8);
                byte low = (byte) (pc & 0x00ff);                    //grab program counter + 2 (past the jsr instruction)
                nes.getCPUmemory()[nes.getStackPointer()] = high;
                nes.setStackPointer( (byte) ((nes.getStackPointer() - 1) & 0xff) );
                nes.getCPUmemory()[nes.getStackPointer()] = low;
                nes.setStackPointer( (byte) ((nes.getStackPointer() - 1) & 0xff) ); //push the program counter to stack
                nes.setpgrmCtr(nes.getpgrmCtr()+1);                                       
                low = nes.getCPUmemory()[nes.getpgrmCtr()];
                nes.setpgrmCtr(nes.getpgrmCtr()+1);
                high = nes.getCPUmemory()[nes.getpgrmCtr()];
                nes.setpgrmCtr(nes.absoluteAddressing(low, high));      // grab the new program counter from the jsr
                nes.setcycleCtr(nes.getcycleCtr()+6);  
                break;
            case "A0": case "A4": case "B4": case "AC": case "BC"://ldy 
                nes.increasePgrmCtr(1);
                tmp = 0;
                switch (temp) {
					case "A0": //immediate
                        tmp = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.increaseCycleCtr(2);
                        break;
                    case "A4": //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        nes.increaseCycleCtr(3);
                        break;
                    case "B4": // indexed Addressing Zero Page
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.increaseCycleCtr(4);
                        break;
                    case "AC": // absolute addressing
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(4);
                        break;
                    case "BC": // indexed addressing absolute X
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(4);
                        if(nes.pageBoundryCrossed())
                        {
                            nes.increaseCycleCtr(1);
                        }
                        break;
                    default:
                        System.out.println("LDY error");
                        break;
                }
                nes.increasePgrmCtr(1);
                nes.setIndexRegY(tmp);
                if(tmp < 0)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(tmp == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                break;
            case "4A": case "46": case "56": case "4E": case "5E"://lsr
                nes.increasePgrmCtr(1);
                tmp = 0;
                switch(temp)
                {
                    case "4A":  //accumulator
                        tmp = nes.getAccumulator();
                        break;
                    case "46":  //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        break;
                    case "56": //zero page,x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        break;
                    case "4E": //absolute
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        break;
                    case "5E": //absolute, x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        break;
                }
                //accumulator
                if((tmp & 0x1) == 1)
                {
                    nes.carryFlagSet();
                }
                else
                {
                    nes.carryFlagClear();
                }
                nes.signFlagClear();
                tmp = (byte)(tmp >> 1);
                if(tmp == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                switch(temp)
                {
                    case "4A": //accumulator
                        nes.setAccumulator(tmp);
                        nes.increaseCycleCtr(2);
                        break;
                    case "46": //zeropage
                        nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    case "56": //zeropage, x
                        nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "4E": //absolute
                        nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(6);
                        break;
                    case "5E": //absolute, x
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(7);
                        break;
                }
                break;
            case "EA": //nop
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(2);                
                break;
            case "09": case "05": case "15": case "0D":
            case "1D": case "19": case "01": case "11": //ora
                tmp = 0;
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                switch(temp)
                {
                    case "09": //immediate
                        tmp = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(2);                    
                        break;
                    case "05": //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(3);
                        break; 
                    case "15": //zero page, x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(4);
                        break;
                    case "0D": //absolute
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);                        
                        break;
                    case "1D": //absolute, x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        if(nes.pageBoundryCrossed())
                        {
                            nes.increaseCycleCtr(1);
                        }
                        break;
                    case "19": //absolute, y
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        if(nes.pageBoundryCrossed())
                        {
                            nes.increaseCycleCtr(1);
                        }
                        break;
                    case "01": //(indirect, x)
                        tmp = nes.getCPUmemory()[nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "11": //(indirect),y
                        tmp = nes.getCPUmemory()[nes.postIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        if(nes.pageBoundryCrossed())
                        {
                            nes.increaseCycleCtr(1);
                        }
                        break;
                }
                nes.setAccumulator((byte)((nes.getAccumulator()|tmp)&0xff));
                if( (nes.getAccumulator()&0x80) == 0x80)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(nes.getAccumulator() == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                break;
            case "48": //pha
                nes.getCPUmemory()[nes.getStackPointer()] = nes.getAccumulator();
                nes.setStackPointer((byte)(nes.getStackPointerByte()-1));
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(3);
                break;
            case "08": //php
                nes.getCPUmemory()[nes.getStackPointer()] = nes.getStatusReg();
                nes.setStackPointer((byte)(nes.getStackPointerByte()-1));
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(3);
                break;
            case "68": //pla
                nes.setStackPointer((byte)(nes.getStackPointerByte()+1));
                nes.setAccumulator(nes.getCPUmemory()[nes.getStackPointer()]);
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(4);
                if((nes.getAccumulator() & 0x80) == 0x80)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(nes.getAccumulator() == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
            case "28": //plp
                nes.setStackPointer((byte)(nes.getStackPointerByte()+1));
                nes.setStatusReg(nes.getCPUmemory()[nes.getStackPointer()]);
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(4);
                break;
            case "2A": case "26": case "36": case "2E": case "3E": // rol
                nes.increasePgrmCtr(1);
                tmp = 0;
                switch(temp)
                {
                    case "2A": //accumulator
                        tmp = nes.getAccumulator();
                        break;
                    case "26": //zero page
                        tmp = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        break;
                    case "36": //zero page, x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        break;
                    case "2E": //absolute
                        tmp = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        break;
                    case "3E": //absolute, x
                        tmp = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        break;
                }
                boolean toCarry = false;
                if((tmp & 0x80) == 0x80)
                {
                    toCarry = true;
                }
                tmp = (byte)(tmp << 1);
                if(nes.carryFlag())
                {
                    tmp = (byte)(tmp + 1);
                }
                if(toCarry)
                {
                    nes.carryFlagSet();
                }
                else
                {
                    nes.carryFlagClear();
                }
                if((tmp & 0x80) == 0x80)
                {
                    nes.signFlagSet();
                }
                else
                {
                    nes.signFlagClear();
                }
                if(tmp == 0)
                {
                    nes.zeroFlagSet();
                }
                else
                {
                    nes.zeroFlagClear();
                }
                
                switch(temp)
                {
                    case "2A": //accumulator
                        nes.setAccumulator(tmp);
                        nes.increaseCycleCtr(2);
                        break;
                    case "26": //zero page
                        nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    case "36": //zero page, x
                        nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "2E": //absolute
                        nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(6);
                        break;
                    case "3E": //absolute, x
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(7);
                        break;
                }
                break;
			
	case "C9" : case "C5" : case "D5" : case "CD" :
        case "DD" : case "D9" : case "C1" : case "D1" : //cmp
                nes.increasePgrmCtr(1);
                tmp = nes.getAccumulator();
                tmp1 = 0;
                tmp2 = 0;
                switch (temp)
                {
                    case "C9" : // immediate
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getpgrmCtr()]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(2);
                        break;
                    case "C5" : // zero page
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(3);
                        break;
                    case "D5" : // zero page, x
                        tmp1 = (byte)(nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(4);
                        break;
                    case "CD" : // absolute
                        tmp1 = (byte)(nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "DD" : // absolute, x
                        tmp1 = (byte)(nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "D9" : // absolute, y
                        tmp1 = (byte)(nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "C1" : // (indirect, x)
                        tmp1 = (byte)(nes.getCPUmemory()[nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "D1" : // (indirect), y
                        tmp1 = (byte)(nes.getCPUmemory()[nes.postIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    default : 
                        System.out.println("CMP error");
                        break;
                }
                if ((tmp2 & 0x80) == 0x80){
                    nes.signFlagSet();
                } else {
                    nes.signFlagClear();
                }
                if (tmp2 == 0){
                    nes.zeroFlagSet();
                } else {
                    nes.zeroFlagClear();
                }
                if (tmp1 > tmp) {
                    nes.carryFlagSet();
                } else {
                    nes.carryFlagClear();
                }
                break;
            case "E0" : case "E4" : case "EC" : // cpx
                tmp = nes.getIndexRegX();
                tmp1 = 0;
                tmp2 = 0;
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "E0" : // immediate
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getpgrmCtr()]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(2);
                        break;
                    case "E4" : // zero page
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(3);
                        break;
                    case "EC" : // absolute
                        tmp1 = (byte)(nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    default:
                        System.out.println("CPX error");
                        break;                            
                }
                if ((tmp2 & 0x80) == 0x80){
                    nes.signFlagSet();
                } else {
                    nes.signFlagClear();
                }
                if (tmp2 == 0){
                    nes.zeroFlagSet();
                } else {
                    nes.zeroFlagClear();
                }
                if (tmp1 > tmp) {
                    nes.carryFlagSet();
                } else {
                    nes.carryFlagClear();
                }
                break;
            case "C0" : case "C4" : case "CC" : // cpy
                tmp = nes.getIndexRegY();
                tmp1 = 0;
                tmp2 = 0;
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "C0" : // immediate
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getpgrmCtr()]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(2);
                        break;
                    case "C4" : // zero page
                        tmp1 = (byte)(nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(3);
                        break;
                    case "CC" : // absolute
                        tmp1 = (byte)(nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])]);
                        tmp2 = (byte)(tmp - tmp1);
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    default:
                        System.out.println("CPY error");
                        break;
                }
                if ((tmp2 & 0x80) == 0x80){
                    nes.signFlagSet();
                } else {
                    nes.signFlagClear();
                }
                if (tmp2 == 0){
                    nes.zeroFlagSet();
                } else {
                    nes.zeroFlagClear();
                }
                if (tmp1 > tmp) {
                    nes.carryFlagSet();
                } else {
                    nes.carryFlagClear();
                }
                break;
            case "C6" : case "D6" : case "CE" : case "DE" : // dec
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "C6" : // zero page
                        tmp = (byte)(nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] - 1);
                        nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    case "D6" : // zero page, x
                        tmp = (byte)(nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] - 1);
                        nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "CE" : // absolute
                        tmp = (byte)(nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] - 1);
                        nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(6);
                        break;
                    case "DE" : // absolute, x
                        tmp = (byte)(nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] - 1);
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(7);
                        break;
                    default: 
                        System.out.println("DEC error");
                        break;
                }
                break;
            case "CA" : // dex
                tmp = (byte)(nes.getIndexRegX() - 1);
                nes.getIndexRegX() = tmp;
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(2);
                break;
            case "88" : // dey
                tmp = (byte)(nes.getIndexRegY() - 1);
                nes.getIndexRegY() = tmp;
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(2);
                break;
            case "49" : case "45" : case "55" : case "4D" :
            case "5D" : case "59" : case "41" : case "51" : // eor
                tmp = nes.getAccumulator();
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "49" : // immediate
                        tmp1 = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.getCPUmemory()[nes.getpgrmCtr()] = tmp ^ tmp1; 
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(2);
                        break;
                    case "45" : // zero page
                        tmp1 = nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]];
                        nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] = tmp ^ tmp1;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(3);
                        break;
                    case "55" : // zero page, x
                        tmp1 = nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp ^ tmp1; 
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(4);
                        break;
                    case "4D" : // absolute
                        tmp1 = nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp ^ tmp1;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "5D" : // absolute, x
                        tmp1 = nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp ^ tmp1;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "59" : // absolute, y
                        tmp1 = nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])];
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteY(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp ^ tmp1;
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(4);
                        break;
                    case "41" : // (indirect, x)
                        tmp1 = nes.getCPUmemory()[nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.getCPUmemory()[nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp ^ tmp1; 
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "51" : // (indirect), y
                        tmp1 = nes.getCPUmemory()[nes.postIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])];
                        nes.getCPUmemory()[nes.postIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()])] = tmp ^ tmp1; 
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    default : 
                        System.out.println("EOR error");
                        break;
                }
                break;
            case "E6" : case "F6" : case "EE" : case "FE" : // inc
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "E6" : // zero page
                        tmp = (byte)(nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] + 1);
                        nes.getCPUmemory()[nes.getCPUmemory()[nes.getpgrmCtr()]] = tmp; 
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(5);
                        break;
                    case "F6" : // zero page, x
                        tmp = (byte)(nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()])] + 1);
                        nes.getCPUmemory()[nes.indexedAddressingZeroPageX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(1);
                        nes.increaseCycleCtr(6);
                        break;
                    case "EE" : // absolute
                        tmp = (byte)(nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] + 1);
                        nes.getCPUmemory()[nes.absoluteAddressing(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp; 
                        nes.increasePgrmCtr(2);
                        nes.increaseCycleCtr(6);
                        break;
                    case "FE" : // absolute, x
                        tmp = (byte)(nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] + 1);
                        nes.getCPUmemory()[nes.indexedAddressingAbsoluteX(nes.getCPUmemory()[nes.getpgrmCtr()], nes.getCPUmemory()[nes.getpgrmCtr()+1])] = tmp;
                        nes.increasePgrmCtr(3);
                        nes.increaseCycleCtr(7);
                        break;
                    default :
                        System.out.println("INC error");
                        break;
                }
                break;
            case "E8" : // inx
                tmp = (byte)(nes.getIndexRegX() + 1);
                nes.getIndexRegX() = tmp;
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(2);
                break;
            case "C8" : // iny
                tmp = (byte)(nes.getIndexRegY() + 1);
                nes.getIndexRegY() = tmp;
                nes.increasePgrmCtr(1);
                nes.increaseCycleCtr(2);
                break;
            case "6C" : case "4C" : // jmp
                nes.increasePgrmCtr(1);
                switch (temp) {
                    case "6C" : // indirect
                        nes.setpgrmCtr(nes.preIndexedIndirectAddressing(nes.getCPUmemory()[nes.getpgrmCtr()]));                                           
                        nes.setcycleCtr(nes.getcycleCtr()+5);
                        break;
                    case "4C" : // absolute
                        low = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.setpgrmCtr(nes.getpgrmCtr()+1);
                        high = nes.getCPUmemory()[nes.getpgrmCtr()];
                        nes.setpgrmCtr(nes.absoluteAddressing(low, high));
                        nes.setcycleCtr(nes.getcycleCtr()+3);
                        break;
                    default :
                        System.out.println("JMP error");
                        break;
                }
                break;
            default:
                nes.setpgrmCtr(nes.getpgrmCtr() + 1);
                System.out.println(temp);
        }
            
    }
}
