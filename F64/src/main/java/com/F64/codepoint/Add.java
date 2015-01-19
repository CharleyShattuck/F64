package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp2;
import com.F64.RegOp3;
import com.F64.Register;

public class Add extends com.F64.Codepoint {
	private int src1;
	private int src2;
	private int dest;


	public Add()
	{
		src1 = src2 = dest = -1;
	}

	public Add(int d, int s1, int s2)
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
						lit1.setValue(lit1.getValue() + lit2.getValue());
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
						if (data == 1) {
							// increment
							lit.replaceWith(new Inc());
							this.remove();
							return true;
						}
						if (data < Processor.SLOT_SIZE) {
							lit.replaceWith(new RegOpCode(RegOp3.ADDI, Register.T.ordinal(), Register.T.ordinal(), (int)data));
							this.remove();
							return true;
						}
					}
					else {
						if (data == -1) {
							// decrement
							lit.replaceWith(new Dec());
							this.remove();
							return true;
						}
						if (data > -Processor.SLOT_SIZE) {
							lit.replaceWith(new RegOpCode(RegOp3.SUBI, Register.T.ordinal(), Register.T.ordinal(), -(int)data));
							this.remove();
							return true;
						}
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
				c.generate(ISA.ADD);
			}
			else {
				c.generate(RegOp2.ADD, dest, src2);
			}
		}
		else {
			c.generate(RegOp3.ADD, dest, src1, src2);
		}
	}

}
