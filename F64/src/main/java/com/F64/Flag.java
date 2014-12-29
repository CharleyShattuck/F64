package com.F64;

public enum Flag {
	// interrupt flags
	RESET("reset interrupt"),
	NMI("non-maskable interrupt"),
	MEMORY("invalid memory access"),
	ILLEGAL("illegal instruction"),
	ARITHMETIC("arithmetic exception"),
	TOUCHED("a reserved memory location has been touched"),
	SOVER("parameter stack overflow"),
	ROVER("return stack overflow"),
	SUNDER("parameter stack underflow"),
	RUNDER("return stack underflow"),
	EXTERNAL("external interrupt"),
	CLOCK("external clock interrupt"),
	// arithmetic
	CARRY("carry flag"),
	// ports flags
	UPREAD("data was fetched from up port"),
	DOWNREAD("data was fetched from down port"),
	LEFTREAD("data was fetched from left port"),
	RIGHTREAD("data was fetched from right port"),
	UPWRITE("data was stored into up port"),
	DOWNWRITE("data was stored into down port"),
	LEFTWRITE("data was stored into left port"),
	RIGHTWRITE("data was stored into right port");

	private String tooltip;

	private Flag(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}

	public String getTooltip() {return tooltip;}


}
