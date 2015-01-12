package com.F64.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class System extends JFrame implements ActionListener {
	private com.F64.System				system;
	private JToggleButton[]				toggle_list;
	private JTextField[]				cells;
	private JSplitPane 					split;
	private JPanel						area;
	private JPanel						memory;
	private JScrollPane 				scroll;
	private int							selected_area;
	private boolean						updating;
	private final int BUTTON_COLUMNS = 8;
//	private final int MEMORY_COLUMNS = 16;
	private final int MEMORY_SIZE = 16;
//	private final int MEMORY_DIGITS = MEMORY_SIZE / 16;

	public System(com.F64.System s)
	{
		JLabel label;
		system = s;
		this.setSize(800,500);
		this.setPreferredSize(new Dimension(700, 800));
		this.setTitle("Memory View");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);

		area = new JPanel( new GridBagLayout() );
		memory = new JPanel( new GridBagLayout() );
		scroll = new JScrollPane(area);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		Insets label_insets = new Insets( 0, 0, 0, 0);
		Insets field_insets = new Insets( 0, 0, 0, 0);
		Insets button_insets = new Insets( 0, 0, 0, 0);

		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, memory);
		split.setDividerLocation(580);
	
		int no_of_areas = (int)(system.getMemorySize() / MEMORY_SIZE);
		toggle_list = new JToggleButton[no_of_areas];
//		int no_of_area_lines = no_of_areas / BUTTON_COLUMNS;
		int x0 = 0;
		int y0 = 0;
		int y = 0;
		int x = 0;
		for (int i=0; i<no_of_areas; ++i) {
			toggle_list[i] = new JToggleButton(String.format("%04xx", i));
			toggle_list[i].setFont(font);
			toggle_list[i].addActionListener(this);

			this.area.add(
				toggle_list[i],
				new GridBagConstraints(
					x0+x, y0+y,
					1, 1,
					0.0, 0.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					button_insets,
					0, 0
				)
			);

			if (++x >= BUTTON_COLUMNS) {
				x -= BUTTON_COLUMNS;
				++y;
			}
		}
		this.toggle_list[0].setSelected(true);
		this.cells = new JTextField[MEMORY_SIZE];
		y = y0;
		x = x0;
//		for (int i=0; i<MEMORY_SIZE; ++i) {
//			label = new JLabel(String.format("%01x", i));
//			label.setFont(font);
//			label.setHorizontalAlignment(JTextField.CENTER);
//			this.memory.add(
//				label,
//				new GridBagConstraints(
//					x+i + ((i<8) ? 1 : 2), y,
//					1, 1,
//					0.0, 0.0,
//					GridBagConstraints.WEST,
//					GridBagConstraints.BOTH,
//					label_insets,
//					0, 0
//				)
//			);
//
//		}
//		label = new JLabel("  ");
//		label.setFont(font);
//		label.setHorizontalAlignment(JTextField.CENTER);
//		this.memory.add(
//			label,
//			new GridBagConstraints(
//				x+9, y,
//				1, 1,
//				0.0, 0.0,
//				GridBagConstraints.WEST,
//				GridBagConstraints.BOTH,
//				label_insets,
//				0, 0
//			)
//		);
		y = 0;
		x = 0;
		for (int i=0; i<MEMORY_SIZE; ++i) {
			if (x == 0) {
				label = new JLabel(String.format(" %01x ", y));
				label.setFont(font);
				label.setHorizontalAlignment(JTextField.RIGHT);
				this.memory.add(
					label,
					new GridBagConstraints(
						x0+x, y0+y+1,
						1, 1,
						0.0, 0.0,
						GridBagConstraints.WEST,
						GridBagConstraints.BOTH,
						label_insets,
						0, 0
					)
				);
			}
			cells[i] = new JTextField(20);
			cells[i].setFont(font);
			cells[i].setHorizontalAlignment(JTextField.CENTER);
			cells[i].addActionListener(this);
			this.memory.add(
				cells[i],
				new GridBagConstraints(
					x0+1, y0+y+1,
					1, 1,
					1.0, 1.0,
					GridBagConstraints.WEST,
					GridBagConstraints.BOTH,
					field_insets,
					0, 0
				)
			);
			++y;
		}
		this.add(this.split);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent ev)
	{
		if (this.updating) {return;}
		Object source = ev.getSource();
		for (int i=0; i<toggle_list.length; ++i) {
			if (toggle_list[i] == source) {
				this.updating = true;
				if (i == this.selected_area) {
					// deselect current
					this.toggle_list[i].setSelected(true);
				}
				else {
					this.toggle_list[this.selected_area].setSelected(false);
					this.selected_area = i;
					this.update();
				}
				this.updating = false;
				return;
			}
		}
		for (int i=0; i<cells.length; ++i) {
			if (cells[i] == source) {
				long adr = selected_area;
				adr *= MEMORY_SIZE;
				++adr;
				long data = this.system.getMemory(adr);
				try {
					String txt = ev.getActionCommand();
					data = Long.parseLong(txt.replaceAll(" ", ""), 16);
					this.system.setMemory(adr, data);
				}
				catch (Exception ex) {}
				this.updating = true;
				this.cells[i].setText(Processor.convertLongToString(data));
				this.updating = false;
				return;
			}
		}
	}

	public void update()
	{
		long adr = selected_area;
		adr *= MEMORY_SIZE;
		for (int i=0; i<MEMORY_SIZE; ++i) {
			this.cells[i].setText(Processor.convertLongToString(this.system.getMemory(adr++)));
		}
	}

}
