package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Optimization;
import com.F64.Processor;

public class Under extends com.F64.Codepoint {

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(Ext2.UNDER);
	}

}
