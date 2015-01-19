package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;

public class Execute extends com.F64.Codepoint {
	private long	adr;
	private boolean	adr_valid;
	private boolean	use_jump;
	
	public boolean optimizeExit()
	{
		if (adr_valid && !use_jump) {
			use_jump = true;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if ((p instanceof Literal) && !adr_valid) {
					Literal lit = (Literal) p;
					adr = lit.getValue();
					adr_valid = true;
					lit.remove();
					return true;
				}
				break;


			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (adr_valid) {
			c.generateCall(adr, use_jump);
		}
		else {
			c.generate(Ext1.EXECUTE);
			if (use_jump) {
				c.generate(ISA.EXIT);
			}
		}
	}


}
