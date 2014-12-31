package com.F64;

public enum Port {
	UP("up port"),
	DOWN("down port"),
	LEFT("left port"),
	RIGHT("right port"),
	FRONT("front port"),
	BACK("back port");

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
