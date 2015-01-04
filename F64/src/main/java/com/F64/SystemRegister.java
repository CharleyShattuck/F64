package com.F64;

public enum SystemRegister {
	L0("local register 0"),
	L1("local register 1"),
	L2("local register 2"),
	L3("local register 3"),
	L4("local register 4"),
	L5("local register 5"),
	L6("local register 6"),
	L7("local register 7"),
	FLAG("flags register"),
	I("current instruction cell"),
	P("instruction pointer"),
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
