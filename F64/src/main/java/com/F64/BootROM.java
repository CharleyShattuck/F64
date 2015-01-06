package com.F64;

public class BootROM {
	private int position;
	
	public BootROM()
	{
		
	}

	public void reset()
	{
		position = 0;
	}

	public long nextValue()
	{
		++position;
		return 0;
	}


}
