package com.F64;

public class Fixup {
//	private Branch		branch;
//	private Condition	cond;
//	private long		fixup_adr;
//	private long		p_adr;
//	private int			fixup_slot;
//
//	public Fixup(Condition c)
//	{
//		cond = c;
//	}
//
//	public void setBranch(Branch br) {branch = br;}
//	
//	public void generateBranch(Builder b)
//	{
//		int slot_offset = 0;
//		
//		fixup_slot = b.getCurrentSlot() + slot_offset;
//		switch (branch) {
//		case LONG:
//			b.add(ISA.BRANCH, cond.encode(Branch.LONG));
//			fixup_adr = b.getCurrentP();
//			b.addAdditionalCell(0);
//			p_adr = b.getCurrentP();
//			break;
//		case REM:
//			if (b.getCurrentSlot() > (Processor.NO_OF_SLOTS-4)) {
//				b.flush();
//			}
//			b.add(ISA.BRANCH, cond.encode(Branch.REM));
//			fixup_adr = b.getCurrentPosition();
//			p_adr = b.getCurrentP();
//			fixup_slot = b.getCurrentSlot();
//			b.flush();
//			break;
//		case SHORT:
//			if (!Builder.fit(b.getCurrentSlot(), ISA.BRANCH.ordinal(), cond.encode(Branch.SHORT), Processor.SLOT_MASK)) {
//				b.flush();
//			}
//			b.add(ISA.BRANCH, cond.encode(Branch.SHORT), Processor.SLOT_MASK);
//			fixup_adr = b.getCurrentPosition();
//			p_adr = b.getCurrentP();
//			fixup_slot = b.getCurrentSlot()-1;
//			break;
//
//		default:
//			break;
//		
//		}
//	}
//
//	
//	public Condition getCondition() {return cond;}
//	public long getFixupAdr() {return fixup_adr;}
//	public long getPAdr() {return p_adr;}
//	public int getFixupSlot() {return fixup_slot;}
//
//	public boolean fixup(Builder b, long target_adr)
//	{
//		int diff1;
//		System s = b.getSystem();
//		if ((cond != null) && (branch != null) && b.doesGenerate()) {
//			switch (branch) {
//			case LONG:
//				s.setMemory(fixup_adr, target_adr);
//				return true;
//			case REM:
//				diff1 = Builder.getHighestDifferentBit1(target_adr, p_adr);
//				if (diff1 <= Builder.getRemainingBits(fixup_slot)) {
//					long data = s.getMemory(fixup_adr);
//					long mask = Builder.getAddressMask(fixup_slot);
//					s.setMemory(fixup_adr, data ^ ((data ^ target_adr) & mask));
//					return true;
//				}
//				assert(false);
//				return false;
//			case SHORT:
//				diff1 = Builder.getHighestDifferentBit1(target_adr, p_adr);
//				if (diff1 <= Processor.SLOT_BITS) {
//					s.setMemory(fixup_adr, Processor.writeSlot(s.getMemory(fixup_adr), fixup_slot, (int)(Processor.SLOT_MASK & target_adr)));
//					return true;
//				}
//				return false;
//
//			default:
//				break;
//			
//			}
//		}
//		assert(false);
//		return false;
//	}
//	
}
