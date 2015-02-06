package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class SystemFetch extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		int reg = (int)p.getTask().getRegister(Register.T.ordinal());
		p.getTask().drop();
		p.doFetchSystem(reg);
		
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.SystemFetch());
	}


}
