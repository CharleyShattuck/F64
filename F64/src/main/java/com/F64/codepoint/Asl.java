package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.RegOp1;
import com.F64.Register;

public class Asl extends com.F64.Codepoint {

	@Override
	public boolean optimize()
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(RegOp1.ASL, Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal());
	}


}
