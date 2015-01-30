package com.F64;

public enum SystemRegister {
	FLAG("flag",	"flags register"),
	I("i",			"current instruction cell"),
	P("p",			"instruction pointer"),
	W("w",			"temporary register"),
	SELF("self",	"current object"),
	S0("s0",		"stack pointer start"),
	SP("sp",		"stack pointer"),
	SL("sl",		"stack pointer limit"),
	R0("r0",		"return stack pointer start"),
	RP("rp",		"return pointer"),
	RL("rl",		"return stack pointer limit"),
	MT("mt",		"current method table"),
	IT("it",		"current interface table"),
	RES("res",		"address of reserved memory location"),
	INTE("inte",	"interrupt enable register"),
	INTS("ints",	"interrupt service register"),
	INTV("intv",	"interrupt vector register"),
	MD("md",		"multiply/divide register"),
	MDP("mdp",		"multiply/divide prepare register"),
	EXC("exc",		"exception register. Contains last exception thrown"),
	EXF("exf",		"exception frame register"),
	CLK("clk",		"external clock counter"),
	CLI("cli",		"external clock interrupt");

	private String display;
	private String tooltip;

	private SystemRegister(String display, String tooltip)
	{
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}

	public static String getDisplay(int r)
	{
		if (r < SystemRegister.values().length) {return SystemRegister.values()[r].getDisplay();}
		return "s"+r;
	}
}
