package com.F64.word;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Interpreter;
import com.F64.Processor;

public class Secondary extends com.F64.Word {
	private com.F64.codepoint.Secondary	codepoint;
	private com.F64.Block				inline;

	public Secondary(Interpreter i)
	{
		i.getCompiler().getBuilder().flush();
		codepoint = new com.F64.codepoint.Secondary(i.getSystem().getCodePosition());
	}

	public void setInline(com.F64.Block b)
	{
		inline = b;
	}
	
	@Override
	public boolean isInline() {return inline != null;}

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
		if (inline != null) {
			c.append(inline);
		}
		else {
			c.compile(new com.F64.codepoint.Secondary(codepoint.getAdr()));
		}
	}


}
