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
	DUP(1,""),
	DROP(1,""),
	OVER(1,""),
	NIP(1,""),
	LIT(2,"literal 0..63 (value in next slot)"),
	BLIT(2,"bit literal (bit position in next slot)"),
	EXT(2,"shift T by 6 bits to the left and fill the lower 6 bits with the value from the next slot"),
	NEXT(-2,"decrement R and branch if R is not 0 (next slot contains the conditional slot specifier)"),
	BRANCH(-2,"branch (next slot contains the conditional slot specifier)"),
	CALLM(-1,"call a method on SELF (method # in remaining slots)"),
	JMPM(-1,"jump to a method on SELF (method # in remaining slots)"),
	SJMP(1,"short jump to slot 0 (lowest 6 bits of address in next slot)"),
	CALL(-1,"call (address replacement in remaining slots)"),
	JMP(-1,"jump (address replacement in remaining slots)"),
	USKIP(1,"skip all remaining slots"),
	UJMP6(1,"jump to slot 0"),
	UJMP7(1,"jump to slot 0"),
	UJMP8(1,"jump to slot 0"),
	UJMP9(1,"jump to slot 0"),
	UJMP10(1,"jump to slot 0"),
	SWAP(3,"swap register (register in next 2 slots)"),
	SWAP0(3,"swap register and jump to slot 0 (register in next 2 slots)"),
	MOV(3,"move source register (next slot) to destination register (next slot+1)"),
	MOVS(3,"move source register (next slot) to destination register (next slot+1) with stack manipulation"),
	LOADSELF(1,"push SELF on return stack and load SELF with T"),
	LOADMT(1,"load MT with the value designated by SELF"),
	RFETCH(2,"fetch register (register in next slot)"),
	RSTORE(2,"store register (register in next slot)"),
	FETCHR(2,"fetch via register (register in next slot)"),
	STORER(2,"store via register (register in next slot)"),
	RINC(2,"increment register"),
	RDEC(2,"decrement register"),
	RPINC(2,"increment register"),
	RPDEC(2,"decrement register"),
	FETCHPINC(1,"fetch via register P post-increment"),
	STOREPINC(1,"store via register P post-increment"),
	ADD(1,"+"),
	SUB(1,"-"),
	OR(1,"bitwise or"),
	NOT(1,"bitwise invert"),
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
	EXT7(0,"code extension 7"),
	EXT8(0,"code extension 8");

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
