package com.F64;

//import java.io.IOException;

public class Compiler {
	private System		system;
	private Processor	processor;
	private	long		current_cell;
	private	long[]		additional_cell;
	private int			current_slot;
	private int			addtional_cnt;
	
	public Compiler(System system, Processor processor)
	{
		this.system = system;
		this.processor = processor;
		this.additional_cell = new long[Processor.NO_OF_SLOTS];
	}

	public System getSystem() {return system;}
	public Processor getProcessor() {return processor;}

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

	public void compile(int slot1)
	{
		if (!doesFit(slot1)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compile(int slot1, int slot2)
	{
		if (!doesFit(slot1, slot2)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compile(int slot1, int slot2, int slot3)
	{
		if (!doesFit(slot1, slot2, slot3)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compile(int slot1, int slot2, int slot3, int slot4)
	{
		if (!doesFit(slot1, slot2, slot3, slot4)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compile(int slot1, int slot2, int slot3, int slot4, int slot5)
	{
		if (!doesFit(slot1, slot2, slot3, slot4, slot5)) {flush();}
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot1);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot2);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot3);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot4);
		this.current_cell = Processor.writeSlot(this.current_cell, this.current_slot++, slot5);
		if (this.current_slot >= Processor.NO_OF_SLOTS) {flush();}
	}

	public void compile(int slot1, int slot2, int slot3, int slot4, int slot5, int slot6)
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

	
}
