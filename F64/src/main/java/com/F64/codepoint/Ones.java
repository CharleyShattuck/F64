package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Register;

public class Ones extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
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

			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(ISA.MOV, Register.T.ordinal(), Register.Z.ordinal());
		c.generate(ISA.NOT);
	}


}
