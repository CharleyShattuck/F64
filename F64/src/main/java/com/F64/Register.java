package com.F64;

public enum Register {
	Z("0",	"always 0"),
	T("t",	"top of parameter stack"),
	S("s",	"second of parameter stack"),
	R("r",	"top of return stack"),
//	W("w",	"temporary (work) register"),
	L("l",	"limit register"),
	;

	private String display;
	private String tooltip;

	private Register(String display, String tooltip)
	{
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}

	public static String getTooltip(int r)
	{
		if (r < Register.values().length) {return Register.values()[r].getTooltip();}
		return "r"+r;
	}

	public static String getDisplay(int r)
	{
		if (r < Register.values().length) {return Register.values()[r].getDisplay();}
		return "r"+r;
	}

}
