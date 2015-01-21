package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.Optimization;

public class Under extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Builder b)
	{
		b.add(Ext1.UNDER);
	}

}
