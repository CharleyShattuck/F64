package com.F64;

public enum Ext2 {
	ROL(3,"rotate left and copy most significant bit into carry (register in next slot)"),
	ROR(3,"rotate right and copy least significant bit into carry (register in next slot)"),
	RCL(3,"rotate left with carry (register in next slot)"),
	RCR(3,"rotate right with carry (register in next slot)"),
	ROLI(3,"rotate left T and copy most significant bit into carry (shift in next slot)"),
	RORI(3,"rotate right T and copy least significant bit into carry (shift in next slot)"),
	RCLI(3,"rotate left T with carry (shift in next slot)"),
	RCRI(3,"rotate right T with carry (shift in next slot)"),
	EQ0Q(3,"== 0 ? ( - flag) (register in next slot)"),
	NE0Q(3,"!= 0 ? ( - flag) (register in next slot)"),
	GT0Q(3,"> 0 ? ( - flag) (register in next slot)"),
	GE0Q(3,">= 0 ? ( - flag) (register in next slot)"),
	LT0Q(3,"< 0 ? ( - flag) (register in next slot)"),
	LE0Q(3,"<= 0 ? ( - flag) (register in next slot)"),
	SFLAG(3,"set flag (flag # in next slot)"),
	CFLAG(3,"clear flag (flag # in next slot)"),
	SBIT(3,"set bit in T and put old bit in carry (bit position in next slot)"),
	CBIT(3,"clear bit in T and put old bit in carry (bit position in next slot)"),
	TBIT(3,"toggle bit in T and put old bit in carry (bit position in next slot)"),
	RBIT(3,"read bit from T into carry (bit position in next slot)"),
	WBIT(3,"write bit from carry into T and put old bit in carry (bit position in next slot)"),
	FETCHINC(3,"fetch via register post-increment (register in next slot)"),
	STOREINC(3,"store via register post-increment (register in next slot)"),
	FETCHR(3,"fetch via register (register in next slot)"),
	STORER(3,"store via register (register in next slot)"),
	FETCHL(3,"fetch via local register (register in next slot)"),
	STOREL(3,"store via local register (register in next slot)"),
	FETCHS(3,"fetch via system register (register in next slot)"),
	STORES(3,"store via system register (register in next slot)"),
	SFETCH(3,"fetch system register (system register in next slot) ( - n)"),
	SSTORE(3,"store system register (system register in next slot) ( n -)"),
	SFETCHI(3,"fetch system register indirect (register in next slot)"),
	SSTOREI(3,"store system register indirect (register in next slot)"),
	FETCHPORT(3,"fetch from port(s) without waiting. The next slot contains the mask with one bit per port."),
	STOREPORT(3,"fetch from port(s) without waiting. The next slot contains the mask with one bit per port."),
	FETCHPORTWAIT(3,"fetch from port(s) with waiting. The next slot contains the mask with one bit per port."),
	STOREPORTWAIT(3,"fetch from port(s) with waiting. The next slot contains the mask with one bit per port."),
	PUSHR(3,"Push register on return stack (register in next slot)"),
	POPR(3,"Pop register from return stack (register in next slot)"),
	PUSHL(3,"Push local register on return stack (register in next slot)"),
	POPL(3,"Pop local register from return stack (register in next slot)"),
	PUSHS(3,"Push system register on return stack (register in next slot)"),
	POPS(3,"Pop system register from return stack (register in next slot)"),
	BLIT(3,"bit literal (bit position in next slot)"),
	JMPIO(3,"jump to I/O address (mask in next slot)"),
	CONFIGFETCH(3,"fetch processor configuration (selector in next slot) ( - n)");

	private int size;
	private String tooltip;

	private Ext2(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
