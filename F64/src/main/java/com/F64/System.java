package com.F64;

public class System {
	private	boolean		compiling;
	private	long[]		memory;
	private	long[]		stack;
	private	long[]		return_stack;

	
	
	public System(int memory_size, int stack_size, int return_stack_size)
	{
		memory = new long[memory_size];
		stack = new long[stack_size];
		return_stack = new long[return_stack_size];
	}

	public long getMemorySize() {return memory.length;}
	public long getStackSize() {return stack.length;}
	public long getReturnStackSize() {return return_stack.length;}
	
	public void setMemory(long adr, long value)
	{
		this.memory[(int)adr] = value;
	}

	public long getMemory(long adr)
	{
		return this.memory[(int)adr];
	}

	public void setStackMemory(long adr, long value)
	{
		this.stack[(int)adr] = value;
	}

	public long getStackMemory(long adr)
	{
		return this.stack[(int)adr];
	}

	public void setReturnStackMemory(long adr, long value)
	{
		this.return_stack[(int)adr] = value;
	}

	public long getReturnStackMemory(long adr)
	{
		return this.return_stack[(int)adr];
	}

	public boolean getCompiling() {return compiling;}
	public long[] getMemory() {return memory;}
	
	
	public void createMemory(int size)
	{
		memory = new long[size];
	}

	public long read(long adr)
	{
		return memory[(int)adr];
	}

	public void write(long adr, long value)
	{
		memory[(int)adr] = value;
	}

}
