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
public class ReturnStack extends JPanel implements ActionListener {
	private JLabel[]			labels;
	private JTextField[]		fields;
	private boolean				updating;
	private com.F64.Task		task;

	public final int			INITIAL_OFFSET = 3;
	public final int			RANGE = 32;
	
	public ReturnStack(com.F64.Task task)
	{
		super( new GridBagLayout() );
		this.task = task;
		int limit = com.F64.Processor.NO_OF_REG;
		JLabel label;
		int x = 0;
		int y = 0;
		this.labels = new JLabel[limit];
		this.fields = new JTextField[limit];
		Insets label_insets = new Insets( 0, 2, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 4);
		Dimension registerFieldMin = new Dimension(100, 10);

		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);
		for (int i=0; i<RANGE; ++i) {
			int offset = INITIAL_OFFSET-i;
			label = new JLabel(( offset > 0 ? "+" : "")+offset);
			label.setFont(font);
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			JTextField field = new JTextField("", 20);
			field.setFont(font);
			field.setMinimumSize(registerFieldMin);
			field.addActionListener(this);
			this.labels[i] = label;
			this.fields[i] = field;
			this.add(
				label,
				new GridBagConstraints(
					x, y+i,
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
					x+1, y+i,
					1, 1,
					0.0, 1.0,
					GridBagConstraints.EAST,
					GridBagConstraints.BOTH,
					field_insets,
					4, 0
				)
			);
		}	
		label = new JLabel(" <-- RP");
		label.setFont(font);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(
			label,
			new GridBagConstraints(
				x+2, y+INITIAL_OFFSET,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
	}

	
	public void setTask(com.F64.Task value) {task = value;}

	public void update()
	{
		for (int i=0; i<RANGE; ++i) {
			long value = task.getReturnStack(task.getReturnStackPosition(i-INITIAL_OFFSET));
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
				int offset = i-INITIAL_OFFSET;
				long pos = task.getReturnStackPosition(offset);
				if (i > 0) {// do not overwrite the Z register
					try {
						String txt = ev.getActionCommand();
						long value = Long.parseLong(txt.replaceAll(" ", ""), 16);
						this.task.setReturnStack(pos, value);
					}
					catch (Exception ex) {}
				}
				this.updating = true;
				this.fields[i].setText(Processor.convertLongToString(this.task.getReturnStack(pos)));
				this.updating = false;
				return;
			}
		}
		
	}

}
