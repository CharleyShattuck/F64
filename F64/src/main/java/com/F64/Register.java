package com.F64;

public enum Register {
	Z("always 0"),
	T("top of parameter stack"),
	S("second of parameter stack"),
	R("top of return stack");

	private String tooltip;

	private Register(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
