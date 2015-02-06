package com.F64.word;

import com.F64.Compiler;
import com.F64.Flag;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class Rcl extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.setRegister(Register.T, p.rcl(p.getRegister(Register.S), (int)p.getRegister(Register.T), p.getTask().getFlag(Flag.CARRY)));
		p.getTask().nip();
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Rcl());
	}


}
