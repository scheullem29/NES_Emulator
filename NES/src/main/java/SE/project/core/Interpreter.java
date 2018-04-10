package SE.project.core;

public class Interpreter 
{

	/*
	OPCODE	HEX	ADDRESSING		LEN	CPU	S	V	B	D	I	Z	C 	IN ENGLISH
	ADC 	69	Immediate 		2	2	x	x				x	x	Add the immediate data, with carry, to those of the accumulator
	ADC 	65	Zero Page 		2	3	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	75	Zero Page, X 	2	4	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	6D	Absolute 		3	4	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	7D	Absolute, X 	3	4*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	79	Absolute, Y 	3	4*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	61	(Indirect, X) 	2	6	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	ADC 	71	(Indirect), Y 	2	5*	x	x				x	x	Add contents of memory location, with carry, to those of the accumulator
	AND 	29	Immediate 		2	2	x					x		Logical AND the contents of the accumulator with the immediate data.
	AND 	25	Zero Page 		2	3	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	35	Zero Page, X 	2	4	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	2D	Absolute 		3	4	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	3D	Absolute, X 	3	4*	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	39	Absolute, Y 	3	4*	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	21	(Indirect, X) 	2	6	x					x		Logical AND the contents of the accumulator with those of memory location.
	AND 	31	(Indirect), Y 	2	5*	x					x		Logical AND the contents of the accumulator with those of memory location.
	ASL 	0A	Accumulator 	1	2	x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	06	Zero Page 		2		x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	16	Zero Page, X 	2		x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	0E	Absolute 		3		x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
	ASL 	1E	Absolute, X 	3		x					x	x	Arithmetic shift left contents of memory location. Index through register X only.
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
	CLD		D8	None			1	2				0				Clear decimal mode (0) NOT USED IN THE NES.
	CLI 	58	None			1	2					0			Enable interrupts by claring the interrupt disabled bit of status register (0).
	CLV 	B8	None			1	2		0						Clear the overflow flag (0)
	CMP 	C9	Immediate 		2	2	x					x	x	Compare the contents of the accumulator with the immediate data. Only the status bits are affected.
	CMP 	C5	Zero Page 		2	3	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D5	Zero Page, X 	2	4	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	CD	Absolute 		3	4	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	DD	Absolute, X 	3	4*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D9	Absolute, Y 	3	4*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	C1	(Indirect, X) 	2	6	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CMP 	D1	(Indirect), Y 	2	5*	x					x	x	Compare the contents of the accumulator with those of memory location. Only the status bits are affected.
	CPX 	E0	Immediate 		2	2	x					x	x	Compare contents of the X register with the immediate. Only the status flags are affected.
	CPX 	E4	Zero Page 		2	3	x					x	x	Compare contents of the X register with those of memory location. Only the status flags are affected.
	CPX 	EC	Absolute 		3	4	x					x	x	Compare contents of the X register with those of memory location. Only the status flags are affected.
	CPY 	C0	Immediate 		2	2	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	CPY 	C4	Zero Page 		2	3	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	CPY 	CC	Absolute 		3	4	x					x	x	Compare contents of the Y register with those of memory location. Only the status flags are affected.
	DEC 	C6 	Zero Page 		2	5	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	D6	Zero Page, X 	2	6	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	CE	Absolute 		3	6	x					x		Decrements contents of memory location. Index through the X register only.
	DEC 	DE	Absolute, X 	3	7	x					x		Decrements contents of memory location. Index through the X register only.
	DEX 	CA	None			1	2	x					x		Decrements contents of the X register.
	DEY 	88	None			1	2	x					x		Decrements contents of the Y register.
	EOR 	49	Immediate 		2	2	x					x		XOR contents of the accumulator with the immediate data.
	EOR 	45	Zero Page 		2	3	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	55	Zero Page, X 	2	4	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	4D	Absolute 		3	4	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	5D	Absolute, X 	3	4*	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	59	Absolute, Y 	3	4*	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	41	(Indirect, X) 	2	6	x					x		XOR contents of the accumulator with those of memory location.
	EOR 	51	(Indirect), Y 	2	5*	x					x		XOR contents of the accumulator with those of memory location.
	INC 	E6	Zero Page 		2	5	x					x		Increment contents of memory location. Index through the X register only.
	INC 	F6	Zero Page, X 	2	6	x					x		Increment contents of memory location. Index through the X register only.
	INC 	EE	Absolute 		3	6	x					x		Increment contents of memory location. Index through the X register only.
	INC 	FE	Absolute, X 	3	7	x					x		Increment contents of memory location. Index through the X register only.
	INX 	E8	None			1	2	x					x		Increment contents of the X register.
	INY 	C8	None			1	2	x					x		Increment contents of the Y register.
	JMP 	6C	Indirect 		3	5								Jump to new location, using extended or indirect addressing
	JMP 	4C	Absolute 		3	3								Jump to new location, using extended or indirect addressing
	JSR 	20	None			3	6								Jump to subroutine beginning at address given in bytes 2 and 3 of the inctruction. Note that the stored program counter points to the last byte of the JSR inctruction.
	LDA 	A9	Immediate 		2	2	x					x		Load the accumulator with immediate data.
	LDA 	A5	Zero Page 		2	3	x					x		Load the accumulator for memory.
	LDA 	B5	Zero Page, X 	2	4	x					x		Load the accumulator for memory.
	LDA 	AD	Absolute 		3	4	x					x		Load the accumulator for memory.
	LDA 	BD	Absolute, X 	3	4*	x					x		Load the accumulator for memory.
	LDA 	B9	Absolute, Y 	3	4*	x					x		Load the accumulator for memory.
	LDA 	A1	(Indirect, X) 	2	6	x					x		Load the accumulator for memory.
	LDA 	B1	(Indirect), Y 	2	5*	x					x		Load the accumulator for memory.
	LDX 	A6	Zero Page 		2	3	x					x		Load the X register for memory.
	LDX 	B6	Zero Page, Y 	2	4	x					x		Load the X register for memory.
	LDX 	AE	Absolute 		3	4	x					x		Load the X register for memory.
	LDX 	BE	Absolute, Y 	3	4*	x					x		Load the X register for memory.
	LDX 	A2	Immediate 		2	2	x					x		Load the X register with immediate data.
	LDY 	A0	Immediate 		2	2	x					x		Load the Y register with immediate data.
	LDY 	A4	Zero Page 		2	3	x					x		Load the Y register for memory.
	LDY 	B4	Zero Page, X 	2	4	x					x		Load the Y register for memory.
	LDY 	AC	Absolute 		3	4	x					x		Load the Y register for memory.
	LDY 	BC	Absolute, X 	3	4	x					x		Load the Y register for memory.
	LSR 	4A	Accumulator 	1	2	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	46	Zero Page 		2	5	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	56	Zero Page, X 	2	6	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	4E	Absolute 		3	6	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
	LSR 	5E	Absolute, X 	3	7	0					x	x	Logical shift right the contents of the memory location. Index through the x register only.
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
	ROL 	2A	Accumulator 	1	2	x					x	x	Rotate contents of the accumulator left through carry.
	ROL 	26	Zero Page 		2	5	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	36	Zero Page, X 	2	6	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	2E	Absolute 		2	6	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROL 	3E	Absolute, X 	2	7	x					x	x	Rotate contents of memory location left through carry. Index through the x register only.
	ROR 	6A	Accumulator 	1	2	x					x	x	Rotate contents of the accumulator right through carry.
	ROR 	66	Zero Page 		2	5	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	76	Zero Page, X 	2	6	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	6E	Absolute 		3	6	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	ROR 	7E	Absolute, X 	3	7	x					x	x	Rotate contents of memory location right through carry. Index through the x register only.
	RTI 	40	None			1	6	x	x	x	x	x	x	x	Return from interrupts, restore status.
	RTS 	60	None			1	6								Return from subroutine, incrementing program counter to point to the instruction after the JSR which called the routine.
	SBC 	E9	Immediate 		2	2	x	x				x	x	Subtract the immediate data, with borrow, from the accumulator.
	SBC 	E5	Zero Page 		2	3	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F5	Zero Page, X 	2	4	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	ED	Absolute 		3	4	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	FD	Absolute, X 	3	4*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F9	Absolute, Y 	3	4*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	E1	(Indirect, X) 	2	6	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SBC 	F1	(Indirect), Y 	2	5*	x	x				x	x	Subtract contents of memory location, with borrow, from contents of the accumulator.
	SEC 	38	None			1	2							1	Set carry flag (1).
	SED		F8	None			1	2				1				Set decimal mode (1). NOT USED IN THE NES.
	SEI 	78	None			1	2					1			Disable interrupts (1).
	STA 	85	Zero Page 		2	3								Store the accumulator to memory.
	STA 	95	Zero Page, X 	2	4								Store the accumulator to memory.
	STA 	8D	Absolute 		3	4								Store the accumulator to memory.
	STA 	9D	Absolute, X 	3	4*								Store the accumulator to memory.
	STA 	99	Absolute, Y 	3	4*								Store the accumulator to memory.
	STA 	81	(Indirect, X) 	2	6								Store the accumulator to memory.
	STA 	91	(Indirect), Y 	2	6								Store the accumulator to memory.
	STX 	86	Zero Page 		2	3								Store the X register to memory.
	STX 	96	Zero Page, Y 	2	4								Store the X register to memory.
	STX 	8E	Absolute 		3	4								Store the X register to memory.
	STY 	84	Zero Page 		2	3								Store the Y register to memory.
	STY 	94	Zero Page, X 	2	4								Store the Y register to memory.
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
}
