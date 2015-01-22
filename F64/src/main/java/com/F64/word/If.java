package com.F64.word;

import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class If extends com.F64.Word {

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
		com.F64.scope.If op = new com.F64.scope.If(c, Condition.EQ0);
		c.compile(op);
		c.setScope(op);
	}


	
}
