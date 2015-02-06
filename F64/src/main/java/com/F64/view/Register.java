package com.F64.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class Register extends JPanel implements ActionListener {
	private JLabel[]			labels;
	private JTextField[]		fields;
	private boolean				updating;
	private com.F64.Task		task;

	
	public Register(com.F64.Task task)
	{
		super( new GridBagLayout() );
		this.task = task;
		int limit = com.F64.Processor.NO_OF_REG;
		JLabel label;
		int i;
		int x = 0;
		int y = 0;
		this.labels = new JLabel[limit];
		this.fields = new JTextField[limit];
		Insets label_insets = new Insets( 0, 2, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 4);
		Dimension registerFieldMin = new Dimension(100, 10);
//		label = new JLabel("Register");
//		this.add(
//			label,
//			new GridBagConstraints(
//				x+1, y,
//				1, 1,
//				0.0, 1.0,
//				GridBagConstraints.WEST,
//				GridBagConstraints.BOTH,
//				label_insets,
//				2, 0
//			)
//		);
//		label = new JLabel("Register");
//		this.add(
//			label,
//			new GridBagConstraints(
//				x+3, y,
//				1, 1,
//				0.0, 1.0,
//				GridBagConstraints.WEST,
//				GridBagConstraints.BOTH,
//				label_insets,
//				2, 0
//			)
//		);
//		y += 1;

		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);
		for (i=0; i<limit; ++i) {
			label = new JLabel();
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			JTextField field = new JTextField("", 20);
			field.setFont(font);
			field.setMinimumSize(registerFieldMin);
			field.addActionListener(this);
			if (i < com.F64.Register.values().length) {
				label.setText(" "+com.F64.Register.values()[i].name());
				label.setToolTipText(com.F64.Register.values()[i].getTooltip());
			}
			else {
				label.setText(" R"+i);
			}
			this.labels[i] = label;
			this.fields[i] = field;
			this.add(
				label,
				new GridBagConstraints(
					i < (com.F64.Processor.NO_OF_REG/2) ? x : x+2, i < (com.F64.Processor.NO_OF_REG/2) ? y+i : y+i - (com.F64.Processor.NO_OF_REG/2),
					1, 1,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					label_insets,
					2, 0
				)
			);
			this.add(
				field,
				new GridBagConstraints(
					i < (com.F64.Processor.NO_OF_REG/2) ? x+1 : x+3, i < (com.F64.Processor.NO_OF_REG/2) ? y+i : y+i - (com.F64.Processor.NO_OF_REG/2),
					1, 1,
					0.0, 1.0,
					GridBagConstraints.EAST,
					GridBagConstraints.BOTH,
					field_insets,
					4, 0
				)
			);
		}	
	}

	public void setTask(com.F64.Task value) {task = value;}

	public void update()
	{
		for (int i=0; i<com.F64.Processor.NO_OF_REG; ++i) {
			long value = task.getRegister(i);
			this.fields[i].setText(Processor.convertLongToString(value));
		}

	}


	@Override
	public void actionPerformed(ActionEvent ev)
	{
		if (this.updating) {return;}
		Object source = ev.getSource();
		for (int i=0; i<fields.length; ++i) {
			if (fields[i] == source) {
				if (i > 0) {// do not overwrite the Z register
					try {
						String txt = ev.getActionCommand();
						long value = Long.parseLong(txt.replaceAll(" ", ""), 16);
						this.task.setRegister(i, value);
					}
					catch (Exception ex) {}
				}
				this.updating = true;
				this.fields[i].setText(Processor.convertLongToString(this.task.getRegister(i)));
				this.updating = false;
				return;
			}
		}
		
	}

	
}
