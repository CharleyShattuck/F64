package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp2;
import com.F64.RegOp3;

public class Min extends com.F64.Codepoint {
	private int src1;
	private int src2;
	private int dest;

	public Min()
	{
		src1 = src2 = dest = -1;
	}

	public Min(int d, int s1, int s2)
	{
		src1 = s1;
		src2 = s2;
		dest = d;
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if ((p != null) && (dest==-1)) {
			switch (opt) {
			case CONSTANT_FOLDING:
				com.F64.Codepoint pp = p.getPrevious();
				if (pp != null) {
					if ((p instanceof Literal) && (pp instanceof Literal)) {
						// constant
						Literal lit1 = (Literal) pp;
						Literal lit2 = (Literal) p;
						lit1.setValue(Processor.min(lit1.getValue(), lit2.getValue()));
						lit2.remove();
						this.remove();
						return true;
					}
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
		if (dest == src1) {
			if (dest == -1) {
				c.generate(Ext1.MIN);
			}
			else {
				c.generate(RegOp2.MIN, dest, src2);
			}
		}
		else {
			c.generate(RegOp3.MIN, dest, src1, src2);
		}
	}


}
