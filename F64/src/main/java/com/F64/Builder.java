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

	public static int getHighestDifferentBit1(long value1, long value2)
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

	public static boolean forwardJumpFitsIntoSlot(long P, long target)
	{
		return (target - P) <= Processor.SLOT_SIZE;		
	}

	public static boolean backJumpFitsIntoSlot(long P, long target)
	{
		return (P - target) <= Processor.SLOT_SIZE;		
	}

	public static boolean forwardBranchCanBeImplicit(int slot, int instructionCount)
	{
		return((instructionCount+slot) < (Processor.NO_OF_SLOTS-2));
	}

	public static boolean forwardBranchIsImplicit(Builder b, Condition cond, Scope block)
	{
		long cp = b.getCurrentPosition();
		b.add(ISA.BRANCH, cond.encode(Branch.SKIP));
		block.generate(b);
		return b.getCurrentP() == cp;
	}

	public static boolean forwardBranchIsImplicit(Builder b, ConditionalBranch br, Scope block)
	{
		long cp = b.getCurrentPosition();
		b.add(ISA.BRANCH, br.getCondition().encode(Branch.SKIP));
		br.set(b, -1);
		block.generate(b);
		return b.getCurrentP() == cp;
	}

	public static void forwardBranchImplicit(Builder b, Condition cond, Scope block)
	{
		long fixup = b.getCurrentPosition();
		int fixup_slot = b.getCurrentSlot()+1;
		b.add(ISA.BRANCH, Branch.SKIP.ordinal());
		block.generate(b);
		int target_slot = b.getCurrentSlot();
		if (target_slot < Processor.NO_OF_SLOTS) {
			com.F64.System s = b.getSystem();
			s.setMemory(fixup, Processor.writeSlot(s.getMemory(fixup), fixup_slot, cond.encode(target_slot)));
		}
	}

	public static boolean forwardBranchCanBeShort(long P, int slot, int instructionCount)
	{
		long target = P + instructionCount / Processor.NO_OF_SLOTS;
		long diff1 = Builder.getHighestDifferentBit1(target, P);
		return diff1 <= Processor.SLOT_BITS;
	}

	public static boolean forwardBranchIsShort(Builder b, ConditionalBranch pre, Scope block, ConditionalBranch fixup)
	{
//		pre.generateBranch(b, Branch.SHORT);
//		long P = b.getCurrentP();
//		block.generate(b);
//		if (fixup != null) {
//			fixup.generateBranch(b, Branch.SHORT);
//		}
//		return shortJumpFitsIntoSlot(P, b.getCurrentPosition());
		pre.generateBranch(b, Branch.FORWARD);
		long P = b.getCurrentP();
		block.generate(b);
		if (fixup != null) {
			fixup.generateBranch(b, Branch.FORWARD);
		}
		return forwardJumpFitsIntoSlot(P, b.getCurrentPosition());
	}

//	public static Location generateForwardBranchShort(Builder b, Condition cond)
//	{
//		b.add(ISA.BRANCH, cond.encode(Branch.SHORT), Processor.SLOT_MASK);
//		return new Location(b.getCurrentP(), b.getCurrentPosition(), b.getCurrentSlot()-1);
//	}
//
//	public static Location generateForwardBranchRem(Builder b, Condition cond)
//	{
//		b.add(ISA.BRANCH, cond.encode(Branch.REM));
//		Location res = new Location(b.getCurrentP(), b.getCurrentPosition(), b.getCurrentSlot());
//		b.flush();
//		return res;
//	}
//

	public static long generateForwardBranchLong(Builder b, Condition cond)
	{
		b.add(ISA.BRANCH, cond.encode(Branch.LONG));
		long res = b.getCurrentP();
		b.addAdditionalCell(0);
		return res;
	}

