package com.F64;

import java.io.IOException;

//import java.nio.charset.StandardCharsets;


public class Machine {
	private View		view;
	private Processor	processor;
	private System		system;
	private Compiler	compiler;
	private Interpreter	interpreter;
	private Dictionary	dictionary;

	Machine(int memory_size, int stack_size, int return_stack_size)
	{
		system = new System(memory_size, stack_size, return_stack_size);
		processor = new Processor(system);
		dictionary = new Dictionary();
		compiler = new Compiler();
		interpreter = new Interpreter();
		interpreter.setEnvironment(system, dictionary);
		processor.powerOn();
		view = new View(processor, interpreter, compiler, system, dictionary);
		View.systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
	}
	
	public void interpret(String txt)
	{
		try {
			interpreter.interpret(new java.io.ByteArrayInputStream(txt.getBytes()));
		}
		catch (IOException ex) {
			
		}
	}

	public static void main(String[] args)
	{
		Machine main = new Machine(100000, 16, 16);
		main.interpret("1 2 + .");
	}

}
