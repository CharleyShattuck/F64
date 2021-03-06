package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;

public class Ones extends com.F64.Codepoint {

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
					lit.setValue(-1L);
					this.remove();
					return true;
				}
				if (p instanceof Dup) {
					p.replaceWith(new Literal(-1L));
					this.remove();
					return true;					
				}
				if (p instanceof Over) {
					p.replaceWith(new Literal(-1L));
					this.remove();
					return true;					
				}
				break;

			case PEEPHOLE:
				if (p instanceof Add) {
					Add op = (Add) p;
					if (op.isConstant()) {
						p.remove();
						return true;					
					}
				}
				if (p instanceof Sub) {
					Sub op = (Sub) p;
					if (op.isConstant()) {
						p.remove();
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
		b.add(ISA.NLIT, 0);
	}


}
