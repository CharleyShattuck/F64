package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Ext3;
import com.F64.Optimization;
import com.F64.Register;

public class Negate extends com.F64.Codepoint {
	private int reg;

	public Negate()
	{
		reg = -1;
	}

	public Negate(int reg)
	{
		this.reg = reg;
	}

	@Override
	public boolean optimize(Optimization opt)
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
					lit.setValue(-lit.getValue());
					this.remove();
					return true;
				}
				break;

			case PEEPHOLE:
				if (p instanceof Negate) {
					// 2 negates do nothing
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
			c.generate(Ext2.NEGATE);
		}
		else {
			c.generate(Ext3.NEGATE, reg);			
		}
	}

}
