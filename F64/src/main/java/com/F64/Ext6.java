package com.F64;

public enum Ext6 {
	UNDEFINED(4,	"?",	"-"),
	;

	
	
	private int size;
	private String display;
	private String tooltip;

	private Ext6(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}


}
