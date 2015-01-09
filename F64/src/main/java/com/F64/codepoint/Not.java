package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Ext3;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.Register;

public class Not extends com.F64.Codepoint {
	private int reg;

	public Not()
	{
		reg = -1;
	}

	public Not(int reg)
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
					lit.setValue(~lit.getValue());
					this.remove();
					return true;
				}
				break;

			case PEEPHOLE:
				if (p instanceof Not) {
					// 2 not do nothing
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
		if ((reg < 0) || (reg == Register.T.ordinal())) {
			c.generate(ISA.NOT);
		}
		else {
			c.generate(RegOp1.XNOR, reg, reg, Register.Z.ordinal());			
		}
	}

}
