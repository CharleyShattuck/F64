package com.F64.word;

import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class Begin extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.doThrow(Exception.COMPILE_ONLY);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Begin(c));
	}


}
