package com.F64;

public enum Ext5 {
	UNDEFINED(4,	"?",	"-"),
	;

	
	private int size;
	private String display;
	private String tooltip;

	private Ext5(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}

}
