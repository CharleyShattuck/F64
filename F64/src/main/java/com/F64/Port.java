package com.F64;

public enum Port {
	UP("up port", "U"),
	DOWN("down port", "D"),
	RIGHT("right port", "R"),
	LEFT("left port", "L"),
	FRONT("front port", "F"),
	BACK("back port", "B"),
	FUTURE("future port", "T"),
	PAST("past port", "P");

	private String tooltip;
	private String abbr;

	private Port(String tooltip, String abbr)
	{
		this.tooltip = tooltip;
		this.abbr = abbr;
	}
	
	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}

	public String getTooltip() {return tooltip;}
	public String getAbbreviation() {return abbr;}

}
