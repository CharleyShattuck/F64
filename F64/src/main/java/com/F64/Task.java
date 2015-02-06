package com.F64;

public class Task {
	private System				system;
	private Processor			processor;
	private int					index;
	private long[]				register;
	private long[]				local_register;
	private long[]				system_register;
	private long[]				parameter_stack;
	private long[]				return_stack;
	private	com.F64.SIMD.Unit	simd;

	public Task(System s, Processor p, int i, int stack_size, int return_stack_size)
	{
		system = s;
		processor = p;
		index = i;
		this.simd = new com.F64.SIMD.Unit();
		this.register = new long[Processor.NO_OF_REG];
		this.local_register = new long[Processor.NO_OF_REG];
		this.system_register = new long[Processor.NO_OF_REG];
		this.register[Register.Z.ordinal()] = 0;
		if (stack_size > 0) {parameter_stack = new long[stack_size];}
		if (return_stack_size > 0) {return_stack = new long[return_stack_size];}
	}

	public	com.F64.SIMD.Unit getSIMD() {return simd;}

	public void reset()
	{
		if (parameter_stack == null) {
			this.setSystemRegister(SystemRegister.SP, system.getStackTop(0, false));
			this.setSystemRegister(SystemRegister.S0, system.getStackBottom(0, false));
			this.setSystemRegister(SystemRegister.SL, system.getStackTop(0, false));
		}
		else {
			this.setSystemRegister(SystemRegister.SP, parameter_stack.length - 1);
			this.setSystemRegister(SystemRegister.S0, 0);
			this.setSystemRegister(SystemRegister.SL, parameter_stack.length - 1);
		}
		// initialize return stack
		if (return_stack == null) {
			this.setSystemRegister(SystemRegister.RP, system.getStackTop(0, true));
			this.setSystemRegister(SystemRegister.R0, system.getStackBottom(0, true));
			this.setSystemRegister(SystemRegister.RL, system.getStackTop(0, true));
		}
		else {
			this.setSystemRegister(SystemRegister.RP, return_stack.length - 1);
			this.setSystemRegister(SystemRegister.R0, 0);
			this.setSystemRegister(SystemRegister.RL, return_stack.length - 1);
		}
		// system register
		this.setSystemRegister(SystemRegister.INTV, 0);
		this.setSystemRegister(SystemRegister.INTE, 0);
	}

	public long[] getSIMDRegister(int reg) {return simd.getRegister(reg);}

	public long getLocalRegister(int reg)
	{
		return this.local_register[reg];
	}

	public void setLocalRegister(int reg, long value)
	{
		local_register[reg] = value;
	}

	public long getRegister(int reg)
	{
		assert(reg < Processor.NO_OF_REG);
		return this.register[reg];
	}

	
	public void setRegister(int reg, long value)
	{
		assert(reg < Processor.NO_OF_REG);
		if (reg > 0) {
			register[reg] = value;
		}
	}

