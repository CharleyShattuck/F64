package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Optimization;

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
	public boolean optimize(Compiler c, Optimization opt)
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
	public void generate(Builder b)
	{
		if (adr_valid) {
			b.addCall(adr, use_jump);
		}
		else {
			b.add(Ext1.EXECUTE);
			if (use_jump) {
				b.add(ISA.EXIT);
			}
		}
	}


}
