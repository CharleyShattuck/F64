package com.F64;

public enum RegOp2 {
	MIN("signed minimum. dest = min(dest, src)"),
	MAX("signed maximum. dest = max(dest, src)"),
	ABS("Absolute. dest = abs(src)"),
	NEGATE("Negate. dest = -src"),
	NOT("Not. dest = ~src"),
	SIGN("Signum return -1,0 or 1. dest = sign(src)"),
	REVERSE("reverse bits. dest = reverse(src)"),
	NEXTPOW2("round up to the next power of 2. dest = nextpow2(src)"),
	PARITY("parity. dest = parity(src)"),
	BITCNT1("count 1 bits. dest = bitcount1(src)"),
	BITCNT0("count 0 bits. dest = bitcount0(src)"),
	BYTECOUNT("Count bytes in a cell. dest = countbytes(dest, src)"),
	ADD("Add. dest += src. If src is register S then a nip operation is appended"),
	ADDI("Add immediate (value in next slot). dest += imm"),
	ADDC("Add with carry. C,dest = src + C. If src is register S then a nip operation is appended"),
	SUB("Subtract. dest -= src. If src is register S then a nip operation is appended"),
	SUBI("Subtract immediate (value in next slot). dest -= imm"),
	SUBC("Subtract with carry. C,dest += ~src + C. If src is register S then a nip operation is appended"),
	AND("Bitwise and. dest &= src. If src is register S then a nip operation is appended"),
	OR("Bitwise or. dest |= src. If src is register S then a nip operation is appended"),
	XOR("Bitwise exclusive or. dest ^= src. If src is register S then a nip operation is appended"),
	XORN("Bitwise exclusive or. dest ^= ~src. If src is register S then a nip operation is appended"),
	ASL("Arithmetic shift left. dest <<= src. If src < 0 then dest = src1. If src >= 64 then dest = 0(src1>=0) or MIN_INT(src1<0). If src is register S then a nip operation is appended"),
	ASR("Arithmetic shift right. dest >>= src. If src < 0 then dest = src1. If src >= 64 then dest = 0(src1>=0) or -1(src1<0). If src is register S then a nip operation is appended"),
	LSL("Logical shift left. dest <<<= src. If src < 0 then dest = src1. If src >= 64 then dest = 0. If src is register S then a nip operation is appended"),
	LSR("Logical shift right. dest >>>= src. If src < 0 then dest = src1. If src >= 64 then dest = 0. If src is register S then a nip operation is appended"),
	ROL("Rotate left. dest ^<<= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	ROR("Rotate right. dest >>^= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	RCL("Rotate left with carry. dest ^.<<= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	RCR("Rotate right with carry. dest >>.^= src. If src < 0 then dest = src1. If src is register S then a nip operation is appended"),
	ASLI("Arithmetic shift left immediate (value in next slot). dest <<= imm"),
	ASRI("Arithmetic shift right immediate (value in next slot). dest >>= imm"),
	LSLI("Logical shift left immediate (value in next slot). dest <<<= imm"),
	LSRI("Logical shift right immediate (value in next slot). dest >>>= imm"),
	ROLI("Rotate left immediate (value in next slot). dest = src1 ^<<= imm"),
	RORI("Rotate right immediate (value in next slot). dest = src1 >>^= imm"),
	RCLI("Rotate left immediate with carry (value in next slot). dest = src1 ^.<<= imm"),
	RCRI("Rotate right immediate with carry (value in next slot). dest = src1 >>.^= imm");


	private String tooltip;

	private RegOp2(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}


}
