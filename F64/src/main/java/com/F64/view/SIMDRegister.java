package com.F64.view;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class SIMDRegister extends JTabbedPane {
	private SliceRegister[]		slice_panels;
	private	int					blend;

	
	public SIMDRegister(com.F64.Task t)
	{
		int i;
		slice_panels = new SliceRegister[com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS];

		for (i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			slice_panels[i] = new SliceRegister(t, i);
			this.addTab("Slice "+i, null, slice_panels[i], "General purpose register");
		}

	}

	public void setTask(com.F64.Task value)
	{
		for (int i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			this.slice_panels[i].setTask(value);
		}
	}

	public void update()
	{
		for (int i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			this.slice_panels[i].update();
		}
	}

}
