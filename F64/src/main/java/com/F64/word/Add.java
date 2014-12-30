package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Processor;
import com.F64.Register;

public class Add extends com.F64.Word {

	@Override
	public boolean isImmediate() {return false;}

	@Override
	public void execute(Processor p)
	{
		p.setRegister(Register.T, p.getRegister(Register.S) + p.getRegister(Register.T));
		p.doNip();
	}

	@Override
	public void compile(Compiler c)
	{
		c.compile(ISA.ADD);
	}

}
