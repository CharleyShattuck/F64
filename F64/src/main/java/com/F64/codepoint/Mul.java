package com.F64.codepoint;

public class Mul extends com.F64.Codepoint {

	public Mul()
	{
	}

	@Override
	public boolean optimize()
	{
		if (this.isReferenced()) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if (p instanceof Literal) {
				
			}
		}
		return false;
	}
}
