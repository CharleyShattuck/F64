package com.F64;

public class Block extends Scope {
	java.util.ArrayList<Local>		locals_;

	public Block(Scope parent)
	{
		super(parent);
	}

	public Block clone() throws CloneNotSupportedException
	{
		Block res = (Block)super.clone();
		return res;
	}

}
