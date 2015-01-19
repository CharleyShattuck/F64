package com.F64;

public enum Ext5 {
	PUSHR(3,"Push register on return stack (register in next slot)"),
	POPR(3,"Pop register from return stack (register in next slot)"),
	PUSHL(3,"Push local register on return stack (register in next slot)"),
	POPL(3,"Pop local register from return stack (register in next slot)"),
	PUSHS(3,"Push system register on return stack (register in next slot)"),
	POPS(3,"Pop system register from return stack (register in next slot)");

	private int size;
	private String tooltip;

	private Ext5(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
