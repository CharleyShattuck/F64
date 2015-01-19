package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Scope;

public class While extends com.F64.Scope {
	private Condition	cond;		// for branching

	public While(Compiler c, Condition cond)
	{
		super(c.getScope());
		this.cond = cond;
		c.setScope(this);
	}

}
