package com.F64.view;

import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class SIMDRegister extends JTabbedPane {
	private SliceRegister[]		slice_panels;
	private	int					blend;

	
	public SIMDRegister(com.F64.Processor processor)
	{
		int i;
		slice_panels = new SliceRegister[com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS];

		for (i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			slice_panels[i] = new SliceRegister(processor, i);
			this.addTab("Slice "+i, null, slice_panels[i], "General purpose register");
		}

	}

	public void setProcessor(com.F64.Processor value)
	{
		for (int i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			this.slice_panels[i].setProcessor(value);
		}
	}

	public void update()
	{
		for (int i=0; i<com.F64.Processor.NO_OF_SIMD_REGISTER_CELLS; ++i) {
			this.slice_panels[i].update();
		}
	}

}
