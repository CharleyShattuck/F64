package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;

public class Mul2 extends com.F64.Codepoint {

	@Override
	public boolean optimize()
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(ISA.MUL2);
	}

}