//	public static boolean forwardForwardBranch(Builder b, ConditionalBranch pre, Scope block, ConditionalBranch post)
//	{
//		pre.generateBranch(b);
//		b.flush();
//		long target1 = b.getCurrentPosition();
//		block.generate(b);
//		if (post != null) {
//			post.generateBranch(b);
//		}
//		b.flush();
//		long target2 = b.getCurrentPosition();
//
//		if (!pre.fixup(b, target1, 0)) {return false;}
//		if (post != null) {
//			if (!post.fixup(b, target2, 0)) {return false;}
//		}
//		return true;
//	}

	public static boolean forwardBranchShort(Builder b, ConditionalBranch pre, Scope block, ConditionalBranch post)
	{
		pre.generateBranch(b, Branch.FORWARD);
		b.flush();
		long target1 = b.getCurrentPosition();
		block.generate(b);
		if (post != null) {
			post.generateBranch(b, Branch.FORWARD);
		}
		b.flush();
		long target2 = b.getCurrentPosition();

		if (!pre.fixup(b, target1, 0)) {return false;}
		if (post != null) {
			if (!post.fixup(b, target2, 0)) {return false;}
		}
		return true;
	}

//	public static boolean forwardBranchCanBeRemaining(long P, int slot, int instructionCount)
//	{
//		long target = P + instructionCount / Processor.NO_OF_SLOTS;
//		long diff1 = Builder.getHighestDifferentBit1(target, P);
//		return diff1 <= Builder.getRemainingBits(slot+1);
//	}
//
//	public static boolean forwardBranchIsRemaining(Builder b, Condition cond, Scope block)
//	{
//		if (b.getCurrentSlot() > (Processor.NO_OF_SLOTS-4)) {
//			b.flush();
//		}
//		b.add(ISA.BRANCH, cond.encode(Branch.REM));
//		long P = b.getCurrentP();
//		block.generate(b);
//		b.flush();
//		long target = b.getCurrentPosition();
//		int diff1 = Builder.getHighestDifferentBit1(target, P);
//		return diff1 <= Processor.SLOT_BITS;
//	}

//	public static Location forwardBranchRemaining(Builder b, Condition cond, Scope block, Condition append_cond)
//	{
//		Location res = null;
//		if (b.getCurrentSlot() > (Processor.NO_OF_SLOTS-4)) {
//			b.flush();
//		}
//		long fixup = b.getCurrentPosition();
//		int fixup_slot = b.getCurrentSlot()+2;
//		b.add(ISA.BRANCH, cond.encode(Branch.REM));
////		long P = b.getCurrentP();
//		block.generate(b);
//		if (append_cond != null) {
//			res = generateForwardBranchRem(b, append_cond);
//		}
//		b.flush();
//		long target = b.getCurrentPosition();
//		long mask = getAddressMask(fixup_slot);
//		com.F64.System s = b.getSystem();
//		long cell = s.getMemory(fixup);
//		cell ^= (cell ^ target) & mask;
//		s.setMemory(fixup, cell);
//		return res;
//	}

	public static long forwardBranchLong(Builder b, Condition cond, Scope block, Condition append_cond)
	{
		long res = 0;
		if (!fit(b.getCurrentSlot(), ISA.BRANCH.ordinal(), cond.encode(Branch.LONG))) {
			b.flush();
		}
		b.add(ISA.BRANCH, cond.encode(Branch.LONG));
		b.addAdditionalCell(0);
		b.flush();
		long fixup = b.getCurrentPosition()-1;
		block.generate(b);
		b.flush();
		long target = b.getCurrentPosition();
		com.F64.System s = b.getSystem();
		s.setMemory(fixup, target);
		return res;
	}
	
