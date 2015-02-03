package com.F64.scope;

import com.F64.Branch;
import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Condition;
import com.F64.Optimization;
import com.F64.Processor;

public class Begin extends com.F64.Block implements java.lang.Cloneable {
	private com.F64.Block									first_part;
	private com.F64.ConditionalBranch						branch_to_begin;
	private java.util.ArrayList<com.F64.ConditionalBranch>	branch_list;
	private java.util.ArrayList<com.F64.Block>				block_list;

	public Begin(Compiler c)
	{
		super(c.getScope());
		first_part = new com.F64.Block(this);
		c.setScope(first_part);	
	}

	public Begin clone() throws CloneNotSupportedException
	{
		int i,limit;
		Begin res = (Begin)super.clone();
		if (first_part != null) {
			res.first_part = first_part.clone();
			res.first_part.setOwner(res);
		}
		if (branch_to_begin != null) {
			res.branch_to_begin = branch_to_begin.clone();
		}
		if (branch_list != null) {
			limit = branch_list.size();
			res.branch_list = new java.util.ArrayList<com.F64.ConditionalBranch>(limit);
			for (i=0; i<limit; ++i) {
				res.branch_list.add(branch_list.get(i).clone());
			}
		}
		if (block_list != null) {
			limit = block_list.size();
			res.block_list = new java.util.ArrayList<com.F64.Block>(limit);
			for (i=0; i<limit; ++i) {
				com.F64.Block blk = block_list.get(i).clone();
				res.block_list.add(blk);
				blk.setOwner(res);
			}
		}
		return res;
	}

	public void doWhile(Compiler c, Condition cc)
	{
		com.F64.ConditionalBranch cond = new com.F64.ConditionalBranch(cc);
		com.F64.Block blk = new com.F64.Block(this);
		if (branch_list == null) {
			branch_list = new java.util.ArrayList<com.F64.ConditionalBranch>();
		}
		if (block_list == null) {
			block_list = new java.util.ArrayList<com.F64.Block>();
			cond.setPrecedingBlock(first_part);
		}
		else {
			cond.setPrecedingBlock(block_list.get(block_list.size()-1));
		}
		branch_list.add(cond);
		block_list.add(blk);
		c.setScope(blk);	
	}

