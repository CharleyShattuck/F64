package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.RegOp2;
import com.F64.RegOp3;

public class RegOpCode extends com.F64.Codepoint {
	private int		opcode1;
	private int		opcode2;
	private int		opcode3;
	private int		dest;
	private	int		src1;
	private	int		src2;

	public RegOpCode(RegOp1 c, int d)
	{
		this.opcode1 = c.ordinal();
		opcode2 = opcode3 = -1;
		this.dest = d;
	}

	public RegOpCode(RegOp2 c, int d, int s)
	{
		this.opcode2 = c.ordinal();
		opcode1 = opcode3 = -1;
		this.dest = d;
		this.src1 = s;
	}

	public RegOpCode(RegOp3 c, int d, int s1, int s2)
	{
		this.opcode3 = c.ordinal();
		opcode1 = opcode2 = -1;
		this.dest = d;
		this.src1 = s1;
		this.src2 = s2;
	}
	
	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (opcode1 >= 0) {c.generate(ISA.REGOP1, opcode1, dest);}
		if (opcode2 >= 0) {c.generate(ISA.REGOP2, opcode2, dest, src1);}
		if (opcode3 >= 0) {c.generate(ISA.REGOP3, opcode3, dest, src1, src2);}
	}


}
