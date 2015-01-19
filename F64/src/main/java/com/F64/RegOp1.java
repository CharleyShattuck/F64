package com.F64;

public enum RegOp1 {
	ABS("Absolute. dest = abs(dest)"),
	NEGATE("Negate. dest = -dest"),
	NOT("Not. dest = ~dest"),
	SIGN("Signum return -1,0 or 1. dest = sign(dest)"),
	REVERSE("reverse bits. dest = reverse(dest)"),
	NEXTPOW2("round up to the next power of 2. dest = nextpow2(dest)"),
	PARITY("parity. dest = parity(dest)"),
	BITCNT1("reverse bits. dest = bitcount1(dest)"),
	BITCNT0("reverse bits. dest = bitcount1(dest)"),
	ASL1("Arithmetic shift left. dest = dest << 1"),
	ASR1("Arithmetic shift right. dest = dest >> 1"),
	LSL1("Logical shift left. dest = dest <<< 1"),
	LSR1("Logical shift right. dest = dest >>> 1"),
	ROL1("Rotate left. dest = dest ^<< 1"),
	ROR1("Rotate right. dest = dest >>^ 1"),
	RCL1("Rotate left with carry. dest = dest ^.<< 1"),
	RCR1("Rotate right with carry. dest = dest >>.^ 1");


	private String tooltip;

	private RegOp1(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
