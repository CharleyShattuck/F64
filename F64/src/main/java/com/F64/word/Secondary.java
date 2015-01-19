package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Interpreter;
import com.F64.Processor;

public class Secondary extends com.F64.Word {
	private com.F64.codepoint.Secondary	codepoint;

	public Secondary(Interpreter i)
	{
		i.getCompiler().flush();
		codepoint = new com.F64.codepoint.Secondary(i.getSystem().getCodePosition());
	}
	
	
	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		long code = Processor.writeSlot(codepoint.getAdr(), 0, ISA.CALL.ordinal());
		p.execute(code);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Secondary(codepoint.getAdr()));
	}


}
