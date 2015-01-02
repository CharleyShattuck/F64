package com.F64;

public enum Branch {
	SLOT0(0,"goto slot 0"),
	SLOT1(0,"goto slot 1"),
	SLOT2(0,"goto slot 2"),
	SLOT3(0,"goto slot 3"),
	SLOT4(0,"goto slot 4"),
	SLOT5(0,"goto slot 5"),
	SLOT6(0,"goto slot 6"),
	SLOT7(0,"goto slot 7"),
	SLOT8(0,"goto slot 8"),
	SLOT9(0,"goto slot 9"),
	SLOT10(0,"goto slot 10"),
	NEXT(0,"goto next cell"),
	SHORT(1,"replace the lowest bits of P with next slot and beginn with slot 0"),
	IO(1,"next slot holds I/O address"),
	REM(-1,"remaining slots define the address + 4 lowest bits define the slot"),
	LONG(-2,"fetch P from Memory[P] and beginn with slot 0");

	private int size;
	private String tooltip;

	private Branch(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
}
