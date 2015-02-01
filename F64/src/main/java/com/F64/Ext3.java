package com.F64;

public enum Ext3 {
//	BITCNT1(4,"count the number of 1 bits in a register (destination register in next slot, source register in next slot+1)"),
//	BITCNT0(4,"count the number of 0 bits in a register (destination register in next slot, source register in next slot+1)"),
	SWAPRS(4,	"swaps",	"swap a register with a system register (register in next slot, system register in next slot+1)"),
	SWAPRL(4,	"swapl",	"swap a register with a local register (register in next slot, local register in next slot+1)"),
	MOVSR(4,	"r->s",		"move a register to a system register (system register in next slot, register in next slot+1)"),
	MOVRS(4,	"s->r",		"move a system register to a register (register in next slot, system register in next slot+1)"),
	MOVLR(4,	"r->l",		"move a register to a local register (local register in next slot, register in next slot+1)"),
	MOVRL(4,	"l->r",		"move a local register to a register (register in next slot, local register in next slot+1)"),
	MOVRI(4,	"r!",		"move a literal to a register (register in next slot, literal in next slot+1)"),
	MOVSI(4,	"s!",		"move a literal to a system register (system register in next slot, literal in next slot+1)"),
	MOVLI(4,	"l!",		"move a literal to a local register (local register in next slot, literal in next slot+1)"),
	ROL(4,		"^<<",		"rotate left and copy most significant bit into carry (register in next slot, shift in next slot+1)"),
	ROR(4,		">>^",		"rotate right and copy least significant bit into carry (register in next slot, shift in next slot+1)"),
	RCL(4,		"^.<<",		"rotate left with carry (register in next slot, shift in next slot+1)"),
	RCR(4,		">>.^",		"rotate right with carry (register in next slot, shift in next slot+1)"),
	EQ0Q(4,		"=0?",		"== 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	NE0Q(4,		"!=0?",		"!= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	GT0Q(4,		">0?",		"> 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	GE0Q(4,		">=0?",		">= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	LT0Q(4,		"<0?",		"< 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	LE0Q(4,		"<=0?",		"<= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	BITFF1(4,	"bff1",		"find first 1 bit in a register (destination register in next slot, source register in next slot+1)"),
	BITFL1(4,	"bfl1",		"find last 1 bit in a register (destination register in next slot, source register in next slot+1)"),
	RSBIT(4,	"bset",		"set bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RCBIT(4,	"bclr",		"clear bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RTBIT(4,	"btgl",		"toggle bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RRBIT(4,	"bread",	"read bit from register into carry (register in next slot, bit position in next slot+1)"),
	RWBIT(4,	"bwrite",	"write bit from carry into register (register in next slot, bit position in next slot+1)"),
	RRSBIT(4,	"bset",		"set bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRCBIT(4,	"bclr",		"clear bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRTBIT(4,	"btgl",		"toggle bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRRBIT(4,	"bread",	"read bit from register into carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRWBIT(4,	"bwrite",	"write bit from carry into register (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	;

	private int size;
	private String display;
	private String tooltip;

	private Ext3(int size, String display, String tooltip)
	{
		this.size = size;
		this.display = display;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}
	public String getDisplay() {return display;}
}
