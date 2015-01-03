package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.RegOp1;

public class RegOpCode extends com.F64.Codepoint {
	private RegOp1	opcode;
	private int		dest;
	private	int		src1;
	private	int		src2;

	public RegOpCode(RegOp1 c, int d, int s1, int s2)
	{
		this.opcode = c;
		this.dest = d;
		this.src1 = s1;
		this.src2 = s2;
	}
	
	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		c.generate(ISA.REGOP, opcode.ordinal(), dest, src1, src2);
	}


}
