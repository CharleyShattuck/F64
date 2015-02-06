package com.F64.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
//import javax.swing.JLabel;
import javax.swing.JPanel;

import com.F64.Flag;

@SuppressWarnings("serial")
public class Flags extends JPanel {
	private JCheckBox[]			flags;
	private com.F64.Task		task;

	public Flags(Task p)
	{
		super( new GridBagLayout() );

		int limit = Flag.values().length;
//		JLabel label;
		int i;
		int x = 0;
		int y = 0;
//		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( -4, 10, 0, 4);
//		label = new JLabel("Flags");
//		this.add(
//			label,
//			new GridBagConstraints(
//				x, y,
//				1, 1,
//				0.0, 1.0,
//				GridBagConstraints.WEST,
//				GridBagConstraints.BOTH,
//				label_insets,
//				2, 0
//			)
//		);
//		y += 1;
		this.flags = new JCheckBox[limit];
		for (i=0; i<limit; ++i) {
			this.flags[i] = new JCheckBox(Flag.values()[i].name());
			this.flags[i].setToolTipText(Flag.values()[i].getTooltip());
			this.flags[i].addItemListener(p);
			this.add(
				this.flags[i],
				new GridBagConstraints(
					i < (com.F64.Processor.NO_OF_REG/2) ? x : x+2, i < (com.F64.Processor.NO_OF_REG/2) ? y+i : y+i - (com.F64.Processor.NO_OF_REG/2),
					1, 1,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
		}

	}
	
	public void setTask(com.F64.Task value) {task = value;}

	public void update()
	{
		for (int i=0; i<Flag.values().length; ++i) {
			this.flags[i].setSelected(task.getFlag(i));
		}
	}

	public boolean isFlag(int i, Object obj)
	{
		return this.flags[i] == obj;
	}
	
}
