package com.F64;

public enum Interrupt {
	Reset,
	NMI,
	Clock;

	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}

}