	public long getSystemRegister(int reg)
	{
		long res = this.system_register[reg];
//		if (reg == SystemRegister.FLAG.ordinal()) {
//			// fill in the slot bits into the upper 4 bits
//			long mask = -1;
//			res &= mask >>> 4;
//			mask = this.slot;
//			mask <<= (BIT_PER_CELL - 4);
//			res |= mask;
//		}
		return res;
	}
	
	
	public boolean setSystemRegister(int reg, long value)
	{
		if (reg == SystemRegister.I.ordinal()) {
			// we must set reset the slot # if we write the I register
			processor.setSlot(0);
		}
		else if (reg == SystemRegister.INTE.ordinal()) {
			value |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
		else if (reg == SystemRegister.P.ordinal()) {
			if (!processor.getSystem().isValidCodeAddress(value)) {
				if (processor.interrupt(Flag.CODE)) {
					return false;
				}
			}
		}
		system_register[reg] = value;
		return true;
	}

	public long getRegister(Register reg) {return this.getRegister(reg.ordinal());}
	public void setRegister(Register reg, long value) {this.setRegister(reg.ordinal(), value);}
	public long getSystemRegister(SystemRegister reg) {return this.getSystemRegister(reg.ordinal());}
	public void setSystemRegister(SystemRegister reg, long value) {this.setSystemRegister(reg.ordinal(), value);}
	public long getT() {return this.register[Register.T.ordinal()];}

	public long drop()
	{
		long res = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();
		return res;
	}

	public void dup()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];		
	}

	public void qdup()
	{
		if (this.register[Register.T.ordinal()] != 0) {
			this.pushStack(this.register[Register.S.ordinal()]);
			this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		}
	}

	public void over()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		long tmp = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = tmp;
	}

	public void under()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		long tmp = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = tmp;
	}

	public void tuck()
	{
		this.pushStack(this.register[Register.T.ordinal()]);
	}

	public void nip()
	{
		this.register[Register.S.ordinal()] = this.popStack();
	}

	public void lit(int data)
	{
		this.pushT(data);
	}

	public void nLit(int data)
	{
		this.pushT(~(long)data);
	}

	public void bLit(int data)
	{
		this.pushT(1L << data);
	}

	public void extLit(int data)
	{
		this.register[Register.T.ordinal()] <<= Processor.SLOT_BITS;
		this.register[Register.T.ordinal()] |= data;
	}

	public long getStackPosition(int offset)
	{
		long pos = system_register[SystemRegister.SP.ordinal()] + offset;
		while (pos > system_register[SystemRegister.SL.ordinal()]) {
			pos -= system_register[SystemRegister.SL.ordinal()] + 1;
			pos += system_register[SystemRegister.S0.ordinal()];
		}
		while (pos < system_register[SystemRegister.S0.ordinal()]) {
			pos += system_register[SystemRegister.SL.ordinal()] + 1;
			pos -= system_register[SystemRegister.S0.ordinal()];			
		}
		return pos;
	}

	public long getReturnStackPosition(int offset)
	{
		long pos = system_register[SystemRegister.RP.ordinal()] + offset;
		while (pos > system_register[SystemRegister.RL.ordinal()]) {
			pos -= system_register[SystemRegister.RL.ordinal()] + 1;
			pos += system_register[SystemRegister.R0.ordinal()];
		}
		while (pos < system_register[SystemRegister.R0.ordinal()]) {
			pos += system_register[SystemRegister.RL.ordinal()] + 1;
			pos -= system_register[SystemRegister.R0.ordinal()];			
		}
		return pos;
	}


	public long getStack(long pos)
	{
		if (parameter_stack == null) {
			return system.getStackMemory(pos);
		}
		return parameter_stack[(int)pos];
	}

	public long getReturnStack(long pos)
	{
		if (return_stack == null) {
			return system.getReturnStackMemory(pos);
		}
		return return_stack[(int)pos];
	}

	public void setStack(long pos, long value)
	{
		if (parameter_stack == null) {
			system.setStackMemory(pos, value);
		}
		else {
			parameter_stack[(int)pos] = value;
		}
	}

	public void setReturnStack(long pos, long value)
	{
		if (parameter_stack == null) {
			system.setReturnStackMemory(pos, value);
		}
		else {
			return_stack[(int)pos] = value;
		}
	}

	
	public void pushStack(long value)
	{
		if (this.system_register[SystemRegister.SP.ordinal()] == this.system_register[SystemRegister.S0.ordinal()]) {
			this.system_register[SystemRegister.SP.ordinal()] = this.system_register[SystemRegister.SL.ordinal()];
			this.setFlag(Flag.SOVER, true);
		}
		else {
			--this.system_register[SystemRegister.SP.ordinal()];
		}
		setStack(this.system_register[SystemRegister.SP.ordinal()], value);
	}

	public void pushReturnStack(long value)
	{
		if (this.system_register[SystemRegister.RP.ordinal()] == this.system_register[SystemRegister.R0.ordinal()]) {
			this.system_register[SystemRegister.RP.ordinal()] = this.system_register[SystemRegister.RL.ordinal()];
			this.setFlag(Flag.ROVER, true);
		}
		else {
			--this.system_register[SystemRegister.RP.ordinal()];
		}
		setReturnStack(this.system_register[SystemRegister.RP.ordinal()], value);
	}

	public long popStack()
	{
		long res = getStack(this.system_register[SystemRegister.SP.ordinal()]);
		if (this.system_register[SystemRegister.SP.ordinal()] == this.system_register[SystemRegister.SL.ordinal()]) {
			this.system_register[SystemRegister.SP.ordinal()] = this.system_register[SystemRegister.S0.ordinal()];
			this.setFlag(Flag.SUNDER, true);
		}
		else {
			++this.system_register[SystemRegister.SP.ordinal()];
		}
		return res;
	}
	
	public long popReturnStack()
	{
		long res = getReturnStack(this.system_register[SystemRegister.RP.ordinal()]);
		if (this.system_register[SystemRegister.RP.ordinal()] == this.system_register[SystemRegister.RL.ordinal()]) {
			this.system_register[SystemRegister.RP.ordinal()] = this.system_register[SystemRegister.R0.ordinal()];
			this.setFlag(Flag.RUNDER, true);
		}
		else {
			++this.system_register[SystemRegister.RP.ordinal()];
		}
		return res;
	}



	public boolean getFlag(int fl)
	{
		long mask = Flag.values()[fl].getMask();
		return (getSystemRegister(SystemRegister.FLAG) & mask) != 0;
	}

	public boolean getFlag(Flag fl)
	{
		return (getSystemRegister(SystemRegister.FLAG) & fl.getMask()) != 0;
	}

	public void setFlag(SystemRegister reg, Flag fl, boolean value)
	{
		setFlag(reg, fl.ordinal(), value);
	}

	public void setFlag(SystemRegister reg, int fl, boolean value)
	{
		long mask = Flag.values()[fl].getMask();
		long data = getSystemRegister(reg);
		if (value) {
			data |= mask;
		}
		else {
			data &= ~mask;			
		}
		setSystemRegister(reg, data);
	}

	public void setFlag(int fl, boolean value)
	{
		long mask = Flag.values()[fl].getMask();
		long data = getSystemRegister(SystemRegister.FLAG);
		if (value) {
			data |= mask;
		}
		else {
			data &= ~mask;			
		}
		setSystemRegister(SystemRegister.FLAG, data);
	}

	public void setFlag(Flag fl, boolean value)
	{
		setFlag(fl.ordinal(), value);
	}

	public boolean getFlag(SystemRegister reg, Flag fl)
	{
		return (getSystemRegister(reg) & fl.getMask()) != 0;
	}

	public boolean getFlag(SystemRegister reg, int fl)
	{
		return (getSystemRegister(reg) & Flag.values()[fl].getMask()) != 0;
	}


	public void setFlag(int flag)
	{
		setFlag(flag, true);
	}

	public void clearFlag(int flag)
	{
		setFlag(flag, false);
	}

	public void rDrop()
	{
		register[Register.R.ordinal()] = popReturnStack();
	}


	public void rDup()
	{
		pushReturnStack(register[Register.R.ordinal()]);
	}


	public void pushT(long value)
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = value;
	}

	public long popT()
	{
		long value = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();
		return value;
	}


	public void nextP()
	{
		system_register[SystemRegister.P.ordinal()] = Processor.incAdr(system_register[SystemRegister.P.ordinal()]);
	}

	public long remainingSlots()
	{
		long res = this.system_register[SystemRegister.I.ordinal()] & Processor.REMAINING_MASKS[processor.getSlot()];
		processor.setSlot(Processor.NO_OF_SLOTS);
		return res;
	}

	public void shortJump(int slot_bits, boolean forward)
	{
		if (slot_bits == 0) {slot_bits = Processor.SLOT_SIZE;}
//		long mask = SLOT_MASK;
		if (forward) {
			this.system_register[SystemRegister.P.ordinal()] += slot_bits;
		}
		else {
			this.system_register[SystemRegister.P.ordinal()]-= slot_bits;
		}
		processor.setSlot(Processor.NO_OF_SLOTS);
	}

	public void longJump()
	{
		this.setSystemRegister(SystemRegister.P, this.pFetchInc());
		processor.setSlot(Processor.NO_OF_SLOTS);
	}

	public void longCol()
	{
		dup();
		long cont_adr = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
		long col_adr = this.getSystemRegister(SystemRegister.P);
		this.register[Register.T.ordinal()] = col_adr;
		this.system_register[SystemRegister.P.ordinal()] = cont_adr;
		processor.setSlot(Processor.NO_OF_SLOTS);
	}

	public boolean callMethod(long index)
	{
		// check index is in range of method table
		if (index >= system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			if (processor.interrupt(Flag.BOUND)) {
				return false;
			}
		}
		// load address of method
		long adr = this.system_register[SystemRegister.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY;
		this.system_register[SystemRegister.W.ordinal()] = Processor.incAdr(adr);
		// load I with content of adr
		this.system_register[SystemRegister.I.ordinal()] = system.getMemory(adr);
		processor.setSlot(0);
		return true;
	}

	public void execute()
	{
		long code = Processor.writeSlot(register[Register.T.ordinal()], 0, ISA.CALL.ordinal());
		this.drop();
		this.system_register[SystemRegister.I.ordinal()] = code;
		processor.setSlot(0);
	}

	public void call()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = Processor.REMAINING_MASKS[processor.getSlot()];
		long adr = this.getSystemRegister(SystemRegister.I) & mask;
		// P contains return address
		// save next address to register W
		this.system_register[SystemRegister.W.ordinal()] = Processor.incAdr(adr);
		// load I with content of adr
		this.system_register[SystemRegister.I.ordinal()] = system.getMemory(adr);
		processor.setSlot(0);
	}

	public void enter()
	{
		long new_adr =  this.getSystemRegister(SystemRegister.W);
		// push P on return stack
		this.pushSystem(SystemRegister.P.ordinal());
		// set P with saved address in W. W is set by CALL
		this.setSystemRegister(SystemRegister.P, new_adr);
		
	}


	public void enterM()
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.MT.ordinal()];
		this.system_register[SystemRegister.MT.ordinal()] = system.getMemory(this.system_register[SystemRegister.SELF.ordinal()]);
	}

	public void saveLocal(int from, int to)
	{
		if (from <= to) {
			for (int i=to; i>=from; --i) {
				this.pushLocal(i);
				this.setLocalRegister(i, this.register[Register.T.ordinal()]);
				this.drop();
			}
		}
	}

	public void restoreLocal(int from, int to)
	{
		if (from <= to) {
			for (int i=from; i<=to; ++i) {
				this.popLocal(i);
			}
		}
	}

	public void saveSelf()
	{
		// push SELF on return stack
		this.pushSystem(SystemRegister.SELF.ordinal());
		// load MT
		loadMT();
	}

	public void restoreSelf()
	{
		// pop SELF from return stack
		this.popSystem(SystemRegister.SELF.ordinal());
		// load MT
		loadMT();
	}

	public void exit()
	{
		this.system_register[SystemRegister.P.ordinal()] = this.register[Register.R.ordinal()];
		this.register[Register.R.ordinal()] = this.popReturnStack();
		processor.setSlot(Processor.NO_OF_SLOTS);
	}

	public void loadMT()
	{
		this.system_register[SystemRegister.MT.ordinal()] = system.getMemory(this.system_register[SystemRegister.SELF.ordinal()]);
	}

	public void push(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getRegister(reg);
	}

	public void pop(int reg)
	{
		this.setRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void pushSystem(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getSystemRegister(reg);
	}

	public void popSystem(int reg)
	{
		this.setSystemRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void pushLocal(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getLocalRegister(reg);
	}

	public void popLocal(int reg)
	{
		this.setLocalRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void loadSelf()
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.SELF.ordinal()];
		this.system_register[SystemRegister.SELF.ordinal()] = this.register[Register.T.ordinal()];
		this.drop();
	}

//	public void doLongJump()
//	{
//		this.system_register[SystemRegister.P.ordinal()] = system.getMemory(this.getSystemRegister(SystemRegister.P));
//		this.slot = NO_OF_SLOTS;		
//	}

	public void snext(int slot, boolean forward)
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.shortJump(slot, forward);
		}
	}

	public void unext()
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			processor.setSlot(0);
		}
	}

	public void rnext()
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.jumpRemainigSlots();
		}
	}

	public void jumpRemainigSlots()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = Processor.REMAINING_MASKS[processor.getSlot()];
		long data = this.getSystemRegister(SystemRegister.I) & mask;
		// P contains return address
		long pc = this.system_register[SystemRegister.P.ordinal()];
		// replace lower bits of pc with the bits from the remaining slots
		long adr = pc ^ ((pc ^ data) & mask);
		// save return address to register W
		this.system_register[SystemRegister.W.ordinal()] = this.system_register[SystemRegister.P.ordinal()];
		// load P with new address
		this.system_register[SystemRegister.P.ordinal()] = adr;
		// start with new address
		processor.setSlot(Processor.NO_OF_SLOTS);
	}

	public void lnext()
	{
		long target = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.system_register[SystemRegister.P.ordinal()] = target;
			processor.setSlot(Processor.NO_OF_SLOTS);
		}
	}

	public void cont()
	{
		this.system_register[SystemRegister.I.ordinal()] = this.register[Register.T.ordinal()];
		this.drop();
		processor.setSlot(0);
	}

	public void dodo()
	{
		long start = this.register[Register.S.ordinal()];
		long limit = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = this.popStack();
		this.register[Register.S.ordinal()] = this.popStack();
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.pushReturnStack(this.register[Register.L.ordinal()]);
		this.register[Register.R.ordinal()] = start;
		this.register[Register.L.ordinal()] = limit;
	}

	public void qdo()
	{
		long start = this.register[Register.S.ordinal()];
		long limit = this.register[Register.T.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();
		if (start < limit) {
			this.register[Register.T.ordinal()] = Processor.TRUE;
			this.pushReturnStack(this.register[Register.R.ordinal()]);
			this.pushReturnStack(this.register[Register.L.ordinal()]);
			this.register[Register.R.ordinal()] = start;
			this.register[Register.L.ordinal()] = limit;
		}
		else {
			this.register[Register.T.ordinal()] = Processor.FALSE;
		}
	}

	public void qfor()
	{
		long limit = this.register[Register.T.ordinal()];
		if (limit != 0) {
			this.register[Register.T.ordinal()] = Processor.TRUE;
			this.pushReturnStack(this.register[Register.R.ordinal()]);
			this.register[Register.R.ordinal()] = limit-1;
		}
		else {
			this.register[Register.T.ordinal()] = Processor.FALSE;
		}
	}

	public void loop()
	{
		this.dup();
		if (++this.register[Register.R.ordinal()] == this.register[Register.T.ordinal()]) {
			this.register[Register.T.ordinal()] = Processor.FALSE;
		}
		else {
			this.register[Register.T.ordinal()] = Processor.TRUE;
			this.register[Register.L.ordinal()] = this.popReturnStack();
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
	}

	public void ploop()
	{
		boolean beforeSign = this.register[Register.R.ordinal()] < 0;
		boolean before = this.register[Register.R.ordinal()] < this.register[Register.T.ordinal()];
		this.register[Register.R.ordinal()] += this.register[Register.T.ordinal()];
		boolean afterSign = this.register[Register.R.ordinal()] < 0;
		boolean after = beforeSign == afterSign
			? this.register[Register.R.ordinal()] < this.register[Register.T.ordinal()]
			: this.register[Register.R.ordinal()] >= this.register[Register.T.ordinal()];
		if (before == after) {
			this.register[Register.T.ordinal()] = Processor.FALSE;
		}
		else {
			this.register[Register.T.ordinal()] = Processor.TRUE;
			this.register[Register.L.ordinal()] = this.popReturnStack();
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
	}

	public void fetch()
	{
		this.register[Register.T.ordinal()] = system.getMemory(this.register[Register.T.ordinal()]);
	}

	public void store()
	{
		system.setMemory(this.register[Register.T.ordinal()], this.register[Register.S.ordinal()]);
		this.drop();
		this.drop();
	}

	public void rFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		long tmp = this.getRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void rStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		this.setRegister(reg, tmp);
	}

	public void sFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		long tmp = this.getSystemRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void sStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		this.setSystemRegister(reg, tmp);
	}

	public void lFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		long tmp = this.getLocalRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void lStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		int reg = (int)(this.getRegister(ireg) & Processor.SLOT_MASK);
		this.setLocalRegister(reg, tmp);
	}

	public void lFetch(int reg)
	{
		long tmp = this.getLocalRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void lStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		this.setLocalRegister(reg, tmp);
	}

	public void rFetch(int reg)
	{
		long tmp = this.getRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void rStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		this.setRegister(reg, tmp);
	}

	public void sFetch(int reg)
	{
		long tmp = this.getSystemRegister(reg);
		this.dup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void sStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.drop();
		this.setSystemRegister(reg, tmp);
	}

	public void swapRL(int dst, int src)
	{
		long tmp = getRegister(dst);
		setRegister(dst, getLocalRegister(src));
		setLocalRegister(src, tmp);
	}

	public void rFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
		this.dup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
	}

	public void rFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.drop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
	}

	public void lFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getLocalRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
		this.dup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
	}

	public void lFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getLocalRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.drop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
	}

	public void sFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getSystemRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
		this.dup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
	}

	public void sFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getSystemRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.drop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
	}


	public long pFetchInc()
	{
		long res = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
		return res;
		
	}

	public void fetchPInc()
	{
		dup();
		this.register[Register.T.ordinal()] = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
	}


	public void storePInc()
	{
		system.setMemory(this.getSystemRegister(SystemRegister.P), this.register[Register.T.ordinal()]);
		drop();
		nextP();
	}

	/**
	 * set the system register MDP with sign information about S & T.
	 * Convert T & S into unsigned numbers
	 */
	public void multiplyDividePrepare()
	{
		long res = 0;
		long data = this.register[Register.T.ordinal()];
		if (data < 0) { // divisor
			res |= 3;
			// bit 0 signals that the divisor is negative
			// bit 1 signald that the signs of the divisor and dividend are different
			this.register[Register.T.ordinal()] = -data;
		}
		data = this.register[Register.S.ordinal()];
		if (data < 0) { // dividend
			res ^= 2;
			this.register[Register.S.ordinal()] = -data;
		}
		setSystemRegister(SystemRegister.MDP, res);
	}

	public void multiplyStep()
	{
		long tmp = 0;
		long t = this.register[Register.T.ordinal()];
		if ((t & 1) != 0) {
			tmp = this.register[Register.S.ordinal()];
		}
		long md = this.system_register[SystemRegister.MD.ordinal()];
		md = processor.adc(tmp, md, false);
		md = processor.rcr(md, 1, this.getFlag(Flag.CARRY));
		t = processor.rcr(t, 1, this.getFlag(Flag.CARRY));
		this.system_register[SystemRegister.MD.ordinal()] = md;
		this.register[Register.T.ordinal()] = t;
	}

	/**
	 * S = Dividend
	 * T = Divisor
	 * MD = Remainder
	 */
	public void divideStep()
	{
		long dd = this.register[Register.S.ordinal()];
		long ds = this.register[Register.T.ordinal()];
		long md = this.system_register[SystemRegister.MD.ordinal()];
		md <<= 1;
		if ((dd & 0x8000_0000_0000_0000L) != 0) {
			++md;
		}
		dd <<= 1;
		if (md >= ds) {
			md-= ds;
			++dd;
		}
		this.register[Register.S.ordinal()] = dd;
		this.system_register[SystemRegister.MD.ordinal()] = md;
	}

	/*
	 * S = multiplicand
	 * T = result low
	 * MD = result high
	 */
	public void multiplyFinished()
	{
		int mdp = (int)this.system_register[SystemRegister.MDP.ordinal()];
		if ((mdp & 2) != 0) {
			this.register[Register.T.ordinal()] = -this.register[Register.T.ordinal()];
		}
		this.nip();
	}

	public void divideModFinished()
	{
		int mdp = (int)this.system_register[SystemRegister.MDP.ordinal()];
		long q = this.register[Register.S.ordinal()];
		long r = this.system_register[SystemRegister.MD.ordinal()];
		if ((mdp & 2) != 0) {
			q = ~q;
		}
		if ((mdp & 1) != 0) {
			r = -r;
		}
		this.register[Register.S.ordinal()] = q;
		this.register[Register.T.ordinal()] = r;
	}

	public void eq0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data == 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}

	public void ne0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data != 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}
	
	public void gt0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data > 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}

	public void ge0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data >= 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}

	public void lt0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data < 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}

	public void le0q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.dup();}
			else if (dest == Register.S.ordinal()) {this.under();}
		}
		if (data <= 0) {
			this.register[dest] = Processor.TRUE;
		}
		else {
			this.register[dest] = Processor.FALSE;
		}
	}

	public void abs(int dest, int src)
	{
		this.register[dest] = Processor.abs(this.register[src]);
	}

	public void negate(int dest, int src)
	{
		this.register[dest] = -this.register[src];
	}

	public void reverse(int dest, int src)
	{
		this.register[dest] = Processor.reverseBits(this.register[src]);
	
	}

	public void nextPow2(int dest, int src)
	{
		this.register[dest] = Processor.nextPow2(this.register[src]);
	
	}

	public void parityq(int dest, int src)
	{
		this.register[dest] = Processor.parityBits(this.register[src]) ? Processor.TRUE : Processor.FALSE;
	}

	public void configFetch(int no)
	{
		this.dup(); 
		if (no > Config.values().length) {
			this.register[Register.T.ordinal()] = 0;
			return;
		}
		switch (Config.values()[no]) {
		case VERSION:		this.register[Register.T.ordinal()] = Processor.VERSION; break;
		case BITPERCELL:	this.register[Register.T.ordinal()] = Processor.BIT_PER_CELL; break;
		case BITPERSLOT:	this.register[Register.T.ordinal()] = Processor.SLOT_BITS; break;
		case FLAGS:			this.register[Register.T.ordinal()] = Flag.values().length; break;
		case X:				this.register[Register.T.ordinal()] = processor.getX(); break;
		case Y:				this.register[Register.T.ordinal()] = processor.getY(); break;
		case Z:				this.register[Register.T.ordinal()] = processor.getZ(); break;
		default: processor.interrupt(Flag.ILLEGAL);
		}
	}

}
