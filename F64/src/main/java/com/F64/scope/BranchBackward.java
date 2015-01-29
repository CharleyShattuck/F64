package com.F64.scope;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Optimization;

public class BranchBackward extends com.F64.Block implements java.lang.Cloneable {
	private Condition		cond;		// for branching

	public BranchBackward(Compiler c)
	{
		super(c.getScope());
		c.setScope(this);
	}


	@Override
	public BranchBackward clone() throws CloneNotSupportedException
	{
		BranchBackward res = (BranchBackward)super.clone();
		return res;
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
	}

}
