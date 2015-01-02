package com.F64.codepoint;

import com.F64.Compiler;

public class ISACode extends com.F64.Codepoint {

	@Override
	public boolean optimize()
	{
		if (this.getPrevious() == null) {return false;}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		
	}

}
