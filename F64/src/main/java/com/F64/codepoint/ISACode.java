package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;

public class ISACode extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		
	}

}
