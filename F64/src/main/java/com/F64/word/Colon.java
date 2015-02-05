package com.F64.word;

import com.F64.Compiler;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class Colon extends com.F64.Word {
	private boolean		inline;
	private boolean		local;
	
	public Colon (boolean inline, boolean local)
	{
		assert(!inline || !local);
		this.inline = inline;
		this.local = local;
	}
	
	@Override
	public void execute(Interpreter i)
	{
		assert(!local);
		String name = i.getNextWord();
		com.F64.word.Secondary sec = new com.F64.word.Secondary(i);
		i.getDictionary().register(name, false, sec);
		i.getCompiler().start(sec);
		if (inline) {
			sec.setInline(i.getCompiler().getBlock());
		}
		i.setCompiling(true);
		i.getCompiler().compile(new com.F64.codepoint.Enter());
	}

	@Override
	public void compile(Interpreter i)
	{
		if (local) {
			Compiler c = i.getCompiler();
			new com.F64.scope.Colon(c);
		}
		else {
			Processor p = i.getProcessor();
			p.doThrow(Exception.EXECUTE_ONLY);
		}
	}


}
