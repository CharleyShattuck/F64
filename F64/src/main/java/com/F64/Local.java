package com.F64;

public class Local {
	private int 	index;
	private String	name;

	public Local(String name, int ind)
	{
		index = ind;
		this.name = name;
	}

	public int getIndex() {return index;}
	public String getName() {return name;}
	public String getDisplay() {return "l" + index;}

	public static String getDisplay(int index) {return "l" + index;}

}
