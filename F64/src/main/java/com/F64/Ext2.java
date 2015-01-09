package com.F64;

public enum Ext2 {
	TUCK(2,"tuck ( n1 n2 - n2 n1 n2)"),
	UNDER(2,"under ( n1 n2 - n1 n1 n2)"),
	POSQ(2,"return true on non-negaive numbers ( n - flag)"),
	NEGQ(2,"return true on negaive numbers ( n - flag)"),
	ABS(2,"absolute ( n1 - n2)"),
	NEGATE(2,"negate ( n1 - n2)"),
	ROL(3,"rotate left T and copy most significant bit into carry (shift in next slot)"),
	ROR(3,"rotate right T and copy least significant bit into carry (shift in next slot)"),
	ROLC(3,"rotate left T with carry (shift in next slot)"),
	RORC(3,"rotate right T with carry (shift in next slot)"),
	FETCHSYSTEM(3,"fetch system register (selector in next slot) ( - n)"),
	STORESYSTEM(3,"store system register (selector in next slot) ( n -)"),
	FETCHRES(2,"fetch and reserve memory location"),
	STORECOND(2,"store conditionally a reserved memory location"),
	FETCHPORT(3,"fetch from port(s) without waiting. The next slot contains the mask with one bit per port."),
	STOREPORT(3,"fetch from port(s) without waiting. The next slot contains the mask with one bit per port."),
	FETCHPORTWAIT(3,"fetch from port(s) with waiting. The next slot contains the mask with one bit per port."),
	STOREPORTWAIT(3,"fetch from port(s) with waiting. The next slot contains the mask with one bit per port.");

	private int size;
	private String tooltip;

	private Ext2(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
