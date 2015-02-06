package com.F64;

public class Block extends Scope {
	private java.util.ArrayList<Local>		locals;
//	private Compiler						compiler;

	public Block(Scope parent)
	{
		super(parent);
	}

	public void addLocal(Compiler c, String name)
	{
//		compiler = c;
		Local loc = c.requestLocal(name);
		if (locals == null) {
			locals = new java.util.ArrayList<Local>();
		}
		locals.add(loc);
	}
	
	public void releaseLocals(Compiler c)
	{
		if ((locals != null) && (locals.size() > 0)) {
			for (int i=locals.size()-1; i>=0; --i) {
				c.releaseLocal(locals.get(i));
			}
		}
	}

	public Block clone() throws CloneNotSupportedException
	{
		Block res = (Block)super.clone();
		return res;
	}

	@Override
	public boolean optimize(Compiler c, Optimization opt)
	{
		return super.optimize(c, opt);
	}

	public void generateEnterLocals(Builder b)
	{
		if ((locals != null) && (locals.size() > 0)) {
			b.add(Ext3.LSAVE, locals.get(0).getIndex(), locals.get(locals.size()-1).getIndex());
		}
		
//		if( (locals == null) || (locals.size() == 0)) {return;}
//		for (int i=locals.size()-1; i>=0; --i) {
//			Local loc = locals.get(i);
//			b.add(Ext2.PUSHL, loc.getIndex());
//			b.add(ISA.LSTORE, loc.getIndex());
//		}
	}

	public void generateLeaveLocals(Builder b)
	{
		if ((locals != null) && (locals.size() > 0)) {
			b.add(Ext3.LRESTORE, locals.get(0).getIndex(), locals.get(locals.size()-1).getIndex());
		}
//		if( (locals == null) || (locals.size() == 0)) {return;}
//		for (int i=0; i<locals.size(); ++i) {
//			Local loc = locals.get(i);
//			b.add(Ext2.POPL, loc.getIndex());
//		}
		
	}

//	@Override
//	public void generate(Builder b)
//	{
//		int i;
//		if( (locals == null) || (locals.size() == 0)) {
//			super.generate(b);
//		}
//		else {
//			super.generate(b);
//			generateReleaseLocals(b);
////			for (i=locals.size(); i>=0; --i) {
////				compiler.releaseLocal(locals.get(i));
////			}
//		}
//	}

}
