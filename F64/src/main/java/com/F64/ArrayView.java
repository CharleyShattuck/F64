package com.F64;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class ArrayView extends JFrame implements ActionListener, ItemListener, Runnable {
	private View				view;
	private ProcessorArray		processor_array;
	private Interpreter			interpreter;
	private JToggleButton[][]	toggle_array;
	private JTextField[][]		connector_array1;
	private JTextField[][]		connector_array2;
	private JTextField[][]		connector_array3;
	private JTextField[][]		connector_array4;
	private JSplitPane			main_split_pane;
	private JToolBar			toolbar;
	private JButton				run;
	private JButton				go;
	private JButton				trace;
	private JButton				step;
	private JButton				stop;
	private JScrollPane 		scroll;
	private JPanel				main_panel;
	private int					selected_x;
	private int					selected_y;
	private volatile boolean	updating;
	private volatile boolean	tracing;
	private volatile boolean	running;
	private volatile boolean	free_running;

	private void connect(int columns, int rows, int x, int y)
	{
		
	}
	
	private void addArray(JPanel panel, int x0, int y0)
	{
		JLabel label;
		int rows = this.processor_array.getRows();
		int columns = this.processor_array.getColumns();
		Insets insets = new Insets( 0, 0, 0, 0);
		this.toggle_array = new JToggleButton[rows][columns];
		if ((rows > 1) && (columns > 1)) {
			this.connector_array1 = new JTextField[rows][columns];
			this.connector_array2 = new JTextField[rows][columns];
			this.connector_array3 = new JTextField[rows][columns];
			this.connector_array4 = new JTextField[rows][columns];
		}
		final int factor = 5;
		for (int y=0; y<rows; ++y) {
			for (int x=0; x<columns; ++x) {
				this.processor_array.getProcessor(x, y).powerOn();
				this.toggle_array[y][x] = new JToggleButton(x+"."+y);
				this.toggle_array[y][x].addActionListener(this);
				panel.add(
					this.toggle_array[y][x],
					new GridBagConstraints(
						x0+factor*x, y0+factor*y,
						factor-1, factor-1,
						0.0, 1.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				//
				label = new JLabel(((x & 1) == 0) ? "R" : "L");
				panel.add(
					label,
					new GridBagConstraints(
						x0+factor*x+factor-1, y0+factor*y,
						1, 1,
						0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				label = new JLabel(((x & 1) == 0) ? "R" : "L");
				panel.add(
					label,
					new GridBagConstraints(
						x0+factor*x+factor-1, y0+factor*y+3,
						1, 1,
						0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				//
				this.connector_array1[y][x] = new JTextField(" ");
				this.connector_array1[y][x].setHorizontalAlignment(JTextField.CENTER);
				panel.add(
					this.connector_array1[y][x],
					new GridBagConstraints(
						x0+factor*x+1, y0+factor*y+factor-1,
						1, 1,
						0.5, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				this.connector_array2[y][x] = new JTextField(" ");
				this.connector_array2[y][x].setHorizontalAlignment(JTextField.CENTER);
				panel.add(
					this.connector_array2[y][x],
					new GridBagConstraints(
						x0+factor*x+2, y0+factor*y+factor-1,
						1, 1,
						0.5, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				//
				label = new JLabel(((y & 1) == 0) ? "U" : "D");
				panel.add(
					label,
					new GridBagConstraints(
						x0+factor*x, y0+factor*y+factor-1,
						1, 1,
						0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				label = new JLabel(((y & 1) == 0) ? "U" : "D");
				panel.add(
					label,
					new GridBagConstraints(
						x0+factor*x+3, y0+factor*y+factor-1,
						1, 1,
						0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						0, 0
					)
				);
				//
				this.connector_array3[y][x] = new JTextField(" ");
				panel.add(
					this.connector_array3[y][x],
					new GridBagConstraints(
						x0+factor*x+factor-1, y0+factor*y+1,
						1, 1,
						0.0, 0.5,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						10, 0
					)
				);
				this.connector_array4[y][x] = new JTextField(" ");
				panel.add(
					this.connector_array4[y][x],
					new GridBagConstraints(
						x0+factor*x+factor-1, y0+factor*y+2,
						1, 1,
						0.0, 0.5,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						insets,
						10, 0
					)
				);
				connect(columns, rows, x, y);
//				if ((x > 0) && (y > 0)) {
//					
//				}
			}			
		}
		this.toggle_array[0][0].setSelected(true);
	}
	
	public ArrayView(ProcessorArray pa, Interpreter i, Compiler c, System s, Dictionary d)
	{
		processor_array = pa;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(640,480);
		
		this.run = new JButton("Run");
		this.go = new JButton("Go");
		this.trace = new JButton("Trace");
		this.step = new JButton("Step");
		this.stop = new JButton("Stop");

		this.toolbar = new JToolBar();
		this.toolbar.setFloatable(false);
		this.toolbar.add(this.run);
		this.toolbar.add(this.trace);
		this.toolbar.add(this.step);
		this.toolbar.add(this.stop);
		this.toolbar.add(this.go);
		this.main_panel = new JPanel( new GridBagLayout() );
		this.main_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.scroll = new JScrollPane(this.main_panel);
		this.main_split_pane.setTopComponent(this.toolbar);
		this.main_split_pane.setBottomComponent(this.scroll);

		addArray(this.main_panel, 0, 0);

		this.stop.setEnabled(false);
		this.run.addActionListener(this);
		this.go.addActionListener(this);
		this.trace.addActionListener(this);
		this.step.addActionListener(this);
		this.stop.addActionListener(this);
		this.add(this.main_split_pane);

		this.interpreter = i;
		this.view = new View(pa.getProcessor(0, 0), i, c, s, d);
		
		setVisible(true);
	}
	
	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		if (this.updating) {return;}
		
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
		if (source == this.go) {
			this.go();
		}
		else {
			int rows = this.processor_array.getRows();
			int columns = this.processor_array.getColumns();
			for (int y=0; y<rows; ++y) {
				for (int x=0; x<columns; ++x) {
					if (this.toggle_array[y][x] == source) {
						this.updating = true;
						if ((this.selected_x == x) && (this.selected_y == y)) {
							// current deselected
							this.toggle_array[y][x].setSelected(true);
						}
						else {
							this.toggle_array[this.selected_y][this.selected_x].setSelected(false);
							this.view.setProcessor(this.processor_array.getProcessor(x, y));
							this.selected_x = x;
							this.selected_y = y;
						}
						this.updating = false;
					}
				}
			}
		}
	}

	public boolean step()
	{
		boolean res = true;
		int x = 0, y = 0;
		int rows = this.processor_array.getRows();
		int columns = this.processor_array.getColumns();
		for (y=0; y<rows; ++y) {
			for (x=0; x<columns; ++x) {
				try {
					this.processor_array.getProcessor(x, y).step();
				}
				catch (java.lang.Exception ex) {
					res = false;
				}
			}
		}
		return res;
	}

	public void start()
	{
		if (!this.running) {
			this.running = true;
			this.free_running = false;
			this.tracing = false;
			this.step.setEnabled(false);
			this.run.setEnabled(false);
			this.go.setEnabled(false);
			this.trace.setEnabled(false);
			this.stop.setEnabled(true);
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public void go()
	{
		if (!this.running) {
			this.running = true;
			this.free_running = true;
			this.processor_array.start();
			this.tracing = false;
			this.step.setEnabled(false);
			this.run.setEnabled(false);
			this.go.setEnabled(false);
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
			this.go.setEnabled(false);
			this.trace.setEnabled(false);
			this.stop.setEnabled(true);
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public void stop()
	{
		this.running = false;
		if (this.free_running) {
			this.processor_array.stop();
			this.free_running = false;
		}
		this.step.setEnabled(true);
		this.run.setEnabled(true);
		this.go.setEnabled(true);
		this.trace.setEnabled(true);
		this.stop.setEnabled(false);
	}

	public void update()
	{
		view.update();
		int x = 0, y = 0;
		int rows = this.processor_array.getRows();
		int columns = this.processor_array.getColumns();
		for (y=0; y<rows; ++y) {
			for (x=0; x<columns; ++x) {
				if (processor_array.getProcessor(x, y).hasFailed()) {
					this.toggle_array[y][x].setBackground(Color.RED);
				}
				else {
					this.toggle_array[y][x].setBackground(null);
				}
			}
		}
	}

	@Override
	public void run()
	{
		int cnt = 0;
		while (this.running) {
			if (this.free_running) {
				this.update();
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e) {
				}
			}
			else {
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
					}
				}
			}
		}
	}
	

	
}
