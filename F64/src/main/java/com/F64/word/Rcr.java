package com.F64.word;

import com.F64.Compiler;
import com.F64.Flag;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class Rcr extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.setRegister(Register.T, p.rcr(p.getRegister(Register.S), (int)p.getRegister(Register.T), p.getFlag(Flag.CARRY)));
		p.doNip();
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Rcr());
	}



}
