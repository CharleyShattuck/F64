package com.F64;

public enum Ext4 {
	RFETCHI(3,		"@@r",	"fetch register indirect (register in next slot)"),
	RSTOREI(3,		"@!r",	"store register indirect (register in next slot)"),
	LFETCHI(3,		"@@l",	"fetch local register indirect (register in next slot)"),
	LSTOREI(3,		"@!l",	"store local register indirect (register in next slot)"),
	SFETCHI(3,		"@@s",	"fetch system register indirect (register in next slot)"),
	SSTOREI(3,		"@!s",	"store system register indirect (register in next slot)"),
	RFETCHPEI(3,	"+@r",	"fetch register pre-increment (register in next slot)"),
	RSTOREPEI(3,	"+!r",	"store register pre-increment (register in next slot)"),
	LFETCHPEI(3,	"+@l",	"fetch local register pre-increment (register in next slot)"),
	LSTOREPEI(3,	"+!l",	"store local register pre-increment (register in next slot)"),
	SFETCHPEI(3,	"+@s",	"fetch system register pre-increment (register in next slot)"),
	SSTOREPEI(3,	"+!s",	"store system register pre-increment (register in next slot)"),
	RFETCHPOI(3,	"@r+",	"fetch register post-increment (register in next slot)"),
	RSTOREPOI(3,	"!r+",	"store register post-increment (register in next slot)"),
	LFETCHPOI(3,	"@l+",	"fetch local register post-increment (register in next slot)"),
	LSTOREPOI(3,	"!l+",	"store local register post-increment (register in next slot)"),
	SFETCHPOI(3,	"@s+",	"fetch system register post-increment (register in next slot)"),
	SSTOREPOI(3,	"!s+",	"store system register post-increment (register in next slot)"),
	RFETCHPED(3,	"-@r",	"fetch register pre-decrement (register in next slot)"),
	RSTOREPED(3,	"-!r",	"store register pre-decrement (register in next slot)"),
	LFETCHPED(3,	"-@l",	"fetch local register pre-decrement (register in next slot)"),
	LSTOREPED(3,	"-!l",	"store local register pre-decrement (register in next slot)"),
	SFETCHPED(3,	"-@s",	"fetch system register pre-decrement (register in next slot)"),
	SSTOREPED(3,	"-!s",	"store system register pre-decrement (register in next slot)"),
	RFETCHPOD(3,	"@r-",	"fetch register post-decrement (register in next slot)"),
	RSTOREPOD(3,	"!r-",	"store register post-decrement (register in next slot)"),
	LFETCHPOD(3,	"@l-",	"fetch local register post-decrement (register in next slot)"),
	LSTOREPOD(3,	"!l-",	"store local register post-decrement (register in next slot)"),
	SFETCHPOD(3,	"@s-",	"fetch system register post-decrement (register in next slot)"),
	SSTOREPOD(3,	"!s-",	"store system register post-decrement (register in next slot)"),
	;

	private int size;
	private String display;
	private String tooltip;

	private Ext4(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}

}
