package com.F64.codepoint;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Condition;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Scope;
import com.F64.Compiler;

public class If extends com.F64.Scope {
	private Condition	cond;		// for branching
	private Scope		false_part;

	public If(Compiler c, Condition cond)
	{
		super(c.getScope());
		this.cond = cond;
	}

	public void doElse(Compiler c)
	{
		false_part = new Scope(this);
		c.setScope(false_part);	
	}

	public void doThen(Compiler c)
	{
		c.setScope(this.getOwner());	
	}
	
	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = false;
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (cond == Condition.ALWAYS) {
				if (!isEmpty()) {
					this.clear();
					if (false_part != null) {
						false_part.optimize(c, opt);
					}
					res = true;
				}
			}
			else if (cond == Condition.NEVER) {
				if (false_part != null) {
					false_part = null;
					res = true;
				}
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
		if (false_part != null) {
			if (false_part.optimize(c, opt)) {res = true;}
		}
		if (super.optimize(c, opt)) {res = true;}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
		int slot;
		if (cond == Condition.ALWAYS) {
			if (false_part != null) {
				false_part.generate(b);
			}
			return;
		}
		if (cond == Condition.NEVER) {
			super.generate(b);
			return;
		}
		if (this.isEmpty()) {
			if (false_part == null) {return;}
		}
		else {
			if (false_part == null) {
				// only if part
				// first we test if the if statement fits into current cell
				Builder probe = b.fork(false);
				probe.add(ISA.BRANCH, cond.encode(Branch.NEXT));
				super.generate(probe);
				if (!probe.exceed1Cell()) {
					slot = probe.getCurrentSlot();
					b.add(ISA.BRANCH, cond.encode(slot == 0 ? Branch.NEXT.ordinal() : slot));
					super.generate(b);
					return;
				}
			}
		}
	}

}
