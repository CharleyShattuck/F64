package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Optimization;

public class For extends com.F64.Scope {

	public For(Compiler c)
	{
		super(c.getScope());
		c.setScope(this);
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = false;
		if (super.optimize(c, opt)) {res = true;}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
//		if (cond == Condition.ALWAYS) {
//			if (false_part != null) {
//				false_part.generate(c);
//			}
//			return;
//		}
//		if (cond == Condition.NEVER) {
//			if (true_part != null) {
//				true_part.generate(c);
//			}
//			return;
//		}		
	}


}
