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
						this.getScope().remove(lit2);
						this.getScope().remove(this);
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
						this.getScope().replace(lit, new Zero());
						this.getScope().remove(this);
						return true;
					}
					if (data == 1) {
						// multiply with 1 does nothing
						this.getScope().remove(lit);
						this.getScope().remove(this);
						return true;
					}
					if (data == -1) {
						// multiply with 1 is negate
						this.getScope().replace(lit, new Negate());
						this.getScope().remove(this);
						return true;
					}
					if (Processor.countBits(data) == 1) {
						// multiply with a power of 2 can be realized with a shift operation
						int bit_pos = Processor.findFirstBit1(data);
						if (bit_pos == 1) {
							// multiply by 2
							this.getScope().replace(lit, new Mul2());
							this.getScope().remove(this);
							return true;
						}
						// shift by bit_pos
						this.getScope().replace(lit, new Asl(bit_pos));
						this.getScope().remove(this);
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
