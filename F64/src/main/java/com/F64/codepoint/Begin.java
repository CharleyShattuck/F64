package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.Scope;

public class Begin extends com.F64.Scope {
	private Condition	cond;		// for branching

	public Begin(Compiler c, Condition cond)
	{
		super(c.getScope());
		this.cond = cond;
		c.setScope(this);
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = false;
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (cond == Condition.ALWAYS) {
			}
			else if (cond == Condition.NEVER) {
			}
		}
		else if (opt == Optimization.CONSTANT_FOLDING) {
			if ((cond == Condition.EQ0) || (cond == Condition.GE0)) {
				com.F64.Codepoint p = this.getPrevious();
				if (p != null) {
					if (p instanceof Literal) {
						// top of stack is multiplied with a constant
						// this gives a lot of opportunities for optimization
						Literal lit = (Literal) p;
						long data = lit.getValue();
						if (cond == Condition.EQ0) {
							if (data == 0) {
								cond = Condition.ALWAYS;
							}
							else {
								cond = Condition.NEVER;
							}
						}
						else {
							if (data >= 0) {
								cond = Condition.ALWAYS;
							}
							else {
								cond = Condition.NEVER;
							}
						}
						lit.remove();
						res = true;
					}
				}
			}
		}
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
