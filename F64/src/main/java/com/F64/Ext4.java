package com.F64;

public enum Ext4 {
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
