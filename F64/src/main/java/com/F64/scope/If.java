package com.F64.scope;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Ext1;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.codepoint.Literal;
import com.F64.codepoint.QDup;

public class If extends com.F64.Block implements java.lang.Cloneable {
	private com.F64.ConditionalBranch	branch_to_false;
	private com.F64.Block				true_part;
	private com.F64.ConditionalBranch	branch_to_end;
	private com.F64.Block				false_part;

	public If(Compiler c, Condition cond)
	{
		super(c.getScope());
		branch_to_false = new com.F64.ConditionalBranch(cond);
		true_part = new com.F64.Block(this);
		c.setScope(true_part);	
	}

	public If clone() throws CloneNotSupportedException
	{
		If res = (If)super.clone();
		if (true_part != null) {
			res.true_part = true_part.clone();
			res.true_part.setOwner(res);
		}
		if (false_part != null) {
			res.false_part = false_part.clone();
			res.false_part.setOwner(res);
		}
		return res;
	}

	public void doElse(Compiler c)
	{
		branch_to_end = new com.F64.ConditionalBranch(Condition.ALWAYS);
		false_part = new com.F64.Block(this);
		c.setScope(false_part);	
	}

	public void doThen(Compiler c)
	{
		c.setScope(this.getOwner());	
	}
	
	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		Condition cond;
		boolean res = false;
		switch (opt) {
		case CONSTANT_FOLDING:
			cond = branch_to_false.getCondition();
			if (cond == Condition.EQ0) {
				com.F64.Codepoint p = this.getPrevious();
				if (p != null) {
					if (p instanceof Literal) {
						Literal lit = (Literal) p;
						if (lit.getValue() == 0) {
							cond = Condition.ALWAYS;
						}
						else {
							cond = Condition.NEVER;
						}
						lit.remove();
						res = true;
					}
				}
			}
			break;
		case DEAD_CODE_ELIMINATION:
			cond = branch_to_false.getCondition();
			if (cond == Condition.ALWAYS) {
				if ((false_part == null) || false_part.isEmpty()) {
					this.remove();
				}
				else {
					false_part.optimize(c, opt);
					this.replaceWithScope(false_part);
				}
				res = true;
			}
			else if (cond == Condition.NEVER) {
				true_part.optimize(c, opt);
				this.replaceWithScope(true_part);
				res = true;
			}
			break;
		case PEEPHOLE:
			com.F64.Codepoint p = this.getPrevious();
			if ((p != null) && (p instanceof QDup) && (branch_to_false.getCondition() == Condition.EQ0)) {
				branch_to_false.setCondition(Condition.QEQ0);
				p.remove();
				res = true;
			}
			break;
		default:
			break;
		
		}
		if (false_part != null) {
			if (false_part.optimize(c, opt)) {res = true;}
		}
		if (true_part.optimize(c, opt)) {res = true;}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
		int additional, bits1, bits2, diff1, diff2, slot1, slot2;
		long target1, target2, cpos;
		Condition cond = branch_to_false.getCondition();
		if (cond == Condition.ALWAYS) {
			if (false_part != null) {
				false_part.generate(b);
			}
			return;
		}
		if (cond == Condition.NEVER) {
			true_part.generate(b);
			return;
		}
		int t_cnt = true_part.countInstructions();
		int f_cnt = false_part == null ? 0 : false_part.countInstructions();
		b.getSystem();
		if (t_cnt == 0) {
			// there is no true part
			if (f_cnt == 0) {return;}
			switch (cond) {
			case CARRY:
				b.add(Ext1.CARRYQ);
				b.add(Ext1.EQ0Q);
				branch_to_false.setCondition(Condition.EQ0);
				break;
			case EQ0:
				b.add(Ext1.EQ0Q);
				branch_to_false.setCondition(Condition.EQ0);
				break;
//			case GE0:
//				b.add(Ext1.LT0Q);
//				branch_to_false.setCondition(Condition.EQ0);
//				break;
			default:
				break;
			}
			b.forwardBranch(branch_to_false, false_part);
			return;
		}
		if (f_cnt == 0) {
			// only true part
			// first we test if the if statement fits into current cell
			b.forwardBranch(branch_to_false, true_part);
			return;
		}
		// non-empty true and false part
		Builder probe = null;

