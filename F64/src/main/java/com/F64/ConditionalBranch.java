package com.F64;

import com.F64.codepoint.Literal;
import com.F64.codepoint.QDup;


public class ConditionalBranch implements java.lang.Cloneable {
	private Branch		branch;
	private Condition	cond;
	private long		fixup_adr;
	private long		p_adr;
	private int			fixup_slot;
//	private int			remaining_bits;
	private	ISA			code;
	private	Block		preceding_block;

	public ConditionalBranch(ISA isa, Condition c)
	{
		cond = c;
		code = isa;
	}

	public ConditionalBranch(Condition c)
	{
		cond = c;
		code = ISA.BRANCH;
	}

	public ConditionalBranch clone() throws CloneNotSupportedException
	{
		ConditionalBranch res = (ConditionalBranch)super.clone();
		return res;
	}

	public void setBranch(Branch br) {branch = br;}

//	public void generateBranch(Builder b, Block bl, int instr_cnt)
//	{
//		Builder probe = null;
//		if (Builder.fit(ISA.BRANCH.ordinal(), cond.encode(Branch.SKIP))) {
//			if (Builder.forwardBranchCanBeImplicit(b.getCurrentSlot(), instr_cnt)) {
//				probe = b.fork(false);
//				if (Builder.forwardBranchIsImplicit(probe, cond, bl)) {
//					Builder.forwardBranchImplicit(b, cond, bl);
//					return;
//				}
//			}
//			
//		}
//		b.flush();
//		
//	}
//
//	public void generateBranch(Builder b, Block bl, ConditionalBranch cb)
//	{
//		
//	}

	
//	public void generateBranch(Builder b)
//	{
//		switch (branch) {
//		case SKIP:
//			if (Builder.fit(ISA.BRANCH.ordinal(), cond.encode(Branch.SKIP))) {
//				fixup_adr = b.getCurrentPosition();
//				fixup_slot = b.getCurrentSlot()+1;
//				p_adr = b.getCurrentP();
//				
//			}
//			branch = Branch.SHORT;
//		case SHORT:
//			if (Builder.fit(b.getCurrentSlot(), ISA.BRANCH.ordinal(), cond.encode(Branch.SHORT), Processor.SLOT_MASK)) {
//				b.add(ISA.BRANCH, cond.encode(Branch.SHORT), Processor.SLOT_MASK);
//				fixup_adr = b.getCurrentPosition();
//				p_adr = b.getCurrentP();
//				fixup_slot = b.getCurrentSlot()-1;
//				return;
//			}
//		case REM:
//			if (b.getCurrentSlot() <= (Processor.NO_OF_SLOTS-4)) {
//				b.add(ISA.BRANCH, cond.encode(Branch.REM));
//				fixup_adr = b.getCurrentPosition();
//				p_adr = b.getCurrentP();
//				fixup_slot = b.getCurrentSlot();
//				b.flush();
//				return;
//			}
//			break;
//
//		
//		case LONG:
//			b.add(ISA.BRANCH, cond.encode(Branch.LONG));
//			fixup_adr = b.getCurrentP();
//			b.addAdditionalCell(0);
//			p_adr = b.getCurrentP();
//			break;
//
//		default:
//			break;
//		
//		}
//	}

	public void setCondition(Condition value) {cond = value;}
	public Condition getCondition() {return cond;}
	public long getFixupAdr() {return fixup_adr;}
	public long getPAdr() {return p_adr;}
	public int getFixupSlot() {return fixup_slot;}
	public void setPrecedingBlock(Block value) {preceding_block = value;}
	public Block getPrecedingBlock() {return preceding_block;}

	public boolean optimize(Compiler c, Optimization opt)
	{
		Codepoint p;
		if (preceding_block == null) {return false;}
		switch (opt) {
		case CONSTANT_FOLDING:
			p = preceding_block.getTail();
			if ((p != null) && (p instanceof Literal)) {
				Literal lit = (Literal) p;
				switch (cond) {
				case EQ0:
					cond = lit.getValue() == 0 ? Condition.ALWAYS : Condition.NEVER;
					lit.remove();
					return true;

				case QEQ0:
					if (lit.getValue() == 0) {
						cond = Condition.ALWAYS;
						lit.remove();
					}
					else {
						cond = Condition.NEVER;
					}
					return true;

				default:
					break;
				
				}
			}
			break;

		case PEEPHOLE:
			p = preceding_block.getTail();
			if ((p != null) && (p instanceof QDup)) {
				if (cond == Condition.EQ0) {
					cond = Condition.QEQ0;
					p.remove();
					return true;
				}
			}
			break;

		default:
			break;
		
		}
		return false;
	}

