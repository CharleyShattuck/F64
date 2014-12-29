package com.F64;

import java.nio.charset.StandardCharsets;


public class Machine {

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		View.systemLookAndFeel();
		javax.swing.SwingUtilities.invokeLater(new View());
//		javax.swing.SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				View view = new View();
//				view.run();
//			}
//		});

	}

}
