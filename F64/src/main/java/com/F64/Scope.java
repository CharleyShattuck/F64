package com.F64;

public class Scope {
	private Codepoint	head;
	private Codepoint	tail;

	public Codepoint getHead() {return head;}
	public Codepoint getTail() {return tail;}

	public void add(Codepoint cp)
	{
		if (head == null) {
			head = tail = cp;
		}
		else {
			cp.setPrevious(tail);
			tail.setNext(cp);
			tail = cp;
		}

	}
	
}
