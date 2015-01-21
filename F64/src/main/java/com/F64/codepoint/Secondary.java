package com.F64.codepoint;

import com.F64.Builder;

public class Secondary extends com.F64.Codepoint {
	private long 		adr;	// address of secondary word
	private boolean		useJump;

	public Secondary(long adr)
	{
		this.adr = adr;
	}
	
	public long getAdr() {return adr;}
	public void setUseJump(boolean value) {useJump = value;}
	
	@Override
	public void generate(Builder b)
	{
		b.addCall(adr, useJump);
	}

}
