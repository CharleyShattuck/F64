package com.F64;

public enum Port {
	UP(		"u",	"up port"),
	DOWN(	"d",	"down port"),
	RIGHT(	"r",	"right port"),
	LEFT(	"l",	"left port"),
	FRONT(	"f",	"front port"),
	BACK(	"b",	"back port"),
	FUTURE(	"t",	"future port"),
	PAST(	"p",	"past port");

	private String tooltip;
	private String display;

	private Port(String display, String tooltip)
	{
		this.tooltip = tooltip;
		this.display = display;
	}
	
	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}

	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}


	public static String getDisplayMask(int mask)
	{
		String res = "";
		for (int i=0; i<Port.values().length; ++i) {
			if ((mask & (1 << i)) != 0) {
				res = res + Port.values()[i].getDisplay();
			}			
		}
		return res;
	}
	
}
