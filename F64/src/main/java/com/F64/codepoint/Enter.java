package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Scope;

public class Enter extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Builder b)
	{
		b.add(ISA.ENTER);
		Scope s = getOwner();
		if (s instanceof com.F64.Block) {
			com.F64.Block blk = (com.F64.Block)s;
			blk.generateEnterLocals(b);
		}
	}


}
