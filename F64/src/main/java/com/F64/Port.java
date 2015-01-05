package com.F64;

public enum Port {
	UP("up port"),
	DOWN("down port"),
	RIGHT("right port"),
	LEFT("left port"),
	FRONT("front port"),
	BACK("back port"),
	FUTURE("future port"),
	PAST("past port");

	private String tooltip;

	private Port(String tooltip)
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
