package com.F64;

public enum Ext3 {
	ABS(2,"absolute (register in next slot)"),
	NEGATE(2,"negate (register in next slot)"),
	ROL(3,"rotate left and copy most significant bit into carry (register in next slot)"),
	ROR(3,"rotate right and copy least significant bit into carry (register in next slot)"),
	RCL(3,"rotate left with carry (register in next slot)"),
	RCR(3,"rotate right with carry (register in next slot)");

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
