package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;

public class Over extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Builder b)
	{
		b.add(ISA.OVER);
	}

}
