package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Processor;
import com.F64.Register;

public class Swap extends com.F64.Word {
	@Override
	public boolean isImmediate() {return false;}

	@Override
	public void execute(Processor p)
	{
		p.doSwap(Register.T.ordinal(), Register.S.ordinal());
	}

	@Override
	public void compile(Compiler c)
	{
		c.compile(ISA.SWAP.ordinal(), Register.T.ordinal(), Register.S.ordinal());
	}

}
