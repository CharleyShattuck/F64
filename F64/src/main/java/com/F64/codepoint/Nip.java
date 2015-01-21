package com.F64.codepoint;

import com.F64.Builder;
import com.F64.Compiler;
import com.F64.Ext2;
import com.F64.Optimization;
import com.F64.Processor;

public class Nip extends com.F64.Codepoint {

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			switch (opt) {

			case PEEPHOLE:
				if (p instanceof Under) {
					// under nip ->
					p.remove();
					this.remove();
					return true;
				}
				if (p instanceof Swap) {
					// swap nip -> drop
					p.replaceWith(new Drop());
					this.remove();
					return true;
				}
				if (p instanceof Over) {
					// over nip -> drop dup
					p.replaceWith(new Drop());
					this.replaceWith(new Dup());
					return true;
				}
				if (p instanceof Tuck) {
					// tuck nip -> nip dup
					p.replaceWith(new Nip());
					this.replaceWith(new Dup());
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
		b.add(Ext2.UNDER);
		
	}

}
