package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class DivMod extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		long dd = p.getRegister(Register.S);
		long ds = p.getRegister(Register.T);
		p.setRegister(Register.S, dd/ds);
		p.setRegister(Register.T, dd%ds);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.DivMod());
	}


}
