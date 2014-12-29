package com.F64;

public enum Flag {
	Carry("carry flag"),
	UpRead("data was fetched from up port"),
	DownRead("data was fetched from down port"),
	LeftRead("data was fetched from left port"),
	RightRead("data was fetched from right port"),
	UpWrite("data was stored into up port"),
	DownWrite("data was stored into down port"),
	LeftWrite("data was stored into left port"),
	RightWrite("data was stored into right port");

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
