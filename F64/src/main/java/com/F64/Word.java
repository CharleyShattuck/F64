package com.F64;

public abstract class Word {
	public abstract boolean isImmediate();
	public abstract void execute(Processor p);
	public abstract void compile(Compiler c);
}
