package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
//import com.F64.ISA;
import com.F64.Processor;
//import com.F64.Register;
import com.F64.Exception;

public class Dot extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		java.lang.System.out.println(p.popT());
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.getProcessor().doThrow(Exception.EXECUTE_ONLY);
	}

}
