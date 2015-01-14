package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp3;
import com.F64.Register;

public class Rol extends com.F64.Codepoint {
	private	int			cnt;

	public Rol()
	{
		cnt = -1;
	}

	public Rol(int value)
	{
		cnt = value;
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
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
						long data = lit1.getValue();
						int shift = (int)(lit2.getValue() & (Processor.BIT_PER_CELL-1));
						lit1.setValue(processor.rol(data, shift));
						lit2.replaceWith(new Carry(processor.getInternalCarry()));
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
					int shift = (int)(lit.getValue() & (Processor.BIT_PER_CELL-1));
					if (shift == 0) {
						lit.remove();
						this.remove();
						return true;
					}
					lit.replaceWith(new Rol(shift));
					this.remove();
					return true;
				}
				if (p instanceof Rol) {
					Rol prev = (Rol)p;
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
		if (cnt == -1) {
			c.generate(Ext1.ROL);
		}
		else {
			c.generate(RegOp3.ROLI, Register.T.ordinal(), Register.T.ordinal(), cnt);
		}
	}


}
