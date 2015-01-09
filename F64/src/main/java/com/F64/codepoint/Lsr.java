package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.Register;

public class Lsr extends com.F64.Codepoint {
	private	int			cnt;

	public Lsr()
	{
		cnt = -1;
	}

	public Lsr(int value)
	{
		cnt = value;
	}

	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		if (this.cnt >= 0) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				com.F64.Codepoint pp = p.getPrevious();
				if (pp != null) {
					if ((p instanceof Literal) && (pp instanceof Literal)) {
						// constant
						Literal lit1 = (Literal) pp;
						Literal lit2 = (Literal) p;
						lit1.setValue(lit1.getValue() >>> lit2.getValue());
						lit2.remove();
						this.remove();
						return true;
					}
				}
				break;

			case PEEPHOLE:
				if (p instanceof Literal) {
					// top of stack is multiplied with a constant
					// this gives a lot of opportunities for optimization
					Literal lit = (Literal) p;
					long data = lit.getValue();
					if (data == 0) {
						// add by 0 does nothing
						lit.remove();
						this.remove();
						return true;
					}
					if (data > 0) {
						if (data < Processor.SLOT_SIZE) {
							lit.replaceWith(new Lsr((int)data));
							this.remove();
							return true;
						}
						else {
							lit.replaceWith(new Zero());
							this.remove();
							return true;
						}
					}
					else {
						if (data == -0x8000_0000_0000_0000L) {
							assert(false);
							lit.remove();
							this.remove();
							return true;
						}
						lit.setValue(-data);
						this.replaceWith(new Lsl());
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
		if (cnt == -1) {
			c.generate(RegOp1.LSR, Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal());
		}
		else {
			c.generate(RegOp1.LSRI, Register.T.ordinal(), Register.T.ordinal(), cnt);
		}
	}


}
