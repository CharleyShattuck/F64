package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;

public class Drop extends com.F64.Codepoint {

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {
			case CONSTANT_FOLDING:
				if (p instanceof Literal) {
					p.remove();
					this.remove();
					return true;
				}
				break;


			case PEEPHOLE:
				if ((p instanceof Dup) || (p instanceof Over)) {
					// dup drop ->
					// over drop ->
					p.remove();
					this.remove();
					return true;
				}
				if (p instanceof Swap) {
					// swap drop -> nip
					p.replaceWith(new Nip());
					this.remove();
					return true;
				}
				if (p instanceof Under) {
					// under drop -> drop dup
					p.replaceWith(new Drop());
					this.replaceWith(new Dup());
					return true;
				}
				if (p instanceof Nip) {
					// nip drop -> drop drop
					p.replaceWith(new Drop());
					return true;
				}
				if (p instanceof Tuck) {
					// tuck drop -> swap
					p.replaceWith(new Swap());
					this.remove();
					return true;
				}
				break;

			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Builder b)
	{
		b.add(ISA.DROP);
	}

}
