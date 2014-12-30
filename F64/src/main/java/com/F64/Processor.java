package com.F64;

public class Processor {
	private System	sys;
	private long[]	register;
	private int		slot;
	private int		max_slot;	// max # of slots
	public static final int BITS_PER_CELL = 64;
	public static final int SLOT_BITS = 6;
	public static final int SLOT_SIZE = 1 << SLOT_BITS;
	public static final int SLOT_MASK = SLOT_SIZE - 1;
	public static final int FINAL_SLOT_BITS = 4;
	public static final int FINAL_SLOT_SIZE = 1 << FINAL_SLOT_BITS;
	public static final int FINAL_SLOT_MASK = FINAL_SLOT_SIZE - 1;
	public static final int FINAL_SLOT = 10;
	public static final int NO_OF_SLOTS = FINAL_SLOT+1;
	
	
	public Processor(System sys)
	{
		this.sys = sys;
		this.register = new long[SLOT_SIZE];
		this.register[Register.Z.ordinal()] = 0;
		this.register[Register.INTE.ordinal()] = Flag.RESET.getMask() | Flag.NMI.getMask();
		//this.setInterruptFlag(Register.INTF, Interrupt.Reset, true);
		max_slot = 0;
		int i, size;
		for (i=0; i<ISA.values().length; ++i) {
			size = ISA.values()[i].size();
			if (size < 0) {size = -size;}
			if (size > max_slot) {max_slot = size;}
		}
		for (i=0; i<Ext1.values().length; ++i) {
			size = Ext1.values()[i].size();
			if (size < 0) {size = -size;}
			if (size > max_slot) {max_slot = size;}
		}
	}

	public int getSlot() {return this.slot;}
	public int getMaxSlot() {return this.max_slot;}

	public void setSlot(int reg, int slot, int value)
	{
		long tmp1 = value & SLOT_MASK;
		tmp1 <<= (BITS_PER_CELL-(slot*SLOT_BITS));
		long tmp2 = SLOT_MASK;
		tmp2 <<= (BITS_PER_CELL-(slot*SLOT_BITS));
		tmp2 = ~tmp2;
		tmp2 &= this.getRegister(reg);
		this.setRegister(reg, tmp1 | tmp2);
	}

	public int getSlot(int reg, int slot)
	{
		int res = 0;
		long tmp = this.getRegister(reg);
		if (slot < FINAL_SLOT) {
			res = ((int)(tmp >> (BITS_PER_CELL-(slot*SLOT_BITS)))) & SLOT_MASK;
		}
		else if (slot == FINAL_SLOT) {
			res = ((int)tmp) & FINAL_SLOT_MASK;
		}
		return res;
	}

	public int getSlot(int slot)
	{
		int res = 0;
		if (slot < FINAL_SLOT) {
			res = ((int)(this.register[Register.I.ordinal()] >> (BITS_PER_CELL-(slot*SLOT_BITS)))) & SLOT_MASK;
		}
		else if (slot == FINAL_SLOT) {
			res = ((int)(this.register[Register.I.ordinal()])) & FINAL_SLOT_MASK;
		}
		return res;
	}

	public int nextSlot()
	{
		int res = 0;
		if (slot < FINAL_SLOT) {
			res = ((int)(this.register[Register.I.ordinal()] >> (BITS_PER_CELL-(slot*SLOT_BITS)))) & SLOT_MASK;
			++slot;
		}
		else if (slot == FINAL_SLOT) {
			res = ((int)(this.register[Register.I.ordinal()])) & FINAL_SLOT_MASK;
			++slot;
		}
		return res;
	}

	public long getRegister(int reg)
	{
		long res = register[reg];
		if (reg == Register.FLAGS.ordinal()) {
			long mask = this.slot;
			mask <<= (BITS_PER_CELL - SLOT_BITS);
			res |= mask;
		}
		return res;
	}

	public long getRegister(Register reg)
	{
		long res = register[reg.ordinal()];
		if (reg == Register.FLAGS) {
			long mask = this.slot;
			mask <<= (BITS_PER_CELL - SLOT_BITS);
			res |= mask;
		}
		return res;
	}
	
	public void setRegister(int reg, long value)
	{
		if (reg > 0) {
			if (reg == Register.INTE.ordinal()) {
				value |= Flag.RESET.getMask() | Flag.NMI.getMask();
			}
			register[reg] = value;
		}
	}

	public void setRegister(Register reg, long value)
	{
		if (reg != Register.Z) {
			if (reg == Register.INTE) {
				value |= Flag.RESET.getMask() | Flag.NMI.getMask();
			}
			register[reg.ordinal()] = value;
		}
	}

