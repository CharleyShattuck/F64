package com.F64;

import java.io.IOException;

//import java.nio.charset.StandardCharsets;


public class Machine {
	private View		view;
	private Interpreter	vm;
	private System		sys;
	private Compiler	comp;
	private Dictionary	dict;

	Machine(int memory_size, int stack_size, int return_stack_size)
	{
		sys = new System(memory_size, stack_size, return_stack_size);
		vm = new Interpreter(sys);
		dict = new Dictionary();
		comp = new Compiler();
		comp.setEnvironment(sys, dict);
		comp.setEnvironment(sys, dict);
		vm.powerOn();
		view = new View(vm, comp, sys, dict);
		View.systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
	}
	
	public void interpret(String txt)
	{
		try {
			comp.interpret(new java.io.ByteArrayInputStream(txt.getBytes()));
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
