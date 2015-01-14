package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext3;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.Register;

public class Reverse extends com.F64.Codepoint {
	private int reg;

	public Reverse()
	{
		reg = -1;
	}

	public Reverse(int reg)
	{
		this.reg = reg;
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Literal) {
					// top of stack is multiplied with a constant
					// this gives a lot of opportunities for optimization
					Literal lit = (Literal) p;
					lit.setValue(Processor.reverseBits(lit.getValue()));
					this.remove();
					return true;
				}
				break;

			case PEEPHOLE:
				if (p instanceof Reverse) {
					// 2 reverse do nothing
					p.remove();
					this.remove();
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
		if (reg < 0) {
			c.generate(Ext3.REVERSE, Register.T.ordinal());
		}
		else {
			c.generate(Ext3.REVERSE, reg);
		}
	}


}
