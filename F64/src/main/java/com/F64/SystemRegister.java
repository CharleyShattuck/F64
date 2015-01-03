package com.F64;

public enum SystemRegister {
	SP("stack pointer"),
	RP("return pointer"),
	S0("stack pointer start"),
	SL("stack pointer limit"),
	R0("return stack pointer start"),
	RL("return stack pointer limit"),
	SELF("current object"),
	MT("current method table"),
	IT("current interface table"),
	RES("address of reserved memory location"),
	INTE("interrupt enable register"),
	INTS("interrupt service register"),
	INTV("interrupt vector register"),
	MD("multiply/divide register"),
	EX("exception register. Contains last exception thrown"),
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
