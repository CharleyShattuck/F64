package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.Optimization;

public class Tuck extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {

			case PEEPHOLE:
				if (p instanceof Dup) {
					// the sequence dup drop does nothing and can be deleted
					this.replaceWith(new Dup());
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
		b.add(Ext1.TUCK);
	}

}
