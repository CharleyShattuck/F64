package com.F64;

public enum RegOp2 {
	MIN(false,		"min",		"signed minimum. dest = min(dest, src)"),
	MAX(false,		"max",		"signed maximum. dest = max(dest, src)"),
	ABS(false,		"abs",		"Absolute. dest = abs(src)"),
	NEGATE(false,	"negate",	"Negate. dest = -src"),
	NOT(false,		"not",		"Not. dest = ~src"),
	SIGN(false,		"sign",		"Signum return -1,0 or 1. dest = sign(src)"),
	REVERSE(false,	"rev",		"reverse bits. dest = reverse(src)"),
	NEXTPOW2(false,	"next^2",	"round up to the next power of 2. dest = nextpow2(src)"),
	PARITY(false,	"parity",	"parity. dest = parity(src)"),
	BITCNT1(false,	"b#1",		"count 1 bits. dest = bitcount1(src)"),
	BITCNT0(false,	"b#0",		"count 0 bits. dest = bitcount0(src)"),
	BYTECOUNT(false,"byte#",	"Count bytes in a cell. dest = countbytes(dest, src)"),
	ADD(false,		"+",		"Add. dest += src. If src is register S then a nip operation is appended"),
	ADDI(true,		"+",		"Add immediate (value in next slot). dest += imm"),
	ADDC(false,		"+y",		"Add with carry. C,dest = src + C. If src is register S then a nip operation is appended"),
	SUB(false,		"-",		"Subtract. dest -= src. If src is register S then a nip operation is appended"),
	SUBI(true,		"-",		"Subtract immediate (value in next slot). dest -= imm"),
	SUBC(false,		"-y",		"Subtract with carry. C,dest += ~src + C. If src is register S then a nip operation is appended"),
	AND(false,		"and",		"Bitwise and. dest &= src. If src is register S then a nip operation is appended"),
	OR(false,		"or",		"Bitwise or. dest |= src. If src is register S then a nip operation is appended"),
	XOR(false,		"xor",		"Bitwise exclusive or. dest ^= src. If src is register S then a nip operation is appended"),
	EQV(false,		"eqv",		"Bitwise equivalent. dest ^= ~src. If src is register S then a nip operation is appended"),
	ASL(false,		"<<",		"Arithmetic shift left. dest <<= src. If src < 0 then dest = src1. If src >= 64 then dest = 0(src1>=0) or MIN_INT(src1<0). If src is register S then a nip operation is appended"),
	ASR(false,		">>",		"Arithmetic shift right. dest >>= src. If src < 0 then dest = src1. If src >= 64 then dest = 0(src1>=0) or -1(src1<0). If src is register S then a nip operation is appended"),
	LSL(false,		"<<<",		"Logical shift left. dest <<<= src. If src < 0 then dest = src1. If src >= 64 then dest = 0. If src is register S then a nip operation is appended"),
	LSR(false,		">>>",		"Logical shift right. dest >>>= src. If src < 0 then dest = src1. If src >= 64 then dest = 0. If src is register S then a nip operation is appended"),
	ROL(false,		"^<<",		"Rotate left. dest ^<<= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	ROR(false,		">>^",		"Rotate right. dest >>^= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	RCL(false,		"^.<<",		"Rotate left with carry. dest ^.<<= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	RCR(false,		">>.^",		"Rotate right with carry. dest >>.^= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	ASLI(true,		"<<",		"Arithmetic shift left immediate (value in next slot). dest <<= imm"),
	ASRI(true,		">>",		"Arithmetic shift right immediate (value in next slot). dest >>= imm"),
	LSLI(true,		"<<<",		"Logical shift left immediate (value in next slot). dest <<<= imm"),
	LSRI(true,		">>>",		"Logical shift right immediate (value in next slot). dest >>>= imm"),
	ROLI(true,		"^<<",		"Rotate left immediate (value in next slot). dest = src1 ^<<= imm"),
	RORI(true,		">>^",		"Rotate right immediate (value in next slot). dest = src1 >>^= imm"),
	RCLI(true,		"^.<<",		"Rotate left immediate with carry (value in next slot). dest = src1 ^.<<= imm"),
	RCRI(true,		">>.^",		"Rotate right immediate with carry (value in next slot). dest = src1 >>.^= imm"),
	;


	private boolean	is_immediate;
	private String	display;
	private String	tooltip;

	private RegOp2(boolean imm, String display, String tooltip)
	{
		this.display = display;
		this.tooltip = tooltip;
		this.is_immediate = imm;
	}
	
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}
	public boolean isImmediate() {return is_immediate;}


}
