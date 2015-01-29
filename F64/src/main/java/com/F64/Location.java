package com.F64;

public class Location {
	private Condition	cond;
	private long		adr;
	private long		padr;
	private int			slot;

	public Location(long padr, long adr, int slot)
	{
		this.padr = padr;
		this.adr = adr;
		this.slot = slot;
	}

	public long getPAdr() {return padr;}
	public long getAdr() {return adr;}
	public int getSlot() {return slot;}

}
