package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class Mod extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		long dd = p.getRegister(Register.S);
		long ds = p.getRegister(Register.T);
		if (dd < 0) {dd = -dd;}
		boolean dsneg = false;
		if (ds < 0) {dsneg = true; ds = -ds;}
		long r = dd%ds;
//		if ((r != 0) && ((dd ^ ds) < 0)) {
//			--q;
//			r += ds;
//		}
		if (dsneg) {
			r = -r;
		}
		p.setRegister(Register.T, r);
		p.getTask().nip();
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.Mod());
	}


}
