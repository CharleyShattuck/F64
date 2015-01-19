package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;

public class Const extends com.F64.Word {
	private long	value;

	public Const(long value)
	{
		this.value = value;
	}

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.pushT(value);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Literal(value));
	}


}
