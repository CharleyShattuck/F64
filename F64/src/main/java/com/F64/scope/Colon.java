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
		b.add(Ext1.LCOL);
		b.addAdditionalCell(0);
		fixup_address = b.getCurrentP();
		b.flush();
		super.generate(b);
		b.flush();
		b.getSystem().setMemory(fixup_address, b.getCurrentPosition());
	}


}
