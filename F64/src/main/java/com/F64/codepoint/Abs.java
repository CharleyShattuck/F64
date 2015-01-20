package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp1;
import com.F64.RegOp2;
import com.F64.Register;

public class Abs extends com.F64.Codepoint {
	private int src;
	private int dest;

	public Abs()
	{
		src = dest = -1;
	}

	public Abs(int reg)
	{
		src = dest = reg;
	}

	
	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Literal) {
					Literal lit = (Literal) p;
					long data = lit.getValue();
					lit.setValue(data < 0 ? data : data);
					this.remove();
					return true;
				}
				break;


			case PEEPHOLE:
				if (p instanceof Abs) {
					// 2 abs is one too much
					this.remove();
					return true;
				}
				break;

			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Builder b)
	{
		if (src == dest) {
			b.add(RegOp1.ABS, dest < 0 ? Register.T.ordinal() : dest);
		}
		else {
			b.add(RegOp2.ABS, dest < 0 ? Register.T.ordinal() : dest, src < 0 ? Register.T.ordinal() : src);
		}
	}

}
