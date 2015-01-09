package com.F64;

public class Codepoint {
	private Scope			owner;			// scope of this code-point
	private Codepoint		next;			// next instruction
	private Codepoint		prev;			// previous instruction

	public void setOwner(Scope n) {owner = n;}
	public Scope getOwner() {return owner;}
	public void setNext(Codepoint n) {next = n;}
	public Codepoint getNext() {return next;}
	public void setPrevious(Codepoint n) {prev = n;}
	public Codepoint getPrevious() {return prev;}
//	public boolean isReferenced() {return references != null;}

	public Codepoint()
	{
	}

	public boolean hasPrecondition(Precondition pc)
	{
		if (owner != null) {
			return owner.hasPrecondition(this, pc);
		}
		return false;
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
		getOwner().replace(this, new_cp);
	}

	public void remove()
	{
		getOwner().remove(this);
	}

	
}
