package com.F64;

public enum ISA {
	NOP(1,"no operation"),
	EXIT(1,"return from call"),
	UNEXT(1,"decrement R and jump to slot 0 if R is not 0"),
	CONT(1,"move R to I, pop return stack and jump to slot 0"),
	UJMP0(1,"jump to slot 0"),
	UJMP1(1,"jump to slot 1"),
	UJMP2(1,"jump to slot 2"),
	UJMP3(1,"jump to slot 3"),
	UJMP4(1,"jump to slot 4"),
	UJMP5(1,"jump to slot 5"),
	AND(1,"bitwise and"),
	XOR(1,"bitwise exclusive or"),
	DUP(1,"( n - n n)"),
	DROP(1,"( n - )"),
	OVER(1,"( n1 n2 - n1 n2 n1 )"),
	NIP(1,"( n1 n2 - n2 )"),
	// 16
	LIT(2,"literal 0..63 (value in next slot)"),
	NLIT(2,"inverted literal. Push next slot on the stack and invert all bits"),
	EXT(2,"shift T by 6 bits to the left and fill the lower 6 bits with the value from the next slot"),
	NEXT(-2,"decrement R and branch if R is not 0 (next slot contains the conditional slot specifier)"),
	BRANCH(-2,"branch (next slot contains the conditional slot specifier)"),
	CALL(-1,"call (address replacement in remaining slots)"),
	CALLM(-1,"call a method in current MT (method # in remaining slots)"),
	SJMP(1,"short jump to slot 0 (lowest 6 bits of address in next slot)"),
	SAVE(1,"Save SELF on return stack and replace it with T. Also load new MT"),
	RESTORE(1,"Restore SELF from return stack. Also restore MT"),
	USKIP(1,"skip all remaining slots"),
	UJMP6(1,"jump to slot 6"),
	UJMP7(1,"jump to slot 7"),
	UJMP8(1,"jump to slot 8"),
	UJMP9(1,"jump to slot 9"),
	UJMP10(1,"jump to slot 10"),
	// 32
	SWAP(3,"swap register (register in next 2 slots)"),
	MOV(3,"move source register (next slot) to destination register (next slot+1)"),
	ENTER(1,"Enter secondary. Push P on return stack and mov W to P."),
//	LEAVE(1,"Leave secondary. Pop P from return stack."),
	LOADSELF(1,"push SELF on return stack and load SELF with T"),
	LOADMT(1,"load MT with the value designated by SELF"),
	RFETCH(2,"fetch register (register in next slot)"),
	RSTORE(2,"store register (register in next slot)"),
	LFETCH(2,"fetch local register (register in next slot)"),
	LSTORE(2,"store local register (register in next slot)"),
	INC(2,"increment register (register in next slot)"),
	DEC(2,"decrement register (register in next slot)"),
	OR(1,"bitwise or"),
//	MIN(1,"minimum"),//	MAX(1,"maximum"),
	FETCHPINC(1,"fetch via register P post-increment"),
	STOREPINC(1,"store via register P post-increment"),
	// 48
	ADD(1,"+"),
	SUB(1,"-"),
	MUL2(1,"2*"),
	DIV2(1,"2/"),
	PUSH(1,">r"),
	POP(1,"r>"),
	EXT1(0,"code extension 1"),
	EXT2(0,"code extension 2"),
	EXT3(0,"code extension 3"),
	EXT4(0,"code extension 4"),
	EXT5(0,"code extension 5"),
	EXT6(0,"code extension 6"),
	REGOP1(3,"register operation with 1 operand (operation in next slot, destination in next slot+1)"),
	REGOP2(4,"register operation with 2 operand (operation in next slot, destination in next slot+1, source in next slot+2)"),
	REGOP3(5,"register operation with 3 operands (operation in next slot, destination in next slot+1, source 1 in next slot+2, source 2 in next slot+3)"),
	SIMD(6,"SIMD operation set 1 (operation in next slot, additional parmater in next slot+1, destination in next slot+2, source 1 in next slot+3, source 2 in next slot+4)");

	private int size;
	private String tooltip;

	private ISA(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

	
}
