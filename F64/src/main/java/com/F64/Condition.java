package com.F64;

public enum Condition {
	EQ0("=0"),			// T == 0. T is consumed
	QEQ0("?=0"),		// T == 0. T is consumed if == 0
//	NE0("!=0"),			// T != 0. T is consumed
//	GE0(">=0"),			// T >= 0. T is consumed
	CARRY("y=1"),		// carry flag set
	ALWAYS("true"),
	NEVER("false");		// cannot be encoded. This condition is only used during compilation.

	private String display;

	private Condition(String display)
	{
		this.display = display;
	}
	
	public String getDisplay() {return display;}

	public int encode(Branch br)
	{
		return (ordinal() << 4) | br.ordinal();
	}

	public int encode(int slot)
	{
		assert(slot >= 0);
		assert(slot < Processor.NO_OF_SLOTS);
		assert(this.ordinal() < 4);
		return (ordinal() << 4) | slot;
	}

}
