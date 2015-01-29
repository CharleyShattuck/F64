package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;

public class Div2 extends com.F64.Codepoint {

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
					lit.setValue(lit.getValue() / 2);
					this.remove();
					return true;
				}
				break;

			case PEEPHOLE:
				if (p instanceof Div2) {
					p.replaceWith(new Asr(2));
					this.remove();
					return true;
				}
				if (p instanceof Asr) {
					Asr prev = (Asr)p;
					if (prev.isStandardConstant()) {
						prev.setConstant(prev.getConstant()+1);
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
	public void generate(Builder b)
	{
		b.add(ISA.DIV2);
	}


}