	public void doRepeat(Compiler c, Condition cond)
	{
		branch_to_begin = new com.F64.ConditionalBranch(cond);
		c.setScope(this.getOwner());	
	}


	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		int i, limit;
		boolean res = false;
		if (super.optimize(c, opt)) {res = true;}
		if (first_part.optimize(c, opt)) {res = true;}
		if (block_list != null) {
			limit = block_list.size();
			for (i=0; i<limit; ++i) {
				if (block_list.get(i).optimize(c, opt)) {res = true;}
				if (branch_list.get(i).optimize(c, opt)) {res = true;}
			}
			if (opt == Optimization.DEAD_CODE_ELIMINATION) {
				for (i=0; i<limit; ++i) {
					switch (branch_list.get(i).getCondition()) {
					case ALWAYS:
						while ((limit-1) > i) {
							--limit;
							block_list.remove(limit);
							branch_list.remove(limit);
							res = true;
						}
						break;
					case NEVER:
						if (i == 0) {
							first_part.append(block_list.get(i));
						}
						else {
							block_list.get(i-1).append(block_list.get(i));
						}
						block_list.remove(i);
						branch_list.remove(i);
						--i;
						res = true;
						break;
					default:
						break;
					
					}
				}
			}
		}
		return res;
	}
	
	@Override
	public void generate(Builder b)
	{
		long cpos, target;
		int additional, slot, limit, i;
		int first_cnt = first_part.countInstructions();
		int single_cnt;
		boolean empty = (block_list == null) || (block_list.size() == 0);
		boolean single = (block_list != null) && (block_list.size() == 1);
		Builder probe = null;


		if (empty) {
			slot = b.getCurrentSlot();
			if (Builder.backwardBranchCanBeImplicit(slot+first_cnt)) {
				// try pattern 1 (simple loopback)
				//						+-----------------------+
				//						v						|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//	|		|		|	+	|	+	|	+	| UJMPn	|		|		|		|		|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+

				probe = b.fork(false);
				cpos = probe.getCurrentPosition();
				additional = probe.getAdditionalDataSize();
				first_part.generate(probe);
				branch_to_begin.generateBranch(probe, Branch.values()[slot]);
				// test if code is still in same cell and no more additional data has been added
				if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
					// pattern 1 fit
					first_part.generate(b);
					branch_to_begin.generateBranch(b, Branch.values()[slot]);
					return;
				}
			}
			
			if ((slot > 0) && Builder.backwardBranchCanBeImplicit(first_cnt)) {
				// try pattern 2 (simple loopback to slot 0)
				//		+---------------------------------------+
				//		v										|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//	|	+	|	+	|	+	|	+	|	+	| UJMPn	|		|		|		|		|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+

				b.flush();
				probe = b.fork(false, probe);
				slot = b.getCurrentSlot();
				cpos = probe.getCurrentPosition();
				additional = probe.getAdditionalDataSize();
				first_part.generate(probe);
				branch_to_begin.generateBranch(probe, Branch.values()[slot]);
				// test if code is still in same cell and no more additional data has been added
				if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
					first_part.generate(b);
					branch_to_begin.generateBranch(b, Branch.values()[slot]);
					return;
				}
			}
		}
		if (single) {
			// 1 branch
			slot = b.getCurrentSlot();
			single_cnt = block_list.get(0).countInstructions();
			if (Builder.backwardBranchCanBeImplicit(slot + first_cnt + single_cnt + 2)) {
				// try pattern 3 (1 jump + loopback)
				//				+-------------------------------------------------------+
				//				v														|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//	|		|	+	|	+	|	+	|BRANCH	| JMP	|	+	|	+	| UJMPn	|		|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//												|								^
				//												+-------------------------------+
				probe = b.fork(false, probe);
				cpos = probe.getCurrentPosition();
				additional = probe.getAdditionalDataSize();
				first_part.generate(probe);
				branch_list.get(0).generateBranch(probe, Branch.SKIP);
				block_list.get(0).generate(probe);
				branch_to_begin.generateBranch(probe, Branch.values()[slot]);
				// test if code is still in same cell and no more additional data has been added
				if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
					first_part.generate(b);
					branch_list.get(0).generateBranch(b, Branch.SKIP);
					block_list.get(0).generate(b);
					branch_to_begin.generateBranch(b, Branch.values()[slot]);
					branch_list.get(0).fixup(b, b.getCurrentPosition(), b.getCurrentSlot());
					return;
				}
			}
			if ((slot > 0) && Builder.backwardBranchCanBeImplicit(first_cnt + single_cnt + 2)) {
				// try pattern 4 (1 jump + loopback to block 0)
				//		+---------------------------------------------------------------+
				//		v																|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//	|	+	|	+	|	+	|	+	|BRANCH	| JMP	|	+	|	+	| UJMPn	|		|
				// -+-------+-------+-------+-------+-------+-------+-------+-------+-------+-------+
				//												|								^
				//												+-------------------------------+
				b.flush();
				slot = b.getCurrentSlot();
				probe = b.fork(false, probe);
				cpos = probe.getCurrentPosition();
				additional = probe.getAdditionalDataSize();
				first_part.generate(probe);
				branch_list.get(0).generateBranch(probe, Branch.SKIP);
				block_list.get(0).generate(probe);
				branch_to_begin.generateBranch(probe, Branch.values()[slot]);
				// test if code is still in same cell and no more additional data has been added
				if ((cpos == probe.getCurrentPosition()) && (additional == probe.getAdditionalDataSize())) {
					first_part.generate(b);
					branch_list.get(0).generateBranch(b, Branch.SKIP);
					block_list.get(0).generate(b);
					branch_to_begin.generateBranch(b, Branch.values()[slot]);
					branch_list.get(0).fixup(b, b.getCurrentPosition(), b.getCurrentSlot());
					return;
				}
			}
		}

		limit = block_list.size();
		b.flush();
		cpos = b.getCurrentPosition();
		// try pattern 5 (all jumps are short)
		probe = b.fork(false, probe);
		first_part.generate(probe);
		for (i=0; i<limit; ++i) {
			branch_list.get(i).generateBranch(probe, Branch.FORWARD);
			block_list.get(i).generate(probe);
		}
		branch_to_begin.generateBranch(probe, Branch.BACK);
		probe.flush();
		target = probe.getCurrentPosition();
		boolean short_is_ok = true;
		for (i=0; i<limit; ++i) {
			if (!branch_list.get(i).fixup(probe, target, 0)) {
				short_is_ok = false;
				break;
			}
		}
		if (short_is_ok) {
			short_is_ok = branch_to_begin.fixup(probe, cpos, 0);
		}
		if (short_is_ok) {
			first_part.generate(b);
			for (i=0; i<limit; ++i) {
				branch_list.get(i).generateBranch(b, Branch.FORWARD);
				block_list.get(i).generate(b);
			}
			branch_to_begin.generateBranch(b, Branch.BACK);
			b.flush();
			target = b.getCurrentPosition();
			for (i=0; i<limit; ++i) {
				branch_list.get(i).fixup(b, target, 0);
			}
			branch_to_begin.fixup(probe, cpos, 0);
			return;
		}

		// pattern 6 (all jumps are long)
		first_part.generate(b);
		for (i=0; i<limit; ++i) {
			branch_list.get(i).generateBranch(b, Branch.LONG);
			block_list.get(i).generate(b);
		}
		branch_to_begin.generateBranch(b, Branch.LONG);
		b.flush();
		target = b.getCurrentPosition();
		for (i=0; i<limit; ++i) {
			branch_list.get(i).fixup(b, target, 0);
		}
		branch_to_begin.fixup(probe, cpos, 0);

	
	}

}
