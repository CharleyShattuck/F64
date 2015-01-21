package com.F64;

public enum Ext1 {
	RDROP(2,"drop R"),
	RDUP(2,"dup R"),
	EXECUTE(2,"Execute word. Move T to I"),
	EXITI(3,"return from interrupt (interrupt # in next slot)"),
	SWAP0(4,"swap register and jump to slot 0 (register in next 2 slots)"),
	MIN(2,"minimum"),
	MAX(2,"maximum"),
	ADDC(2,"add with carry"),
	SUBC(2,"subtract with carry"),
	ROL(2,"rotate S left by T and copy most significant bit into carry"),
	ROR(2,"rotate S right by T and copy least significant bit into carry"),
	RCL(2,"rotate S left by T with carry"),
	RCR(2,"rotate S right by T with carry"),
	TUCK(2,"tuck ( n1 n2 - n2 n1 n2)"),
	UNDER(2,"under ( n1 n2 - n1 n1 n2)"),
	MULS(2,"multiply step"),
	DIVS(2,"divide step"),
	MDP(2,"multiply & divide prepare"),
	MULF(2,"multiply finished"),
	DIVMODF(2,"division & modulo finished"),
	CARRYQ(2,"return true if carry flag is set ( - flag)"),
	EQ0Q(2,"T == 0 ? ( n - flag)"),
	NE0Q(2,"T != 0 ? ( n - flag)"),
	GT0Q(2,"T > 0 ? ( n - flag)"),
	GE0Q(2,"T >= 0 ? ( n - flag)"),
	LT0Q(2,"T < 0 ? ( n - flag)"),
	LE0Q(2,"T <= 0 ? ( n - flag)"),
	FETCHRES(2,"fetch and reserve memory location"),
	STORECOND(2,"store conditionally a reserved memory location"),
	SBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	CBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	TBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	RBITS(2,"read bit T from S into carry. Put result in T and pop stack into S"),
	WBITS(2,"write bit T from carry into S and put old bit in carry. Put result in T and pop stack into S"),
	ENTERM(2,"push the MT to the return stack and load the MT with the method table from SELF"),
	LCALLM(2,"long method call. Call a method on SELF (T contains the method #)"),
	LJMPM(2,"long method jump. Jump to a method on SELF (T contains the method #)");

	private int size;
	private String tooltip;

	private Ext1(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
}
