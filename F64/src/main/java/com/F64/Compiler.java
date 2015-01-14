package com.F64;

import com.F64.scope.Main;

//import java.io.IOException;

public class Compiler {
	private System				system;
	private Processor			processor;
	private	long				current_cell;
	private	long[]				additional_cell;
	private int					current_slot;
	private int					addtional_cnt;
	private Scope				main_scope;
	private Scope				current_scope;
	
	public Compiler(System system, Processor processor)
	{
		this.system = system;
		this.processor = processor;
		this.additional_cell = new long[Processor.NO_OF_SLOTS];
	}

	public System getSystem() {return system;}
	public Processor getProcessor() {return processor;}
	public boolean hasAdditionalCells() {return addtional_cnt > 0;}
	public Scope getScope() {return current_scope;}
	public Scope getMainScope() {return main_scope;}
	public void setScope(Scope s) {current_scope = s;}
	public int getRemainingSlots() {return Processor.NO_OF_SLOTS - current_slot;}
	
	public int getRemainingBits()
	{
		if (current_slot > Processor.FINAL_SLOT) {
			return 0;
		}
		if (current_slot == Processor.FINAL_SLOT) {
			return Processor.FINAL_SLOT_BITS;
		}
		return (Processor.FINAL_SLOT - 1 - current_slot) * Processor.SLOT_BITS + Processor.FINAL_SLOT_BITS;
	}

	public int getDifferentBits(long value1, long value2)
	{
		int diff = 0;
		long mask = -1;
		while ((value1 & mask) != (value2 & mask)) {
			++diff;
			mask <<= 1;
		}
		return diff;
	}
	
	public long getAddressMask(int slot)
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
	
	public void start()
	{
		this.main_scope = new Main();
		this.current_scope = this.main_scope;
	}
	
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

	public void addAdditional(long value)
	{
		this.additional_cell[this.addtional_cnt++] = value;
	}
	
	public void flush()
	{
		if (this.current_slot > 0) {
			if (current_slot < Processor.FINAL_SLOT) {
				this.generate(ISA.USKIP);
			}
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

	public void generate(ISA opcode)
	{
		if (!doesFit(opcode.ordinal())) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(ISA opcode, int slot2)
	{
		if (!doesFit(opcode.ordinal(), slot2)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(ISA opcode, int slot2, int slot3)
	{
		if (!doesFit(opcode.ordinal(), slot2, slot3)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(ISA opcode, int slot2, int slot3, int slot4)
	{
		if (!doesFit(opcode.ordinal(), slot2, slot3, slot4)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(ISA opcode, int slot2, int slot3, int slot4, int slot5)
	{
		if (!doesFit(opcode.ordinal(), slot2, slot3, slot4, slot5)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot5);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(ISA opcode, int slot2, int slot3, int slot4, int slot5, int slot6)
	{
		if (!doesFit(opcode.ordinal(), slot2, slot3, slot4, slot5, slot6)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot5);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot6);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext1 opcode)
	{
		if (!doesFit(ISA.EXT1.ordinal(), opcode.ordinal())) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT1.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext1 opcode, int arg0)
	{
		if (!doesFit(ISA.EXT1.ordinal(), opcode.ordinal(), arg0)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT1.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, arg0);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext2 opcode)
	{
		if (!doesFit(ISA.EXT2.ordinal(), opcode.ordinal())) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT2.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext2 opcode, int arg0)
	{
		if (!doesFit(ISA.EXT2.ordinal(), opcode.ordinal(), arg0)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT2.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, arg0);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext3 opcode)
	{
		if (!doesFit(ISA.EXT3.ordinal(), opcode.ordinal())) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT3.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(Ext3 opcode, int reg)
	{
		if (!doesFit(ISA.EXT3.ordinal(), opcode.ordinal(), reg)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.EXT3.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, reg);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void generate(RegOp1 opcode, int dest)
	{
		if (!doesFit(ISA.REGOP1.ordinal(), opcode.ordinal(), dest)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.REGOP1.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, dest);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
		
	}

	public void generate(RegOp2 opcode, int dest, int src)
	{
		if (!doesFit(ISA.REGOP2.ordinal(), opcode.ordinal(), dest, src)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.REGOP2.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, dest);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, src);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
		
	}

	public void generate(RegOp3 opcode, int dest, int src1, int src2)
	{
		if (!doesFit(ISA.REGOP3.ordinal(), opcode.ordinal(), dest, src1, src2)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, ISA.REGOP3.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, opcode.ordinal());
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, dest);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, src1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, src2);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
		
	}

	public void generateCall(long adr, boolean useJumpInsteadOfCall)
	{
		long here = this.current_cell + this.addtional_cnt; // that is the value of P
		// now we must replace the lower bits of P with some new value.
		// first we check if we can fit it in the remaining slots of the current word
		int remaining_bits = this.getRemainingBits();
		int different_bits = this.getDifferentBits(adr, here);
		if (different_bits > (remaining_bits - Processor.SLOT_BITS)) {
			this.flush();
		}
		// address fits now
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, useJumpInsteadOfCall ? ISA.JMP.ordinal() : ISA.CALL.ordinal());
		long mask = this.getAddressMask(this.current_slot);
		this.current_cell |= adr & mask;
		current_slot = Processor.FINAL_SLOT+1;
		this.flush();
	}

	public void compile(Codepoint cp)
	{
		getScope().add(cp);		
	}

//	public void compile(ISA opcode)
//	{
//		getScope().add(new ISACode(opcode));
//	}
//
//	public void compile(ISA opcode, int arg0)
//	{
//		getScope().add(new ISACode(opcode, arg0));
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1)
//	{
//		getScope().add(new ISACode(opcode, arg0, arg1));
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1, int arg2)
//	{
//		getScope().add(new ISACode(opcode, arg0, arg1, arg2));
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1, int arg2, int arg3)
//	{
//		getScope().add(new ISACode(opcode, arg0, arg1, arg2, arg3));
//	}
//
//	public void compile(ISA opcode, int arg0, int arg1, int arg2, int arg3, int arg4)
//	{
//		getScope().add(new ISACode(opcode, arg0, arg1, arg2, arg3, arg4));
//	}

	public void optimize()
	{
		boolean allowDeadCodeElimkination = true;
		boolean allowLoopUnrolling = true;
		boolean allowConstantFolding = true;
		boolean allowPeepholeOptimization = true;
		boolean cont = true;
		while (cont) {
			cont = false;
			cont |= allowConstantFolding		&& getMainScope().optimize(processor, Optimization.CONSTANT_FOLDING);
			cont |= allowDeadCodeElimkination	&& getMainScope().optimize(processor, Optimization.DEAD_CODE_ELIMINATION);
			cont |= allowLoopUnrolling			&& getMainScope().optimize(processor, Optimization.LOOP_UNROLLING);
			cont |= allowPeepholeOptimization	&& getMainScope().optimize(processor, Optimization.PEEPHOLE);
		};
	}
	
	public void generate()
	{
		getMainScope().generate(this);		
	}

	
}
