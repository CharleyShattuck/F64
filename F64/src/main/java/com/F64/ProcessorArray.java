package com.F64;

public class ProcessorArray {
	private System			system;
	private BootROM			rom;
	private int				rows;
	private int				columns;
	private Processor[][]	array;

	public System getSystem() {return system;}
	
	public ProcessorArray(int columns, int rows, System system, BootROM rom, int stack_size, int return_stack_size)
	{
		this.system = system;
		this.rom = rom;
		this.columns = columns;
		this.rows = rows;
		this.array = new Processor[rows][columns];
		for (int y=0; y<rows; ++y) {
			for (int x=0; x<columns; ++x) {
				this.array[y][x] = new Processor(system, x, y, 0, stack_size, return_stack_size);
			}
		}
	}

	public int getRows() {return rows;}
	public int getColumns() {return columns;}
	
	public Processor getProcessor(int x, int y)
	{
		assert(x >= 0);
		assert(y >= 0);
		assert(x < this.columns);
		assert(y < this.rows);
		return this.array[y][x];
	}
	
	public Processor getNeighbor(int x, int y, Port p)
	{
		switch (p) {
		case DOWN:
			if ((x & 1) != 0) {--y;}
			else {++y;}
			break;
		case LEFT:
			if ((x & 1) == 0) {--x;}
			else {++x;}
			break;
		case RIGHT:
			if ((x & 1) != 0) {--x;}
			else {++x;}
			break;
		case UP:
			if ((x & 1) == 0) {--y;}
			else {++y;}
			break;
		default:
			return null;
		}
		if (x < 0) {x += this.columns;}
		else if (x >= this.columns) {x -= this.columns;}
		if (y < 0) {y += this.rows;}
		else if (y >= this.rows) {y -= this.rows;}
		return this.array[y][x];
	}
	
	public synchronized void start()
	{
		for (int y=0; y<this.rows; ++y) {
			for (int x=0; x<this.columns; ++x) {
				this.array[y][x].start();
			}
		}
	}
	
	public synchronized void stop()
	{
		for (int y=0; y<this.rows; ++y) {
			for (int x=0; x<this.columns; ++x) {
				this.array[y][x].stop();
			}
		}
	}

	public synchronized void reset()
	{
		rom.reset();
		for (int y=0; y<this.rows; ++y) {
			for (int x=0; x<this.columns; ++x) {
				this.array[y][x].reset();
			}
		}
	}

	public synchronized void powerOn()
	{
		rom.reset();
		for (int y=0; y<this.rows; ++y) {
			for (int x=0; x<this.columns; ++x) {
				this.array[y][x].powerOn();
			}
		}
	}

	public synchronized void step()
	{
		for (int y=0; y<this.rows; ++y) {
			for (int x=0; x<this.columns; ++x) {
				this.array[y][x].powerOn();
			}
		}
	}

	public synchronized void boot()
	{
		Processor p = this.array[0][0];
		if (p.canReadFromPort(Port.LEFT.ordinal())) {
			p.externalWriteToPort(Port.LEFT.ordinal(), rom.nextValue());
		}
	}
	
}
