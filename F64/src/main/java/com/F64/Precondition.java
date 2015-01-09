package com.F64;

public enum Precondition {
	T_EQ_0("T == 0"),
	T_NE_0("T != 0"),
	T_NEG("T < 0"),
	T_POS("T >= 0"),
	S_EQ_0("S == 0"),
	S_NE_0("S != 0"),
	S_NEG("S < 0"),
	S_POS("S >= 0"),
	R_EQ_0("R == 0"),
	R_NE_0("R != 0"),
	R_NEG("R < 0"),
	R_POS("R >= 0");

	private String tooltip;

	private Precondition(String tooltip)
	{
		this.tooltip = tooltip;
	}
	
	public String getTooltip() {return tooltip;}
}
