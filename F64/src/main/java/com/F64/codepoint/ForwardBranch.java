package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.ISA;
import com.F64.Optimization;

public class ForwardBranch extends com.F64.Codepoint {
	private Condition		cond;
	private com.F64.Block	block;

	public ForwardBranch(Condition c)
	{
		cond = c;
	}

	public void setBlock(com.F64.Block b) {block = b;}
	public com.F64.Block getBlock() {return block;}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = false;
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (cond == Condition.ALWAYS) {
				this.remove();
				res = true;
			}
			else if (cond == Condition.NEVER) {
				super.optimize(c, opt);
				this.replaceWithScope(block);
				res = true;
			}
		}
		else if (opt == Optimization.CONSTANT_FOLDING) {
			if ((cond == Condition.EQ0) || (cond == Condition.GE0)) {
				com.F64.Codepoint p = this.getPrevious();
				if (p != null) {
					if (p instanceof Literal) {
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
//		if (false_part != null) {
//			if (false_part.optimize(c, opt)) {res = true;}
//		}
//		if (true_part.optimize(c, opt)) {res = true;}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
	}


}
