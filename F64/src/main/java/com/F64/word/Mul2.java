package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Processor;
import com.F64.Register;

public class Mul2 extends com.F64.Word {
	@Override
	public boolean isImmediate() {return false;}

	@Override
	public void execute(Processor p)
	{
		p.doMul2Add(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal());
	}

	@Override
	public void compile(Compiler c)
	{
		c.compile(ISA.MUL2.ordinal());
	}

}
