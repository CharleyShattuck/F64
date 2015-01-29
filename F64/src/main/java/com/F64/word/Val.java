package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;

public class Val extends com.F64.Word {
	private long	adr;

	public Val(com.F64.System s, long value)
	{
		this.adr = s.reserveData(1);
		s.setMemory(adr, value);
	}

	public long getAdr() {return adr;}

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.pushT(adr);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Literal(adr));
		c.compile(new com.F64.codepoint.Fetch());
	}


}
