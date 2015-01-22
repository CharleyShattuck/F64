package com.F64.word;

import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;

public class Colon extends com.F64.Word {
	private boolean		inline;
	
	public Colon (boolean inline)
	{
		this.inline = inline;
	}
	
	@Override
	public void execute(Interpreter i)
	{
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
		Processor p = i.getProcessor();
		p.doThrow(Exception.EXECUTE_ONLY);
	}


}
