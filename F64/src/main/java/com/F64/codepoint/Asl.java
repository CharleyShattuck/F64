package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.RegOp1;
import com.F64.Register;

public class Asl extends com.F64.Codepoint {
	private	int			cnt;

	public Asl()
	{
		cnt = -1;
	}

	public Asl(int value)
	{
		cnt = value;
	}

	@Override
	public boolean optimize(Optimization opt)
	{
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (cnt == -1) {
			c.generate(RegOp1.ASL, Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal());
		}
		else {
			c.generate(RegOp1.ASLI, Register.T.ordinal(), Register.T.ordinal(), cnt);
		}
	}


}
