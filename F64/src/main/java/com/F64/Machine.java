package com.F64;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.UIManager;

//import java.nio.charset.StandardCharsets;


public class Machine {
	private com.F64.view.ProcessorArray		view;
	private ProcessorArray	processor_array;
	private Processor		processor;
	private BootROM			rom;
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
		rom = new BootROM();
		system = new System(dictionary_size, heap_size, stack_size, return_stack_size, no_of_threads);
		//
		long interrupt_code = Processor.writeSlot(0, 0, ISA.EXT1.ordinal());
		interrupt_code = Processor.writeSlot(interrupt_code, 1, Ext1.EXITI.ordinal());
		for (int i=0; i<Processor.BIT_PER_CELL; ++i) {
			interrupt_code = Processor.writeSlot(interrupt_code, 2, i);
			system.setMemory(i, interrupt_code);
		}
		//
		processor_array = new ProcessorArray(columns, rows, system, rom, 0x40, 0x80);
		processor = processor_array.getProcessor(0, 0);
		dictionary = new Dictionary(system);
		dictionary.createStandardWords();
		compiler = new Compiler(system, processor);
		interpreter = new Interpreter(system, processor, compiler, dictionary);
		Builder builder = compiler.getBuilder();
		builder.start(true);;
		// * secondary
		com.F64.codepoint.Mul.setAdr(system.getCodePosition());
		builder.add(ISA.LIT, 0);
		builder.add(Ext2.SSTORE, SystemRegister.MD.ordinal());
		builder.add(ISA.LIT, Processor.BIT_PER_CELL-1);
		builder.add(ISA.PUSH);
		builder.flush();
		builder.add(Ext1.MULS);
		builder.add(ISA.UNEXT);
		builder.add(Ext1.MULF);
		builder.add(ISA.EXIT);
		builder.flush();
		// / secondary
		com.F64.codepoint.Div.setAdr(system.getCodePosition());
		builder.add(ISA.LIT, 0);
		builder.add(Ext2.SSTORE, SystemRegister.MD.ordinal());
		builder.add(ISA.LIT, Processor.BIT_PER_CELL-1);
		builder.add(ISA.PUSH);
		builder.flush();
		builder.add(Ext1.DIVS);
		builder.add(ISA.UNEXT);
		builder.add(Ext1.DIVMODF);
		builder.add(ISA.DROP);
		builder.add(ISA.EXIT);
		builder.flush();
		// /mod secondary
		com.F64.codepoint.DivMod.setAdr(system.getCodePosition());
		builder.add(ISA.LIT, 0);
		builder.add(Ext2.SSTORE, SystemRegister.MD.ordinal());
		builder.add(ISA.LIT, Processor.BIT_PER_CELL-1);
		builder.add(ISA.PUSH);
		builder.flush();
		builder.add(Ext1.DIVS);
		builder.add(ISA.UNEXT);
		builder.add(Ext1.DIVMODF);
		builder.add(ISA.EXIT);
		builder.flush();
		// mod secondary
		com.F64.codepoint.Mod.setAdr(system.getCodePosition());
		builder.add(ISA.LIT, 0);
		builder.add(Ext2.SSTORE, SystemRegister.MD.ordinal());
		builder.add(ISA.LIT, Processor.BIT_PER_CELL-1);
		builder.add(ISA.PUSH);
		builder.flush();
		builder.add(Ext1.DIVS);
		builder.add(ISA.UNEXT);
		builder.add(Ext1.DIVMODF);
		builder.add(ISA.NIP);
		builder.add(ISA.EXIT);
		builder.flush();
		//
		builder.stop();
//		processor.powerOn();
		view = new com.F64.view.ProcessorArray(processor_array, interpreter, compiler, system, dictionary);
		view.update();
		systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
	}

	public void load(java.io.InputStream in)
	{
		java.util.Scanner scanner = new java.util.Scanner(in);
		for (;;) {
			try {
				String line = scanner.nextLine();
				interpreter.interpret(new java.io.ByteArrayInputStream(line.getBytes()));
				view.update();
			}
			catch (java.lang.Exception ex) {
				ex.printStackTrace();
				scanner.close();
				break;
			}
		}
		processor.powerOn();
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
				scanner.close();
				break;
			}
		}
	}
	
	public static void main(String[] args)
	{
//		String current;
//		try {
//			current = new java.io.File( "." ).getCanonicalPath();
//	        java.lang.System.out.println("Current dir:"+current);
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		java.lang.System.setProperty( "user.dir", args[0] );
//		String file_base = args[0];
		
		assert((Processor.SLOT_ENCODE_BITS + Processor.SIMD_SLICE_BITS + 9) <= (3*Processor.SLOT_BITS));
		assert(Branch.values().length <= (1 << (Processor.SLOT_BITS-2)));
		assert(ISA.values().length <= Processor.SLOT_SIZE);
		assert(Ext1.values().length <= Processor.SLOT_SIZE);
		assert(Ext2.values().length <= Processor.SLOT_SIZE);
		assert(Ext3.values().length <= Processor.SLOT_SIZE);
		assert(Ext4.values().length <= Processor.SLOT_SIZE);
		assert(Ext5.values().length <= Processor.SLOT_SIZE);
		assert(Ext6.values().length <= Processor.SLOT_SIZE);
		assert(RegOp3.values().length <= Processor.SLOT_SIZE);
		assert(RegOp3.values().length <= Processor.SLOT_SIZE);
		assert(com.F64.SIMD.Operation.values().length <= Processor.SLOT_SIZE);
		assert(Flag.values().length <= (Processor.BIT_PER_CELL - 3*Processor.SLOT_BITS));
		assert(SystemRegister.values().length <= Processor.SLOT_SIZE);
		assert(Register.values().length <= Processor.SLOT_SIZE);
		Machine main = new Machine(8, 4, 10000, 100000, 32, 16, 10);
		for (int i=0; i<args.length; ++i) {
			java.io.FileInputStream reader;
			try {
				reader = new java.io.FileInputStream(args[i]);
				main.load(reader);
			}
			catch (FileNotFoundException e) {
//				e.printStackTrace();
				break;
			}
		}
		main.interpret();
	}

}
