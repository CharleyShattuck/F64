package com.F64.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.F64.Ext1;
import com.F64.Ext2;
import com.F64.ISA;

@SuppressWarnings("serial")
public class Slots extends JPanel {
	private JTextField			task_no;
	private JTextField			slot_no;
	private JTextField[]		slots;
	private JTextField			slice_no;
	private com.F64.Processor	processor;

	public Slots(com.F64.Processor p)
	{
		super( new GridBagLayout() );
		processor = p;
		int limit = com.F64.Processor.max_slot;
		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);
		JLabel label;
		int i;
		int x = 0;
		int y = 0;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 0);
		//
		label = new JLabel("task#");
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
		this.task_no = new JTextField("", 20);
		this.task_no.setFont(font);
//		this.slot_no.setHorizontalAlignment(JTextField.RIGHT);
		this.add(
			this.task_no,
			new GridBagConstraints(
				x+1, y,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				field_insets,
				2, 0
			)
		);
		y += 1;
		//
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
		this.slot_no = new JTextField("", 20);
		this.slot_no.setFont(font);
//		this.slot_no.setHorizontalAlignment(JTextField.RIGHT);
		this.add(
			this.slot_no,
			new GridBagConstraints(
				x+1, y,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				field_insets,
				2, 0
			)
		);
		y += 1;
		this.slots = new JTextField[limit];
		for (i=0; i<limit; ++i) {
			label = new JLabel("slot "+i);
			this.add(
				label,
				new GridBagConstraints(
					x, y+i,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					label_insets,
					2, 0
				)
			);
			this.slots[i] = new JTextField();
			this.slots[i].setFont(font);
//			this.slots[i].setHorizontalAlignment(JTextField.RIGHT);
			this.add(
				this.slots[i],
				new GridBagConstraints(
					x+1, y+i,
					1, 1,
					1.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
		}
		y += limit;
		label = new JLabel("slice#");
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
		this.slice_no = new JTextField("", 20);
		this.slice_no.setFont(font);
//		this.slot_no.setHorizontalAlignment(JTextField.RIGHT);
		this.add(
			this.slice_no,
			new GridBagConstraints(
				x+1, y,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				field_insets,
				2, 0
			)
		);
	}

	public void setProcessor(com.F64.Processor value) {processor = value;}

	public void update()
	{
		int slot = processor.getSlot();
		int task = processor.getCurrentTask();
		this.task_no.setText(Integer.toString(task));
		this.slot_no.setText(Integer.toString(slot));
		this.slice_no.setText(Integer.toString(processor.getSlice()));

		ISA instr = ISA.values()[processor.getSlot(slot)];
		int i = 0;
		this.slots[i].setToolTipText(instr.getTooltip());
		this.slots[i++].setText(instr.name());
		int no_of_slots = instr.size();
		if (no_of_slots == 0) {
			// extension instruction
			switch (instr) {
			case EXT1:
				Ext1 ext1 = Ext1.values()[processor.getSlot(slot+i)];
				this.slots[i].setToolTipText(ext1.getTooltip());
				this.slots[i++].setText(ext1.name());
				no_of_slots = ext1.size();
				break;
			case EXT2:
				Ext2 ext2 = Ext2.values()[processor.getSlot(slot+i)];
				this.slots[i].setToolTipText(ext2.getTooltip());
				this.slots[i++].setText(ext2.name());
				no_of_slots = ext2.size();
				break;
			default:
			}
		}
		while (i<no_of_slots) {
			int value = processor.getSlot(slot+i);
			this.slots[i].setToolTipText("");
			this.slots[i++].setText(Integer.toString(value));
		}
		while (i<this.slots.length) {
			this.slots[i].setToolTipText("");
			this.slots[i++].setText("");
		}

	}

}
