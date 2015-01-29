package com.F64.scope;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.codepoint.Literal;

public class BranchForward extends com.F64.Block implements java.lang.Cloneable {
	private Condition		cond;		// for branching

	public BranchForward(Compiler c)
	{
		super(c.getScope());
		c.setScope(this);
		cond = Condition.NEVER;
	}

	public void setCondition(Condition value) {cond = value;}
	public Condition getCondition() {return cond;}

	@Override
	public BranchForward clone() throws CloneNotSupportedException
	{
		BranchForward res = (BranchForward)super.clone();
		return res;
	}

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
				this.replaceWithScope(this);
				res = true;
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
		int slot;
		if (cond == Condition.ALWAYS) {
			return;
		}
		if (cond == Condition.NEVER) {
			super.generate(b);
			return;
		}
		if (this.isEmpty()) {
			return;
		}
		else {
			// only if part
			// first we test if the if statement fits into current cell
			Builder probe = b.fork(false);
			probe.add(ISA.BRANCH, cond.encode(Branch.SKIP));
			super.generate(probe);
			if (!probe.exceed1Cell()) {
				slot = probe.getCurrentSlot();
				b.add(ISA.BRANCH, cond.encode(slot == 0 ? Branch.SKIP.ordinal() : slot));
				super.generate(b);
				return;
			}
		}
	}

}
