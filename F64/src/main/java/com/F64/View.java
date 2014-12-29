package com.F64;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class View extends JFrame implements ActionListener, ItemListener, Runnable {
	private Interpreter		vm;
	private JSplitPane		split_pane;
	private JToolBar		toolbar;
	private JButton			run;
	private JButton			step;
	private JCheckBox		carry;
	private JScrollPane 	scroll;
	private JPanel			panel;
	private JLabel[]		register_labels;
	private JTextField[]	register_fields;


	public static void systemLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.err.println("Couldn't use system look and feel.");
		}		
	}

	public static String convertLongToString(long value)
	{
		return
			String.format(
				"%04X %04X  %04X %04X",
				(int) ((value >>> 48) & 0xffff),
				(int) ((value >>> 32) & 0xffff),
				(int) ((value >>> 16) & 0xffff),
				(int) (value & 0xffff)
			);
	}
	
	public View()
	{
		vm = new Interpreter();
		
		int i;
		this.setSize(1000,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		toolbar = new JToolBar();
		split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		run = new JButton("Run");
		step = new JButton("Step");

		toolbar.setFloatable(false);
		toolbar.add(run);
		toolbar.add(step);
		split_pane.setTopComponent(toolbar);
		panel = new JPanel( new GridBagLayout() );
		//
		register_labels = new JLabel[Interpreter.SLOT_SIZE];
		register_fields = new JTextField[Interpreter.SLOT_SIZE];
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 0);
		Dimension registerFieldMin = new Dimension(100, 10);
		for (i=0; i<Interpreter.SLOT_SIZE; ++i) {
			JLabel label = new JLabel();
			JTextField field = new JTextField();
			field.setMinimumSize(registerFieldMin);
			if (i < Register.values().length) {
				label.setText(" "+Register.values()[i].name());
				label.setToolTipText(Register.values()[i].getTooltip());
			}
			else {
				label.setText(" X"+i);
			}
			register_labels[i] = label;
			register_fields[i] = field;
			panel.add(
					label,
					new GridBagConstraints(
						i < (Interpreter.SLOT_SIZE/2) ? 0 : 2, i < (Interpreter.SLOT_SIZE/2) ? i : i - (Interpreter.SLOT_SIZE/2),
						1, 1,
						0.0, 1.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						label_insets,
						2, 0
					)
				);
			panel.add(
					field,
					new GridBagConstraints(
						i < (Interpreter.SLOT_SIZE/2) ? 1 : 3, i < (Interpreter.SLOT_SIZE/2) ? i : i - (Interpreter.SLOT_SIZE/2),
						1, 1,
						0.0, 1.0,
						GridBagConstraints.EAST,
						GridBagConstraints.BOTH,
						field_insets,
						4, 0
					)
				);
		}
		carry = new JCheckBox("Carry");
		carry.addItemListener(this);
		panel.add(
				carry,
				new GridBagConstraints(
					4, 0,
					1, 1,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);

		//
		scroll = new JScrollPane(panel);
		split_pane.setBottomComponent(scroll);
		run.setEnabled(false);
		step.setEnabled(false);
		run.setBackground(getBackground());
		step.setBackground(getBackground());
		run.addActionListener(this);
		step.addActionListener(this);

		update();
		
		this.add(split_pane);

		setVisible(true);
	}

	public void update()
	{
		int i;
		for (i=0; i<Interpreter.SLOT_SIZE; ++i) {
			long value = vm.getRegister(i);
			register_fields[i].setText(convertLongToString(value));
		}
		carry.setSelected(vm.getFlag(Flag.Carry));
	}

	public void setInterpreter(Interpreter value) {vm = value;}
	
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run()
	{
		this.vm = new Interpreter();
		
	}

	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		Object source = ev.getItemSelectable();
		if (source == carry) {
			// toggle carry bit
			vm.setFlag(Flag.Carry, !vm.getFlag(Flag.Carry));
			update();
		}
		
	}
	
}
