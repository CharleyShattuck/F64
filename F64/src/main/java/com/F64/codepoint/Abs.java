package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Optimization;

public class Abs extends com.F64.Codepoint {

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
					long data = lit.getValue();
					lit.setValue(data < 0 ? data : data);
					this.remove();
					return true;
				}
				break;


			case PEEPHOLE:
				if (p instanceof Abs) {
					// 2 abs is one too much
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
		c.generate(Ext2.ABS);
	}

}
