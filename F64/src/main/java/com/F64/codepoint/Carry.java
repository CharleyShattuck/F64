package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Flag;
import com.F64.Optimization;

public class Carry extends com.F64.Codepoint {
	private	boolean			set;

	public Carry(boolean value)
	{
		set = value;
	}

	public boolean getValue() {return set;}
	
	@Override
	public boolean optimize(Compiler c, Optimization opt)
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
	public void generate(Builder b)
	{
		if (set) {
			b.add(Ext2.SFLAG, Flag.CARRY.ordinal());
		}
		else {
			b.add(Ext2.CFLAG, Flag.CARRY.ordinal());
		}
	}


}
