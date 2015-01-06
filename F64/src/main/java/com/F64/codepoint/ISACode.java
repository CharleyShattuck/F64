package com.F64.codepoint;

import com.F64.Compiler;
<<<<<<< HEAD
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;

public class ISACode extends com.F64.Codepoint {
	private long			arg;			// arguments for this opcode
	private int				no_args;		// # of arguments
	private ISA				opcode;			// first opcode
	private Ext1			extension1;		// opcode extension 1

	public ISACode(ISA op)
	{
		this.opcode = op;
		this.no_args = 0;
	}

	public ISACode(ISA op, int arg0)
	{
		this.opcode = op;
		this.no_args = 1;
		this.arg = Processor.writeSlot(0, 0, arg0);
	}

	public ISACode(ISA op, int arg0, int arg1)
	{
		this.opcode = op;
		this.no_args = 2;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
	}

	public ISACode(ISA op, int arg0, int arg1, int arg2)
	{
		this.opcode = op;
		this.no_args = 3;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
	}

	public ISACode(ISA op, int arg0, int arg1, int arg2, int arg3)
	{
		this.opcode = op;
		this.no_args = 4;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
		this.arg = Processor.writeSlot(this.arg, 3, arg3);
	}

	public ISACode(ISA op, int arg0, int arg1, int arg2, int arg3, int arg4)
	{
		this.opcode = op;
		this.no_args = 5;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
		this.arg = Processor.writeSlot(this.arg, 3, arg3);
		this.arg = Processor.writeSlot(this.arg, 4, arg4);
	}

	public ISACode(Ext1 ex)
	{
		this.opcode = ISA.EXT1;
		this.extension1 = ex;
		this.no_args = 0;
	}

	@Override
	public boolean optimize(Optimization opt)
=======

public class ISACode extends com.F64.Codepoint {

	@Override
	public boolean optimize()
>>>>>>> refs/remotes/origin/master
	{
		if (this.getPrevious() == null) {return false;}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		
	}

}
