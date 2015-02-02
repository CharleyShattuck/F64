package com.F64;

public enum Branch {
	SLOT0(0,	"j0",		"goto slot 0"),
	SLOT1(0,	"j1",		"goto slot 1"),
	SLOT2(0,	"j2",		"goto slot 2"),
	SLOT3(0,	"j3",		"goto slot 3"),
	SLOT4(0,	"j4",		"goto slot 4"),
	SLOT5(0,	"j5",		"goto slot 5"),
	SLOT6(0,	"j6",		"goto slot 6"),
	SLOT7(0,	"j7",		"goto slot 7"),
	SLOT8(0,	"j8",		"goto slot 8"),
	SLOT9(0,	"j9",		"goto slot 9"),
	SLOT10(0,	"j10",		"goto slot 10"),
	SKIP(0,		"skip",		"goto next cell"),
	FORWARD(1,	"forward",	"Add P with next slot and beginn with slot 0. If next slot is 0 the take use 64"),
	BACK(1,		"back",		"Subtract the next slot from P and beginn with slot 0. If next slot is 0 the take use 64"),
	IO(1,		"i/o",		"next slot holds I/O address"),
//	REM(-1,		"rem",		"remaining slots define the address replacement"),
	LONG(-2,	"long",		"fetch P from Memory[P] and beginn with slot 0");

	private int size;
	private String display;
	private String tooltip;

	private Branch(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}
}