//	public static boolean fixupShort(System s, Location loc, long target)
//	{
//		int diff1 = Builder.getHighestDifferentBit1(target, loc.getPAdr());
//		if (diff1 <= Processor.SLOT_BITS) {
//			long fixup = loc.getAdr();
//			int fixup_slot = loc.getSlot();
//			s.setMemory(fixup, Processor.writeSlot(s.getMemory(fixup), fixup_slot, (int)(Processor.SLOT_MASK & target)));
//			return true;
//		}
//		return false;
//	}
//	
//	public static boolean fixupRem(System s, Location loc, long target)
//	{
//		int diff1 = Builder.getHighestDifferentBit1(target, loc.getPAdr());
//		int fixup_slot = loc.getSlot();
//		if (diff1 <= getRemainingBits(fixup_slot)) {
//			long fixup = loc.getAdr();
//			long data = s.getMemory(fixup);
//			long mask = getAddressMask(fixup_slot);
//			s.setMemory(fixup, data ^ ((data ^ target) & mask));
//			return true;
//		}
//		return false;
//	}

	public static void fixupLong(System s, long loc, long target)
	{
		s.setMemory(loc, target);
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

	public System getSystem() {return system;}
	public int getCurrentSlot() {return current_slot;}
	public long getCurrentPosition() {return current_pos;}
	public long getCurrentP() {return current_pos+addtional_cnt;}
	public boolean doesGenerate() {return generate;}
	public boolean hasAdditionalData() {return addtional_cnt > 0;}
	public int getAdditionalDataSize() {return addtional_cnt;}
	public long getCurrentCell() {return current_cell;}
	public void setCurrentCell(long value) {current_cell = value;}

//	public boolean fixupShort(Location loc)
//	{
//		flush();
//		return fixupShort(system, loc, current_cell);
//	}
//	
//	public boolean fixupRem(Location loc)
//	{
//		flush();
//		return fixupRem(system, loc, current_cell);
//	}
//	
//	public void fixupLong(long loc)
//	{
//		flush();
//		fixupLong(system, loc, current_cell);
//	}
//
//	public int getRemainingBits()
//	{
//		return getRemainingBits(current_slot);
//	}
	
	public Builder fork(boolean flush)
	{
		Builder res = new Builder(system);
		res.current_pos = current_pos;
		res.current_cell = current_cell;
		res.current_slot = current_slot;
		res.addtional_cnt = addtional_cnt;
		res.generate = false;
		res.call_generated = false;
		if (flush) {res.flush();}
		res.start_position = res.current_pos;
		res.start_slot = res.current_slot;
		return res;
	}

	public Builder fork(boolean flush, Builder res)
	{
		if (res == null) {res = new Builder(system);}
		res.current_pos = current_pos;
		res.current_cell = current_cell;
		res.current_slot = current_slot;
		res.addtional_cnt = addtional_cnt;
		res.generate = false;
		res.call_generated = false;
		if (flush) {res.flush();}
		res.start_position = res.current_pos;
		res.start_slot = res.current_slot;
		return res;
	}
	

	public boolean exceed1Cell()
	{
		if (addtional_cnt > 0) {return true;}
		if (start_position == current_pos) {return false;}
		if ((current_pos == start_position+1) && (current_slot == 0)) {return false;}
		return true;
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
		boolean res = (
				(current_pos == start_position) || ((current_pos == start_position+1) && (current_slot == 0))
			) && (addtional_cnt == 0) && !call_generated;
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

	public void finishCell(long bits)
	{
		if (generate) {
			system.compileCode(current_cell | bits);
			for (int i=0; i<addtional_cnt; ++i) {
				system.compileCode(additional_cells[i]);
			}
		}
		current_pos += addtional_cnt+1;
		current_cell = 0;
		current_slot = 0;
		addtional_cnt = 0;
	}

	public void addCall(long dest_adr, boolean is_jump)
	{
		int instr_slots = is_jump ? 2 : 1;
//		long curr_adr = current_pos + addtional_cnt + instr_slots;
		int different_bits = getHighestDifferentBit1(dest_adr, 0);
		int remaining_bits = getRemainingBits(current_slot+instr_slots);
		if (different_bits > remaining_bits) {
			flush();
//			curr_adr = current_pos + instr_slots;
			different_bits = getHighestDifferentBit1(dest_adr, 0);
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
//			if (value < Processor.SLOT_SIZE * Processor.SLOT_SIZE) {
//				if (fit(current_slot, ISA.LIT.ordinal(), (int)(value >> Processor.SLOT_BITS), ISA.EXT.ordinal(), (int)(value & Processor.SLOT_MASK))) {
//					add(ISA.LIT, (int)(value >> Processor.SLOT_BITS));
//					add(ISA.EXT, (int)(value & Processor.SLOT_MASK));
//					return;
//				}
//			}
		}
		else {
			// negative
			long abs = ~value;
			if (abs < Processor.SLOT_SIZE) {
				add(ISA.NLIT, (int)abs);
				return;
			}
//			if (abs < Processor.SLOT_SIZE * Processor.SLOT_SIZE) {
//				if (fit(current_slot, ISA.NLIT.ordinal(), (int)(abs >> Processor.SLOT_BITS), ISA.EXT.ordinal(), (int)(value & Processor.SLOT_MASK))) {
//					add(ISA.NLIT, (int)(abs >> Processor.SLOT_BITS));
//					add(ISA.EXT, (int)(value & Processor.SLOT_MASK));
//					return;
//				}
//			}
		}
		if (!fit(current_slot, ISA.FETCHPINC.ordinal())) {flush();}
		add(ISA.FETCHPINC);
		addAdditionalCell(value);
	}
	

	public void addAdditionalCell(long value)
	{
		additional_cells[addtional_cnt++] = value;
	}

	public void forwardBranch(ConditionalBranch br, Scope block)
	{
		Builder probe = null;
		int instr_cnt = block.countInstructions();
		if (Builder.forwardBranchCanBeImplicit(getCurrentSlot(), instr_cnt)) {
			probe = fork(false);
			if (Builder.forwardBranchIsImplicit(probe, br.getCondition(), block)) {
				Builder.forwardBranchImplicit(this, br.getCondition(), block);
				return;
			}
		}
		if (Builder.forwardBranchCanBeShort(getCurrentP(), getCurrentSlot(), instr_cnt)) {
			probe = fork(false, probe);
			if (Builder.forwardBranchIsShort(probe, br, block, null)) {
				Builder.forwardBranchShort(this, br, block, null);
				probe.flush();
				return;
			}
		}
//		if (Builder.forwardBranchCanBeRemaining(getCurrentP(), getCurrentSlot(), instr_cnt)) {
//			probe = fork(false, probe);
//			if (Builder.forwardBranchIsRemaining(probe, br.getCondition(), block)) {
//				return;
//			}
//		}
		Builder.forwardBranchLong(this, br.getCondition(), block, null);

	}

	public void forwardBranch(Condition cond, Scope block)
	{
		Builder probe = null;
		int instr_cnt = block.countInstructions();
		if (Builder.forwardBranchCanBeImplicit(getCurrentSlot(), instr_cnt)) {
			probe = fork(false);
			if (Builder.forwardBranchIsImplicit(probe, Condition.EQ0, block)) {
				Builder.forwardBranchImplicit(this, Condition.EQ0, block);
				return;
			}
		}
		ConditionalBranch pre = new ConditionalBranch(Condition.EQ0);
		ConditionalBranch post = new ConditionalBranch(Condition.ALWAYS);
		if (Builder.forwardBranchCanBeShort(getCurrentP(), getCurrentSlot(), instr_cnt)) {
			probe = fork(false, probe);
			if (Builder.forwardBranchIsShort(probe, pre, block, post)) {
				Builder.forwardBranchShort(this, pre, block, post);
				probe.flush();
				return;
			}
		}
//		if (Builder.forwardBranchCanBeRemaining(getCurrentP(), getCurrentSlot(), instr_cnt)) {
//			probe = fork(false, probe);
//			if (Builder.forwardBranchIsRemaining(probe, Condition.EQ0, block)) {
//				return;
//			}
//		}
		Builder.forwardBranchLong(this, Condition.EQ0, block, null);
	}

}
