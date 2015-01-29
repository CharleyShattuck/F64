package com.F64.word;

import com.F64.Compiler;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class Value extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		String name = i.getNextWord();
		com.F64.word.Val w = new com.F64.word.Val(i.getSystem(), p.popT());
		i.getDictionary().register(name, false, w);
	}

	@Override
	public void compile(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.doThrow(Exception.EXECUTE_ONLY);
	}

}
