package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;

public class Var extends com.F64.Word {
	private long	adr;
	private long	size;

	public Var(com.F64.System s, long size)
	{
		this.adr = s.reserveData(size);
		this.size = size;
		for (long i=0; i<size; ++i) {
			s.setMemory(adr+i, 0);
		}
	}

	public long getAdr() {return adr;}
	public long getSize() {return size;}

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.getTask().pushT(adr);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Literal(adr));
	}


}