	public boolean getFlag(int fl)
	{
		long mask = 1;
		mask <<= fl;
		return (this.register[Register.FLAGS.ordinal()] & mask) != 0;
	}

	public boolean getFlag(Flag fl)
	{
		return (this.register[Register.FLAGS.ordinal()] & fl.getMask()) != 0;
	}
	
	public void setFlag(int fl, boolean value)
	{
		long mask = 1;
		mask <<= fl;
		if (value) {
			this.register[Register.FLAGS.ordinal()] |= mask;
		}
		else {
			this.register[Register.FLAGS.ordinal()] &= ~mask;			
		}
	}

	public void setFlag(Flag fl, boolean value)
	{
		if (value) {
			this.register[Register.FLAGS.ordinal()] |= fl.getMask();
		}
		else {
			this.register[Register.FLAGS.ordinal()] &= ~fl.getMask();			
		}
	}


	public boolean getFlag(Register reg, Flag fl)
	{
		return (this.register[reg.ordinal()] & fl.getMask()) != 0;
	}

	public boolean getInterruptFlag(Register reg, int fl)
	{
		long mask = 1;
		mask <<= fl;
		return (this.register[reg.ordinal()] & mask) != 0;
	}

	public void setFlag(Register reg, Flag fl, boolean value)
	{
		if (value) {
			this.register[reg.ordinal()] |= fl.getMask();
		}
		else if (reg != Register.INTE) {
			this.register[reg.ordinal()] &= ~fl.getMask();			
		}
		else {
			this.register[Register.INTE.ordinal()] &= ~fl.getMask();			
			this.register[Register.INTE.ordinal()] |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
	}

	public void setInterruptFlag(Register reg, int fl, boolean value)
	{
		long mask = 1;
		mask <<= fl;
		if (value) {
			this.register[reg.ordinal()] |= mask;
		}
		else if (reg != Register.INTE) {
			this.register[reg.ordinal()] &= ~mask;			
		}
		else {
			this.register[Register.INTE.ordinal()] &= ~mask;			
			this.register[Register.INTE.ordinal()] |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
	}


	public void pushStack(long value)
	{
		if (this.register[Register.SP.ordinal()] == this.register[Register.S0.ordinal()]) {
			this.register[Register.SP.ordinal()] = this.register[Register.SL.ordinal()];
			this.setFlag(Flag.SOVER, true);
		}
		else {
			--this.register[Register.SP.ordinal()];
		}
		sys.setStackMemory(this.register[Register.SP.ordinal()], value);
	}

	public void pushReturnStack(long value)
	{
		if (this.register[Register.RP.ordinal()] == this.register[Register.R0.ordinal()]) {
			this.register[Register.RP.ordinal()] = this.register[Register.RL.ordinal()];
			this.setFlag(Flag.ROVER, true);
		}
		else {
			--this.register[Register.RP.ordinal()];
		}
		sys.setReturnStackMemory(this.register[Register.RP.ordinal()], value);
	}

	public long popStack()
	{
		long res = sys.getStackMemory(this.register[Register.SP.ordinal()]);
		if (this.register[Register.SP.ordinal()] == this.register[Register.SL.ordinal()]) {
			this.register[Register.SP.ordinal()] = this.register[Register.S0.ordinal()];
			this.setFlag(Flag.SUNDER, true);
		}
		else {
			++this.register[Register.SP.ordinal()];
		}
		return res;
	}
	
	public long popReturnStack()
	{
		long res = sys.getReturnStackMemory(this.register[Register.RP.ordinal()]);
		if (this.register[Register.RP.ordinal()] == this.register[Register.RL.ordinal()]) {
			this.register[Register.RP.ordinal()] = this.register[Register.R0.ordinal()];
			this.setFlag(Flag.RUNDER, true);
		}
		else {
			++this.register[Register.RP.ordinal()];
		}
		return res;
	}

//	public void storeRegister(int reg, long value)
//	{
//		if (reg != Register.Z.ordinal()) {
//			if (reg == Register.INTE.ordinal()) {
//				value |= Interrupt.Reset.getMask();
//			}
//			this.register[reg] = value;
//		}
//	}
//
//	public void storeRegister(Register reg, long value)
//	{
//		if (reg != Register.Z) {
//			if (reg == Register.INTE) {
//				value |= Interrupt.Reset.getMask();
//			}
//			this.register[reg.ordinal()] = value;
//		}
//	}

	/**
	 * Increment register
	 */
	public void inc(int reg)
	{
		this.setRegister(reg, this.getRegister(reg)+1);
	}


	/**
	 * Increment register
	 */
	public void dec(int reg)
	{
		this.setRegister(reg, this.getRegister(reg)-1);
	}

	/**
	 * Increment register. Do nothing if the register points not into memory
	 */
	public void incPointer(int reg)
	{
		long value = this.getRegister(reg);
		if (value >= 0) {
			this.setRegister(reg, value+1);
		}
	}


	/**
	 * Increment register. Do nothing if the register points not into memory
	 */
	public void decPointer(int reg)
	{
		long value = this.getRegister(reg);
		if (value >= 0) {
			this.setRegister(reg, value-1);
		}
	}

	/**
	 * Advance P to next location
	 */
	public void nextP()
	{
		incPointer(Register.P.ordinal());
	}

//	public long fetchRegister(Register reg)
//	{
//		return this.register[reg.ordinal()];
//	}

	public long remainingSlots()
	{
		long res = this.register[Register.I.ordinal()];
		long mask = -1;
		res &= mask >>> (this.slot*SLOT_BITS);
		this.slot = NO_OF_SLOTS;
		return res;
	}

	public void jumpRemainigSlots()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = -1;
		mask = mask >>> (this.slot*SLOT_BITS);
		this.register[Register.P.ordinal()] &= ~mask;
		this.register[Register.P.ordinal()] |= this.register[Register.P.ordinal()] & mask;
		this.slot = NO_OF_SLOTS;
	}

	public long replaceNextSlot(long base) throws Exception
	{
		long res = (-1 << SLOT_BITS) & base;
		return res | nextSlot();
	}

	public boolean conditionalJump(int condition)
	{
		switch ((condition >> 4) & 3) {
		case 0: break;	// always
		case 1: if (this.register[Register.T.ordinal()] != 0) {return false;}	// if T == 0
		case 2: if (this.register[Register.T.ordinal()] < 0) {return false;}	// if T >= 0
		case 3: if (!this.getFlag(Flag.CARRY)) {return false;}	// if Flags.Carry == 0
		}
		switch (condition & 0xf) {
		case 0: this.slot = 0; break;
		case 1: this.slot = 1; break;
		case 2: this.slot = 2; break;
		case 3: this.slot = 3; break;
		case 4: this.slot = 4; break;
		case 5: this.slot = 5; break;
		case 6: this.slot = 6; break;
		case 7: this.slot = 7; break;
		case 8: this.slot = 8; break;
		case 9: this.slot = 9; break;
		case 10: this.slot = 10; break;
		case 11: nextP(); this.slot = NO_OF_SLOTS; break;
		case 12: nextP(); nextP(); this.slot = NO_OF_SLOTS; break;
		case 13: nextP(); nextP(); nextP(); this.slot = NO_OF_SLOTS; break;
		case 14: nextP(); nextP(); nextP(); nextP(); this.slot = NO_OF_SLOTS; break;
		case 15: this.jumpRemainigSlots(); break;
		}
		return true;
	}
	
	public long drop() throws Exception
	{
		long res = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();
		return res;
	}

	public void doDrop() throws Exception
	{
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();		
	}

	public void doDup() throws Exception
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];		
	}

	public void doNext() throws Exception
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			if (conditionalJump(this.nextSlot())) {
				--this.register[Register.R.ordinal()];
			}
		}
	}
	
	public void doCallMethod(long index) throws Exception
	{
		// push P on return stack
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.register[Register.P.ordinal()];
		// check index is in range of method table
		if (index >= sys.getMemory(this.register[Register.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			throw new Exception();
		}
//		// check if MT must be loaded
//		if (this.register[Register.MT.ordinal()] == 0) {
//			// load the method table register from SELF
//			this.register[Register.MT.ordinal()] = this.memory[(int)this.register[Register.SELF.ordinal()]];
//		}
		// load address of method
		this.register[Register.P.ordinal()] = sys.getMemory(this.register[Register.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY);
		this.slot = NO_OF_SLOTS;
	}

	
	public void doJumpMethod(long index) throws Exception
	{
		// check index is in range of method table
		if (index >= sys.getMemory(this.register[Register.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			throw new Exception();
		}
//		// check if MT must be loaded
//		if (this.register[Register.MT.ordinal()] == 0) {
//			// load the method table register from SELF
//			this.register[Register.MT.ordinal()] = this.memory[(int)this.register[Register.SELF.ordinal()]];
//		}
		// load address of method
		this.register[Register.P.ordinal()] = sys.getMemory(this.register[Register.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY);
		this.slot = NO_OF_SLOTS;
	}

	public void doSwap() throws Exception
	{
		int src = nextSlot();
		int dst = nextSlot();
		long tmp = this.register[dst];
		this.setRegister(dst, this.register[src]);
		this.setRegister(src, tmp);
	}

	public void doMove() throws Exception
	{
		int dst = nextSlot();
		int src = nextSlot();
		this.setRegister(dst, this.register[src]);
	}

	public void doMoveStack() throws Exception
	{
		int dst = nextSlot();
		int src = nextSlot();
		long value = this.register[src];
		if (dst == Register.R.ordinal()) {
			this.pushReturnStack(this.register[Register.R.ordinal()]);			
		}
		else if (dst == Register.S.ordinal()) {
			this.pushStack(this.register[Register.S.ordinal()]);
		}
		else if ((dst == Register.T.ordinal()) && (src == Register.S.ordinal())) {
			this.doDup();
		}
		this.setRegister(dst, value);
		if (src == Register.R.ordinal()) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else if (src == Register.S.ordinal()) {
			this.register[Register.S.ordinal()] = this.popStack();
		}
		else if ((src == Register.T.ordinal()) && (dst == Register.S.ordinal())) {
			this.doDrop();
		}
	}

	public void doRFetch(int reg) throws Exception
	{
		long tmp = this.getRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doRStore(int reg) throws Exception
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		this.setRegister(reg, tmp);
	}

	public void doFetchR(int reg) throws Exception
	{
		this.doDup();
		this.register[Register.T.ordinal()] = sys.getMemory(this.getRegister(reg));
	}
	

	public void doStoreR(int reg) throws Exception
	{
		sys.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
		this.doDrop();
	}

	public void doFetchPInc() throws Exception
	{
		int reg = Register.P.ordinal();
		doDup();
		this.register[Register.T.ordinal()] = sys.getMemory(this.getRegister(reg));
		incPointer(reg);
	}
	

	public void doStorePInc() throws Exception
	{
		int reg = Register.P.ordinal();
		sys.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
		doDrop();
		incPointer(reg);
	}

	public void doFetchAInc() throws Exception
	{
		doDup();
		this.register[Register.T.ordinal()] = sys.getMemory(this.register[Register.A.ordinal()]);
		this.incPointer(Register.A.ordinal());
	}
	

	public void doStoreBInc() throws Exception
	{
		sys.setMemory(this.register[Register.B.ordinal()], this.register[Register.T.ordinal()]);
		doDrop();
		this.incPointer(Register.B.ordinal());
	}

	public void doExit() throws Exception
	{
		this.register[Register.P.ordinal()] = this.register[Register.R.ordinal()];
		this.register[Register.R.ordinal()] = this.popReturnStack();
		this.slot = NO_OF_SLOTS;
	}

	public void doUNext() throws Exception
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.slot = 0;
		}
	}

	public void doCont() throws Exception
	{
		this.register[Register.I.ordinal()] = this.register[Register.T.ordinal()];
		this.doDrop();
//		this.register[Register.R.ordinal()] = this.popReturnStack();
		this.slot = 0;		
	}

	public void doAdd(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 + src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}
	
	public void doAddWithCarry(int d, int s1, int s2) throws Exception
	{
		boolean carry = getFlag(Flag.CARRY);
		long a = this.getRegister(s1);
		long b = this.getRegister(s2);
		long c = a+b;
		boolean overflow = (a > 0 && b > 0 && c < 0) || (a < 0 && b < 0 && c > 0);
		if (carry) {
			if (++c == 0) {overflow = true;}
		}
		this.setRegister(d, c);
		setFlag(Flag.CARRY, overflow);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	
	public void doSubtract(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 - src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doSubtractWithCarry(int d, int s1, int s2) throws Exception
	{
		boolean carry = getFlag(Flag.CARRY);
		long a = this.getRegister(s1);
		long b = ~this.getRegister(s2);
		long c = a+b;
		boolean overflow = (a > 0 && b > 0 && c < 0) || (a < 0 && b < 0 && c > 0);
		if (carry) {
			if (++c == 0) {overflow = true;}
		}
		this.setRegister(d, c);
		setFlag(Flag.CARRY, overflow);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAnd(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 & src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doOr(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 | src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doXor(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doXNor(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ ~src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}


	public void doAsl(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest;
		if (src2 < 0) {dest = src1;}
		else if (src2 >= BITS_PER_CELL) {
			if (src1 >= 0) {dest = 0;}
			else {dest = -1; dest <<= BITS_PER_CELL-1;}
		}
		else {
			dest = src1 << src2;
		}
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAsr(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest;
		if (src2 < 0) {dest = src1;}
		else if (src2 >= BITS_PER_CELL) {
			if (src1 >= 0) {dest = 0;}
			else {dest = -1;;}
		}
		else {
			dest = src1 >> src2;
		}
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doLsl(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest;
		if (src2 < 0) {dest = src1;}
		else if (src2 >= BITS_PER_CELL) {
			dest = 0;
		}
		else {
			dest = src1;
			while (src2 > 0) {
				if (dest >= 0) {
					dest <<= 1;
				}
				else {
					dest = ~(~dest << 1);
					dest ^= 1;
				}
				--src2;
			}
		}
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doLsr(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest;
		if (src2 < 0) {dest = src1;}
		else if (src2 >= BITS_PER_CELL) {
			dest = 0;
		}
		else {
			dest = src1 >>> src2;
		}
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doMul2Add(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = (src1 << 1) + src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doDiv2Sub(int d, int s1, int s2) throws Exception
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = (src1 >> 1) - src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doOver() throws Exception
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		long tmp = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doNip() throws Exception
	{
		this.register[Register.S.ordinal()] = this.popStack();
	}

	public void doLit() throws Exception
	{
		this.doDup();
		this.register[Register.T.ordinal()] = this.nextSlot();
	}

	public void doBitLit() throws Exception
	{
		this.doDup();
		long tmp = 1;
		this.register[Register.T.ordinal()] = tmp << this.nextSlot();
	}

	public void doExtendLiteral() throws Exception
	{
		this.register[Register.T.ordinal()] <<= SLOT_BITS;
		this.register[Register.T.ordinal()] |= this.nextSlot();
	}

	public void doShortJump() throws Exception
	{
		this.register[Register.P.ordinal()] = this.replaceNextSlot(this.register[Register.P.ordinal()]);
		this.slot = NO_OF_SLOTS;		
	}

	public void doCall() throws Exception
	{
		// push P on return stack
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.register[Register.P.ordinal()];
		// load new P
		this.jumpRemainigSlots();		
	}

	public void doPush(int reg) throws Exception
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getRegister(reg);
		if (reg == Register.S.ordinal()) {
			this.register[Register.S.ordinal()] = this.popStack();
		}
		else if (reg == Register.T.ordinal()) {
			this.doDrop();
		}
	}

	public void doPop(int reg) throws Exception
	{
		if (reg == Register.S.ordinal()) {
			this.pushStack(this.register[Register.S.ordinal()]);
		}
		else if (reg == Register.T.ordinal()) {
			this.doDup();
		}
		this.setRegister(reg, this.register[Register.P.ordinal()]);
		this.register[Register.P.ordinal()] = this.popReturnStack();
	}

	public void doLoadSelf() throws Exception
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.register[Register.SELF.ordinal()];
		this.register[Register.SELF.ordinal()] = this.register[Register.T.ordinal()];
		this.doDrop();
	}

	public void doLoadMT() throws Exception
	{
		this.register[Register.MT.ordinal()] = sys.getMemory(this.register[Register.SELF.ordinal()]);
	}

	public void step() throws Exception
	{
		switch (ISA.values()[this.nextSlot()]) {
		case NOP:		break;
		case EXIT:		this.doExit(); break;
		case UNEXT:		this.doUNext(); break;
		case CONT:		this.doCont(); break;
		case UJMP0:		this.slot = 0; break;
		case UJMP1:		this.slot = 1; break;
		case UJMP2:		this.slot = 2; break;
		case UJMP3:		this.slot = 3; break;
		case UJMP4:		this.slot = 4; break;
		case UJMP5:		this.slot = 5; break;
		case AND:		this.doAnd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case XOR:		this.doXor(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case DUP:		this.doDup(); break;
		case DROP:		this.doDrop(); break;
		case OVER:		this.doOver(); break;
		case NIP:		this.doNip(); break;
		case LIT:		this.doLit(); break;
		case BLIT:		this.doBitLit(); break;
		case EXT:		this.doExtendLiteral(); break;
		case NEXT:		this.doNext(); break;
		case BRANCH:	this.conditionalJump(this.nextSlot()); break;
		case CALLM:		this.doCallMethod(this.remainingSlots()); break;
		case JMPM:		this.doJumpMethod(this.remainingSlots()); break;
		case SJMP:		this.doShortJump(); break;
		case CALL:		this.doCall(); break;
		case JMP:		this.jumpRemainigSlots(); break;
		case USKIP:		this.slot = NO_OF_SLOTS; break;
		case UJMP6:		this.slot = 6; break;
		case UJMP7:		this.slot = 7; break;
		case UJMP8:		this.slot = 8; break;
		case UJMP9:		this.slot = 9; break;
		case UJMP10:	this.slot = 10; break;
		case SWAP:		this.doSwap(); break;
		case SWAP0:		this.doSwap(); this.slot = 0; break;
		case MOV:		this.doMove(); break;
		case MOVS:		this.doMoveStack(); break;
		case LOADSELF:	this.doLoadSelf(); break;
		case LOADMT:	this.doLoadMT(); break;
		case RFETCH:	this.doRFetch(this.nextSlot()); break;
		case RSTORE:	this.doRStore(this.nextSlot()); break;
		case FETCHR:	this.doFetchR(this.nextSlot()); break;
		case STORER:	this.doStoreR(this.nextSlot()); break;
		case RINC:		this.inc(this.nextSlot()); break;
		case RDEC:		this.dec(this.nextSlot()); break;
		case RPINC:		this.incPointer(this.nextSlot()); break;
		case RPDEC:		this.decPointer(this.nextSlot()); break;
		case FETCHPINC:	this.doFetchPInc(); break;
		case STOREPINC:	this.doStorePInc(); break;
		case ADD:		this.doAdd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case SUB:		this.doSubtract(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case OR:		this.doOr(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case NOT:		this.doXNor(Register.T.ordinal(), Register.S.ordinal(), Register.Z.ordinal()); break;
		case MUL2:		this.doMul2Add(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
		case DIV2:		this.doDiv2Sub(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
		case PUSH:		this.doPush(this.nextSlot()); break;
		case POP:		this.doPop(this.nextSlot()); break;
		case EXT1:		this.doExt1(); break;
		case EXT2:		this.doExt2(); break;
		case EXT3:		this.doExt3(); break;
		case EXT4:		this.doExt4(); break;
		case EXT5:		this.doExt5(); break;
		case EXT6:		this.doExt6(); break;
		case EXT7:		this.doExt7(); break;
		case REGOP:		this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;

		}
		// check if there is some interrupt pending
		if ((this.register[Register.FLAGS.ordinal()] & this.register[Register.INTE.ordinal()]) != 0) {
			// there is some pending interrupt
			triggerInterrupts();
		}
		//
		if (this.slot > FINAL_SLOT) {
			// load new instruction cell
			this.register[Register.I.ordinal()] = sys.getMemory(this.register[Register.P.ordinal()]);
			// increment P
			incPointer(Register.P.ordinal());
			// begin with first slot
			this.slot = 0;
		}
	}

	public void doMultiplyStep() throws Exception
	{
		//TODO
		throw new Exception();
	}

	public void doDivideStep() throws Exception
	{
		//TODO
		throw new Exception();
	}

	public void doRotateLeft() throws Exception
	{
		long value = this.register[Register.T.ordinal()];
		if (value < 0) {
			this.register[Register.T.ordinal()] = (value << 1) | 1;
			setFlag(Flag.CARRY, true);
		}
		else {
			this.register[Register.T.ordinal()] = (value << 1);
			setFlag(Flag.CARRY, false);
		}
	}

	public void doRotateRight() throws Exception
	{
		long value = this.register[Register.T.ordinal()];
		if ((value & 1) != 0) {
			long mask = 1;
			mask <<= 63;
			this.register[Register.T.ordinal()] = (value >> 1) | mask;
			setFlag(Flag.CARRY, true);
		}
		else {
			this.register[Register.T.ordinal()] = (value >>> 1);
			setFlag(Flag.CARRY, false);
		}
	}

	public void doRotateLeftWithCarry() throws Exception
	{
		long value = this.register[Register.T.ordinal()];
		if (getFlag(Flag.CARRY)) {
			this.register[Register.T.ordinal()] = (value << 1) | 1;
		}
		else {
			this.register[Register.T.ordinal()] = (value << 1);
		}
		setFlag(Flag.CARRY, (value < 0));
	}

	public void doRotateRightWithCarry() throws Exception
	{
		long value = this.register[Register.T.ordinal()];
		long mask = 1;
		mask <<= 63;
		if (getFlag(Flag.CARRY)) {
			this.register[Register.T.ordinal()] = (value >> 1) | mask;
		}
		else {
			this.register[Register.T.ordinal()] = (value >>> 1);
		}
		setFlag(Flag.CARRY, (value & mask) != 0);
	}

	public void doSetBit(int reg, int bit, boolean bit_is_reg, boolean swap_source) throws Exception
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, value | mask);
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doClearBit(int reg, int bit, boolean bit_is_reg, boolean swap_source) throws Exception
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, value & ~mask);
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doToggleBit(int reg, int bit, boolean bit_is_reg, boolean swap_source) throws Exception
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, value ^ mask);
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doReadBit(int reg, int bit, boolean bit_is_reg, boolean swap_source) throws Exception
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		setFlag(Flag.CARRY, (value & mask) != 0);
	}

	public void doWriteBit(int reg, int bit, boolean bit_is_reg, boolean swap_source) throws Exception
	{
		if (getFlag(Flag.CARRY)) {
			this.doSetBit(reg, bit, bit_is_reg, swap_source);
		}
		else {
			this.doClearBit(reg, bit, bit_is_reg, swap_source);
		}
	}

	public void doEnterM() throws Exception
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.register[Register.MT.ordinal()];
		this.register[Register.MT.ordinal()] = sys.getMemory(this.register[Register.SELF.ordinal()]);
	}

	public void doEnterInterrupt(int no) throws Exception
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		// save flags (with current slot and interrupt)
		this.setSlot(Register.FLAGS.ordinal(), 0, this.slot);
		this.setSlot(Register.FLAGS.ordinal(), 1, no);
		this.pushReturnStack(this.register[Register.FLAGS.ordinal()]);
		// save I
		this.register[Register.R.ordinal()] = this.register[Register.I.ordinal()];
		// load instruction from interrupt vector table
		this.register[Register.I.ordinal()] = sys.getMemory(this.register[Register.INTV.ordinal()]+no);
		this.slot = 0;
		// mark interrupt as in service
		this.setInterruptFlag(Register.INTS, no, true);
		// clear interrupt flag register
		this.setFlag(no, false);
	}

	public void doExitInterrupt() throws Exception
	{
		// restore I
		this.register[Register.I.ordinal()] = this.register[Register.R.ordinal()];
		// restore flags with slot and clear the interrupt in the INTF register
		this.register[Register.FLAGS.ordinal()] = this.popReturnStack();
		this.slot = this.getSlot(Register.FLAGS.ordinal(), 0);
		int no = this.getSlot(Register.FLAGS.ordinal(), 1);
		this.setInterruptFlag(Register.INTS, no, false);
		//
		this.register[Register.R.ordinal()] = this.popReturnStack();
		// restore slot
	}

	public void doExt1() throws Exception
	{
		switch (Ext1.values()[this.nextSlot()]) {
		case NOP:		break;
		case EXITI:		this.doExitInterrupt(); break;
		case ADDC:		this.doAddWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case SUBC:		this.doSubtractWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case MULS:		this.doMultiplyStep(); break;
		case DIVS:		this.doDivideStep(); break;
		case ROL:		this.doRotateLeft(); break;
		case ROR:		this.doRotateRight(); break;
		case ROLC:		this.doRotateLeftWithCarry(); break;
		case RORC:		this.doRotateRightWithCarry(); break;
		case SBIT:		this.doSetBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case CBIT:		this.doClearBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case TBIT:		this.doToggleBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case RBIT:		this.doReadBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case WBIT:		this.doWriteBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case SBITS:		this.doSetBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case CBITS:		this.doClearBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case TBITS:		this.doToggleBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case RBITS:		this.doReadBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case WBITS:		this.doWriteBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case ENTERM:	this.doEnterM(); break;
		case LCALLM:	this.doCallMethod(this.drop()); break;
		case LJMPM:		this.doJumpMethod(this.drop()); break;
		case FETCHAINC:	this.doFetchAInc(); break;
		case STOREBINC:	this.doStoreBInc(); break;
		case RSBIT:		this.doSetBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RCBIT:		this.doClearBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RTBIT:		this.doToggleBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RRBIT:		this.doReadBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RWBIT:		this.doWriteBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RRSBIT:	this.doSetBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRCBIT:	this.doClearBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRTBIT:	this.doToggleBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRRBIT:	this.doReadBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRWBIT:	this.doWriteBit(this.nextSlot(), this.nextSlot(), true, false); break;
		}
	}

	/**
	 * ( a - n )
	 * Fetch a value and mark the location as reserved. Any write access to this location
	 * may generate a RESERVED exception.
	 * The RESERVE register is set to the observed address.
	 * @throws Exception
	 */
	public void doFetchReserved() throws Exception
	{
		long adr = this.getRegister(Register.T);
		if (this.getRegister(Register.RESERVE) != 0) {
			// some memory has already been reserved
			if (interrupt(Flag.TOUCHED)) {
				return;
			}
//			this.setFlag(Flag.RESERVED, true);
		}
		this.setRegister(Register.RESERVE, adr);
		this.setRegister(Register.T, sys.getMemory(adr));
	}

	/**
	 * ( n1 a - n2 )
	 * Store the value n1 at address a, iff a is a reserved location and it has not been touched.
	 * The result n2 is 0 if the store was successful, otherwise it contains an reason code.
	 * The RESERVE register is reset to 0.
	 */
	public void doStoreConditional() throws Exception
	{
		long adr = this.getRegister(Register.T);
		long value = this.getRegister(Register.S);
		if (this.getRegister(Register.RESERVE) != adr) {
			// memory has not been reserved
		}
		if (getFlag(Flag.TOUCHED)) {
			this.setRegister(Register.T, -1);
		}
		else {
			sys.setMemory(adr, value);
			this.setRegister(Register.T, 0);
		}
		this.setRegister(Register.RESERVE, 0);
		this.doNip();
	}

	public void doExt2() throws Exception
	{
		switch (Ext2.values()[this.nextSlot()]) {
		case FETCHRES:	this.doFetchReserved(); break;
		case STORECOND:	this.doStoreConditional(); break;
		}
	}

	public void doExt3() throws Exception
	{
	}

	public void doExt4() throws Exception
	{
	}

	public void doExt5() throws Exception
	{
	}

	public void doExt6() throws Exception
	{
	}

	public void doExt7() throws Exception
	{
	}

	public boolean interrupt(Flag no)
	{
		// signaling interrupt
		try {
			this.setFlag(no, true);
			if (!this.getFlag(Register.INTS, no)) {
				// interrupt is enabled
				// save slot to flags register
				doEnterInterrupt(no.ordinal());
				return true;
			}
		}
		catch (Exception ex) {
			
		}
		return false;
	}

	public void triggerInterrupts()
	{
		// scan interrupts in reverse priority
		for (int i=Flag.values().length-1; i>=0; --i) {
			if (this.getFlag(i) && this.getInterruptFlag(Register.INTE, i)) {
				this.interrupt(Flag.values()[i]);
			}
		}
	}

	public void reset()
	{
		try {
			this.interrupt(Flag.RESET);
		}
		catch (Exception ex) {
		}
	}

	public void nmi() throws Exception
	{
		this.interrupt(Flag.NMI);
	}

	public void incrementExternalClock() throws Exception
	{
		this.inc(Register.CLK.ordinal());
		if (this.getRegister(Register.CLK) == this.getRegister(Register.CLI)) {
			this.setFlag(Flag.CLOCK, true);;
		}
	}

	public void powerOn()
	{
		// initialize instruction pointer
		this.setRegister(Register.P, 0);
		this.setRegister(Register.I, sys.getMemory(0));
		this.setRegister(Register.FLAGS, 0);
		this.slot = 0;
		// initialize stack
		this.setRegister(Register.SP, sys.getStackSize()-1);
		this.setRegister(Register.S0, 0);
		this.setRegister(Register.SL, sys.getStackSize()-1);
		// initialize return stack
		this.setRegister(Register.RP, sys.getReturnStackSize()-1);
		this.setRegister(Register.R0, 0);
		this.setRegister(Register.RL, sys.getReturnStackSize()-1);
		// memory
		this.setRegister(Register.INTV, 0);
		this.setRegister(Register.INTE, 0);
		// power-on reset clears the reset interrupt flag
		this.setFlag(Flag.RESET, false);
		this.setFlag(Register.INTS, Flag.RESET, false);
//		this.setRegister(Register.P, 0);
//		try {
//			for (;;) {
//				step();
//			}
//		}
//		catch (Exception ex) {
//		}
	}
	
	public void doRegisterOperation(int op, int s1, int s2, int d) throws Exception
	{
		switch (RegOp1.values()[op]) {
		case ADD: this.doAdd(d, s1, s2);; break;
		case ADDC: this.doAddWithCarry(d, s1, s2);; break;
		case ADDCC: this.setFlag(Flag.CARRY, false); this.doAddWithCarry(d, s1, s2);; break;
		case ADDCS: this.setFlag(Flag.CARRY, true); this.doAddWithCarry(d, s1, s2);; break;
		case SUB: this.doSubtract(d, s1, s2);; break;
		case SUBC: this.doSubtractWithCarry(d, s1, s2); break;
		case SUBCC: this.setFlag(Flag.CARRY, false); this.doSubtractWithCarry(d, s1, s2); break;
		case SUBCS: this.setFlag(Flag.CARRY, true); this.doSubtractWithCarry(d, s1, s2); break;
		case AND: this.doAnd(d, s1, s2);; break;
		case OR: this.doOr(d, s1, s2);; break;
		case XOR: this.doXor(d, s1, s2);; break;
		case XNOR: this.doXNor(d, s1, s2);; break;
		case ASL: this.doAsl(d, s1, s2);; break;
		case ASR: this.doAsr(d, s1, s2);; break;
		case LSL: this.doLsl(d, s1, s2);; break;
		case LSR: this.doLsr(d, s1, s2);; break;
		case MUL2ADD: this.doMul2Add(d, s1, s2);; break;
		case DIV2SUB: this.doDiv2Sub(d, s1, s2);; break;
		}
	}


}
