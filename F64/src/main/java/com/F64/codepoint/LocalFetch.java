package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext4;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.Register;

public class LocalFetch extends com.F64.Codepoint {
	private int reg;

	public LocalFetch()
	{
		reg = -1;
	}

	public LocalFetch(int reg)
	{
		this.reg = reg;
	}
	
	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if ((reg < 0) && (p instanceof Literal)) {
					Literal lit = (Literal) p;
					int data = (int)lit.getValue();
					reg = data & Processor.SLOT_MASK;
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
		if (reg < 0) {
			b.add(Ext4.LFETCHI, Register.T.ordinal());
		}
		else {
			b.add(ISA.LFETCH, reg);
		}
	}


}
