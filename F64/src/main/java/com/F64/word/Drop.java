package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Processor;

public class Drop extends com.F64.Word {

	@Override
	public boolean isImmediate() {return false;}

	@Override
	public void execute(Processor p)
	{
		p.doDrop();
	}

	@Override
	public void compile(Compiler c)
	{
		c.compile(ISA.DROP.ordinal());
	}


}
