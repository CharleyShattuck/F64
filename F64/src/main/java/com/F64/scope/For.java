package com.F64.scope;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Ext1;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.codepoint.Literal;

public class For extends com.F64.Block implements java.lang.Cloneable {
	private int							unroll_limit;
	private long						count;
	private boolean						count_valid;
	private boolean						conditional;
	private com.F64.Block				body;

	public For(Compiler c)
	{
		super(c.getScope());
		unroll_limit = 20;
		body = new com.F64.Block(this);
		c.setScope(body);	
	}

	public For(Compiler c, boolean cond)
	{
		super(c.getScope());
		unroll_limit = 20;
		body = new com.F64.Block(this);
		c.setScope(body);	
		this.conditional = cond;
	}

	public void doNext(Compiler c)
	{
		c.setScope(this.getOwner());	
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = body.optimize(c, opt);
		if (super.optimize(c, opt)) {res = true;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if ((p instanceof Literal) && !count_valid) {
				Literal lit = (Literal) p;
				count = lit.getValue();
				count_valid = true;
				lit.remove();
				if (count <= 0) {
					if (opt == Optimization.DEAD_CODE_ELIMINATION) {
						this.remove();
						return true;
					}
				}
			}
			if (opt == Optimization.LOOP_UNROLLING) {
				if (count_valid && !conditional) {
					int bodycnt = body.countInstructions();
					if ((bodycnt > 3)
						&& (count < unroll_limit)
						&& (((count*body.countInstructions())) <= unroll_limit)
					) {
						com.F64.Scope replacement = new com.F64.Scope(this);
						for (int i=0; i<=count; ++i) {
							com.F64.Block blk = null;
							try {
								blk = (com.F64.Block)body.clone();
							} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}
							replacement.append(blk);
						}
						replaceWithScope(replacement);
						return true;
					}
				}
			}
		}
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (body.isEmpty()) {
				if (count_valid) {
					this.remove();
				}
				else {
					this.replaceWith(new com.F64.codepoint.Drop());
				}
				return true;
			}
		}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
		long target;
		int diff;
		Builder probe = null;
		int t_cnt = body.countInstructions();
		
		
		if (count_valid) {
			if (conditional) {
				if (count == 0) {return;}
				--count;
				conditional = false;
			}
			if (t_cnt == 0) {return;}
			b.addLiteral(count);
		}
		if (t_cnt == 0) {
			b.add(ISA.DROP);
			return;
		}
		if (conditional) {
			// check if short variant is possible
			probe = b.fork(false);
			com.F64.ConditionalBranch branch_to_end = new com.F64.ConditionalBranch(Condition.EQ0);
			probe.add(Ext1.QFOR);
			branch_to_end.generateBranch(probe, Branch.FORWARD);
			probe.flush();
			target = probe.getCurrentPosition();
			body.generate(probe);
			if ((target == probe.getCurrentP()) && (probe.getCurrentSlot() < Processor.NO_OF_SLOTS))  {
				b.add(Ext1.QFOR);
				branch_to_end.generateBranch(b, Branch.FORWARD);
				b.flush();
				target = b.getCurrentPosition();
				body.generate(b);
				b.add(ISA.UNEXT);
				b.flush();
				branch_to_end.fixup(b, b.getCurrentPosition(), 0);
				return;
			}
			if (probe.getCurrentSlot() == (Processor.NO_OF_SLOTS-1))  {
				probe.flush();
			}
			diff = Builder.getHighestDifferentBit1(probe.getCurrentP(), target);
			if (diff <= Processor.SLOT_BITS) {
				// short next
				probe.add(ISA.SNEXT, ((int)target) & Processor.SLOT_MASK);
			}
			else {
				probe.add(Ext1.LNEXT);
				probe.addAdditionalCell(target);
			}
			probe.flush();
			if (branch_to_end.fixup(probe, probe.getCurrentPosition(), 0)) {
				// short jump is ok
				b.add(Ext1.QFOR);
				branch_to_end.generateBranch(b, Branch.FORWARD);
				b.flush();
				target = probe.getCurrentPosition();
				body.generate(b);
				if (b.getCurrentSlot() == (Processor.NO_OF_SLOTS-1))  {
					b.flush();
				}
				diff = Builder.getHighestDifferentBit1(b.getCurrentP(), target);
				if (diff <= Processor.SLOT_BITS) {
					// short next
					b.add(ISA.SNEXT, ((int)target) & Processor.SLOT_MASK);
				}
				else {
					b.add(Ext1.LNEXT);
					b.addAdditionalCell(target);
				}
				b.flush();
				branch_to_end.fixup(b, b.getCurrentPosition(), 0);
				return;
			}
			b.add(Ext1.QFOR);
			branch_to_end.generateBranch(b, Branch.LONG);
			b.flush();
			target = probe.getCurrentPosition();
			body.generate(b);
			if (b.getCurrentSlot() == (Processor.NO_OF_SLOTS-1))  {
				b.flush();
			}
			diff = Builder.getHighestDifferentBit1(b.getCurrentP(), target);
			if (diff <= Processor.SLOT_BITS) {
				// short next
				b.add(ISA.SNEXT, ((int)target) & Processor.SLOT_MASK);
			}
			else {
				b.add(Ext1.LNEXT);
				b.addAdditionalCell(target);
			}
			b.flush();
			branch_to_end.fixup(b, b.getCurrentPosition(), 0);
		}
		else {
			b.add(ISA.PUSH);
			b.flush();
			target = b.getCurrentPosition();
			body.generate(b);
			if ((target == b.getCurrentP()) && (b.getCurrentSlot() < Processor.NO_OF_SLOTS))  {
				b.add(ISA.UNEXT);
				return;
			}
			if (b.getCurrentSlot() == (Processor.NO_OF_SLOTS-1))  {
				b.flush();
			}
			diff = Builder.getHighestDifferentBit1(b.getCurrentP(), target);
			if (diff <= Processor.SLOT_BITS) {
				// short next
				b.add(ISA.SNEXT, ((int)target) & Processor.SLOT_MASK);
				return;
			}
			b.add(Ext1.LNEXT);
			b.addAdditionalCell(target);
		}
	}

}
