package com.F64;

public enum Ext1 {
	NOP(2,"no operation"),
	EXITI(3,"return from interrupt (interrupt # in next slot)"),
	ADDC(2,"add with carry"),
	SUBC(2,"subtract with carry"),
	MULS(2,"multiply step"),
	DIVS(2,"divide step"),
	ROL(2,"rotate left and copy most significant bit into carry"),
	ROR(2,"rotate right and copy least significant bit into carry"),
	ROLC(2,"rotate left with carry"),
	RORC(2,"rotate right with carry"),
	SBIT(3,"set bit in T and put old bit in carry (bit position in next slot)"),
	CBIT(3,"clear bit in T and put old bit in carry (bit position in next slot)"),
	TBIT(3,"toggle bit in T and put old bit in carry (bit position in next slot)"),
	RBIT(3,"read bit from T into carry (bit position in next slot)"),
	WBIT(3,"write bit from carry into T and put old bit in carry (bit position in next slot)"),
	SBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	CBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	TBITS(2,"set bit T in S and put old bit in carry. Put result in T and pop stack into S"),
	RBITS(2,"read bit T from S into carry. Put result in T and pop stack into S"),
	WBITS(2,"write bit T from carry into S and put old bit in carry. Put result in T and pop stack into S"),
	ENTERM(2,"push the MT to the return stack and load the MT with the method table from SELF"),
	LCALLM(2,"long method call. Call a method on SELF (T contains the method #)"),
	LJMPM(2,"long method jump. Jump to a method on SELF (T contains the method #)"),
	FETCHAINC(2,"fetch via register A post-increment"),
	STOREBINC(2,"store via register B post-increment"),
	RSBIT(4,"set bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RCBIT(4,"clear bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RTBIT(4,"toggle bit in register and put old bit in carry (register in next slot, bit position in next slot+1)"),
	RRBIT(4,"read bit from register into carry (register in next slot, bit position in next slot+1)"),
	RWBIT(4,"write bit from carry into register (register in next slot, bit position in next slot+1)"),
	RRSBIT(4,"set bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRCBIT(4,"clear bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRTBIT(4,"toggle bit in register and put old bit in carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRRBIT(4,"read bit from register into carry (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	RRWBIT(4,"write bit from carry into register (register in next slot, register with bit position in next slot+1). If the bit position register is S then a nip is appended"),
	JMPIO(3,"jump to I/O address (mask in next slot)"),
	CONFIGFETCH(3,"return processor configuration (selector in next slot) ( - n)");

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
