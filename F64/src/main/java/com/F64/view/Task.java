package com.F64.view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.F64.Flag;

public class Task extends JPanel implements ActionListener, ItemListener {
	private com.F64.Processor	processor;
	private com.F64.Task		task;
	private JTabbedPane			register_pane;
	private JComboBox<String>	task_selection;
	private Flags				flag_panel;
	private Register			register_panel;
	private LocalRegister		local_panel;
	private SystemRegister		system_register_panel;
	private SIMDRegister		simd_panel;
	private ParameterStack		parameter_stack;
	private ReturnStack			return_stack;
	private	int					no_of_tasks;
	private	int					selected_task;
	private volatile boolean	updating;

	public Task(com.F64.Processor p)
	{
		super( new GridBagLayout() );
		processor = p;
		task = p.getTask();
		int i,limit = p.getNoOfTasks();
		task_selection = new JComboBox<String>();
		this.register_pane = new JTabbedPane();
		this.register_panel = new Register(task);
		this.local_panel = new LocalRegister(task);
		this.system_register_panel = new SystemRegister(task);
		this.simd_panel = new SIMDRegister(task);
		this.parameter_stack = new ParameterStack(task);
		this.return_stack = new ReturnStack(task);
		this.flag_panel = new Flags(this);

		this.register_pane.addTab("Register", null, this.register_panel, "General purpose register");
		this.register_pane.addTab("Local", null, this.local_panel, "Local register");
		this.register_pane.addTab("System", null, this.system_register_panel, "System register");
		this.register_pane.addTab("SIMD", null, this.simd_panel, "SIMD register");
		this.register_pane.addTab("Flags", null, this.flag_panel, "Flags");
		this.register_pane.addTab("Stack", null, this.parameter_stack, "Parameter stack");
		this.register_pane.addTab("Return", null, this.return_stack, "Return stack");

		task_selection.addActionListener(this);
		
		this.no_of_tasks = p.getNoOfTasks();
		this.selected_task = p.getCurrentTask();
		for (i=0; i<this.no_of_tasks; ++i) {
			task_selection.addItem("Task "+i);
		}
		task_selection.setSelectedIndex(selected_task);
		
		int x = 0;
		int y = 0;
		Insets panel_insets = new Insets( 0, 0, 0, 0);

		// register
		this.add(
			this.task_selection,
			new GridBagConstraints(
				x, y,
				1, 1,
				0.0, 0.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				panel_insets,
				0, 0
			)
		);
		y += 1;

		// register
		this.add(
			this.register_pane,
			new GridBagConstraints(
				x, y,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.WEST,
				GridBagConstraints.BOTH,
				panel_insets,
				0, 0
			)
		);
		y += 1;

	}

	public void update()
	{
		this.updating = true;
		if (processor.hasFailed()) {
			this.simd_panel.setBackground(Color.RED);
			this.register_panel.setBackground(Color.RED);
			this.local_panel.setBackground(Color.RED);
			this.system_register_panel.setBackground(Color.RED);
			this.simd_panel.setBackground(Color.RED);
			this.flag_panel.setBackground(Color.RED);
		}
		else {
			this.simd_panel.setBackground(null);
			this.register_panel.setBackground(null);
			this.local_panel.setBackground(null);
			this.system_register_panel.setBackground(null);
			this.simd_panel.setBackground(null);
			this.flag_panel.setBackground(null);
		}
		this.register_panel.update();
		this.local_panel.update();
		this.system_register_panel.update();
		this.simd_panel.update();
		this.parameter_stack.update();
		this.return_stack.update();
		this.flag_panel.update();
		this.updating = false;
	}

	public void setTask(com.F64.Task value)
	{
		this.task = value;
	
		this.local_panel.setTask(task);
		this.simd_panel.setTask(task);
		this.flag_panel.setTask(task);
		this.parameter_stack.setTask(task);
		this.return_stack.setTask(task);
		this.register_panel.setTask(task);
		this.system_register_panel.setTask(task);
		this.update();
	}

	public void setProcessor(com.F64.Processor p)
	{
		this.processor = p;
		this.no_of_tasks = p.getNoOfTasks();
		selectTask(p.getCurrentTask());
	}

	public void selectTask(int index)
	{
		this.selected_task = index;
		this.updating = true;
		task_selection.setSelectedIndex(index);
		this.updating = false;
		setTask(processor.getTask(index));
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
				this.task.setFlag(i, !this.task.getFlag(i));
				flag_panel.update();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev)
	{
		if (this.updating) {return;}
		if (ev.getSource() == task_selection) {
			selectTask(task_selection.getSelectedIndex());
			update();
		}
	}

}
