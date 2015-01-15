package com.F64;

public enum Ext3 {
	ROL(3,"rotate left and copy most significant bit into carry (register in next slot)"),
	ROR(3,"rotate right and copy least significant bit into carry (register in next slot)"),
	RCL(3,"rotate left with carry (register in next slot)"),
	RCR(3,"rotate right with carry (register in next slot)"),
	SFETCH(3,"fetch system register indirect (register in next slot)"),
	SSTORE(3,"store system register indirect (register in next slot)"),
//	BITCNT1(4,"count the number of 1 bits in a register (destination register in next slot, source register in next slot+1)"),
//	BITCNT0(4,"count the number of 0 bits in a register (destination register in next slot, source register in next slot+1)"),
	BITFF1(4,"find first 1 bit in a register (destination register in next slot, source register in next slot+1)"),
	BITFL1(4,"find last 1 bit in a register (destination register in next slot, source register in next slot+1)");

	private int size;
	private String tooltip;

	private Ext3(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
