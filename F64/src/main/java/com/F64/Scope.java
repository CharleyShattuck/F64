package com.F64;

public class Scope extends Codepoint {
	private Codepoint							head;
	private Codepoint							tail;
	private java.util.ArrayList<Precondition>	preconditions;
	
	public Codepoint getHead() {return head;}
	public Codepoint getTail() {return tail;}

	public Scope(Scope parent)
	{
		this.setOwner(parent);
	}

	public void addPrecondition(Precondition pc)
	{
		if (preconditions == null) {
			preconditions = new java.util.ArrayList<Precondition>();
		}
		preconditions.add(pc);
	}
	
	public boolean hasPrecondition(Codepoint cp, Precondition pc)
	{
		if ((preconditions != null) && (head == cp)) {
			int limit = preconditions.size();
			for (int i=0; i<limit; ++i) {
				if (preconditions.get(i) == pc) {
					return true;
				}
			}
		}
		return false;
	}

	public void add(Codepoint cp)
	{
		cp.setOwner(this);
		if (head == null) {
			head = tail = cp;
		}
		else {
			cp.setPrevious(tail);
			tail.setNext(cp);
			tail = cp;
		}
	}
	
	public void remove(Codepoint cp)
	{
		assert(cp.getOwner() == this);
		Codepoint p = cp.getPrevious();
		Codepoint n = cp.getNext();
		if (head == cp) {head = n;}
		if (tail == cp) {tail = p;}
		if (p != null) {p.setNext(n);}
		if (n != null) {n.setPrevious(p);}
	}

	public void replace(Codepoint cp, Codepoint new_cp)
	{
		assert(cp.getOwner() == this);
		Codepoint p = cp.getPrevious();
		Codepoint n = cp.getNext();
		new_cp.setOwner(this);
		new_cp.setNext(n);
		new_cp.setPrevious(p);
		if (head == cp) {head = new_cp;}
		if (tail == cp) {tail = new_cp;}
		if (p != null) {p.setNext(new_cp);}
		if (n != null) {n.setPrevious(new_cp);}
		
	}

	@Override
	public boolean optimize(Optimization opt)
	{
		boolean res = false;
		boolean optimized = true;
		while (optimized) {
			optimized = false;
			Codepoint cp = head;
			while (cp != null) {
				Codepoint n = cp.getNext();
				if (cp.optimize(opt)) {optimized = true;}
				cp = n;
			}
			if (optimized) {res = true;}
		}
		return res;
	}

	@Override
	public void generate(Compiler c)
	{
		Codepoint cp = head;
		while (cp != null) {
			cp.generate(c);
			cp = cp.getNext();
		}
	}

	
}
