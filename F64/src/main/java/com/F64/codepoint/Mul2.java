package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
<<<<<<< HEAD
import com.F64.Optimization;

public class Mul2 extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
=======

public class Mul2 extends com.F64.Codepoint {

	@Override
	public boolean optimize()
>>>>>>> refs/remotes/origin/master
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(ISA.MUL2);
	}

}
