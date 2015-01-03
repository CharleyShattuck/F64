package com.F64;

public enum Condition {
	ALWAYS,
	EQ0,		// T == 0. T is consumed
	GE0,		// T >= 0. T is consumed
	CARRY,		// carry flag set
	NEVER;		// cannot be encoded. This condition is only used during compilation.
}
