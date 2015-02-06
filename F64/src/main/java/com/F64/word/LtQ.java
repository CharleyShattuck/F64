package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class LtQ extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.getTask().setRegister(Register.T, p.getTask().getRegister(Register.S) - p.getTask().getRegister(Register.T));
		p.getTask().nip();
		p.getTask().lt0q(Register.T.ordinal(), Register.T.ordinal(), false);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Sub());
		c.compile(new com.F64.codepoint.Lt0Q());
	}


}
