package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Ext3;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.Register;

public class Abs extends com.F64.Codepoint {
	private int reg;

	public Abs()
	{
		reg = -1;
	}

	public Abs(int reg)
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
					Literal lit = (Literal) p;
					long data = lit.getValue();
					lit.setValue(data < 0 ? data : data);
					this.remove();
					return true;
				}
				break;


			case PEEPHOLE:
				if (p instanceof Abs) {
					// 2 abs is one too much
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
			c.generate(Ext2.ABS);
		}
		else {
			c.generate(Ext3.ABS, reg);			
		}
	}

}
