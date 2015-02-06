package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Exception;
import com.F64.Scope;

public class Exit extends com.F64.Word {

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
		Scope s = c.getScope();
		while (s != null) {
			if (s instanceof com.F64.Block) {
				if (s instanceof com.F64.scope.Main) {
					((com.F64.scope.Main)s).internalExit();
				}
			}
			s = s.getOwner();
		}
		c.compile(new com.F64.codepoint.Exit());
	}

}
