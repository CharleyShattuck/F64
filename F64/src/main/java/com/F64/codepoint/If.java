package com.F64.codepoint;

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
		
	}

}
