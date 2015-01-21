package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.RegOp2;
import com.F64.Register;

public class BitCount0 extends com.F64.Codepoint {
	private int src;
	private int dest;

	public BitCount0()
	{
		src = dest = -1;
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
					Literal lit = (Literal) p;
					lit.setValue(Processor.BIT_PER_CELL-Processor.countBits(lit.getValue()));
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
			b.add(RegOp1.BITCNT0, dest < 0 ? Register.T.ordinal() : dest);
		}
		else {
			b.add(RegOp2.BITCNT0, dest < 0 ? Register.T.ordinal() : dest, src < 0 ? Register.T.ordinal() : src);
		}
	}


}
