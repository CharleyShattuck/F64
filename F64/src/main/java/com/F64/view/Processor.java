package com.F64.view;

import java.awt.Color;
import java.awt.Dimension;
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
public class Processor  extends JFrame implements ActionListener, Runnable {
	private com.F64.Processor	processor;
	private JSplitPane			main_split_pane;
	private JToolBar			toolbar;
	private JButton				run;
	private JButton				trace;
	private JButton				step;
	private JButton				stop;
	private JScrollPane 		scroll;
	private JPanel				main_panel;
//	private JTabbedPane			register_pane;
//	private Register			register_panel;
//	private LocalRegister		local_panel;
//	private SystemRegister		system_register_panel;
//	private SIMDRegister		media_panel;
//	private Flags				flag_panel;
	private Task				task_panel;
	private Ports				port_panel;
	private Slots				slot_panel;
//	private ParameterStack		parameter_stack;
//	private ReturnStack			return_stack;
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


	public static String convertRemainingToString(long value, int slot)
	{
		long mask = com.F64.Processor.geRemainingMask(slot);
		value &= mask;
		if (mask <= 0xffL) {
			return String.format("%02X", (int) value);
		}
		if (mask <= 0xffffL) {
			return String.format("%04X", (int) value);
		}
		if (mask <= 0xff_ffffL) {
			return
				String.format(
					"%02X_%04X",
					(int) ((value >>> 16) & 0xffff),
					(int) (value & 0xffff)
				);
		}
		if (mask <= 0xffff_ffffL) {
			return
				String.format(
					"%04X_%04X",
					(int) ((value >>> 16) & 0xffff),
					(int) (value & 0xffff)
				);
		}
		if (mask <= 0xff_ffff_ffffL) {
			return
				String.format(
						"%02X_%04X_%04X",
						(int) ((value >>> 32) & 0xffff),
						(int) ((value >>> 16) & 0xffff),
						(int) (value & 0xffff)
					);
		}
		if (mask <= 0xffff_ffff_ffffL) {
			return
				String.format(
						"%04X_%04X_%04X",
						(int) ((value >>> 32) & 0xffff),
						(int) ((value >>> 16) & 0xffff),
						(int) (value & 0xffff)
					);
		}
		if (mask <= 0xff_ffff_ffff_ffffL) {
			String.format(
					"%02X_%04X_%04X_%04X",
					(int) ((value >>> 48) & 0xffff),
					(int) ((value >>> 32) & 0xffff),
					(int) ((value >>> 16) & 0xffff),
					(int) (value & 0xffff)
				);			
		}
		return
			String.format(
				"%04X_%04X_%04X_%04X",
				(int) ((value >>> 48) & 0xffff),
				(int) ((value >>> 32) & 0xffff),
				(int) ((value >>> 16) & 0xffff),
				(int) (value & 0xffff)
			);
	}

	public static String convertSlotToString(int value)
	{
		return
			String.format(
				"%02X",
				(int) (value & com.F64.Processor.SLOT_MASK)
			);
	}

//	private void addSlots(JPanel panel, int x, int y)
//	{
//	}
//
//	private void addPorts(JPanel panel, int x, int y)
//	{
//	}

	public Processor(com.F64.Processor p, Interpreter i, Compiler c, System s, Dictionary d)
	{
		this.processor = p;
		this.setSize(1000,900);
		this.setPreferredSize(new Dimension(1000, 900));
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

//		this.register_pane = new JTabbedPane();
		this.main_panel = new JPanel( new GridBagLayout() );
		this.main_split_pane.setTopComponent(this.toolbar);
		this.scroll = new JScrollPane(this.main_panel);
		this.main_split_pane.setBottomComponent(this.scroll);
		task_panel = new Task(p);
		this.port_panel = new Ports(p);
		this.slot_panel = new Slots(p);
//		this.port_panel = new JPanel( new GridBagLayout() );
//		this.other_panel = new JPanel( new GridBagLayout() );
		// register
//		this.addRegister(this.register_panel, 0, 0);

		
		int x = 0;
		int y = 0;
		// register
		this.main_panel.add(
				this.task_panel,
				new GridBagConstraints(
					x, y,
					1, 2,
					0.8, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					panel_insets,
					0, 0
				)
			);
		x += 1;
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
			this.task_panel.setBackground(Color.RED);
			this.port_panel.setBackground(Color.RED);
			this.slot_panel.setBackground(Color.RED);
		}
		else {
			this.main_panel.setBackground(null);
			this.task_panel.setBackground(null);
			this.port_panel.setBackground(null);
			this.slot_panel.setBackground(null);
		}
		this.task_panel.update();
		this.slot_panel.update();
		this.port_panel.update();
		this.updating = false;
	}

	public void setProcessor(com.F64.Processor p)
	{
		this.processor = p;
		this.task_panel.setProcessor(p);
		this.slot_panel.setProcessor(p);
		this.port_panel.setProcessor(p);
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
