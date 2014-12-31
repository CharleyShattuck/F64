package com.F64;

public enum Config {
	VERSION("version of processor"),
	BITPERCELL("bit per cell"),
	BITPERSLOT("bit per slot"),
	FLAGS("# of flag bits"),
	X("processor coordinate X"),
	Y("processor coordinate Y"),
	Z("processor coordinate Z");

	private String tooltip;

	private Config(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}

	
}
