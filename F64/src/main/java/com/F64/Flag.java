package com.F64;

public enum Flag {
	Carry;

	long getMask()
	{
		long res = 1;
		res <<= this.ordinal();
		return res;
	}


}
