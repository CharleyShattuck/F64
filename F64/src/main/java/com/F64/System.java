package com.F64;

public class System {
	private long		dictionary_size;
	private long		heap_size;
	private long		stack_size;
	private long		return_stack_size;
	private int			no_of_threads;
	private	long[]		memory;
	private	long[]		stack;
	private	long[]		return_stack;
	private long		code_position;
	private long		data_position;

	public System(int dictionary_size, int heap_size, int stack_size, int return_stack_size, int no_of_threads)
	{
		this.dictionary_size = dictionary_size;
		this.heap_size = heap_size;
		this.stack_size = stack_size;
		this.return_stack_size = return_stack_size;
		this.no_of_threads = no_of_threads;
		this.code_position = Processor.BIT_PER_CELL;
		this.data_position = dictionary_size;
		memory = new long[dictionary_size+heap_size];
		stack = new long[stack_size*no_of_threads];
		return_stack = new long[return_stack_size*no_of_threads];
	}

	public long getMemorySize() {return memory.length;}
	public long getStackSize() {return stack.length;}
	public long getReturnStackSize() {return return_stack.length;}
	public long getCodePosition() {return code_position;}
	public long getDataPosition() {return data_position;}

	public long reserveData(long cells)
	{
		long new_pos = data_position - cells;
		assert(new_pos > code_position);
		data_position = new_pos;
		return data_position;
	}

	public boolean isValidCodeAddress(long adr)
	{
		if (adr < 0) {
			// negative address is I/O space and a valid code address
			return true;
		}
		return adr < code_position;
	}
	
	public void compileCode(long value)
	{
		this.memory[(int)this.code_position] = value;
		++this.code_position;
	}

	public long compileData(long size)
	{
		while (size > 0) {
			--this.data_position;
			this.memory[(int)this.data_position] = 0;
		}
		return this.data_position;
	}

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

	public long getStackBottom(int thread, boolean return_stack)
	{
		long adr = thread;
		adr *= return_stack ? return_stack_size : stack_size;
		return adr;
	}
	
	public long getStackTop(int thread, boolean return_stack)
	{
		long adr = thread;
		adr *= return_stack ? return_stack_size : stack_size;
		return adr + (return_stack ? return_stack_size : stack_size) - 1;		
	}

	
}
