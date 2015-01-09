package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.Flag;
import com.F64.Optimization;
import com.F64.Processor;

public class Carry extends com.F64.Codepoint {
	private	boolean			set;

	public Carry(boolean value)
	{
		set = value;
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Carry) {
					p.remove();
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
	public void generate(Compiler c)
	{
		if (set) {
			c.generate(Ext1.SFLAG, Flag.CARRY.ordinal());
		}
		else {
			c.generate(Ext1.CFLAG, Flag.CARRY.ordinal());
		}
	}


}
