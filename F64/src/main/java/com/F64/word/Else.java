package com.F64.word;

import com.F64.Compiler;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Scope;

public class Else extends com.F64.Word {

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
		while (!(s instanceof com.F64.codepoint.If)) {
			s = s.getOwner();
			if (s == null) {
				i.getProcessor().doThrow(Exception.INVALID_SCOPE);
				return;
			}
		}
		com.F64.codepoint.If if_scope = (com.F64.codepoint.If)s;
		if_scope.doElse(c);
	}



}
