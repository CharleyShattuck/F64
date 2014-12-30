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

	Machine(int dictionary_size, int heap_size, int stack_size, int return_stack_size, int no_of_threads)
	{
		system = new System(dictionary_size, heap_size, stack_size, return_stack_size, no_of_threads);
		processor = new Processor(system);
		dictionary = new Dictionary(system);
		dictionary.createStandardWords();
		compiler = new Compiler(system);
		interpreter = new Interpreter(system, processor, compiler, dictionary);
		processor.powerOn();
		view = new View(processor, interpreter, compiler, system, dictionary);
		View.systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
	}
	
	public void interpret(String txt)
	{
		try {
			interpreter.interpret(new java.io.ByteArrayInputStream(txt.getBytes()));
			view.update();
		}
		catch (IOException ex) {
			
		}
	}
	
	public static void main(String[] args)
	{
		Machine main = new Machine(10000, 100000, 1024, 1024, 10);
		main.interpret("1 2 + .");
	}

}
