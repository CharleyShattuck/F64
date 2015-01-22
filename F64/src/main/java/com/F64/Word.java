package com.F64;

public abstract class Word {

	public boolean isInline() {return false;}

	public abstract void execute(Interpreter i);
	public abstract void compile(Interpreter i);
}
