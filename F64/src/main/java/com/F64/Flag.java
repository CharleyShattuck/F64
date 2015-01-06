package com.F64;

public enum Flag {
	// interrupt flags
	RESET("power-on reset interrupt"),
	NMI("non-maskable interrupt"),
	COLD("cold restart interrupt"),
	WARM("warm restart interrupt"),
	EXCEPTION("some exception has been thrown"),
	MEMORY("invalid memory access"),
	ILLEGAL("illegal instruction"),
	ARITHMETIC("arithmetic exception, e.g. division by 0"),
	ALIGNED("a value is not aligned properly"),
	BOUND("a value is out of bound"),
	PRIVILEDGE("priviledge violation"),
	CODE("code was executed out of code area"),
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
	RIGHTREAD("data was fetched from right port"),
	LEFTREAD("data was fetched from left port"),
	FRONTREAD("data was fetched from front port"),
	BACKREAD("data was fetched from back port"),
	FUTUREREAD("data was fetched from front port"),
	PASTREAD("data was fetched from back port"),
	UPWRITE("data was stored into up port"),
	DOWNWRITE("data was stored into down port"),
	RIGHTWRITE("data was stored into right port"),
	LEFTWRITE("data was stored into left port"),
	FRONTWRITE("data was stored into front port"),
	BACKWRITE("data was stored into back port"),
	FUTUREWRITE("data was stored into front port"),
	PASTWRITE("data was stored into back port");

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
