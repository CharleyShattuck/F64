package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;

public class Exit extends com.F64.Codepoint {

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		
	}

}
