package com.F64.view;

import java.awt.Color;
//import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
//import javax.swing.JCheckBox;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
//import javax.swing.JTextField;
import javax.swing.JToolBar;
//import javax.swing.SwingConstants;


import com.F64.Compiler;
import com.F64.Dictionary;
import com.F64.Flag;
//import com.F64.ISA;
import com.F64.Interpreter;
//import com.F64.Port;
import com.F64.System;

@SuppressWarnings("serial")
public class Processor  extends JFrame implements ActionListener, ItemListener, Runnable {
	private com.F64.Processor	processor;
	private JSplitPane			main_split_pane;
	private JToolBar			toolbar;
	private JButton				run;
	private JButton				trace;
	private JButton				step;
	private JButton				stop;
	private JScrollPane 		scroll;
	private JPanel				main_panel;
	private JTabbedPane			register_pane;
	private Register			register_panel;
	private SystemRegister		system_register_panel;
	private Flags				flag_panel;
	private Ports				port_panel;
	private Slots				slot_panel;
//	private JPanel				other_panel;
	private volatile boolean	running;
	private volatile boolean	tracing;
	private volatile boolean	updating;

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

	private void addSlots(JPanel panel, int x, int y)
	{
	}

	private void addPorts(JPanel panel, int x, int y)
	{
	}

	public Processor(com.F64.Processor p, Interpreter i, Compiler c, System s, Dictionary d)
	{
		this.processor = p;
		this.setSize(1000,800);
		this.setTitle("Single Processor View");
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

		this.register_pane = new JTabbedPane();
		this.main_panel = new JPanel( new GridBagLayout() );
		this.main_split_pane.setTopComponent(this.toolbar);
		this.scroll = new JScrollPane(this.main_panel);
		this.main_split_pane.setBottomComponent(this.scroll);
//		this.register_panel = new JPanel( new GridBagLayout() );
		this.register_panel = new Register();
		this.system_register_panel = new SystemRegister();
		this.flag_panel = new Flags(this);
		this.port_panel = new Ports();
		this.slot_panel = new Slots();
//		this.port_panel = new JPanel( new GridBagLayout() );
//		this.other_panel = new JPanel( new GridBagLayout() );
		// register
//		this.addRegister(this.register_panel, 0, 0);

		this.register_pane.addTab("Register", null, this.register_panel, "General purpose register");
		this.register_pane.addTab("System", null, this.system_register_panel, "System register");
		this.register_pane.addTab("Flags", null, this.flag_panel, "Flags");
		
		int x = 0;
		int y = 0;
		// register
		this.main_panel.add(
				this.register_pane,
				new GridBagConstraints(
					x, y,
					1, 2,
					0.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					panel_insets,
					0, 0
				)
			);
		x += 1;
//		// system register
//		this.main_panel.add(
//				this.system_register_panel,
//				new GridBagConstraints(
//					x, y,
//					1, 2,
//					0.0, 1.0,
//					GridBagConstraints.WEST,
//					GridBagConstraints.BOTH,
//					panel_insets,
//					0, 0
//				)
//			);
//		x += 1;
		// flags
//		this.addFlags(this.flag_panel, 0, 0);
//		this.main_panel.add(
//				this.flag_panel,
//				new GridBagConstraints(
//					x, y,
//					1, 2,
//					0.0, 1.0,
//					GridBagConstraints.WEST,
//					GridBagConstraints.BOTH,
//					panel_insets,
//					0, 0
//				)
//			);
//		x += 1;
		// slots
//		this.addSlots(this.other_panel, 0, 0);
		this.main_panel.add(
				this.slot_panel,
				new GridBagConstraints(
					x, y,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.NONE,
					panel_insets,
					0, 0
				)
			);
		y += 1;
		// ports
//		this.addPorts(this.port_panel, 0, 0);
		this.main_panel.add(
				this.port_panel,
				new GridBagConstraints(
					x, y,
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
//		step.setEnabled(false);
//		run.setBackground(getBackground());
//		step.setBackground(getBackground());
		this.run.addActionListener(this);
		this.trace.addActionListener(this);
		this.step.addActionListener(this);
		this.stop.addActionListener(this);

		this.stop.setEnabled(false);

		this.update();
		
		this.add(this.main_split_pane);

		setVisible(true);
	}
	
//	public Interpreter getInterpreter() {return vm;}	
	
	public void update()
	{
		this.updating = true;
		if (processor.hasFailed()) {
			this.main_panel.setBackground(Color.RED);
			this.register_panel.setBackground(Color.RED);
			this.system_register_panel.setBackground(Color.RED);
			this.flag_panel.setBackground(Color.RED);
			this.port_panel.setBackground(Color.RED);
			this.slot_panel.setBackground(Color.RED);
		}
		else {
			this.main_panel.setBackground(null);
			this.register_panel.setBackground(null);
			this.system_register_panel.setBackground(null);
			this.flag_panel.setBackground(null);
			this.port_panel.setBackground(null);
			this.slot_panel.setBackground(null);
		}
		this.register_panel.update(processor);
		this.system_register_panel.update(processor);
//		for (i=0; i<com.F64.Processor.SLOT_SIZE; ++i) {
//			long value = processor.getRegister(i);
//			this.register_fields[i].setText(convertLongToString(value));
//		}
		this.flag_panel.update(processor);
		this.slot_panel.update(processor);
		this.port_panel.update(processor);
//		for (i=0; i<Interrupt.values().length; ++i) {
//			interrupts[i].setSelected(vm.getInterruptFlag(Register.INTF, Interrupt.values()[i]));
//		}
		this.updating = false;
	}

	public void setProcessor(com.F64.Processor p)
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
//			this.setTitle(ex.getMessage());
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent ev)
	{
		if (this.updating) {return;}
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
			if (flag_panel.isFlag(i, source)) {
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
		if (this.running) {
			this.running = false;
			this.step.setEnabled(true);
			this.run.setEnabled(true);
			this.trace.setEnabled(true);
			this.stop.setEnabled(false);
		}
	}

	@Override
	public void run()
	{
		int cnt = 0;
		while (this.running) {
			if (!this.step()) {
				this.update();
				this.stop();
			}
			if (this.tracing) {
				this.update();
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
				}
			}
			else {
				if ((++cnt & 0xff) == 0) {
					this.update();
					try {
						Thread.sleep(1);
					}
					catch (InterruptedException e) {
					}
				}
			}
		}
	}


}
