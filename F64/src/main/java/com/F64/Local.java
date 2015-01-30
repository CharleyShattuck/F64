package com.F64;

public class Local {
	private int 	index;

	public Local(int ind)
	{
		index = ind;
	}

	public int getIndex() {return index;}
	public String getDisplay() {return "l" + index;}

	public static String getDisplay(int index) {return "l" + index;}

}
