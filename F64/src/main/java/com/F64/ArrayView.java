package com.F64;

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
	private ProcessorArray		processor_array;
	private JToggleButton[][]	toggle_array;
	private JTextField[][]		connector_array1;
	private JTextField[][]		connector_array2;
	private JTextField[][]		connector_array3;
	private JTextField[][]		connector_array4;
	private JSplitPane			main_split_pane;
	private JToolBar			toolbar;
	private JButton				run;
	private JButton				trace;
	private JButton				step;
	private JButton				stop;
	private JScrollPane 		scroll;
	private JPanel				main_panel;

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
				this.toggle_array[y][x] = new JToggleButton(x+"."+y);
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
	
	public ArrayView(ProcessorArray pa, Compiler c, System s, Dictionary d)
	{
		processor_array = pa;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1000,600);
		
		this.run = new JButton("Run");
		this.trace = new JButton("Trace");
		this.step = new JButton("Step");
		this.stop = new JButton("Stop");

		this.toolbar = new JToolBar();
		this.toolbar.setFloatable(false);
		this.toolbar.add(this.run);
		this.toolbar.add(this.trace);
		this.toolbar.add(this.step);
		this.toolbar.add(this.stop);
		this.main_panel = new JPanel( new GridBagLayout() );
		this.main_split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		this.scroll = new JScrollPane(this.main_panel);
		this.main_split_pane.setTopComponent(this.toolbar);
		this.main_split_pane.setBottomComponent(this.scroll);

		addArray(this.main_panel, 0, 0);

		this.stop.setEnabled(false);
		this.run.addActionListener(this);
		this.trace.addActionListener(this);
		this.step.addActionListener(this);
		this.stop.addActionListener(this);
		this.add(this.main_split_pane);

		
		setVisible(true);
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
