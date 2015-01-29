package com.F64.scope;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.codepoint.Literal;

public class For extends com.F64.Block implements java.lang.Cloneable {
	private int							unroll_limit;
	private int							unroll_cnt;
	private long						count;
	private boolean						count_valid;
	private com.F64.ConditionalBranch	branch_to_end;
	private com.F64.ConditionalBranch	branch_to_loop;
	private com.F64.Block				body;

	public For(Compiler c)
	{
		super(c.getScope());
		c.setScope(this);
		unroll_limit = 20;
		unroll_cnt = -1;
	}

	public void doNext(Compiler c)
	{
		c.setScope(this.getOwner());	
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		boolean res = false;
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
				if (count_valid) {
					if ((count < unroll_limit) && (((count*countInstructions())) <= unroll_limit)) {
						com.F64.Scope replacement = new com.F64.Scope(this);
						for (int i=0; i<count; ++i) {
							replacement.append(this);
						}
						replaceWithScope(replacement);
						return true;
					}
				}
			}
		}
		if (opt == Optimization.DEAD_CODE_ELIMINATION) {
			if (this.isEmpty()) {
				replaceWith(new com.F64.codepoint.Drop());
				return true;
			}
		}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
		if (count_valid) {
			b.addLiteral(count);
		}
		b.add(ISA.PUSH);
		b.flush();
		long target = b.getCurrentPosition();
		super.generate(b);
		long cp = b.getCurrentP();
		int cs = b.getCurrentSlot();
		if ((cp == target) && (cs <= Processor.FINAL_SLOT)) {
			// same cell and there is room for micro-next instruction
			b.add(ISA.UNEXT);
		}
		else {
			int diff1 = Builder.getHighestDifferentBit1(target, cp);
			if (diff1 <= Processor.getSlotBits(cs+2)) {
				// we can take the short jump
				b.add(ISA.NEXT, Condition.ALWAYS.encode(Branch.SHORT), (int)(target & Processor.SLOT_MASK));
			}
			else if (diff1 <= Builder.getRemainingBits(cs+1)) {
				// we can use the remaining bits for a short
				b.add(ISA.NEXT, Condition.ALWAYS.encode(Branch.REM));
				b.finishCell(target & Builder.getAddressMask(cs+1));
			}
			else {
				// we must use the long variant
				b.add(ISA.NEXT, Condition.ALWAYS.encode(Branch.LONG));
				b.addAdditionalCell(target);
			}
		}
	}

}
