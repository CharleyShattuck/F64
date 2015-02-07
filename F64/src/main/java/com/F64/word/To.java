package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;

public class To extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		String name = i.getNextWord();
		com.F64.Word w = i.lookup(name);
		assert(w != null);
		if (w instanceof Val) {
			Val value = (Val)w;
			p.getTask().pushT(i.getSystem().getMemory(value.getAdr()));
			p.getTask().store();
		}
		else {
			assert(false);
		}
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		String name = i.getNextWord();
		com.F64.Word w = i.lookup(name);
		assert(w != null);
		if (w instanceof Val) {
			Val value = (Val)w;
			c.compile(new com.F64.codepoint.Literal(value.getAdr()));
			c.compile(new com.F64.codepoint.Store());
		}
		else if (w instanceof Var) {
			Var value = (Var)w;
			c.compile(new com.F64.codepoint.Literal(value.getAdr()));
			c.compile(new com.F64.codepoint.Store());
		}
		else if (w instanceof Local) {
			Local value = (Local)w;
			c.compile(new com.F64.codepoint.LocalStore(value.getIndex()));
		}
		else {
			assert(false);
		}
	}


}
