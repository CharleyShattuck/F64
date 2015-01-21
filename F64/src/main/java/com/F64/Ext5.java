package com.F64;

public enum Ext5 {
	UNDEFINED(4,"-");

	
	
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
