package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Processor;


public class Literal extends com.F64.Codepoint {
	private long	value;

	public Literal(long value)
	{
		this.value = value;
	}

	public long getValue() {return value;}
	public void setValue(long value) {this.value = value;}

	@Override
	public boolean optimize()
	{
		return false;
	}
	
	private boolean generateOptimized(Compiler c, long data)
	{
		if ((data >= 0) && (data < Processor.SLOT_SIZE)) {
			// constant fits into a slot
			c.generate(ISA.LIT, (int)data);
			return true;
		}
		if ((data & (data-1)) == 0) {
			// 1 bit set constant
			c.generate(ISA.BLIT, Processor.findFirstBit1(data));
			return true;
		}
		data = ~data;
		if ((data >= 0) && (data < Processor.SLOT_SIZE)) {
			// inverted constant fits into a slot
			c.generate(Ext1.NLIT, (int)data);
			return true;
		}
		return false;
		
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (generateOptimized(c, value)) {return;}
		if (value >= 0) {
			// positive number
			if (!c.doesFit(ISA.FETCHPINC.ordinal())) {c.flush();}
			c.generate(ISA.FETCHPINC);
			c.addAdditional(value);
		}
		else {
			// negative number
			if (!c.doesFit(ISA.FETCHPINC.ordinal())) {c.flush();}
			c.generate(ISA.FETCHPINC);
			c.addAdditional(value);
		}
		
	}


	
}
