package com.F64;

import java.nio.charset.StandardCharsets;


public class Machine {
	private View	view;

	Machine(int memory_size, int stack_size, int return_stack_size)
	{
		view = new View();
		view.setup(memory_size, stack_size, return_stack_size);
		View.systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(view);
		
	}
	
	public static void main(String[] args)
	{
		Machine main = new Machine(1000, 16, 16);
	}

}
