package com.F64.codepoint;

<<<<<<< HEAD
import com.F64.Condition;
import com.F64.Optimization;
import com.F64.Scope;
import com.F64.Compiler;

public class If extends com.F64.Scope {
	private Condition	cond;		// for branching
	private	Scope		true_part;
	private Scope		false_part;

	public If(Compiler c, Condition cond)
	{
		super(c.getScope());
		this.cond = cond;
		true_part = new Scope(this);
		c.setScope(true_part);
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
	public boolean optimize(Optimization opt)
	{
		boolean res = false;
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (cond == Condition.ALWAYS) {
				if (true_part != null) {
					true_part = null;
					if (false_part != null) {
						false_part.optimize(opt);
					}
					res = true;
				}
			}
			else if (cond == Condition.NEVER) {
				if (false_part != null) {
					false_part = null;
					if (true_part != null) {
						true_part.optimize(opt);
					}
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
						this.getOwner().remove(lit);
						res = true;
					}
				}
			}
		}
		if (false_part != null) {
			if (false_part.optimize(opt)) {res = true;}
		}
		if (true_part != null) {
			if (true_part.optimize(opt)) {res = true;}
		}
		return res;
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (cond == Condition.ALWAYS) {
			if (false_part != null) {
				false_part.generate(c);
			}
			return;
		}
		if (cond == Condition.NEVER) {
			if (true_part != null) {
				true_part.generate(c);
			}
			return;
		}
=======
import com.F64.Scope;
import com.F64.Compiler;

public class If extends com.F64.Codepoint {
	private	Scope	true_part;
	private Scope	false_part;

	public If(Compiler c)
	{
		true_part = new Scope(c.getScope());
		c.setScope(true_part);
	}

	public void doElse(Compiler c)
	{
		false_part = new Scope(true_part.getParent());
		c.setScope(false_part);	
	}

	public void doThen(Compiler c)
	{
		c.setScope(true_part.getParent());	
	}

	@Override
	public boolean optimize()
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
>>>>>>> refs/remotes/origin/master
		
	}

}
