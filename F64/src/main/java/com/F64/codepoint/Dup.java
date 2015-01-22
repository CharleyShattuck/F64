package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;

public class Dup extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Literal) {
					Literal lit = (Literal)p;
					this.replaceWith(new Literal(lit.getValue()));
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
		b.add(ISA.DUP);
	}

}
