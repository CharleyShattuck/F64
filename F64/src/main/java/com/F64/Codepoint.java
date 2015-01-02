package com.F64;

public class Codepoint {
	private Scope			scope;			// scope of this code-point
	private Codepoint		next;			// next instruction
	private Codepoint		prev;			// previous instruction
	private long			arg;			// arguments for this opcode
	private int				no_args;		// # of arguments
	private ISA				opcode;			// first opcode
	private Ext1			extension1;		// opcode extension 1
//	private Codepoint[]		references;		// flag indication that this code-point is the target of a reference
//	private int				ref_cnt;

	public void setScope(Scope n) {scope = n;}
	public Scope getScope() {return scope;}
	public void setNext(Codepoint n) {next = n;}
	public Codepoint getNext() {return next;}
	public void setPrevious(Codepoint n) {prev = n;}
	public Codepoint getPrevious() {return prev;}
//	public boolean isReferenced() {return references != null;}

	public Codepoint()
	{
	}

	public Codepoint(ISA op)
	{
		this.opcode = op;
		this.no_args = 0;
	}

	public Codepoint(ISA op, int arg0)
	{
		this.opcode = op;
		this.no_args = 1;
		this.arg = Processor.writeSlot(0, 0, arg0);
	}

	public Codepoint(ISA op, int arg0, int arg1)
	{
		this.opcode = op;
		this.no_args = 2;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
	}

	public Codepoint(ISA op, int arg0, int arg1, int arg2)
	{
		this.opcode = op;
		this.no_args = 3;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
	}

	public Codepoint(ISA op, int arg0, int arg1, int arg2, int arg3)
	{
		this.opcode = op;
		this.no_args = 4;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
		this.arg = Processor.writeSlot(this.arg, 3, arg3);
	}

	public Codepoint(ISA op, int arg0, int arg1, int arg2, int arg3, int arg4)
	{
		this.opcode = op;
		this.no_args = 5;
		this.arg = Processor.writeSlot(0, 0, arg0);
		this.arg = Processor.writeSlot(this.arg, 1, arg1);
		this.arg = Processor.writeSlot(this.arg, 2, arg2);
		this.arg = Processor.writeSlot(this.arg, 3, arg3);
		this.arg = Processor.writeSlot(this.arg, 4, arg4);
	}

	public Codepoint(Ext1 ex)
	{
		this.opcode = ISA.EXT1;
		this.extension1 = ex;
		this.no_args = 0;
	}

//	public void addReference(Codepoint p)
//	{
//		if (references == null) {
//			references = new Codepoint[1];
//			ref_cnt = 1;
//		}
//		else {
//			int limit = references.length;
//			if (ref_cnt >= limit) {
//				Codepoint[] new_ref = new Codepoint[limit+limit];
//				java.lang.System.arraycopy(references, 0, new_ref, 0, ref_cnt);
//				references = new_ref;
//			}
//		}
//		references[ref_cnt++] = p;
//	}
//
//	public boolean removeReference(Codepoint p)
//	{
//		boolean found = false;
//		int i = 0;
//		int j = 0;
//		while (i<ref_cnt) {
//			if (references[i] == p) {
//				found = true;
//				++i;
//				continue;
//			}
//			if (i != j) {
//				references[j] = references[i];
//			}
//			++i;
//			++j;
//		}
//		return found;
//	}

//	/**
//	 * Informs a referencing code-point that a reference has changed
//	 * @param old_value
//	 * @param new_value
//	 */
//	public void changePointer(Codepoint old_cp, Codepoint new_cp)
//	{
//	}
	
	public boolean optimize(Optimization opt)
	{
		return false;
	}
	
	public void generate(Compiler c)
	{
		
	}

	/**
	 * Replace this with a new code-point
	 * @param new_cp
	 */
	public void replaceWith(Codepoint new_cp)
	{
		scope.replace(this, new_cp);
	}
	
	
}
