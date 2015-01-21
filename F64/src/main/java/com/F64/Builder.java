package com.F64;

public class Builder {

	public static boolean fit(int slot, int slot0)
	{
		if (slot < Processor.FINAL_SLOT) {return true;}
		if (slot == Processor.FINAL_SLOT) {return slot0 < Processor.FINAL_SLOT_SIZE;}
		return false;
	}

	public static boolean fit(int slot, int slot0, int slot1)
	{
		if (slot < (Processor.FINAL_SLOT-1)) {return true;}
		if (slot == (Processor.FINAL_SLOT-1)) {return slot1 < Processor.FINAL_SLOT_SIZE;}
		if (slot == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0);
		}
		return false;
	}

	public static boolean fit(int slot, int slot0, int slot1, int slot2)
	{
		if (slot < (Processor.FINAL_SLOT-2)) {return true;}
		if (slot == (Processor.FINAL_SLOT-2)) {return slot2 < Processor.FINAL_SLOT_SIZE;}
		if (slot == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0);
		}
		if (slot == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0);
		}
		return false;
	}

	public static boolean fit(int slot, int slot0, int slot1, int slot2, int slot3)
	{
		if (slot < (Processor.FINAL_SLOT-3)) {return true;}
		if (slot == (Processor.FINAL_SLOT-3)) {return slot3 < Processor.FINAL_SLOT_SIZE;}
		if (slot == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0);
		}
		if (slot == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0);
		}
		if (slot == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0);
		}
		return false;
	}

	public static boolean fit(int slot, int slot0, int slot1, int slot2, int slot3, int slot4)
	{
		if (slot < (Processor.FINAL_SLOT-4)) {return true;}
		if (slot == (Processor.FINAL_SLOT-4)) {return slot4 < Processor.FINAL_SLOT_SIZE;}
		if (slot == Processor.FINAL_SLOT-3) {
			return (slot3 < Processor.FINAL_SLOT_SIZE) && (slot4 == 0);
		}
		if (slot == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0) && (slot4 == 0);
		}
		if (slot == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0);
		}
		if (slot == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0);
		}
		return false;
	}

	public static boolean fit(int slot, int slot0, int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		if (slot < (Processor.FINAL_SLOT-5)) {return true;}
		if (slot == (Processor.FINAL_SLOT-5)) {return slot5 < Processor.FINAL_SLOT_SIZE;}
		if (slot == Processor.FINAL_SLOT-4) {
			return (slot4 < Processor.FINAL_SLOT_SIZE) && (slot5 == 0);
		}
		if (slot == Processor.FINAL_SLOT-3) {
			return (slot3 < Processor.FINAL_SLOT_SIZE) && (slot4 == 0) && (slot5 == 0);
		}
		if (slot == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		if (slot == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		if (slot == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		return false;
	}

	public static int getNoOfDifferentBits(long value1, long value2)
	{
		int diff = 0;
		long mask = -1;
		while ((value1 & mask) != (value2 & mask)) {
			++diff;
			mask <<= 1;
		}
		return diff;
	}
	
	public static int getRemainingBits(int pos)
	{
		int res = 0;
		while (pos < Processor.FINAL_SLOT) {
			res += Processor.SLOT_BITS;
			++pos;
		}
		if (pos == Processor.FINAL_SLOT) {
			res += Processor.FINAL_SLOT_BITS;
		}
		return res;
	}
	
	public static long getAddressMask(int slot)
	{
		if (slot > Processor.FINAL_SLOT) {return 0;}
		else if (slot == Processor.FINAL_SLOT) {return Processor.FINAL_SLOT_MASK;}
		long mask = -1L >>> (Processor.SLOT_BITS - Processor.FINAL_SLOT_BITS);
		while (slot > 0) {
			--slot;
			mask = mask >>> Processor.SLOT_BITS;
		}
		return mask;
	}

	
	private System				system;
	private long				start_position;
	private int					start_slot;
	private	long				current_pos;
	private	long				current_cell;
	private	long[]				additional_cells;
	private int					current_slot;
	private int					addtional_cnt;
	private boolean				generate;
	private boolean				call_generated;

	public Builder(System value)
	{
		system = value;
		additional_cells = new long[Processor.NO_OF_SLOTS];
	}

	public boolean exceed1Cell()
	{
		return (addtional_cnt > 0) || (start_position != current_pos);
	}
	
	public void start(boolean generate)
	{
		current_pos = start_position = system.getCodePosition();
		addtional_cnt = current_slot = start_slot = 0;
		current_cell = 0;
		call_generated = false;
		this.generate = generate;
	}
	
	/**
	 * 
	 * @return true if generated code fits into a single cell and can be inlined
	 */
	public boolean stop()
	{
		boolean res = (current_pos == start_position) && (addtional_cnt == 0) && !call_generated;
		flush();
		return res;
	}

	public void flush()
	{
		if (current_slot > 0) {
			if (generate) {
				if (current_slot < Processor.FINAL_SLOT) {
					// add a skip instruction if cell is not full
					current_cell = Processor.writeSlot(current_cell, current_slot++, ISA.USKIP.ordinal());
				}
				if ((current_pos == start_position) && (start_slot > 0)) {
					system.compileCode(current_cell | system.getMemory(current_pos));
				}
				else {
					system.compileCode(current_cell);
				}
				for (int i=0; i<addtional_cnt; ++i) {
					system.compileCode(additional_cells[i]);
				}
			}
			current_pos += addtional_cnt+1;
			current_cell = 0;
			current_slot = 0;
			addtional_cnt = 0;
		}
	}
	
	public void add(ISA op)
	{
		if (!fit(current_slot, op.ordinal())) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
	}
	
	public void add(ISA op, int slot0)
	{
		if (!fit(current_slot, op.ordinal(), slot0)) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot0);
	}

	public void add(Ext1 op) {add(ISA.EXT1, op.ordinal());}
	public void add(Ext2 op) {add(ISA.EXT2, op.ordinal());}
	public void add(Ext3 op) {add(ISA.EXT3, op.ordinal());}
	public void add(Ext4 op) {add(ISA.EXT4, op.ordinal());}
	public void add(Ext5 op) {add(ISA.EXT5, op.ordinal());}

	public void add(ISA op, int slot0, int slot1)
	{
		if (!fit(current_slot, op.ordinal(), slot0, slot1)) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot0);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot1);
	}

	public void add(Ext1 op, int slot0) {add(ISA.EXT1, op.ordinal(), slot0);}
	public void add(Ext2 op, int slot0) {add(ISA.EXT2, op.ordinal(), slot0);}
	public void add(Ext3 op, int slot0) {add(ISA.EXT3, op.ordinal(), slot0);}
	public void add(Ext4 op, int slot0) {add(ISA.EXT4, op.ordinal(), slot0);}
	public void add(Ext5 op, int slot0) {add(ISA.EXT5, op.ordinal(), slot0);}
	public void add(RegOp1 op, int slot0) {add(ISA.REGOP1, op.ordinal(), slot0);}

	public void add(ISA op, int slot0, int slot1, int slot2)
	{
		if (!fit(current_slot, op.ordinal(), slot0, slot1, slot2)) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot0);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot1);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot2);
	}

	public void add(Ext1 op, int slot0, int slot1) {add(ISA.EXT1, op.ordinal(), slot0, slot1);}
	public void add(Ext2 op, int slot0, int slot1) {add(ISA.EXT2, op.ordinal(), slot0, slot1);}
	public void add(Ext3 op, int slot0, int slot1) {add(ISA.EXT3, op.ordinal(), slot0, slot1);}
	public void add(Ext4 op, int slot0, int slot1) {add(ISA.EXT4, op.ordinal(), slot0, slot1);}
	public void add(Ext5 op, int slot0, int slot1) {add(ISA.EXT5, op.ordinal(), slot0, slot1);}
	public void add(RegOp2 op, int slot0, int slot1) {add(ISA.REGOP2, op.ordinal(), slot0, slot1);}

	public void add(ISA op, int slot0, int slot1, int slot2, int slot3)
	{
		if (!fit(current_slot, op.ordinal(), slot0, slot1, slot2, slot3)) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot0);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot1);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot2);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot3);
	}

	public void add(Ext1 op, int slot0, int slot1, int slot2) {add(ISA.EXT1, op.ordinal(), slot0, slot1, slot2);}
	public void add(Ext2 op, int slot0, int slot1, int slot2) {add(ISA.EXT2, op.ordinal(), slot0, slot1, slot2);}
	public void add(Ext3 op, int slot0, int slot1, int slot2) {add(ISA.EXT3, op.ordinal(), slot0, slot1, slot2);}
	public void add(Ext4 op, int slot0, int slot1, int slot2) {add(ISA.EXT4, op.ordinal(), slot0, slot1, slot2);}
	public void add(Ext5 op, int slot0, int slot1, int slot2) {add(ISA.EXT5, op.ordinal(), slot0, slot1, slot2);}
	public void add(RegOp3 op, int slot0, int slot1, int slot2) {add(ISA.REGOP3, op.ordinal(), slot0, slot1, slot2);}

	public void add(ISA op, int slot0, int slot1, int slot2, int slot3, int slot4)
	{
		if (!fit(current_slot, op.ordinal(), slot0, slot1, slot2, slot3, slot4)) {flush();}
		current_cell = Processor.writeSlot(current_cell, current_slot++, op.ordinal());
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot0);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot1);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot2);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot3);
		current_cell = Processor.writeSlot(current_cell, current_slot++, slot4);
	}

	public void addCall(long dest_adr, boolean is_jump)
	{
		int instr_slots = is_jump ? 2 : 1;
		long curr_adr = current_pos + addtional_cnt + instr_slots;
		int different_bits = getNoOfDifferentBits(dest_adr, curr_adr);
		int remaining_bits = getRemainingBits(current_slot+instr_slots);
		if (different_bits > remaining_bits) {
			flush();
			curr_adr = current_pos + instr_slots;
			different_bits = getNoOfDifferentBits(dest_adr, curr_adr);
			remaining_bits = getRemainingBits(current_slot+instr_slots);
		}
		long mask = getAddressMask(current_slot+instr_slots);
		if (is_jump) {
			current_cell = Processor.writeSlot(current_cell, current_slot++, ISA.EXIT.ordinal());
		}
		current_cell = Processor.writeSlot(current_cell, current_slot++, ISA.CALL.ordinal());
		current_cell |= dest_adr & mask;
		current_slot = Processor.NO_OF_SLOTS;
		flush();
		call_generated = true;
	}

	public void addLiteral(long value)
	{
		if (value >= 0) {
			// positive
			if (value < Processor.SLOT_SIZE) {
				add(ISA.LIT, (int)value);
				return;
			}
			if (value < Processor.SLOT_SIZE * Processor.SLOT_SIZE) {
				if (fit(current_slot, ISA.LIT.ordinal(), (int)(value >> Processor.SLOT_BITS), ISA.EXT.ordinal(), (int)(value & Processor.SLOT_MASK))) {
					add(ISA.LIT, (int)(value >> Processor.SLOT_BITS));
					add(ISA.EXT, (int)(value & Processor.SLOT_MASK));
					return;
				}
			}
		}
		else {
			// negative
			long abs = ~value;
			if (abs < Processor.SLOT_SIZE) {
				add(ISA.NLIT, (int)abs);
				return;
			}
			if (abs < Processor.SLOT_SIZE * Processor.SLOT_SIZE) {
				if (fit(current_slot, ISA.NLIT.ordinal(), (int)(abs >> Processor.SLOT_BITS), ISA.EXT.ordinal(), (int)(value & Processor.SLOT_MASK))) {
					add(ISA.NLIT, (int)(abs >> Processor.SLOT_BITS));
					add(ISA.EXT, (int)(value & Processor.SLOT_MASK));
					return;
				}
			}
		}
		if (!fit(current_slot, ISA.FETCHPINC.ordinal())) {flush();}
		add(ISA.FETCHPINC);
		additional_cells[addtional_cnt++] = value;
	}
	
}
