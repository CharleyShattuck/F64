package com.F64;

public enum RegOp1 {
	ADD("Add. dest = src1 + src2. If either src2 or src2 is register S then a nip operation is appended"),
	ADDI("Add immediate (src2). dest = src1 + src2"),
	ADDC("Add with carry. C,dest = src1 + src2 + C. If either src2 or src2 is register S then a nip operation is appended"),
	ADDCC("Add with carry clear. C,dest = src1 + src2. If either src2 or src2 is register S then a nip operation is appended"),
	ADDCS("Add with carry set. C,dest = src1 + src2 + 1. If either src2 or src2 is register S then a nip operation is appended"),
	SUB("Subtract. dest = src1 - src2. If either src2 or src2 is register S then a nip operation is appended"),
	SUBI("Subtract immediate (src2). dest = src1 - src2"),
	SUBC("Subtract with carry. C,dest = src1 + ~src2 + C. If either src2 or src2 is register S then a nip operation is appended"),
	SUBCC("Subtract with carry clear. C,dest = src1 + ~src2. If either src2 or src2 is register S then a nip operation is appended"),
	SUBCS("Subtract with carry set. C,dest = src1 + ~src2 + 1. If either src2 or src2 is register S then a nip operation is appended"),
	AND("Bitwise and. dest = src1 & src2. If either src2 or src2 is register S then a nip operation is appended"),
	OR("Bitwise or. dest = src1 | src2. If either src2 or src2 is register S then a nip operation is appended"),
	XOR("Bitwise exclusive or. dest = src1 ^ src2. If either src2 or src2 is register S then a nip operation is appended"),
	XNOR("Bitwise exclusive or. dest = src1 ^ ~src2. If either src2 or src2 is register S then a nip operation is appended"),
	ASL("Arithmetic shift left. dest = src1 << src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0(src1>=0) or MIN_INT(src1<0). If either src2 or src2 is register S then a nip operation is appended"),
	ASR("Arithmetic shift right. dest = src1 >> src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0(src1>=0) or -1(src1<0). If either src2 or src2 is register S then a nip operation is appended"),
	LSL("Logical shift left. dest = src1 <<< src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0. If either src2 or src2 is register S then a nip operation is appended"),
	LSR("Logical shift right. dest = src1 >>> src2. If src2 < 0 then dest = src1. If src2 >= 64 then dest = 0. If either src2 or src2 is register S then a nip operation is appended"),
	ROL("Rotate left. dest = src1 ^<< src2. If src2 < 0 then dest = src1. If either src2 or src2 is register S then a nip operation is appended"),
	ROR("Rotate right. dest = src1 >>^ src2. If src2 < 0 then dest = src1. If either src2 or src2 is register S then a nip operation is appended"),
	ASLI("Arithmetic shift left immediate (src2). dest = src1 << src2"),
	ASRI("Arithmetic shift right immediate (src2). dest = src1 >> src2"),
	LSLI("Logical shift left immediate (src2). dest = src1 <<< src2"),
	LSRI("Logical shift right immediate (src2). dest = src1 >>> src2"),
	ROLI("Rotate left immediate (src2). dest = src1 ^<< src2"),
	RORI("Rotate right immediate (src2). dest = src1 >>^ src2"),
	MUL2ADD("*2 and add. dest = src1*2 + src2. If either src2 or src2 is register S then a nip operation is appended"),
	DIV2SUB("/2 and subtract. dest = src1/2 - src2. If either src2 or src2 is register S then a nip operation is appended");


	private String tooltip;

	private RegOp1(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
