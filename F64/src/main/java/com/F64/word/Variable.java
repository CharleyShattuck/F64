package com.F64.word;

import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class Variable extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
//		Processor p = i.getProcessor();
		String name = i.getNextWord();
		com.F64.word.Var w = new com.F64.word.Var(i.getSystem(), 1);
		i.getDictionary().register(name, false, w);
	}

	@Override
	public void compile(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.doThrow(Exception.EXECUTE_ONLY);
	}


}
