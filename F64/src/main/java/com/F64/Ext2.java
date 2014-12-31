package com.F64;

public enum Ext2 {
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
