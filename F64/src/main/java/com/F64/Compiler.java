package com.F64;

//import java.io.IOException;

public class Compiler {
	private System		system;
	private Processor	processor;
	private	long		current_cell;
	private	long[]		additional_cell;
	private int			current_slot;
	private int			addtional_cnt;
	private Scope		scope;
	
	public Compiler(System system, Processor processor)
	{
		this.system = system;
		this.processor = processor;
		this.additional_cell = new long[Processor.NO_OF_SLOTS];
	}

	public System getSystem() {return system;}
	public Processor getProcessor() {return processor;}
	public boolean hasAdditionalCells() {return addtional_cnt > 0;}
	public Scope getScope() {return scope;}

	public static boolean fit(int no_slots, int slot0)
	{
		if (no_slots < Processor.FINAL_SLOT) {return true;}
		if (no_slots == Processor.FINAL_SLOT) {return slot0 < Processor.FINAL_SLOT_SIZE;}
		return false;
	}

	public static boolean fit(int no_slots, int slot0, int slot1)
	{
		if (no_slots < (Processor.FINAL_SLOT-1)) {return true;}
		if (no_slots == (Processor.FINAL_SLOT-1)) {return slot1 < Processor.FINAL_SLOT_SIZE;}
		if (no_slots == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0);
		}
		return false;
	}

	public static boolean fit(int no_slots, int slot0, int slot1, int slot2)
	{
		if (no_slots < (Processor.FINAL_SLOT-2)) {return true;}
		if (no_slots == (Processor.FINAL_SLOT-2)) {return slot2 < Processor.FINAL_SLOT_SIZE;}
		if (no_slots == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0);
		}
		return false;
	}

	public static boolean fit(int no_slots, int slot0, int slot1, int slot2, int slot3)
	{
		if (no_slots < (Processor.FINAL_SLOT-3)) {return true;}
		if (no_slots == (Processor.FINAL_SLOT-3)) {return slot3 < Processor.FINAL_SLOT_SIZE;}
		if (no_slots == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0);
		}
		return false;
	}

	public static boolean fit(int no_slots, int slot0, int slot1, int slot2, int slot3, int slot4)
	{
		if (no_slots < (Processor.FINAL_SLOT-4)) {return true;}
		if (no_slots == (Processor.FINAL_SLOT-4)) {return slot4 < Processor.FINAL_SLOT_SIZE;}
		if (no_slots == Processor.FINAL_SLOT-3) {
			return (slot3 < Processor.FINAL_SLOT_SIZE) && (slot4 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0) && (slot4 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0);
		}
		return false;
	}

	public static boolean fit(int no_slots, int slot0, int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		if (no_slots < (Processor.FINAL_SLOT-5)) {return true;}
		if (no_slots == (Processor.FINAL_SLOT-5)) {return slot5 < Processor.FINAL_SLOT_SIZE;}
		if (no_slots == Processor.FINAL_SLOT-4) {
			return (slot4 < Processor.FINAL_SLOT_SIZE) && (slot5 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-3) {
			return (slot3 < Processor.FINAL_SLOT_SIZE) && (slot4 == 0) && (slot5 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-2) {
			return (slot2 < Processor.FINAL_SLOT_SIZE) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT-1) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		if (no_slots == Processor.FINAL_SLOT) {
			return (slot0 < Processor.FINAL_SLOT_SIZE) && (slot1 == 0) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		return false;
	}

	public static long test(long data, int slot0)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0)) {
			no_slots += 1;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 1;
		}
		data <<= 4;
		return data | no_slots;
	}

	public static long test(long data, int slot0, int slot1)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0, slot1)) {
			no_slots += 2;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 2;
		}
		data <<= 4;
		return data | no_slots;
	}

	public static long test(long data, int slot0, int slot1, int slot2)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0, slot1, slot2)) {
			no_slots += 3;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 3;
		}
		data <<= 4;
		return data | no_slots;
	}

	public static long test(long data, int slot0, int slot1, int slot2, int slot3)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0, slot1, slot2, slot3)) {
			no_slots += 4;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 4;
		}
		data <<= 4;
		return data | no_slots;
	}

	public static long test(long data, int slot0, int slot1, int slot2, int slot3, int slot4)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0, slot1, slot2, slot3, slot4)) {
			no_slots += 5;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 5;
		}
		data <<= 4;
		return data | no_slots;
	}

	public static long test(long data, int slot0, int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		int no_slots = (int)(data & 0xf);
		data >>= 4;
		if (fit(no_slots, slot0, slot1, slot2, slot3, slot4, slot5)) {
			no_slots += 6;
			if (no_slots > Processor.FINAL_SLOT) {
				++data;
				no_slots = 0;
			}
		}
		else {
			++data;
			no_slots = 6;
		}
		data <<= 4;
		return data | no_slots;
	}

	private void addAdditional(long value)
	{
		this.additional_cell[this.addtional_cnt++] = value;
	}
	
	public void flush()
	{
		if (this.current_slot > 0) {
			this.system.compileCode(this.current_cell);
			for (int i=0; i<this.addtional_cnt; ++i) {
				this.system.compileCode(this.additional_cell[i]);
			}
			this.current_cell = 0;
			this.current_slot = 0;
			this.addtional_cnt = 0;
		}
	}
	
	public boolean doesFit(int slot1)
	{
		if (this.current_slot < Processor.FINAL_SLOT) {return true;}
		if (this.current_slot == Processor.FINAL_SLOT) {return slot1 < Processor.FINAL_SLOT_SIZE;}
		return false;
	}

	public boolean doesFit(int slot1, int slot2)
	{
		if (this.current_slot < (Processor.FINAL_SLOT-1)) {return true;}
		if (this.current_slot == (Processor.FINAL_SLOT-1)) {return slot2 < Processor.FINAL_SLOT_SIZE;}
		if (this.current_slot == Processor.FINAL_SLOT) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0);
		}
		return false;
	}

	public boolean doesFit(int slot1, int slot2, int slot3)
	{
		if (this.current_slot < (Processor.FINAL_SLOT-2)) {return true;}
		if (this.current_slot == (Processor.FINAL_SLOT-2)) {return slot3 < Processor.FINAL_SLOT_SIZE;}
		if (this.current_slot == Processor.FINAL_SLOT) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0);
		}
		return false;
	}

	public boolean doesFit(int slot1, int slot2, int slot3, int slot4)
	{
		if (this.current_slot < (Processor.FINAL_SLOT-3)) {return true;}
		if (this.current_slot == (Processor.FINAL_SLOT-3)) {return slot4 < Processor.FINAL_SLOT_SIZE;}
		if (this.current_slot == Processor.FINAL_SLOT) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0);
		}
		return false;
	}

	public boolean doesFit(int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		if (this.current_slot < (Processor.FINAL_SLOT-4)) {return true;}
		if (this.current_slot == (Processor.FINAL_SLOT-4)) {return slot5 < Processor.FINAL_SLOT_SIZE;}
		if (this.current_slot == Processor.FINAL_SLOT) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0);
		}
		return false;
	}

	public boolean doesFit(int slot1, int slot2, int slot3, int slot4, int slot5, int slot6)
	{
		if (this.current_slot < (Processor.FINAL_SLOT-5)) {return true;}
		if (this.current_slot == (Processor.FINAL_SLOT-5)) {return slot6 < Processor.FINAL_SLOT_SIZE;}
		if (this.current_slot == Processor.FINAL_SLOT) {
			return (slot1 < Processor.FINAL_SLOT_SIZE) && (slot2 == 0) && (slot3 == 0) && (slot4 == 0) && (slot5 == 0) && (slot6 == 0);
		}
		return false;
	}

	private void compile(int slot1)
	{
		if (!doesFit(slot1)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	private void compile(int slot1, int slot2)
	{
		if (!doesFit(slot1, slot2)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	private void compile(int slot1, int slot2, int slot3)
	{
		if (!doesFit(slot1, slot2, slot3)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	private void compile(int slot1, int slot2, int slot3, int slot4)
	{
		if (!doesFit(slot1, slot2, slot3, slot4)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	private void compile(int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		if (!doesFit(slot1, slot2, slot3, slot4, slot5)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot5);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	private void compile(int slot1, int slot2, int slot3, int slot4, int slot5, int slot6)
	{
		if (!doesFit(slot1, slot2, slot3, slot4, slot5, slot6)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot5);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot6);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compileLiteral(long value)
	{
		if ((value >= 0) && (value < Processor.SLOT_SIZE)) {
			// constant fits into a slot
			this.compile(ISA.LIT.ordinal(), (int)value);
		}
		else if ((value & (value-1)) == 0) {
			// 1 bit set constant
			int bit = 0;
			value = value >>> 1;
			while (value != 0) {
				++bit;
				value = value >>> 1;
			}
			this.compile(ISA.BLIT.ordinal(), bit);
		}
		else if (value >= 0) {
			// positive number
			if (!doesFit(ISA.FETCHPINC.ordinal())) {flush();}
			this.compile(ISA.FETCHPINC.ordinal());
			this.addAdditional(value);
		}
		else {
			// negative number
			if (!doesFit(ISA.FETCHPINC.ordinal())) {flush();}
			this.compile(ISA.FETCHPINC.ordinal());
			this.addAdditional(value);
		}
	}

	public void compile(Codepoint cp)
	{
		scope.add(cp);		
	}

	public void compile(ISA opcode)
	{
		scope.add(new Codepoint(opcode));
	}

	public void compile(ISA opcode, int arg0)
	{
		scope.add(new Codepoint(opcode, arg0));
	}

	public void compile(ISA opcode, int arg0, int arg1)
	{
		scope.add(new Codepoint(opcode, arg0, arg1));
	}

//	public void compile(ISA opcode, int arg0, int arg1, int arg2)
//	{
//		if (head == null) {
//			head = tail = new Codepoint(null, opcode, arg0, arg1, arg2);
//		}
//		else {
//			Codepoint cp = new Codepoint(tail, opcode, arg0, arg1, arg2);
//			tail.setNext(cp);
//			tail = cp;
//		}
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1, int arg2, int arg3)
//	{
//		if (head == null) {
//			head = tail = new Codepoint(null, opcode, arg0, arg1, arg2, arg3);
//		}
//		else {
//			Codepoint cp = new Codepoint(tail, opcode, arg0, arg1, arg2, arg3);
//			tail.setNext(cp);
//			tail = cp;
//		}
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1, int arg2, int arg3, int arg4)
//	{
//		if (head == null) {
//			head = tail = new Codepoint(null, opcode, arg0, arg1, arg2, arg3, arg4);
//		}
//		else {
//			Codepoint cp = new Codepoint(tail, opcode, arg0, arg1, arg2, arg3, arg4);
//			tail.setNext(cp);
//			tail = cp;
//		}
//	}

	
	
}
