package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Processor;
import com.F64.Exception;

public class Exit extends com.F64.Word {

	@Override
	public boolean isImmediate() {return false;}

	@Override
	public void execute(Processor p)
	{
		p.doThrow(Exception.COMPILE_ONLY);
	}

	@Override
	public void compile(Compiler c)
	{
		c.compile(ISA.EXIT.ordinal());
	}

}
