package com.F64.codepoint;

import com.F64.Compiler;

public class Negate extends com.F64.Codepoint {

	@Override
	public boolean optimize()
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if (p instanceof Negate) {
				// 2 negates do nothing
				this.getScope().remove(p);
				this.getScope().remove(this);
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
