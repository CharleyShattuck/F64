package com.F64;

public enum RegOp3 {
	MIN(false,		"min",			"signed minimum. dest = min(src1, src2)"),
	MAX(false,		"max",			"signed maximum. dest = max(src1, src2)"),
	ADD(false,		"+",			"Add. dest = src1 + src2. If either src1 or src2 is register S then a nip operation is appended"),
	ADDI(true,		"+",			"Add immediate (src2). dest = src1 + src2"),
	ADDC(false,		"+y",			"Add with carry. C,dest = src1 + src2 + C. If either src1 or src2 is register S then a nip operation is appended"),
	SUB(false,		"-",			"Subtract. dest = src1 - src2. If either src1 or src2 is register S then a nip operation is appended"),
	SUBI(true,		"-",			"Subtract immediate (src2). dest = src1 - src2"),
	SUBC(false,		"-y",			"Subtract with carry. C,dest = src1 + ~src2 + C. If either src1 or src2 is register S then a nip operation is appended"),
	AND(false,		"and",			"Bitwise and. dest = src1 & src2. If either src1 or src2 is register S then a nip operation is appended"),
	OR(false,		"or",			"Bitwise or. dest = src1 | src2. If either src1 or src2 is register S then a nip operation is appended"),
	XOR(false,		"xor",			"Bitwise exclusive or. dest = src1 ^ src2. If either src1 or src2 is register S then a nip operation is appended"),
	EQV(false,		"eqv",			"Bitwise equvalent. dest = src1 ^ ~src2. If either src1 or src2 is register S then a nip operation is appended"),
	ASL(false,		"<<",			"Arithmetic shift left. dest = src1 << src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0(src1>=0) or MIN_INT(src1<0). If either src1 or src2 is register S then a nip operation is appended"),
	ASR(false,		">>",			"Arithmetic shift right. dest = src1 >> src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0(src1>=0) or -1(src1<0). If either src1 or src2 is register S then a nip operation is appended"),
	LSL(false,		"<<<",			"Logical shift left. dest = src1 <<< src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0. If either src1 or src2 is register S then a nip operation is appended"),
	LSR(false,		">>>",			"Logical shift right. dest = src1 >>> src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0. If either src1 or src2 is register S then a nip operation is appended"),
	ROL(false,		"^<<",			"Rotate left. dest = src1 ^<< src2. If src2 < 0 then dest = src1. If either src1 or src2 is register S then a nip operation is appended"),
	ROR(false,		">>^",			"Rotate right. dest = src1 >>^ src2. If src2 < 0 then dest = src1. If either src1 or src2 is register S then a nip operation is appended"),
	RCL(false,		"^.<<",			"Rotate left with carry. dest = src1 ^.<< src2. If src2 < 0 then dest = src1. If either src1 or src2 is register S then a nip operation is appended"),
	RCR(false,		">>.^",			"Rotate right with carry. dest = src1 >>.^ src2. If src2 < 0 then dest = src1. If either src1 or src2 is register S then a nip operation is appended"),
	ASLI(true,		"<<",			"Arithmetic shift left immediate (src2). dest = src1 << src2"),
	ASRI(true,		">>",			"Arithmetic shift right immediate (src2). dest = src1 >> src2"),
	LSLI(true,		"<<<",			"Logical shift left immediate (src2). dest = src1 <<< src2"),
	LSRI(true,		">>>",			"Logical shift right immediate (src2). dest = src1 >>> src2"),
	ROLI(true,		"^<<",			"Rotate left immediate (src2). dest = src1 ^<< src2"),
	RORI(true,		">>^",			"Rotate right immediate with carry (src2). dest = src1 >>^ src2"),
	RCLI(true,		"^.<<",			"Rotate left immediate with carry (src2). dest = src1 ^.<< src2"),
	RCRI(true,		">>.^",			"Rotate right immediate with carry (src2). dest = src1 >>.^ src2"),
	BYTECOUNT(false,"bcnt",			"Count bytes in a cell. dest = countbytes(src1, src2)"),
	MUL2ADD(false,	"*2+",			"*2 and add. dest = src1*2 + src2. If either src1 or src2 is register S then a nip operation is appended"),
	DIV2SUB(false,	"/2-",			"/2 and subtract. dest = src1/2 - src2. If either src1 or src2 is register S then a nip operation is appended");

	private boolean	is_immediate;
	private String	display;
	private String	tooltip;

	private RegOp3(boolean imm, String display, String tooltip)
	{
		this.display = display;
		this.tooltip = tooltip;
		this.is_immediate = imm;
	}
	
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}
	public boolean isImmediate() {return is_immediate;}

}