	public void set(Builder b, int slot_offset)
	{
		fixup_adr = b.getCurrentPosition();
		fixup_slot = b.getCurrentSlot()+slot_offset;
		p_adr = b.getCurrentP();
	}
	
	public void generateBranch(Builder b, Branch br)
	{
		branch = br;
		this.generate(b);
	}

	public void generate(Builder b)
	{
		assert(cond != Condition.NEVER);
		if (cond == Condition.ALWAYS) {
			switch (branch) {
			case IO:
				return;
			case LONG:
				b.add(Ext1.LJMP);
				b.addAdditionalCell(0);
				fixup_adr = b.getCurrentP();
				fixup_slot = 0;
				p_adr = b.getCurrentP();
//				remaining_bits = 0;
				return;
//			case REM:
//				b.add(ISA.RJMP);
//				fixup_adr = b.getCurrentPosition();
//				fixup_slot = b.getCurrentSlot();
//				p_adr = b.getCurrentP();
//				remaining_bits = Builder.getRemainingBits(fixup_slot);
//				return;
			case FORWARD:b.add(ISA.FJMP, Processor.SLOT_MASK); break;
			case BACK:	b.add(ISA.BJMP, Processor.SLOT_MASK); break;
			case SKIP:	b.add(ISA.USKIP); break;
			case SLOT0:	b.add(ISA.UJMP0); break;
			case SLOT1:	b.add(ISA.UJMP1); break;
			case SLOT2:	b.add(ISA.UJMP2); break;
			case SLOT3:	b.add(ISA.UJMP3); break;
			case SLOT4:	b.add(ISA.UJMP4); break;
			case SLOT5:	b.add(ISA.UJMP5); break;
			case SLOT6:	b.add(ISA.UJMP6); break;
			case SLOT7:	b.add(ISA.UJMP7); break;
			case SLOT8:	b.add(ISA.UJMP8); break;
			case SLOT9:	b.add(ISA.UJMP9); break;
			case SLOT10:b.add(ISA.UJMP10); break;
			default:
				break;
			
			}
			fixup_adr = b.getCurrentPosition();
			fixup_slot = b.getCurrentSlot()-1;
			p_adr = b.getCurrentP();
//			remaining_bits = 0;
			return;
		}
		switch (branch) {
		case SKIP:
			b.add(code, cond.encode(Branch.SKIP));
			fixup_adr = b.getCurrentPosition();
			fixup_slot = b.getCurrentSlot()-1;
			p_adr = b.getCurrentP();
//			remaining_bits = 0;
			break;
		case FORWARD:
		case BACK:
			b.add(code, cond.encode(branch), Processor.SLOT_MASK);
			fixup_adr = b.getCurrentPosition();
			fixup_slot = b.getCurrentSlot()-1;
			p_adr = b.getCurrentP();
//			remaining_bits = 0;
			break;
//		case REM:
//			b.add(code, cond.encode(Branch.REM));
//			fixup_adr = b.getCurrentPosition();
//			fixup_slot = b.getCurrentSlot();
//			p_adr = b.getCurrentP();
//			remaining_bits = Builder.getRemainingBits(fixup_slot);
//			break;
		case LONG:
			b.add(code, cond.encode(Branch.LONG));
			b.addAdditionalCell(0);
			fixup_adr = b.getCurrentP();
			fixup_slot = 0;
			p_adr = b.getCurrentP();
//			remaining_bits = 0;
			break;
		default:
			b.add(code, cond.encode(branch));
			fixup_adr = b.getCurrentPosition();
			fixup_slot = b.getCurrentSlot()-1;
			p_adr = b.getCurrentP();
//			remaining_bits = 0;
			break;

			
		}
	}
	
