package com.F64;

public enum Ext4 {
	ROL(3,"rotate left and copy most significant bit into carry (register in next slot, shift in next slot+1)"),
	ROR(3,"rotate right and copy least significant bit into carry (register in next slot, shift in next slot+1)"),
	ROLC(3,"rotate left with carry (register in next slot, shift in next slot+1)"),
	RORC(3,"rotate right with carry (register in next slot, shift in next slot+1)");

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
