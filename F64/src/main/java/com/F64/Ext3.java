package com.F64;

public enum Ext3 {
//	BITCNT1(4,"count the number of 1 bits in a register (destination register in next slot, source register in next slot+1)"),
//	BITCNT0(4,"count the number of 0 bits in a register (destination register in next slot, source register in next slot+1)"),
	ROL(4,"rotate left and copy most significant bit into carry (register in next slot, shift in next slot+1)"),
	ROR(4,"rotate right and copy least significant bit into carry (register in next slot, shift in next slot+1)"),
	RCL(4,"rotate left with carry (register in next slot, shift in next slot+1)"),
	RCR(4,"rotate right with carry (register in next slot, shift in next slot+1)"),
	EQ0Q(4,"== 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	NE0Q(4,"!= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	GT0Q(4,"> 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	GE0Q(4,">= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	LT0Q(4,"< 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	LE0Q(4,"<= 0 ? ( - flag) (dest in next slot, src in next slot+1)"),
	BITFF1(4,"find first 1 bit in a register (destination register in next slot, source register in next slot+1)"),
	BITFL1(4,"find last 1 bit in a register (destination register in next slot, source register in next slot+1)"),
	RSBIT(4,"set bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RCBIT(4,"clear bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RTBIT(4,"toggle bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RRBIT(4,"read bit from register into carry (register in next slot, bit position in next slot+1)"),
	RWBIT(4,"write bit from carry into register (register in next slot, bit position in next slot+1)"),
	RRSBIT(4,"set bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRCBIT(4,"clear bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRTBIT(4,"toggle bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRRBIT(4,"read bit from register into carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRWBIT(4,"write bit from carry into register (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended");

	private int size;
	private String tooltip;

	private Ext3(int size, String tooltip)
	{
		this.size = size;
		this.tooltip = tooltip;
	}
	
	public int size() {return size;}
	public String getTooltip() {return tooltip;}

}
