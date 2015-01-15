package com.F64;

public enum Ext4 {
	ROL(3,"rotate left and copy most significant bit into carry (register in next slot, shift in next slot+1)"),
	ROR(3,"rotate right and copy least significant bit into carry (register in next slot, shift in next slot+1)"),
	RCL(3,"rotate left with carry (register in next slot, shift in next slot+1)"),
	RCR(3,"rotate right with carry (register in next slot, shift in next slot+1)"),
	RFETCHI(3,"fetch register indirect (register in next slot)"),
	RSTOREI(3,"store register indirect (register in next slot)"),
	LFETCHI(3,"fetch local register indirect (register in next slot)"),
	LSTOREI(3,"store local register indirect (register in next slot)"),
	SFETCHI(3,"fetch system register indirect (register in next slot)"),
	SSTOREI(3,"store system register indirect (register in next slot)");

	private int size;
	private String tooltip;

	private Ext4(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
