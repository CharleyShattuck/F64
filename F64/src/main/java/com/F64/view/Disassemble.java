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
public class Disassemble extends JFrame implements ActionListener {
	private com.F64.System				system;
	private JToggleButton[]				toggle_list;
	private JTextField[]				cells;
	private JLabel[]					texts;
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

	public Disassemble(com.F64.System s)
	{
		JLabel label;
		system = s;
		this.setSize(1300,500);
		this.setPreferredSize(new Dimension(1300, 800));
		this.setTitle("Disassemble View");
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
		this.texts = new JLabel[MEMORY_SIZE];
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
			texts[i] = new JLabel();
			texts[i].setText("                                                  ");
			texts[i].setFont(font);
			texts[i].setHorizontalAlignment(JTextField.LEFT);
			this.memory.add(
					texts[i],
					new GridBagConstraints(
						x0+2, y0+y+1,
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

//	public String disassembleBranch(long cell, int slot, int additional) {
//		
//	}
	
	
	/**
	 *
	 * @param cell
	 * @return {@link #action(java.awt.Event, Object)}of additional cells
	 */
	public int disassemble(long cell, JLabel text)
	{
		int additional = 0;
		int slot = 0;
		int size;
		int value, value2;
		long target;
		com.F64.ISA opcode;
		com.F64.Ext1 ext1;
		com.F64.Ext2 ext2;
		com.F64.Ext3 ext3;
		com.F64.Ext4 ext4;
		com.F64.RegOp1 rop1;
		com.F64.RegOp2 rop2;
		com.F64.RegOp3 rop3;
		String txt = "";
		while (slot < com.F64.Processor.NO_OF_SLOTS) {
			if (slot > 0) {
				txt = txt + " , ";
			}
			value = com.F64.Processor.readSlot(cell, slot++);
			opcode = com.F64.ISA.values()[value];
			size = opcode.size();
			switch (opcode) {
			case CALL:
			case CALLM:
				txt = txt + opcode.getDisplay()
					+ " " + Processor.convertRemainingToString(cell, slot)
				;
				slot = com.F64.Processor.NO_OF_SLOTS;
				size = 0;
				break;

			case MOV:
			case SWAP:
				txt = txt + opcode.getDisplay()
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
				;
				size = 0;
				break;

			case NLIT:
				txt = txt + opcode.getDisplay()
					+ " #-" + Processor.convertSlotToString(com.F64.Processor.readSlot(cell, slot++)+1)
				;
				size = 0;
				break;

			case RFETCH:
			case RSTORE:
			case INC:
			case DEC:
				txt = txt + opcode.getDisplay()
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
				;
				size = 0;
				break;

			case LFETCH:
			case LSTORE:
				txt = txt + opcode.getDisplay()
				+ " " + com.F64.Local.getDisplay(com.F64.Processor.readSlot(cell, slot++))
			;
			size = 0;
			break;

			case BRANCH:
				int bc = com.F64.Processor.readSlot(cell, slot++);
				com.F64.Condition cond = com.F64.Condition.values()[(bc >> 4) & 3];
				com.F64.Branch br = com.F64.Branch.values()[bc & 0x0f];
				switch (br) {
//				case REM:
//					txt = txt + opcode.getDisplay()
//						+ " " + cond.getDisplay()
//						+ " " + br.getDisplay()
//						+ " " + Processor.convertRemainingToString(cell, slot)
//					;
//					slot = com.F64.Processor.NO_OF_SLOTS;
//					break;
				case IO:
					target = com.F64.Processor.readSlot(cell, slot++);
					txt = txt + opcode.getDisplay()
						+ " " + cond.getDisplay()
						+ " " + br.getDisplay()
						+ " @" + Processor.convertSlotToString((int) target)
					;
					break;

				case FORWARD:
					target = com.F64.Processor.readSlot(cell, slot++);
					txt = txt + opcode.getDisplay()
						+ " " + cond.getDisplay()
						+ " " + br.getDisplay()
						+ " +" + Processor.convertSlotToString((int) target)
					;
					break;

				case BACK:
					target = com.F64.Processor.readSlot(cell, slot++);
					txt = txt + opcode.getDisplay()
						+ " " + cond.getDisplay()
						+ " " + br.getDisplay()
						+ " -" + Processor.convertSlotToString((int) target)
					;
					break;

				case LONG:
					++additional;
				default:
					txt = txt + opcode.getDisplay()
						+ " " + cond.getDisplay()
						+ " " + br.getDisplay()
					;
					break;
				
				}
				break;

			case REGOP1:
				rop1 = com.F64.RegOp1.values()[com.F64.Processor.readSlot(cell, slot++)];
				txt =
					txt + rop1.getDisplay()
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
					;
				size = 0;
				break;

			case REGOP2:
				rop2 = com.F64.RegOp2.values()[com.F64.Processor.readSlot(cell, slot++)];
				txt =
					txt + rop2.getDisplay()
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
					+ (rop2.isImmediate()
						? (" #" + Processor.convertSlotToString(com.F64.Processor.readSlot(cell, slot++)))
						: (" " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++)))
					)
					;
				size = 0;
				break;

			case REGOP3:
				rop3 = com.F64.RegOp3.values()[com.F64.Processor.readSlot(cell, slot++)];
				txt =
					txt + rop3.getDisplay()
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
					+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
					+ (rop3.isImmediate()
						? (" #" + Processor.convertSlotToString(com.F64.Processor.readSlot(cell, slot++)))
						: (" " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++)))
					)
					;
				size = 0;
				break;
			
			case EXT1:
				ext1 = com.F64.Ext1.values()[com.F64.Processor.readSlot(cell, slot++)];
				size = ext1.size()-1;
				switch (ext1) {
				case LJMP:
					++additional;
					txt = txt + ext1.getDisplay();
					break;

//				case SWAP0:
//					txt = txt + ext1.getDisplay()
//						+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
//						+ " " + com.F64.Register.getDisplay(com.F64.Processor.readSlot(cell, slot++))
//					;
//					break;

				case EXITI:
					value = com.F64.Processor.readSlot(cell, slot++);
					txt = txt + ext1.getDisplay()
						+ " #" + Processor.convertSlotToString(value)
					;
					size = 0;
					break;

				default:
					txt = txt + ext1.getDisplay();
					break;
				
				}
				break;

			case EXT2: // all instruction here have size 3
				ext2 = com.F64.Ext2.values()[com.F64.Processor.readSlot(cell, slot++)];
				value = com.F64.Processor.readSlot(cell, slot++);
				size = 0;
				switch (ext2) {
				case EQ0Q:
//				case FETCHINC:
//				case FETCHR:
				case GE0Q:
				case GT0Q:
				case LE0Q:
				case LT0Q:
				case NE0Q:
				case POPR:
				case PUSHR:
				case RCL:
				case RCR:
				case ROL:
				case ROR:
//				case STOREINC:
//				case STORER:
					txt = txt + ext2.getDisplay()
						+ " " + com.F64.Register.getDisplay(value)
					;
					break;

//				case FETCHS:
				case POPS:
				case PUSHS:
				case SFETCH:
				case SSTORE:
//				case STORES:
					txt = txt + ext2.getDisplay()
						+ " " + com.F64.SystemRegister.getDisplay(value)
					;
					break;

//				case FETCHL:
				case POPL:
				case PUSHL:
//				case STOREL:
					txt = txt + ext2.getDisplay()
						+ " " + com.F64.Local.getDisplay(value)
					;
					break;

				case CFLAG:
				case SFLAG:
					txt = txt + ext2.getDisplay()
						+ " " + com.F64.Flag.getDisplay(value)
					;
					break;

				case JMPIO:
				case FETCHPORT:
				case FETCHPORTWAIT:
					txt = txt + ext2.getDisplay()
						+ " " + com.F64.Port.getDisplayMask(value)
					;
					break;

				case BLIT:
				case CBIT:
				case CONFIGFETCH:
				case RBIT:
				case RCLI:
				case RCRI:
				case ROLI:
				case RORI:
				case SBIT:
				case STOREPORT:
				case STOREPORTWAIT:
				case TBIT:
				case WBIT:
				default:
					txt = txt + ext2.getDisplay()
						+ " #" + Processor.convertSlotToString(value)
					;
					break;
				
				}
				break;

			case EXT3: // all instruction here have size 4
				ext3 = com.F64.Ext3.values()[com.F64.Processor.readSlot(cell, slot++)];
				value = com.F64.Processor.readSlot(cell, slot++);
				value2 = com.F64.Processor.readSlot(cell, slot++);
				size = 0;
				switch (ext3) {
				case SWAPRS:
				case MOVRS:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Register.getDisplay(value)
					+ " " + com.F64.SystemRegister.getDisplay(value2)
				;
				break;

				case MOVSR:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.SystemRegister.getDisplay(value)
					+ " " + com.F64.Register.getDisplay(value2)
				;
				break;

				case LSAVE:
				case LRESTORE:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Local.getDisplay(value)
					+ " " + com.F64.Local.getDisplay(value2)
				;
				break;

				case SWAPRL:
				case MOVRL:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Register.getDisplay(value)
					+ " " + com.F64.Local.getDisplay(value2)
				;
				break;

				case MOVLR:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Local.getDisplay(value)
					+ " " + com.F64.Register.getDisplay(value2)
				;
				break;

				case MOVRI:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Register.getDisplay(value)
					+ " #" + Processor.convertSlotToString(value2)
				;

				case MOVSI:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.SystemRegister.getDisplay(value)
					+ " #" + Processor.convertSlotToString(value2)
				;

				case MOVLI:
					txt = txt + ext3.getDisplay()
					+ " " + com.F64.Local.getDisplay(value)
					+ " #" + Processor.convertSlotToString(value2)
				;

				case BITFF1:
				case BITFL1:
				case EQ0Q:
				case GE0Q:
				case GT0Q:
				case LE0Q:
				case LT0Q:
				case NE0Q:
				case RRBIT:
				case RRCBIT:
				case RRRBIT:
				case RRSBIT:
				case RRTBIT:
				case RRWBIT:
					txt = txt + ext3.getDisplay()
						+ " " + com.F64.Register.getDisplay(value)
						+ " " + com.F64.Register.getDisplay(value2)
					;
					break;

				case RCBIT:
				case RCL:
				case RCR:
				case ROL:
				case ROR:
				case RSBIT:
				case RTBIT:
				case RWBIT:
				default:
					txt = txt + ext3.getDisplay()
						+ " " + com.F64.Register.getDisplay(value)
						+ " #" + Processor.convertSlotToString(value2)
					;
					break;				
				}
				break;

			case EXT4: // all instruction here have size 3
				ext4 = com.F64.Ext4.values()[com.F64.Processor.readSlot(cell, slot++)];
				value = com.F64.Processor.readSlot(cell, slot++);
				size = 0;
				switch (ext4) {
				case LFETCH:
				case LFETCHI:
				case LFETCHPED:
				case LFETCHPEI:
				case LFETCHPOD:
				case LFETCHPOI:
				case LSTORE:
				case LSTOREI:
				case LSTOREPED:
				case LSTOREPEI:
				case LSTOREPOD:
				case LSTOREPOI:
					txt = txt + ext4.getDisplay()
						+ " " + com.F64.Local.getDisplay(value)
					;
					break;

				case RFETCH:
				case RFETCHI:
				case RFETCHPED:
				case RFETCHPEI:
				case RFETCHPOD:
				case RFETCHPOI:
				case RSTORE:
				case RSTOREI:
				case RSTOREPED:
				case RSTOREPEI:
				case RSTOREPOD:
				case RSTOREPOI:
					txt = txt + ext4.getDisplay()
						+ " " + com.F64.Register.getDisplay(value)
					;
					break;

				case SFETCH:
				case SFETCHI:
				case SFETCHPED:
				case SFETCHPEI:
				case SFETCHPOD:
				case SFETCHPOI:
				case SSTORE:
				case SSTOREI:
				case SSTOREPED:
				case SSTOREPEI:
				case SSTOREPOD:
				case SSTOREPOI:
					txt = txt + ext4.getDisplay()
						+ " " + com.F64.SystemRegister.getDisplay(value)
					;
					break;
				}
				break;

			default:
				txt = txt + opcode.getDisplay();
			}
			while (size > 1) {
				value = com.F64.Processor.readSlot(cell, slot++);
				txt = txt + " #" + Processor.convertSlotToString(value);
				--size;
			}
		}
		text.setText(txt);
		return additional;
	}

	public void update()
	{
		long cell, adr = selected_area;
		adr *= MEMORY_SIZE;
		int additional = 0;
		for (int i=0; i<MEMORY_SIZE; ++i) {
			cell = this.system.getMemory(adr++);
			this.cells[i].setText(Processor.convertLongToString(cell));
			if (additional == 0) {
				additional = disassemble(cell, this.texts[i]);
			}
			else {
				this.texts[i].setText(this.cells[i].getText());
				--additional;
			}
		}
	}

	
}
