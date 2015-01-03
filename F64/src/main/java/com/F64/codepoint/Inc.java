package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Register;

public class Inc extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(ISA.RINC, Register.T.ordinal());
	}


}
