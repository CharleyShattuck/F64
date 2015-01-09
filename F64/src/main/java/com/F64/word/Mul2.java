package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class Mul2 extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.doMul2Add(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal());
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Literal(2));
		c.compile(new com.F64.codepoint.Mul());
	}

}
