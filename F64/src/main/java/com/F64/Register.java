package com.F64;

public enum Register {
	Z("always 0"),
	I("current instruction cell"),
	P("instruction pointer"),
	T("top of parameter stack"),
	S("second of parameter stack"),
	R("top of return stack"),
	A("index register A"),
	B("index register B"),
	FLAG("flags register");

	private String tooltip;

	private Register(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
