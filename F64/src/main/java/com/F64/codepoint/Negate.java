package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.RegOp1;
import com.F64.RegOp2;
import com.F64.Register;

public class Negate extends com.F64.Codepoint {
	private int src;
	private int dest;

	public Negate()
	{
		src = dest = -1;
	}

	public Negate(int reg)
	{
		src = dest = reg;
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
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
	public void generate(Builder b)
	{
		if (src == dest) {
			b.add(RegOp1.NEGATE, dest < 0 ? Register.T.ordinal() : dest);
		}
		else {
			b.add(RegOp2.NEGATE, dest < 0 ? Register.T.ordinal() : dest, src < 0 ? Register.T.ordinal() : src);
		}
	}

}
