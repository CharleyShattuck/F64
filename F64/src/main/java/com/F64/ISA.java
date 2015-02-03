package com.F64;

public enum ISA {
	NOP(1,		"nop",		"no operation"),
	CALL(-1,	"call",		"call (address replacement in remaining slots)"),
	ENTER(1,	"enter",	"Enter secondary. Push P on return stack and mov W to P."),
	SAVE(1,		"save",		"Save SELF on return stack and replace it with T. Also load new MT"),
	LIT(2,		"lit",		"literal 0..63 (value in next slot)"),
	NLIT(2,		"~lit",		"inverted literal. Push next slot on the stack and invert all bits"),
	REGOP1(3,	"rop1",		"register operation with 1 operand (operation in next slot, destination in next slot+1)"),
	REGOP2(4,	"rop2",		"register operation with 2 operand (operation in next slot, destination in next slot+1, source in next slot+2)"),
	REGOP3(5,	"rop3",		"register operation with 3 operands (operation in next slot, destination in next slot+1, source 1 in next slot+2, source 2 in next slot+3)"),
	SIMD(6,		"simd",		"SIMD operation set 1 (operation in next slot, additional parmater in next slot+1, destination in next slot+2, source 1 in next slot+3, source 2 in next slot+4)"),
	EXT1(0,		"x1",		"code extension 1"),
	EXT2(0,		"x2",		"code extension 2"),
	EXT3(0,		"x3",		"code extension 3"),
	EXT4(0,		"x4",		"code extension 4"),
	EXT5(0,		"x5",		"code extension 5"),
	EXT6(0,		"x6",		"code extension 6"),
	// 16
	UJMP0(1,	"ujmp0",	"jump to slot 0"),
	UJMP1(1,	"ujmp1",	"jump to slot 1"),
	UJMP2(1,	"ujmp2",	"jump to slot 2"),
	UJMP3(1,	"ujmp3",	"jump to slot 3"),
	UJMP4(1,	"ujmp4",	"jump to slot 4"),
	UJMP5(1,	"ujmp5",	"jump to slot 5"),
	UJMP6(1,	"ujmp6",	"jump to slot 6"),
	UJMP7(1,	"ujmp7",	"jump to slot 7"),
	UJMP8(1,	"ujmp8",	"jump to slot 8"),
	UJMP9(1,	"ujmp9",	"jump to slot 9"),
	UJMP10(1,	"ujmp10",	"jump to slot 10"),
	USKIP(1,	"uskip",	"skip all remaining slots"),
	CONT(1,		"cont",		"move R to I, pop return stack and jump to slot 0"),
	BRANCH(-2,	"branch",	"branch (next slot contains the conditional slot specifier)"),
	FJMP(2,		"fjmp",		"forward short jump to slot 0 (next slot (0=64) will be added to P)"),
	BJMP(2,		"bjmp",		"back short jump to slot 0 (next slot (0=64) will be subtracted from P)"),
	// 32
	INC(2,		"++",		"increment register (register in next slot)"),
	DEC(2,		"--",		"decrement register (register in next slot)"),
	ADD(1,		"+",		"+"),
	SUB(1,		"-",		"-"),
	MUL2(1,		"2*",		"2*"),
	DIV2(1,		"2/",		"2/"),
	AND(1,		"and",		"bitwise and"),
	OR(1,		"or",		"bitwise or"),
	XOR(1,		"xor",		"bitwise exclusive or"),
	RESTORE(1,	"restore",	"restore SELF from return stack. Also restore MT"),
	DUP(1,		"dup",		"( n - n n)"),
	DROP(1,		"drop",		"( n - )"),
	OVER(1,		"over",		"( n1 n2 - n1 n2 n1 )"),
	NIP(1,		"nip",		"( n1 n2 - n2 )"),
	PUSH(1,		">r",		">r"),
	POP(1,		"r>",		"r>"),
	// 48
	FETCH(1,	"@",		"@"),
	STORE(1,	"!",		"!"),
	FETCHPINC(1,"@p+",		"fetch via register P post-increment"),
	STOREPINC(1,"!p+",		"store via register P post-increment"),
	LOADSELF(1,	"loadself",	"push SELF on return stack and load SELF with T"),
	LOADMT(1,	"loadmt",	"load MT with the value designated by SELF"),
	UNEXT(1,	"unext",	"decrement R and jump to slot 0 if R is not 0"),
	SNEXT(2,	"snext",	"decrement R and branch if R is not 0 (lowest 6 bits of address in next slot)"),
	SWAP(3,		"swap",		"swap register (register in next 2 slots)"),
	MOV(3,		"mov",		"move source register (next slot) to destination register (next slot+1)"),
	RFETCH(2,	"r@",		"fetch register (register in next slot)"),
	RSTORE(2,	"r!",		"store register (register in next slot)"),
	LFETCH(2,	"l@",		"fetch local register (register in next slot)"),
	LSTORE(2,	"l!",		"store local register (register in next slot)"),
	CALLM(2,	"callm",	"call a method in current MT (method # in next slots)"),
	EXIT(1,		"exit",		"return from call"),
	;

	private int size;
	private String display;
	private String tooltip;

	private ISA(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}

	
}
