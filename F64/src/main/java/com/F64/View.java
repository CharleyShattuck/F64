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
//import java.io.IOException;



import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class View extends JFrame implements ActionListener, ItemListener, Runnable {
	private Processor			processor;
	private JSplitPane			main_split_pane;
	private JToolBar			toolbar;
	private JButton				run;
	private JButton				trace;
	private JButton				step;
	private JButton				stop;
	private JScrollPane 		scroll;
	private JPanel				main_panel;
	private JPanel				register_panel;
	private JPanel				flag_panel;
	private JPanel				port_panel;
	private JPanel				other_panel;
	private JCheckBox[]			flags;
	private JLabel[]			register_labels;
	private JTextField[]		register_fields;
	private JTextField			slot_no;
	private JTextField[]		slots;
	private JTextField[]		ports;
	private volatile boolean	running;
	private volatile boolean	tracing;
	private volatile boolean	updating;

	public static void systemLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (java.lang.Exception ex) {
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

	private int addRegister(JPanel panel, int x, int y)
	{
		int limit = Processor.SLOT_SIZE;
		JLabel label;
		int i;
		this.register_labels = new JLabel[limit];
		this.register_fields = new JTextField[limit];
		Insets label_insets = new Insets( 0, 2, 0, 4);
		Insets field_insets = new Insets( 0,  0, 0, 4);
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
		for (i=0; i<limit; ++i) {
			label = new JLabel();
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			JTextField field = new JTextField();
			field.setMinimumSize(registerFieldMin);
			if (i < Register.values().length) {
				label.setText(" "+Register.values()[i].name());
				label.setToolTipText(Register.values()[i].getTooltip());
			}
			else {
				label.setText(" X"+i);
			}
			this.register_labels[i] = label;
			this.register_fields[i] = field;
			panel.add(
				label,
				new GridBagConstraints(
					i < (Processor.SLOT_SIZE/2) ? x : x+2, i < (Processor.SLOT_SIZE/2) ? y+i : y+i - (Processor.SLOT_SIZE/2),
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
					i < (Processor.SLOT_SIZE/2) ? x+1 : x+3, i < (Processor.SLOT_SIZE/2) ? y+i : y+i - (Processor.SLOT_SIZE/2),
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
	
	private void addFlags(JPanel panel, int x, int y)
	{
		int limit = Flag.values().length;
		JLabel label;
		int i;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( -4, 10, 0, 4);
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
		this.flags = new JCheckBox[limit];
		for (i=0; i<limit; ++i) {
			this.flags[i] = new JCheckBox(Flag.values()[i].name());
			this.flags[i].setToolTipText(Flag.values()[i].getTooltip());
			this.flags[i].addItemListener(this);
			panel.add(
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

	private void addSlots(JPanel panel, int x, int y)
	{
		int limit = this.processor.getMaxSlot();
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
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				label_insets,
				2, 0
			)
		);
		this.slot_no = new JTextField();
		this.slot_no.setHorizontalAlignment(JTextField.RIGHT);
		panel.add(
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
			panel.add(
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
			panel.add(
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

	private void addPorts(JPanel panel, int x, int y)
	{
		int limit = Port.values().length;

		JLabel label;
		int i;
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0, 10, 0, 4);
		label = new JLabel("Read Ports");
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
		y += 1;
		this.ports = new JTextField[limit+limit];
		for (i=0; i<limit; ++i) {
			label = new JLabel(Port.values()[i].name());
			panel.add(
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
			this.ports[i] = new JTextField(Port.values()[i].name());
			this.ports[i].setToolTipText(Port.values()[i].getTooltip());
			panel.add(
					this.ports[i],
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
		}
		y += limit;
		label = new JLabel("Write Ports");
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
		y += 1;
		for (i=0; i<limit; ++i) {
			label = new JLabel(Port.values()[i].name());
			panel.add(
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
			this.ports[i+limit] = new JTextField(Port.values()[i].name());
			this.ports[i+limit].setToolTipText(Port.values()[i].getTooltip());
			panel.add(
				this.ports[i+limit],
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
		}
	}

	public View(Processor p, Interpreter i, Compiler c, System s, Dictionary d)
	{
		this.processor = p;
		this.setSize(1000,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Insets panel_insets = new Insets( 0, 0, 0, 0);

		this.toolbar = new JToolBar();
		this.main_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.run = new JButton("Run");
		this.trace = new JButton("Trace");
		this.step = new JButton("Step");
		this.stop = new JButton("Stop");

		this.toolbar.setFloatable(false);
		this.toolbar.add(this.run);
		this.toolbar.add(this.trace);
		this.toolbar.add(this.step);
		this.toolbar.add(this.stop);
		this.main_panel = new JPanel( new GridBagLayout() );
		this.main_split_pane.setTopComponent(this.toolbar);
		this.scroll = new JScrollPane(this.main_panel);
		this.main_split_pane.setBottomComponent(this.scroll);
		this.register_panel = new JPanel( new GridBagLayout() );
		this.flag_panel = new JPanel( new GridBagLayout() );
		this.port_panel = new JPanel( new GridBagLayout() );
		this.other_panel = new JPanel( new GridBagLayout() );
		// register
		this.addRegister(this.register_panel, 0, 0);
		this.main_panel.add(
				this.register_panel,
				new GridBagConstraints(
					0, 0,
					1, 2,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					panel_insets,
					0, 0
				)
			);
		// flags
		this.addFlags(this.flag_panel, 0, 0);
		this.main_panel.add(
				this.flag_panel,
				new GridBagConstraints(
					1, 0,
					1, 2,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					panel_insets,
					0, 0
				)
			);
		// slots
		this.addSlots(this.other_panel, 0, 0);
		this.main_panel.add(
				this.other_panel,
				new GridBagConstraints(
					2, 0,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.NONE,
					panel_insets,
					0, 0
				)
			);
		// ports
		this.addPorts(this.port_panel, 0, 0);
		this.main_panel.add(
				this.port_panel,
				new GridBagConstraints(
					2, 1,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.NONE,
					panel_insets,
					0, 0
				)
			);
		//
//		split_pane.setResizeWeight(0.0);
		this.stop.setEnabled(false);
//		step.setEnabled(false);
//		run.setBackground(getBackground());
//		step.setBackground(getBackground());
		this.run.addActionListener(this);
		this.trace.addActionListener(this);
		this.step.addActionListener(this);
		this.stop.addActionListener(this);

		this.update();
		
		this.add(this.main_split_pane);

		setVisible(true);
	}
	
//	public Interpreter getInterpreter() {return vm;}	
	
	public void update()
	{
		this.updating = true;
		int i, limit;
		for (i=0; i<Processor.SLOT_SIZE; ++i) {
			long value = processor.getRegister(i);
			this.register_fields[i].setText(convertLongToString(value));
		}
		for (i=0; i<Flag.values().length; ++i) {
			this.flags[i].setSelected(processor.getFlag(i));
		}
		limit = Port.values().length;
		for (i=0; i<limit; ++i) {
			this.ports[i].setText(convertLongToString(processor.getPort(i, false)));
			this.ports[i+limit].setText(convertLongToString(processor.getPort(i, true)));
		}
//		for (i=0; i<Interrupt.values().length; ++i) {
//			interrupts[i].setSelected(vm.getInterruptFlag(Register.INTF, Interrupt.values()[i]));
//		}
		int slot = this.processor.getSlot();
		this.slot_no.setText(Integer.toString(slot));

		ISA instr = ISA.values()[processor.getSlot(slot)];
		i = 0;
		this.slots[i].setToolTipText(instr.getTooltip());
		this.slots[i++].setText(instr.name());
		int no_of_slots = instr.size();
		if (no_of_slots == 0) {
			// extension instruction
		}
		while (i<no_of_slots) {
			this.slots[i++].setText(Integer.toString(this.processor.getSlot(slot+i)));
		}
		while (i<this.slots.length) {
			this.slots[i++].setText("");
		}
		this.updating = false;
	}

	public void setProcessor(Processor p)
	{
		this.processor = p;
		this.update();
	}

	public boolean step()
	{
		try {
			this.processor.step();
			return true;
		}
		catch (java.lang.Exception ex) {
			this.main_panel.setBackground(Color.RED);
			this.register_panel.setBackground(Color.RED);
			this.flag_panel.setBackground(Color.RED);
			this.port_panel.setBackground(Color.RED);
			this.other_panel.setBackground(Color.RED);
//			this.setTitle(ex.getMessage());
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent ev)
	{
		Object source = ev.getSource();
		if (source == this.step) {
			step();
			this.update();
		}
		else if (source == this.run) {
			this.start();
		}
		else if (source == this.trace) {
			this.trace();
		}
		if (source == this.stop) {
			this.stop();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		if (this.updating) {return;}
		int i;
		Object source = ev.getItemSelectable();
		for (i=0; i<Flag.values().length; ++i) {
			if (source == flags[i]) {
				// toggle flag bit
				this.processor.setFlag(i, !this.processor.getFlag(i));
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
		if (!this.running) {
			this.running = true;
			this.tracing = false;
			this.step.setEnabled(false);
			this.run.setEnabled(false);
			this.trace.setEnabled(false);
			this.stop.setEnabled(true);
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public void trace()
	{
		if (!this.running) {
			this.running = true;
			this.tracing = true;
			this.step.setEnabled(false);
			this.run.setEnabled(false);
			this.trace.setEnabled(false);
			this.stop.setEnabled(true);
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public void stop()
	{
		this.running = false;
		this.step.setEnabled(true);
		this.run.setEnabled(true);
		this.trace.setEnabled(true);
		this.stop.setEnabled(false);
	}

	@Override
	public void run()
	{
		int cnt = 0;
		while (this.running) {
			if (!this.step()) {
				this.stop();
			}
			if (this.tracing) {
				this.update();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			else {
				if ((++cnt & 0xff) == 0) {
					this.update();
				}
			}
		}
	}
	

}
