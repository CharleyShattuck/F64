package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class Min extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.setRegister(Register.T, Processor.min(p.getRegister(Register.S), p.getRegister(Register.T)));
		p.getTask().nip();
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Min());
	}


}
