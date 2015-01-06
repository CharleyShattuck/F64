package com.F64.view;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class System extends JFrame implements ActionListener {
	private com.F64.System				system;
	private JToggleButton[]				toggle_list;
	private JSplitPane 					split;
	private JPanel						area;
	private JPanel						memory;
	private JScrollPane 				scroll;
	private int							selected_area;
	private volatile boolean			updating;
	private final int COLUMNS = 4;

	public System(com.F64.System s)
	{
		system = s;
		this.setSize(700,600);
		this.setTitle("System View");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font(Font.MONOSPACED, Font.BOLD , 12);

		area = new JPanel( new GridBagLayout() );
		memory = new JPanel( new GridBagLayout() );
		scroll = new JScrollPane(area);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		Insets label_insets = new Insets( 0, 10, 0, 4);
		Insets field_insets = new Insets( 0, 0, 0, 0);

		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroll, memory);
	
		int no_of_areas = (int)(system.getMemorySize() / 256);
		toggle_list = new JToggleButton[no_of_areas];
		int no_of_area_lines = no_of_areas / COLUMNS;
		int x0 = 0;
		int y0 = 0;
		int y = 0;
		int x = 0;
		for (int i=0; i<no_of_areas; ++i) {
			toggle_list[i] = new JToggleButton(String.format("%04xxx", i));
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
					field_insets,
					0, 0
				)
			);

			
			if (++x >= COLUMNS) {
				x -= COLUMNS;
				++y;
			}
		}
		this.toggle_list[0].setSelected(true);

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
				}
				this.updating = false;
				return;
			}
		}
	}

	public void update()
	{
		
	}

}
