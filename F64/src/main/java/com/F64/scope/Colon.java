package com.F64.scope;

import com.F64.Builder;
import com.F64.Compiler;
//import com.F64.Optimization;
import com.F64.Ext1;

public class Colon extends com.F64.scope.Main implements java.lang.Cloneable {
	private long fixup_address;

	public Colon(Compiler c)
	{
		super(c.getScope());
		c.compile(this);
		c.setScope(this);	
		c.compile(new com.F64.codepoint.Enter());
	}

	public Colon clone() throws CloneNotSupportedException
	{
		Colon res = (Colon)super.clone();
		return res;
	}



//	@Override
//	public boolean optimize(Compiler c, Optimization opt)
//	{
//		return false;
//	}

	@Override
	public void generate(Builder b)
	{
		// try to fit the jump address in the remaining slots
		int slot = b.getCurrentSlot();
		if (slot < com.F64.Processor.NO_OF_SLOTS-2) {
			long source = b.getCurrentP();
			long mask = com.F64.Processor.REMAINING_MASKS[slot+2];
			Builder probe = b.fork(true);
			super.generate(probe);
			probe.flush();
			long target = probe.getCurrentPosition();
			long pattern = source ^ target;
			if (pattern <= mask) {
				b.add(Ext1.RCOL);
				b.fillRemaining(target);
				super.generate(b);
				b.flush();
				return;
			}
		}
		b.add(Ext1.LCOL);
		b.addAdditionalCell(0);
		fixup_address = b.getCurrentP();
		b.flush();
		super.generate(b);
		b.flush();
		b.getSystem().setMemory(fixup_address, b.getCurrentPosition());
	}


}
