package com.F64;

public enum SystemRegister {
	FLAG("flags register"),
	I("current instruction cell"),
	P("instruction pointer"),
	W("temporary register"),
	SELF("current object"),
	S0("stack pointer start"),
	SP("stack pointer"),
	SL("stack pointer limit"),
	R0("return stack pointer start"),
	RP("return pointer"),
	RL("return stack pointer limit"),
	MT("current method table"),
	IT("current interface table"),
	RES("address of reserved memory location"),
	INTE("interrupt enable register"),
	INTS("interrupt service register"),
	INTV("interrupt vector register"),
	MD("multiply/divide register"),
	MDP("multiply/divide prepare register"),
	EXC("exception register. Contains last exception thrown"),
	EXF("exception frame register"),
	CLK("external clock counter"),
	CLI("external clock interrupt");

	private String tooltip;

	private SystemRegister(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

}
