package com.F64.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.F64.Flag;

@SuppressWarnings("serial")
public class Flags extends JPanel {
	private JCheckBox[]			flags;

	public Flags(Processor p)
	{
		super( new GridBagLayout() );

		int limit = Flag.values().length;
		JLabel label;
		int i;
		int x = 0;
		int y = 0;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( -4, 10, 0, 4);
		label = new JLabel("Flags");
		this.add(
			label,
			new GridBagConstraints(
				x, y,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		y += 1;
		this.flags = new JCheckBox[limit];
		for (i=0; i<limit; ++i) {
			this.flags[i] = new JCheckBox(Flag.values()[i].name());
			this.flags[i].setToolTipText(Flag.values()[i].getTooltip());
			this.flags[i].addItemListener(p);
			this.add(
					this.flags[i],
				new GridBagConstraints(
					x, y+i,
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
	
	public void update(com.F64.Processor processor)
	{
		for (int i=0; i<Flag.values().length; ++i) {
			this.flags[i].setSelected(processor.getFlag(i));
		}
	}

	public boolean isFlag(int i, Object obj)
	{
		return this.flags[i] == obj;
	}
	
}
