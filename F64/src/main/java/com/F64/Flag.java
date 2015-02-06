package com.F64;

public enum Flag {
	// interrupt flags
	RESET(		"reset",	"power-on reset interrupt"),
	NMI(		"nmi",		"non-maskable interrupt"),
	COLD(		"cold",		"cold restart interrupt"),
	WARM(		"warm",		"warm restart interrupt"),
	EXCEPTION(	"exc",		"some exception has been thrown"),
	MEMORY(		"mem",		"invalid memory access"),
	ILLEGAL(	"ill",		"illegal instruction"),
	ARITHMETIC(	"arith",	"arithmetic exception, e.g. division by 0"),
	ALIGNED(	"align",	"a value is not aligned properly"),
	BOUND(		"bound",	"a value is out of bound"),
	PRIVILEDGE(	"priv",		"priviledge violation"),
	CODE(		"code",		"code was executed out of code area"),
	TOUCHED(	"touch",	"a reserved memory location has been touched"),
	SOVER(		"so",		"parameter stack overflow"),
	ROVER(		"ro",		"return stack overflow"),
	SUNDER(		"su",		"parameter stack underflow"),
	RUNDER(		"ru",		"return stack underflow"),
	EXTERNAL(	"ext",		"external interrupt"),
	CLOCK(		"clk",		"external clock interrupt"),
	// arithmetic
	CARRY(		"y",		"carry flag"),
//	READPEND(	"r?",		"read pending"),
//	WRITEPEND(	"w?",		"write possible"),
	// ports flags
	UPREAD(		"ur",		"data was fetched from up port"),
	DOWNREAD(	"dr",		"data was fetched from down port"),
	RIGHTREAD(	"rr",		"data was fetched from right port"),
	LEFTREAD(	"lr",		"data was fetched from left port"),
	FRONTREAD(	"fr",		"data was fetched from front port"),
	BACKREAD(	"br",		"data was fetched from back port"),
	FUTUREREAD(	"tr",		"data was fetched from front port"),
	PASTREAD(	"pr",		"data was fetched from back port"),
	UPWRITE(	"uw",		"data was stored into up port"),
	DOWNWRITE(	"dw",		"data was stored into down port"),
	RIGHTWRITE(	"rw",		"data was stored into right port"),
	LEFTWRITE(	"lw",		"data was stored into left port"),
	FRONTWRITE(	"fw",		"data was stored into front port"),
	BACKWRITE(	"bw",		"data was stored into back port"),
	FUTUREWRITE("tw",		"data was stored into front port"),
	PASTWRITE(	"pw",		"data was stored into back port");

	private String display;
	private String tooltip;

	private Flag(String display, String tooltip)
	{
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}
	
	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}

	public static String getDisplay(int r)
	{
		if (r < Flag.values().length) {return Flag.values()[r].getDisplay();}
		return "f"+r;
	}


}