	public boolean flushBeforeFixup()
	{
		return branch != Branch.SKIP;
	}

	
	public boolean fixup(Builder b, long target_adr, int target_slot)
	{
		long data;
		long diff;
		System s = b.getSystem();
		if ((cond != null) && (branch != null)) {
			switch (branch) {
			case IO:
				assert(false);
				return false;
			case LONG:
				assert(target_slot == 0);
				if (b.doesGenerate()) {
					s.setMemory(fixup_adr, target_adr);
				}
				return true;
//			case REM:
//				assert(target_slot == 0);
//				diff1 = Builder.getHighestDifferentBit1(target_adr, p_adr);
//				if (diff1 <= remaining_bits) {
//					long mask = Builder.getAddressMask(fixup_slot);
//					if (b.doesGenerate()) {
//						if (fixup_adr == b.getCurrentPosition()) {
//							data = b.getCurrentCell();
//							data = data ^ ((data ^ target_adr) & mask);
//							b.setCurrentCell(data);
//						}
//						else {
//							data = s.getMemory(fixup_adr);
//							data = data ^ ((data ^ target_adr) & mask);
//							s.setMemory(fixup_adr, data);
//						}
//					}
//					return true;
//				}
//				assert(false);
//				return false;
			case FORWARD:
				assert(target_slot == 0);
				assert(target_adr > p_adr);
				diff = target_adr - p_adr;
				if (diff <= Processor.SLOT_SIZE) {
					if (b.doesGenerate()) {
						if (fixup_adr == b.getCurrentPosition()) {
							data = b.getCurrentCell();
							data = Processor.writeSlot(data, fixup_slot, (int)(Processor.SLOT_MASK & diff));
							b.setCurrentCell(data);
						}
						else {
							data = s.getMemory(fixup_adr);
							data = Processor.writeSlot(data, fixup_slot, (int)(Processor.SLOT_MASK & diff));
							s.setMemory(fixup_adr, data);
						}
					}
					return true;
				}
				return false;

			case BACK:
				assert(target_slot == 0);
				assert(target_adr < p_adr);
				diff = p_adr - target_adr;
				if (diff <= Processor.SLOT_BITS) {
					if (b.doesGenerate()) {
						if (fixup_adr == b.getCurrentPosition()) {
							data = b.getCurrentCell();
							data = Processor.writeSlot(data, fixup_slot, (int)(Processor.SLOT_MASK & diff));
							b.setCurrentCell(data);
						}
						else {
							data = s.getMemory(fixup_adr);
							data = Processor.writeSlot(data, fixup_slot, (int)(Processor.SLOT_MASK & diff));
							s.setMemory(fixup_adr, data);
						}
					}
					return true;
				}
				return false;

			case SKIP:
			default:
				assert(target_adr == fixup_adr);
				if (cond == Condition.ALWAYS) {
					if (b.doesGenerate()) {
						ISA value = ISA.NOP;
						switch (target_slot) {
						case 0:	value = ISA.UJMP0; break;
						case 1:	value = ISA.UJMP1; break;
						case 2:	value = ISA.UJMP2; break;
						case 3:	value = ISA.UJMP3; break;
						case 4:	value = ISA.UJMP4; break;
						case 5:	value = ISA.UJMP5; break;
						case 6:	value = ISA.UJMP6; break;
						case 7:	value = ISA.UJMP7; break;
						case 8:	value = ISA.UJMP8; break;
						case 9:	value = ISA.UJMP9; break;
						case 10:value = ISA.UJMP10; break;
						default:value = ISA.USKIP; break;
						}
						if (fixup_adr == b.getCurrentPosition()) {
							data = b.getCurrentCell();
							data = Processor.writeSlot(data, fixup_slot, value.ordinal());
							b.setCurrentCell(data);
						}
						else {
							data = s.getMemory(fixup_adr);
							data = Processor.writeSlot(data, fixup_slot, value.ordinal());
							s.setMemory(fixup_adr, data);
						}
					}
					return true;
				}
				else {
					if (b.doesGenerate()) {
						int value = 0;
						if (target_slot > Processor.FINAL_SLOT) {
							value = this.cond.encode(Branch.SKIP);
						}
						else {
							value = this.cond.encode(Branch.values()[target_slot]);
						}
						if (fixup_adr == b.getCurrentPosition()) {
							data = b.getCurrentCell();
							data = Processor.writeSlot(data, fixup_slot, value);
							b.setCurrentCell(data);
						}
						else {
							data = s.getMemory(fixup_adr);
							data = Processor.writeSlot(data, fixup_slot, value);
							s.setMemory(fixup_adr, data);
						}
					}
				}
				return true;
			}
		}
		assert(false);
		return false;
	}

	
}
