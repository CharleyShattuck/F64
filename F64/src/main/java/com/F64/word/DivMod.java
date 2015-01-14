package com.F64.word;

import com.F64.Compiler;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Register;

public class DivMod extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		long dd = p.getRegister(Register.S);
		long ds = p.getRegister(Register.T);
		boolean ddneg = false;
		if (dd < 0) {ddneg = true; dd = -dd;}
		boolean dsneg = false;
		if (ds < 0) {dsneg = true; ds = -ds;}
		long q = dd/ds;
		long r = dd%ds;
//		if ((r != 0) && ((dd ^ ds) < 0)) {
//			--q;
//			r += ds;
//		}
		if (dsneg != ddneg) {
			q = ~q;
		}
		if (dsneg) {
			r = -r;
		}
		p.setRegister(Register.S, q);
		p.setRegister(Register.T, r);
	}

	@Override
	public void compile(Interpreter i)
	{
		Compiler c = i.getCompiler();
		c.compile(new com.F64.codepoint.DivMod());
	}


}
