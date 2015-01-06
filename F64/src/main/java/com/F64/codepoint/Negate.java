package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;

public class Negate extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if (p instanceof Negate) {
				// 2 negates do nothing
				this.getOwner().remove(p);
				this.getOwner().remove(this);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{

	}

}
