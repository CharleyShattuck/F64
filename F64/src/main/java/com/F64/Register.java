package com.F64;

public enum Register {
	Z("always 0"),
	P("instruction pointer"),
	I("current instruction cell"),
	T("top of parameter stack"),
	S("second of parameter stack"),
	R("top of return stack"),
	A("index register A"),
	B("index register B"),
	FLAGS("flags register"),
	SP("stack pointer"),
	RP("return pointer"),
	S0("stack pointer start"),
	SL("stack pointer limit"),
	R0("return stack pointer start"),
	RL("return stack pointer limit"),
	CLK("external clock counter"),
	SELF("current object"),
	MT("current method table"),
	IT("current interface table");

	private String tooltip;

	private Register(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
