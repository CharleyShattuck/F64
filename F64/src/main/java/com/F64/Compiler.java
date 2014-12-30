package com.F64;

//import java.io.IOException;

public class Compiler {
	private System		system;
	private Processor	processor;

	public Compiler(System system, Processor processor)
	{
		this.system = system;
		this.processor = processor;
	}

	public System getSystem() {return system;}
	public Processor getProcessor() {return processor;}
	
	public void compile(ISA instr)
	{
		
	}
	
}
