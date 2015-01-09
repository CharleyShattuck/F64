package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.Register;

public class Zero extends com.F64.Codepoint {

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Literal) {
					Literal lit = (Literal) p;
					lit.setValue(0);
					this.remove();
					return true;
				}
				if (p instanceof Dup) {
					p.replaceWith(new Literal(0));
					this.remove();
					return true;					
				}
				if (p instanceof Over) {
					p.replaceWith(new Literal(0));
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
	}

}
