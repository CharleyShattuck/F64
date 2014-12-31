package com.F64;

public class ProcessorArray {
	private System			system;
	private int				rows;
	private int				columns;
	private Processor[][]	array;

	
	public ProcessorArray(int columns, int rows, System system)
	{
		this.system = system;
		this.columns = columns;
		this.rows = rows;
		this.array = new Processor[rows][columns];
		for (int y=0; y<rows; ++y) {
			for (int x=0; x<columns; ++x) {
				this.array[y][x] = new Processor(system);
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
	
	
}
