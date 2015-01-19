package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp2;
import com.F64.RegOp3;
import com.F64.Register;

public class Rcr extends com.F64.Codepoint {
	private int		src1;
	private int 	src2;
	private int 	dest;
	private	int		cnt;

	public Rcr()
	{
		src1 = src2 = dest = -1;
		cnt = -1;
	}

	public Rcr(int value)
	{
		src1 = src2 = dest = -1;
		cnt = value;
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		if (this.cnt >= 0) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if ((p != null) && (dest==-1)) {
			switch (opt) {
//			case CONSTANT_FOLDING:
//				com.F64.Codepoint pp = p.getPrevious();
//				if (pp != null) {
//					if ((p instanceof Literal) && (pp instanceof Literal)) {
//						// constant
//						Literal lit1 = (Literal) pp;
//						Literal lit2 = (Literal) p;
//						long data = lit1.getValue();
//						int shift = (int)(lit2.getValue() & (Processor.BIT_PER_CELL-1));
//						lit1.setValue(processor.rcl(data, shift));
//						lit2.replaceWith(new Carry(processor.getInternalCarry()));
//						this.remove();
//						return true;
//					}
//				}
//				break;

			case PEEPHOLE:
				if (p instanceof Literal) {
					// top of stack is multiplied with a constant
					// this gives a lot of opportunities for optimization
					Literal lit = (Literal) p;
					int shift = (int)(lit.getValue() & (Processor.BIT_PER_CELL-1));
					if (shift == 0) {
						lit.remove();
						this.remove();
						return true;
					}
					lit.replaceWith(new Rcr(shift));
					this.remove();
					return true;
				}
				if (p instanceof Rcr) {
					Rcr prev = (Rcr)p;
					if ((this.cnt >= 0) && (prev.cnt >= 0)) {
						prev.cnt = (prev.cnt+this.cnt) & (Processor.BIT_PER_CELL-1);
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
		if (dest == -1) {
			if (cnt == -1) {
				c.generate(RegOp3.RCR, Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal());
				c.generate(ISA.NIP);
			}
			else {
				c.generate(RegOp3.RCRI, Register.T.ordinal(), Register.T.ordinal(), cnt);
			}
		}
		else if (dest == src1) {
			if (src2 >= 0) {
				c.generate(RegOp2.RCR, dest, src2);
			}
			else {
				c.generate(RegOp2.RCRI, dest, cnt);
			}
		}
		else if (src2 >= 0) {
			c.generate(RegOp3.RCR, dest, src1, src2);
		}
		else {
			c.generate(RegOp3.RCRI, dest, src1, cnt);
		}
	}



}
