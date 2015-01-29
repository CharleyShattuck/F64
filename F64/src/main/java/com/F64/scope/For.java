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
	private com.F64.Block				body;

	public For(Compiler c)
	{
		super(c.getScope());
		unroll_limit = 20;
		body = new com.F64.Block(this);
		c.setScope(body);	
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
		long data;
		int t_cnt = body.countInstructions();
		if (count_valid) {
			if (t_cnt == 0) {return;}
			b.addLiteral(count);
		}
		if (t_cnt == 0) {
			b.add(ISA.DROP);
			return;
		}
		b.add(ISA.PUSH);
		b.flush();
		long target = b.getCurrentPosition();
		body.generate(b);
		if ((target == b.getCurrentP()) && (b.getCurrentSlot() <= Processor.FINAL_SLOT))  {
			b.add(ISA.UNEXT);
			return;
		}
		if (b.getCurrentSlot() == Processor.FINAL_SLOT)  {
			b.flush();
		}
		int diff = Builder.getHighestDifferentBit1(b.getCurrentP(), target);
		int bits = Processor.getSlotBits(b.getCurrentSlot()+1);

		if (diff <= bits) {
			// short next
			b.add(ISA.SNEXT, (int)(Processor.getSlotMask(b.getCurrentSlot()+1) & target));
			return;
		}
		bits = Builder.getRemainingBits(b.getCurrentSlot()+2);
		if (diff <= bits) {
			// remaining next
			b.add(Ext1.RNEXT);
			long mask = Builder.getAddressMask(b.getCurrentSlot());
			if (b.doesGenerate()) {
				data = b.getCurrentCell();
				data = data ^ ((data ^ target) & mask);
				b.setCurrentCell(data);
			}
			b.flush();
			return;
		}
		b.add(Ext1.LNEXT);
		b.addAdditionalCell(target);
	}

}
