package com.F64;

import java.io.IOException;

import javax.swing.UIManager;

//import java.nio.charset.StandardCharsets;


public class Machine {
	private com.F64.view.ProcessorArray		view;
	private ProcessorArray	processor_array;
	private Processor		processor;
	private System			system;
	private Compiler		compiler;
	private Interpreter		interpreter;
	private Dictionary		dictionary;

	public static void systemLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (java.lang.Exception ex) {
			java.lang.System.err.println("Couldn't use system look and feel.");
		}		
	}

	Machine(int columns, int rows, int dictionary_size, int heap_size, int stack_size, int return_stack_size, int no_of_threads)
	{
		assert(ISA.values().length <= Processor.SLOT_SIZE);
		assert(Ext1.values().length <= Processor.SLOT_SIZE);
		assert(Ext2.values().length <= Processor.SLOT_SIZE);
		assert(RegOp1.values().length <= Processor.SLOT_SIZE);
		assert(SimdOp1.values().length <= Processor.SLOT_SIZE);
		system = new System(dictionary_size, heap_size, stack_size, return_stack_size, no_of_threads);
		processor_array = new ProcessorArray(columns, rows, system);
		processor = processor_array.getProcessor(0, 0);
		dictionary = new Dictionary(system);
		dictionary.createStandardWords();
		compiler = new Compiler(system, processor);
		interpreter = new Interpreter(system, processor, compiler, dictionary);
//		processor.powerOn();
		view = new com.F64.view.ProcessorArray(processor_array, interpreter, compiler, system, dictionary);
		systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
	}
	
	public void interpret()
	{
		java.util.Scanner scanner = new java.util.Scanner(java.lang.System.in);
		java.lang.System.out.println("F64 Forth (c) 2014 by S. Mauerhofer (Switzerland)");
		for (;;) {
			try {
				java.lang.System.out.print(">");
				String line = scanner.nextLine();
				interpreter.interpret(new java.io.ByteArrayInputStream(line.getBytes()));
				view.update();
				java.lang.System.out.println(" ok");
			}
			catch (IOException ex) {
				
			}
		}
	}
	
	public static void main(String[] args)
	{
		Machine main = new Machine(8, 4, 10000, 100000, 1024, 1024, 10);
		main.interpret();
	}

}
