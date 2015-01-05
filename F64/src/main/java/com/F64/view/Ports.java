package com.F64.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.F64.Port;

@SuppressWarnings("serial")
public class Ports extends JPanel {
	private JTextField[]		ports;
	private JLabel[]			states;

	public Ports()
	{
		super( new GridBagLayout() );
		JTextField field;
		int limit = Port.values().length;
		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);
		JLabel label;
		int i;
		int x = 0;
		int y = 0;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0, 10, 0, 4);
		label = new JLabel("Read Ports");
		this.add(
			label,
			new GridBagConstraints(
				x+1, y,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		y += 1;
		this.ports = new JTextField[limit+limit];
		this.states = new JLabel[limit+limit];
		for (i=0; i<limit; ++i) {
			label = new JLabel(Port.values()[i].name());
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
			//
			this.ports[i] = field = new JTextField("", 20);
			field.setFont(font);
			field.setToolTipText(Port.values()[i].getTooltip());
			this.add(
				field,
				new GridBagConstraints(
					x+1, y+i,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
			//
			this.states[i] = label = new JLabel();
			this.add(
				label,
				new GridBagConstraints(
					x+2, y+i,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
		}
		y += limit;
		label = new JLabel("Write Ports");
		this.add(
			label,
			new GridBagConstraints(
				x+1, y,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		y += 1;
		for (i=0; i<limit; ++i) {
			label = new JLabel(Port.values()[i].name());
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
			this.ports[i+limit] = field = new JTextField("", 20);
			field.setFont(font);
			field.setToolTipText(Port.values()[i].getTooltip());
			this.add(
				field,
				new GridBagConstraints(
					x+1, y+i,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
			//
			this.states[i+limit] = label = new JLabel();
			this.add(
				label,
				new GridBagConstraints(
					x+2, y+i,
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
		int limit = Port.values().length;
		int read_mask = processor.getPortReadMask();
		int write_mask = processor.getPortWriteMask();
		for (int i=0; i<limit; ++i) {
			this.ports[i].setText(Processor.convertLongToString(processor.getPort(i, false)));
			this.ports[i+limit].setText(Processor.convertLongToString(processor.getPort(i, true)));
			if (((1 << i) & read_mask) == 0) {
				this.states[i].setText("");
			}
			else {
				this.states[i].setText("@");
			}
			if (((1 << i) & write_mask) == 0) {
				this.states[i+limit].setText("");
			}
			else {
				this.states[i+limit].setText("!");
			}
		}

	}

	
}
