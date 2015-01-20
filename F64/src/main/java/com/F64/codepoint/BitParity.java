package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.RegOp2;
import com.F64.Register;

public class BitParity extends com.F64.Codepoint {
	private int src;
	private int dest;

	public BitParity()
	{
		src = dest = -1;
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
					lit.setValue(Processor.parityBits(lit.getValue()) ? Processor.TRUE : Processor.FALSE);
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
			b.add(RegOp1.PARITY, dest < 0 ? Register.T.ordinal() : dest);
		}
		else {
			b.add(RegOp2.PARITY, dest < 0 ? Register.T.ordinal() : dest, src < 0 ? Register.T.ordinal() : src);
		}
	}


}
