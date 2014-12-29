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
	S0("stack pointer start"),
	SP("stack pointer"),
	SL("stack pointer limit"),
	R0("return stack pointer start"),
	RP("return pointer"),
	RL("return stack pointer limit"),
	SELF("current object"),
	MT("current method table"),
	IT("current interface table"),
	RESERVE("address of reserved memory location"),
	INTE("interrupt enable register"),
	FLAGS("flags register"),
	INTS("interrupt service register"),
	INTV("interrupt vector register"),
	CLK("external clock counter"),
	CLI("external clock interrupt");

	private String tooltip;

	private Register(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
