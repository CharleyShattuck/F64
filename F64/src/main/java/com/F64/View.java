package com.F64;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;

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
	private Compiler		compiler;
	private JSplitPane		split_pane;
	private JToolBar		toolbar;
	private JButton			run;
	private JButton			step;
	private JButton			stop;
	private JScrollPane 	scroll;
	private JPanel			panel;
	private JCheckBox[]		flags;
//	private JCheckBox[]		interrupts;
	private JLabel[]		register_labels;
	private JTextField[]	register_fields;
	private JTextField		slot_no;
	private JTextField[]	slots;
	private volatile boolean	is_running;


	public static void systemLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			java.lang.System.err.println("Couldn't use system look and feel.");
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

	private int addRegister(int x, int y)
	{
		JLabel label;
		int i;
		register_labels = new JLabel[Interpreter.SLOT_SIZE];
		register_fields = new JTextField[Interpreter.SLOT_SIZE];
		Insets label_insets = new Insets( 0, 6, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 0);
		Dimension registerFieldMin = new Dimension(100, 10);
		label = new JLabel("Register");
		panel.add(
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
		label = new JLabel("Register");
		panel.add(
			label,
			new GridBagConstraints(
				x+3, y,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		y += 1;
		for (i=0; i<Interpreter.SLOT_SIZE; ++i) {
			label = new JLabel();
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
					i < (Interpreter.SLOT_SIZE/2) ? x : x+2, i < (Interpreter.SLOT_SIZE/2) ? y+i : y+i - (Interpreter.SLOT_SIZE/2),
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
					i < (Interpreter.SLOT_SIZE/2) ? x+1 : x+3, i < (Interpreter.SLOT_SIZE/2) ? y+i : y+i - (Interpreter.SLOT_SIZE/2),
					1, 1,
					0.0, 1.0,
					GridBagConstraints.EAST,
					GridBagConstraints.BOTH,
					field_insets,
					4, 0
				)
			);
		}
		y -= 1;
		return 4;
	}
	
	private int addFlags(int x, int y)
	{
		JLabel label;
		int i;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0, 10, 0, 4);
		label = new JLabel("Flags");
		panel.add(
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
		flags = new JCheckBox[Flag.values().length];
		for (i=0; i<Flag.values().length; ++i) {
			flags[i] = new JCheckBox(Flag.values()[i].name());
			flags[i].setToolTipText(Flag.values()[i].getTooltip());
			flags[i].addItemListener(this);
			panel.add(
				flags[i],
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
		y -= 1;
		return 1;
	}

//	private int addInterrupts(int x, int y)
//	{
//		JLabel label;
//		int i;
//		Insets label_insets = new Insets( 0, 10, 0, 4);
//		Insets field_insets = new Insets( 0,  0, 0, 0);
//		label = new JLabel("Interrupts");
//		panel.add(
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
//		interrupts = new JCheckBox[Interrupt.values().length];
//		for (i=0; i<Interrupt.values().length; ++i) {
//			interrupts[i] = new JCheckBox(Interrupt.values()[i].name());
//			interrupts[i].setToolTipText(Interrupt.values()[i].getTooltip());
//			interrupts[i].addItemListener(this);
//			panel.add(
//				interrupts[i],
//				new GridBagConstraints(
//					x, y+i,
//					1, 1,
//					0.0, 1.0,
//					GridBagConstraints.WEST,
//					GridBagConstraints.BOTH,
//					field_insets,
//					2, 0
//				)
//			);
//		}
//		y -= 1;
//		return 1;
//	}

	private int addSlots(int x, int y)
	{
		JLabel label;
		int i;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 0);
		label = new JLabel("slot#");
		panel.add(
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
		slot_no = new JTextField();
		slot_no.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(
			slot_no,
			new GridBagConstraints(
				x, y+1,
				1, 1,
				0.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				field_insets,
				2, 0
			)
		);
		x += 1;
		slots = new JTextField[vm.getMaxSlot()];
		for (i=0; i<slots.length; ++i) {
			label = new JLabel("slot "+i);
			panel.add(
				label,
				new GridBagConstraints(
					x+i, y,
					1, 1,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					label_insets,
					2, 0
				)
			);
			slots[i] = new JTextField();
			slots[i].setHorizontalAlignment(JTextField.RIGHT);
			panel.add(
				slots[i],
				new GridBagConstraints(
					x+i, y+1,
					1, 1,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					2, 0
				)
			);
		}
		return slots.length;
	}

	public View(Interpreter i, Compiler c, System s, Dictionary d)
	{
		vm = i;
		compiler = c;
		int x, y;
		this.setSize(1000,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		toolbar = new JToolBar();
		split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		run = new JButton("Run");
		step = new JButton("Step");
		stop = new JButton("Stop");

		toolbar.setFloatable(false);
		toolbar.add(run);
		toolbar.add(step);
		toolbar.add(stop);
		split_pane.setTopComponent(toolbar);
		panel = new JPanel( new GridBagLayout() );
		x = 0; y = 0;
		// register
		x += this.addRegister(x, y);
		// Flags
		x += this.addFlags(x, y);
		//
//		x += this.addInterrupts(x, y);
		// slots
		x += this.addSlots(x, y);
		//
		scroll = new JScrollPane(panel);
		split_pane.setBottomComponent(scroll);
//		split_pane.setResizeWeight(0.0);
		stop.setEnabled(false);
//		step.setEnabled(false);
//		run.setBackground(getBackground());
//		step.setBackground(getBackground());
		run.addActionListener(this);
		step.addActionListener(this);
		stop.addActionListener(this);

		update();
		
		this.add(split_pane);

		setVisible(true);
	}
	
	public Interpreter getInterpreter() {return vm;}	
	
	public void update()
	{
		int i;
		for (i=0; i<Interpreter.SLOT_SIZE; ++i) {
			long value = vm.getRegister(i);
			register_fields[i].setText(convertLongToString(value));
		}
		for (i=0; i<Flag.values().length; ++i) {
			flags[i].setSelected(vm.getFlag(i));
		}
//		for (i=0; i<Interrupt.values().length; ++i) {
//			interrupts[i].setSelected(vm.getInterruptFlag(Register.INTF, Interrupt.values()[i]));
//		}
		int slot = this.vm.getSlot();
		slot_no.setText(Integer.toString(slot));

		ISA instr = ISA.values()[vm.getSlot(slot)];
		i = 0;
		slots[i].setToolTipText(instr.getTooltip());
		slots[i++].setText(instr.name());
		int no_of_slots = instr.size();
		if (no_of_slots == 0) {
			// extension instruction
		}
		while (i<no_of_slots) {
			slots[i++].setText(Integer.toString(this.vm.getSlot(slot+i)));
		}
		while (i<slots.length) {
			slots[i++].setText("");
		}
	
	}

	public boolean step()
	{
		try {
			this.vm.step();
			return true;
		}
		catch (Exception ex) {
			this.panel.setBackground(Color.RED);
//			this.setTitle(ex.getMessage());
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent ev)
	{
		Object source = ev.getSource();
		if (source == step) {
			step();
			this.update();
		}
		else if (source == run) {
			this.start();
		}
		if (source == stop) {
			this.stop();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		int i;
		Object source = ev.getItemSelectable();
		for (i=0; i<Flag.values().length; ++i) {
			if (source == flags[i]) {
				// toggle flag bit
				vm.setFlag(i, !vm.getFlag(i));
				this.update();
			}
		}
//		for (i=0; i<Interrupt.values().length; ++i) {
//			if (source == interrupts[i]) {
//				// toggle flag bit
//				vm.setInterruptFlag(Register.INTF, Interrupt.values()[i], !vm.getInterruptFlag(Register.INTF, Interrupt.values()[i]));
//				update();
//			}
//		}
		
	}

	public void start()
	{
		if (!is_running) {
			is_running = true;
			step.setEnabled(false);
			run.setEnabled(false);
			stop.setEnabled(true);
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public void stop()
	{
		is_running = false;
		step.setEnabled(true);
		run.setEnabled(true);
		stop.setEnabled(false);
	}

	@Override
	public void run()
	{
		int cnt = 0;
		while (is_running) {
			if (!this.step()) {
				this.stop();
			}
			if ((++cnt & 0xff) == 0) {
				this.update();
			}
		}
	}
	

}
