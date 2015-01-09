package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;

public class Mul extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
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
						lit1.setValue(lit1.getValue() * lit2.getValue());
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
						// multiply with 0 give always 0
						lit.replaceWith(new Zero());
						this.remove();
						return true;
					}
					if (data == 1) {
						// multiply with 1 does nothing
						lit.remove();
						this.remove();
						return true;
					}
					if (data == -1) {
						// multiply with -1 is negate
						lit.replaceWith(new Negate());
						this.remove();
						return true;
					}
					if (Processor.countBits(data) == 1) {
						// multiply with a power of 2 can be realized with a shift operation
						int bit_pos = Processor.findFirstBit1(data);
						if (bit_pos == 1) {
							// multiply with 2
							lit.replaceWith(new Mul2());
							this.remove();
							return true;
						}
						// shift by bit_pos
						lit.replaceWith(new Asl(bit_pos));
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
		
	}
}
