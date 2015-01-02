package com.F64;

public class Scope {
	private Scope		parent;
	private Codepoint	head;
	private Codepoint	tail;

	public Scope getParent() {return parent;}
	public Codepoint getHead() {return head;}
	public Codepoint getTail() {return tail;}

	public Scope()
	{
	}
	
	public Scope(Scope parent)
	{
		this.parent = parent;
	}

	public void add(Codepoint cp)
	{
		cp.setScope(this);
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
		assert(cp.getScope() == this);
		Codepoint p = cp.getPrevious();
		Codepoint n = cp.getNext();
		if (head == cp) {head = n;}
		if (tail == cp) {tail = p;}
		if (p != null) {p.setNext(n);}
		if (n != null) {n.setPrevious(p);}
	}

	public void replace(Codepoint cp, Codepoint new_cp)
	{
		assert(cp.getScope() == this);
		Codepoint p = cp.getPrevious();
		Codepoint n = cp.getNext();
		new_cp.setScope(this);
		new_cp.setNext(n);
		new_cp.setPrevious(p);
		if (head == cp) {head = new_cp;}
		if (tail == cp) {tail = new_cp;}
		if (p != null) {p.setNext(new_cp);}
		if (n != null) {n.setPrevious(new_cp);}
		
	}

	
}
