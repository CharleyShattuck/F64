package com.F64.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.F64.ISA;

@SuppressWarnings("serial")
public class Slots extends JPanel {
	private JTextField			slot_no;
	private JTextField[]		slots;

	public Slots()
	{
		super( new GridBagLayout() );
		int limit = com.F64.Processor.getMaxSlot();
		JLabel label;
		int i;
		int x = 0;
		int y = 0;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 0);
		label = new JLabel("slot#");
		this.add(
			label,
			new GridBagConstraints(
				x, y,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		this.slot_no = new JTextField();
		this.slot_no.setHorizontalAlignment(JTextField.RIGHT);
		this.add(
			this.slot_no,
			new GridBagConstraints(
				x, y+1,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				field_insets,
				2, 0
			)
		);
		x += 1;
		this.slots = new JTextField[limit];
		for (i=0; i<limit; ++i) {
			label = new JLabel("slot "+i);
			this.add(
				label,
				new GridBagConstraints(
					x+i, y,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					label_insets,
					2, 0
				)
			);
			this.slots[i] = new JTextField();
			this.slots[i].setHorizontalAlignment(JTextField.RIGHT);
			this.add(
				this.slots[i],
				new GridBagConstraints(
					x+i, y+1,
					1, 1,
					0.0, 0.0,
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
		int slot = processor.getSlot();
		this.slot_no.setText(Integer.toString(slot));

		ISA instr = ISA.values()[processor.getSlot(slot)];
		int i = 0;
		this.slots[i].setToolTipText(instr.getTooltip());
		this.slots[i++].setText(instr.name());
		int no_of_slots = instr.size();
		if (no_of_slots == 0) {
			// extension instruction
		}
		while (i<no_of_slots) {
			this.slots[i++].setText(Integer.toString(processor.getSlot(slot+i)));
		}
		while (i<this.slots.length) {
			this.slots[i++].setText("");
		}

	}

}