		// try pattern 1 (implicit + implicit)
		//												+-------------------------------+
		//												|								v
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| JMP	|	+	|	+	|	+	| UJMPn	|	-	|	-	|	-	|		|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//				|										^
		//				+---------------------------------------+


		if (Builder.forwardBranchCanBeImplicit(b.getCurrentSlot(), t_cnt + f_cnt + 1)) {
			probe = b.fork(false);
			cpos = probe.getCurrentPosition();
			additional = probe.getAdditionalDataSize();
			branch_to_false.generateBranch(probe, Branch.SKIP);
			true_part.generate(probe);
			branch_to_end.generateBranch(probe, Branch.SKIP);
			false_part.generate(probe);
			// test if code is still in same cell and no more additional data has been added
			if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
				// pattern 1 fit
				branch_to_false.generateBranch(b, Branch.SKIP);
				true_part.generate(b);
				branch_to_end.generateBranch(b, Branch.SKIP);
				slot1 = b.getCurrentSlot();
				false_part.generate(b);
				slot2 = b.getCurrentSlot();
				branch_to_false.fixup(b, cpos, slot1);
				branch_to_end.fixup(b, cpos, slot2);
				return;
			}
		}

		// try pattern 2 (implicit + short)
		//	
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| SKIP	|	+	|	+	|	+	| 	+	|	+	| SJMP	|	N	|		|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//				|														|
		//		+-------+														+---------------+
		//		v																				|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		//	|	-	|	-	|	-	|	-	|	-	| SKIP	|		|		|		|		|	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	v

		if (Builder.forwardBranchCanBeImplicit(b.getCurrentSlot(), t_cnt + 2)) {
			probe = b.fork(false, probe);
			cpos = probe.getCurrentPosition();
			additional = probe.getAdditionalDataSize();
			branch_to_false.generateBranch(probe, Branch.SKIP);
			true_part.generate(probe);
			branch_to_end.generateBranch(probe, Branch.FORWARD);
			if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
				probe.flush();
				false_part.generate(probe);
				probe.flush();
				// pattern 1 fit
				diff2 = Builder.getHighestDifferentBit1(probe.getCurrentPosition(), probe.getCurrentPosition());
				bits2 = Processor.getSlotBits(branch_to_end.getFixupSlot());
				if (diff2 <= bits2) {
					branch_to_false.generateBranch(b, Branch.SKIP);
					true_part.generate(b);
					branch_to_end.generateBranch(b, Branch.FORWARD);
					b.flush();
					false_part.generate(b);
					b.flush();
					branch_to_end.fixup(b, b.getCurrentPosition(), 0);
					return;
				}
			}
		}
				
		// try pattern 3 (short + short)
		//	
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| SHORT	|	N	|	+	|	+	| 	+	|	+	|	+	|	+	|	+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	| 	+	|	+	| SJMP	|	N	|		|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//						|												|
		//		+---------------+												+---------------+
		//		v																				|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		//	|	-	|	-	|	-	|	-	|	-	| SKIP	|		|		|		|		|	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	v

		probe = b.fork(false, probe);
		branch_to_false.generateBranch(probe, Branch.FORWARD);
		true_part.generate(probe);
		branch_to_end.generateBranch(probe, Branch.FORWARD);
		probe.flush();
		target1 = probe.getCurrentPosition();
		false_part.generate(probe);
		probe.flush();
		target2 = probe.getCurrentPosition();
		diff1 = Builder.getHighestDifferentBit1(branch_to_false.getPAdr(), target1);
		diff2 = Builder.getHighestDifferentBit1(branch_to_end.getPAdr(), target2);
		bits1 = Processor.getSlotBits(branch_to_false.getFixupSlot());
		bits2 = Processor.getSlotBits(branch_to_end.getFixupSlot());

		if ((diff1 <= bits1) && (diff2 <= bits2)) {
			// we can use this pattern
			branch_to_false.generateBranch(b, Branch.FORWARD);
			true_part.generate(b);
			branch_to_end.generateBranch(b, Branch.FORWARD);
			b.flush();
			target1 = b.getCurrentPosition();
			false_part.generate(b);
			b.flush();
			target2 = b.getCurrentPosition();
			branch_to_false.fixup(b, target1, 0);
			branch_to_end.fixup(b, target2, 0);
			return;
		}
		
		// try pattern 4 (short + long)
		//	
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| SHORT	|	N	|	+	|	+	| 	+	|	+	|	+	|	+	|	+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	| 	+	|	+	| EXT1	| LJMP	|		|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|					|				N											|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//						|				|
		//		+---------------+				+-----------------------------------------------+
		//		v																				|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		//	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	v

		probe = b.fork(false, probe);
		branch_to_false.generateBranch(probe, Branch.FORWARD);
		true_part.generate(probe);
		branch_to_end.generateBranch(probe, Branch.LONG);
		probe.flush();
		target1 = probe.getCurrentPosition();
		false_part.generate(probe);
		probe.flush();
		target2 = probe.getCurrentPosition();
		diff1 = Builder.getHighestDifferentBit1(branch_to_false.getPAdr(), target1);
		bits1 = Processor.getSlotBits(branch_to_false.getFixupSlot());

		if (diff1 <= bits1) {
			// we can use this pattern
			branch_to_false.generateBranch(b, Branch.FORWARD);
			true_part.generate(b);
			branch_to_end.generateBranch(b, Branch.LONG);
			b.flush();
			target1 = b.getCurrentPosition();
			false_part.generate(b);
			b.flush();
			target2 = b.getCurrentPosition();
			branch_to_false.fixup(b, target1, 0);
			branch_to_end.fixup(b, target2, 0);
			return;
		}
		
		// try pattern 5 (long + short)
		//	
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| LONG	|		|		|		|		|		|		|		|		|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|					N															|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	|	+	|	+	|	+	|	+	|	+	|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	| 	+	|	+	| SJMP	|	N	|		|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//						|												|
		//		+---------------+												+---------------+
		//		v																				|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		//	|	-	|	-	|	-	|	-	|	-	| SKIP	|		|		|		|		|	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	v

		probe = b.fork(false, probe);
		branch_to_false.generateBranch(probe, Branch.LONG);
		true_part.generate(probe);
		branch_to_end.generateBranch(probe, Branch.FORWARD);
		probe.flush();
		target1 = probe.getCurrentPosition();
		false_part.generate(probe);
		probe.flush();
		target2 = probe.getCurrentPosition();
		diff2 = Builder.getHighestDifferentBit1(branch_to_end.getPAdr(), target2);
		bits2 = Processor.getSlotBits(branch_to_end.getFixupSlot());

		if (diff2 <= bits2) {
			// we can use this pattern
			branch_to_false.generateBranch(b, Branch.LONG);
			true_part.generate(b);
			branch_to_end.generateBranch(b, Branch.FORWARD);
			b.flush();
			target1 = b.getCurrentPosition();
			false_part.generate(b);
			b.flush();
			target2 = b.getCurrentPosition();
			branch_to_false.fixup(b, target1, 0);
			branch_to_end.fixup(b, target2, 0);
			return;
		}

		// pattern 6 (long + long)
		// this works always
		//	
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|BRANCH	| LONG	|		|		|		|		|		|		|		|		|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|					N															|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	|	+	|	+	|	+	|	+	|	+	|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|	+	|	+	|	|	|	+	|	+	| 	+	|	+	| EXT1	| LJMP	|		|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//	|					|				N											|
		// -+-------+-------+---|---+-------+-------+-------+-------+-------+-------+-------+
		//						|				|
		//		+---------------+				+-----------------------------------------------+
		//		v																				|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		//	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	-	|	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	|
		// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+	v

		branch_to_false.generateBranch(b, Branch.LONG);
		true_part.generate(b);
		branch_to_end.generateBranch(b, Branch.LONG);
		b.flush();
		target1 = b.getCurrentPosition();
		false_part.generate(b);
		b.flush();
		target2 = b.getCurrentPosition();
		branch_to_false.fixup(b, target1, 0);
		branch_to_end.fixup(b, target2, 0);

	}

}
