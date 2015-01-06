package com.F64;

<<<<<<< HEAD
public class Scope extends Codepoint {
=======
public class Scope {
	private Scope		parent;
>>>>>>> refs/remotes/origin/master
	private Codepoint	head;
	private Codepoint	tail;

	public Scope getParent() {return parent;}
	public Codepoint getHead() {return head;}
	public Codepoint getTail() {return tail;}

<<<<<<< HEAD
	public Scope(Scope parent)
	{
		this.setOwner(parent);
=======
	public Scope()
	{
	}
	
	public Scope(Scope parent)
	{
		this.parent = parent;
>>>>>>> refs/remotes/origin/master
	}

	public void add(Codepoint cp)
	{
<<<<<<< HEAD
		cp.setOwner(this);
=======
		cp.setScope(this);
>>>>>>> refs/remotes/origin/master
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
<<<<<<< HEAD
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
=======
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
		
>>>>>>> refs/remotes/origin/master
	}

	
}
