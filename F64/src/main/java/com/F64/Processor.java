package com.F64;

public class Processor implements Runnable {

	public static final int		VERSION = 0x010000;
	public static final long	IO_BASE = 0xFFFF_FFFF_FFFF_FF00L;
	public static final int		BIT_PER_CELL = 64;
	public static final int		NO_OF_REG = 64;
	public static final int		SLOT_BITS = 6;
	public static final int		NO_OF_FULL_SLOTS = BIT_PER_CELL / SLOT_BITS;
	public static final int		SLOT_SIZE = 1 << SLOT_BITS;
	public static final int		SLOT_MASK = SLOT_SIZE - 1;
	public static final int		FINAL_SLOT_BITS = BIT_PER_CELL - (NO_OF_FULL_SLOTS*SLOT_BITS);
	public static final int		FINAL_SLOT_SIZE = 1 << FINAL_SLOT_BITS;
	public static final int		FINAL_SLOT_MASK = FINAL_SLOT_SIZE - 1;
	public static final int		FINAL_SLOT = NO_OF_FULL_SLOTS;
	public static final int		NO_OF_SLOTS = FINAL_SLOT+1;
	public static final long	TRUE = -1L;
	public static final long	FALSE = 0L;

	public static final long	SLOT0_MASK = -1L << (BIT_PER_CELL-SLOT_BITS);
	public static final long[]	SLOT_MASKS = {
		SLOT0_MASK,
		SLOT0_MASK >>> SLOT_BITS,
		SLOT0_MASK >>> (SLOT_BITS*2),
		SLOT0_MASK >>> (SLOT_BITS*3),
		SLOT0_MASK >>> (SLOT_BITS*4),
		SLOT0_MASK >>> (SLOT_BITS*5),
		SLOT0_MASK >>> (SLOT_BITS*6),
		SLOT0_MASK >>> (SLOT_BITS*7),
		SLOT0_MASK >>> (SLOT_BITS*8),
		SLOT0_MASK >>> (SLOT_BITS*9),
		SLOT0_MASK >>> (SLOT_BITS*10)
	};

	public static final long[]	REMAINING_MASKS = {
		-1L >>> SLOT_BITS,
		-1L >>> (SLOT_BITS*2),
		-1L >>> (SLOT_BITS*3),
		-1L >>> (SLOT_BITS*4),
		-1L >>> (SLOT_BITS*5),
		-1L >>> (SLOT_BITS*6),
		-1L >>> (SLOT_BITS*7),
		-1L >>> (SLOT_BITS*8),
		-1L >>> (SLOT_BITS*9),
		-1L >>> (SLOT_BITS*10)
	};

	
	public static final long[] BIT_MASK =
		{
			0x0000_0000_0000_0001L,
			0x0000_0000_0000_0002L,
			0x0000_0000_0000_0004L,
			0x0000_0000_0000_0008L,
			0x0000_0000_0000_0010L,
			0x0000_0000_0000_0020L,
			0x0000_0000_0000_0040L,
			0x0000_0000_0000_0080L,
			0x0000_0000_0000_0100L,
			0x0000_0000_0000_0200L,
			0x0000_0000_0000_0400L,
			0x0000_0000_0000_0800L,
			0x0000_0000_0000_1000L,
			0x0000_0000_0000_2000L,
			0x0000_0000_0000_4000L,
			0x0000_0000_0000_8000L,
			//
			0x0000_0000_0001_0000L,
			0x0000_0000_0002_0000L,
			0x0000_0000_0004_0000L,
			0x0000_0000_0008_0000L,
			0x0000_0000_0010_0000L,
			0x0000_0000_0020_0000L,
			0x0000_0000_0040_0000L,
			0x0000_0000_0080_0000L,
			0x0000_0000_0100_0000L,
			0x0000_0000_0200_0000L,
			0x0000_0000_0400_0000L,
			0x0000_0000_0800_0000L,
			0x0000_0000_1000_0000L,
			0x0000_0000_2000_0000L,
			0x0000_0000_4000_0000L,
			0x0000_0000_8000_0000L,
			//
			0x0000_0001_0000_0000L,
			0x0000_0002_0000_0000L,
			0x0000_0004_0000_0000L,
			0x0000_0008_0000_0000L,
			0x0000_0010_0000_0000L,
			0x0000_0020_0000_0000L,
			0x0000_0040_0000_0000L,
			0x0000_0080_0000_0000L,
			0x0000_0100_0000_0000L,
			0x0000_0200_0000_0000L,
			0x0000_0400_0000_0000L,
			0x0000_0800_0000_0000L,
			0x0000_1000_0000_0000L,
			0x0000_2000_0000_0000L,
			0x0000_4000_0000_0000L,
			0x0000_8000_0000_0000L,
			//
			0x0001_0000_0000_0000L,
			0x0002_0000_0000_0000L,
			0x0004_0000_0000_0000L,
			0x0008_0000_0000_0000L,
			0x0010_0000_0000_0000L,
			0x0020_0000_0000_0000L,
			0x0040_0000_0000_0000L,
			0x0080_0000_0000_0000L,
			0x0100_0000_0000_0000L,
			0x0200_0000_0000_0000L,
			0x0400_0000_0000_0000L,
			0x0800_0000_0000_0000L,
			0x1000_0000_0000_0000L,
			0x2000_0000_0000_0000L,
			0x4000_0000_0000_0000L,
			0x8000_0000_0000_0000L
		};
	
	
	public static int max_slot = 0;

	
	static
	{
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
		for (i=0; i<Ext2.values().length; ++i) {
			size = Ext2.values()[i].size();
			if (size < 0) {size = -size;}
			if (size > max_slot) {max_slot = size;}
		}
		for (i=0; i<Ext3.values().length; ++i) {
			size = Ext3.values()[i].size();
			if (size < 0) {size = -size;}
			if (size > max_slot) {max_slot = size;}
		}
	}

	private System				system;
	private long[]				register;
	private long[]				system_register;
	private long[]				read_port;
	private long[]				write_port;
	private long[]				parameter_stack;
	private long[]				return_stack;
	private Processor[]			port_partner;
	private long				communication;
	private int					communication_register;
	private int					x;
	private int					y;
	private int					z;
	private int					port_read_mask;
	private int					port_write_mask;
	private int					slot;
	private int					saved_slot;
//	private int					max_slot;	// max # of slots
	private boolean				carry;
	private boolean				failed;
	private boolean				waiting;
	private boolean				reading;
	private volatile boolean	running;


	public static long getIOAddress(int slot_bits)
	{
		assert(slot_bits >= 0);
		assert(slot_bits < SLOT_SIZE);
		return IO_BASE + slot_bits;
	}
	
	public static int countBits(long data)
	{
		int res = 0;
		while (data != 0) {
			++res;
			data &= data-1;
		}
		return res;
	}

	public static long reverseBits(long data)
	{
		// swap odd and even bits
		data = ((data >>> 1) & 0x55555555_55555555L) | ((data & 0x55555555_55555555L) << 1);
		// swap consecutive pairs
		data = ((data >>> 2) & 0x33333333_33333333L) | ((data & 0x33333333_33333333L) << 2);
		// swap nibbles ... 
		data = ((data >>> 4) & 0x0F0F0F0F_0F0F0F0FL) | ((data & 0x0F0F0F0F_0F0F0F0FL) << 4);
		// swap bytes
		data = ((data >>> 8) & 0x00FF00FF_00FF00FFL) | ((data & 0x00FF00FF_00FF00FFL) << 8);
		// swap 16-bit pairs
		data = ((data >>>16) & 0x0000FFFF_0000FFFFL) | ((data & 0x0000FFFF_0000FFFFL) <<16);
		// swap 32-bit halfs
		data = (data >>> 32) | (data << 32);
		return data;
	}

	public static int findFirstBit1(long data)
	{
		if (data == 0) {return -1;}
		int res = 0;
		while ((data & 1) == 0) {
			++res;
			data >>= 1;
		}
		return res;
	}

	public static int findLastBit1(long data)
	{
		if (data == 0) {return -1;}
		int res = 63;
		while (data >= 0) {
			--res;
			data <<= 1;
		}
		return res;
	}

	public static long writeSlot(long data, int slot, int value)
	{
		long mask;
		assert(slot >= 0);
		assert(value >= 0);
		if (slot > FINAL_SLOT) {
			assert(value == 0);
			return data;
		}
		if (slot == FINAL_SLOT) {assert(value < FINAL_SLOT_SIZE);}
		else {assert(value < SLOT_SIZE);}
		if (slot == FINAL_SLOT) {
			mask = FINAL_SLOT_MASK;
			data &= ~mask;
			data |= value;
			return data | value;
		}
		long tmp = value & SLOT_MASK;
		mask = SLOT_MASK;
		tmp <<= (BIT_PER_CELL-((slot+1)*SLOT_BITS));
		mask <<= (BIT_PER_CELL-((slot+1)*SLOT_BITS));
		data &= ~mask;
		return data | tmp;
	}

	public static int readSlot(long value, int slot)
	{
		assert(slot >= 0);
		int res = 0;
		if (slot < FINAL_SLOT) {res = ((int)(value >> (BIT_PER_CELL-((slot+1)*SLOT_BITS)))) & SLOT_MASK;}
		else if (slot == FINAL_SLOT) {res = ((int)value) & FINAL_SLOT_MASK;}
		return res;
	}

	public static long setBit(long data, int bitpos)
	{
		long mask = 1L << bitpos;
		return data | mask;
	}

	public static long clearBit(long data, int bitpos)
	{
		long mask = 1L << bitpos;
		return data | ~mask;
	}

	public static long toggleBit(long data, int bitpos)
	{
		long mask = 1L << bitpos;
		return data ^ mask;
	}

	
	public Processor(System system, int x, int y, int z, int stack_size, int return_stack_size)
	{
		this.system = system;
		this.x = x;
		this.y = y;
		this.z = z;
		this.register = new long[NO_OF_REG];
		this.system_register = new long[NO_OF_REG];
		this.read_port = new long[Port.values().length];
		this.write_port = new long[Port.values().length];
		this.port_partner = new Processor[Port.values().length];
		this.register[Register.Z.ordinal()] = 0;
		this.system_register[SystemRegister.INTE.ordinal()] = Flag.RESET.getMask() | Flag.NMI.getMask();
		if (stack_size > 0) {parameter_stack = new long[stack_size];}
		if (return_stack_size > 0) {return_stack = new long[return_stack_size];}
		//this.setInterruptFlag(Register.INTF, Interrupt.Reset, true);
	}

	public long getRegister(Register reg) {return this.getRegister(reg.ordinal());}
	public void setRegister(Register reg, long value) {this.setRegister(reg.ordinal(), value);}

	public long getRegister(SystemRegister reg) {return this.getSystemRegister(reg.ordinal());}
	public void setRegister(SystemRegister reg, long value) {this.setSystemRegister(reg.ordinal(), value);}

	public boolean getInternalCarry() {return this.carry;}
	public boolean hasFailed() {return this.failed;}
	public boolean isWaiting() {return this.waiting;}
	public int getSlot() {return this.slot;}
	public void setSlot(int reg, int slot, int value) {this.setRegister(reg, writeSlot(this.getRegister(reg), slot, value));}
	public int getSlot(int reg, int slot) {return readSlot(this.getRegister(reg), slot);}
	public int getSlot(int slot) {return readSlot(this.system_register[SystemRegister.I.ordinal()], slot);}
	public int nextSlot() {return readSlot(this.system_register[SystemRegister.I.ordinal()], this.slot++);}
	public Processor getPortPartner(int p) {return this.port_partner[p];}
	public Processor getPortPartner(Port p) {return this.getPortPartner(p.ordinal());}
	public void setPortPartner(int p, Processor value) {this.port_partner[p] = value;}
	public void setPortPartner(Port p, Processor value) {this.setPortPartner(p.ordinal(), value);}
	public long getPort(Port p, boolean writing) {return this.getPort(p.ordinal(), writing);}
	public void setPort(Port p, boolean writing, long value) {this.setPort(p.ordinal(), writing, value);}
	public int getPortReadMask() {return this.port_read_mask;}
	public int getPortWriteMask() {return this.port_write_mask;}

	public long adc(long a, long b, boolean carry)
	{
		long c = a+b;
		boolean overflow = (a > 0 && b > 0 && c < 0) || (a < 0 && b < 0 && c > 0);
		if (carry) {
			if (++c == 0) {overflow = true;}
		}
		setFlag(Flag.CARRY, overflow);
		return c;
	}

	public long asl(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		long mask = 0x8000_0000_0000_0000L >>> (shift-1);
		this.carry = (data & mask) != 0;
		data = data << shift;
		return data;
	}

	public long lsl(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		long mask = 0x8000_0000_0000_0000L >>> (shift-1);
		this.carry = (data & mask) != 0;
		data = data << shift;
		return data;
	}


	public long asr(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		long mask = 1L << (shift-1);
		this.carry = (data & mask) != 0;
		data = data >> shift;
		return data;
	}

	public long lsr(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		long mask = 1L << (shift-1);
		this.carry = (data & mask) != 0;
		data = data >>> shift;
		return data;
	}

	public long rol(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		data = (data << shift) | (data >>> (BIT_PER_CELL - shift));
		this.carry = (data & 1) != 0;
		return data;
	}

	public long ror(long data, int shift)
	{
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = false;
			return data;
		}
		data = (data << (BIT_PER_CELL - shift)) | (data >>> shift);
		this.carry = data < 0;
		return data;
	}

	public long rcl(long data, int shift, boolean carry)
	{
		boolean c;
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = carry;
			return data;
		}
		while (shift > 0) {
			c = data < 0;
			data <<= 1;
			if (carry) {++data;}
			carry = c;
			--shift;
		}
		this.carry = carry;
		return data;
	}

	public long rcr(long data, int shift, boolean carry)
	{
		boolean c;
		shift &= BIT_PER_CELL-1;
		if (shift == 0) {
			this.carry = carry;
			return data;
		}
		while (shift > 0) {
			c = (data & 1) != 0;
			data = data >>> 1;
			if (carry) {data |= 0x8000_0000_0000_0000L;}
			carry = c;
			--shift;
		}
		this.carry = carry;
		return data;
	}

	public boolean isReadingOn(Port p)
	{
		return this.waiting && ((this.port_read_mask & (1 << p.ordinal())) != 0);
	}

	public boolean isWritingOn(Port p)
	{
		return this.waiting && ((this.port_write_mask & (1 << p.ordinal())) != 0);
	}
	
	public long getRegister(int reg)
	{
		if (reg < NO_OF_REG) {return this.register[reg];}
		return this.getSystemRegister(reg - NO_OF_REG);
	}

	
	public void setRegister(int reg, long value)
	{
		if (reg > 0) {
			if (reg < NO_OF_REG) {
				register[reg] = value;
				return;
			}
			this.setSystemRegister(reg - NO_OF_REG, value);
		}
	}

	public long getFlagForInterrupt()
	{
		long res = this.system_register[SystemRegister.FLAG.ordinal()];
		int aux_data = this.slot;
		aux_data <<= 9;
		if (this.port_read_mask != 0) {
			aux_data |= this.port_read_mask;
		}
		else if (this.port_write_mask != 0) {
			aux_data |= this.port_write_mask;
			aux_data |= 0x100;
		}
		res = writeSlot(res, 0, (aux_data >> (2*SLOT_BITS)) & SLOT_MASK);
		res = writeSlot(res, 1, (aux_data >> (1*SLOT_BITS)) & SLOT_MASK);
		res = writeSlot(res, 2, (aux_data >> (0*SLOT_BITS)) & SLOT_MASK);
		return res;
	}
	
	public void setFlagForInterrupt(long data)
	{
		int aux_data = readSlot(data, 0);
		aux_data <<= SLOT_BITS;
		aux_data |= readSlot(data, 1);
		aux_data <<= SLOT_BITS;
		aux_data |= readSlot(data, 2);
		int port = aux_data & 0xff;
		if ((slot & 0x100) != 0) {
			this.port_write_mask = port;
		}
		else {
			this.port_read_mask = port;
		}
		aux_data >>= 9;
		this.slot = aux_data;
		long mask = -1;
		this.system_register[SystemRegister.FLAG.ordinal()] = data & (mask >>> (3*SLOT_BITS));
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
			this.slot = 0;
		}
//		if (reg == SystemRegister.FLAG.ordinal()) {
//			// upper 4 bits contain slot #
//			this.slot = (int)(value >>> (BIT_PER_CELL - 4));
//		}				
		else if (reg == SystemRegister.INTE.ordinal()) {
			value |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
		else if (reg == SystemRegister.P.ordinal()) {
			if (!system.isValidCodeAddress(value)) {
				if (this.interrupt(Flag.CODE)) {
					return false;
				}
			}
		}
		system_register[reg] = value;
		return true;
	}

//	public boolean handshake(Processor target, long value)
//	{
//		synchronized (target) {
//			if (target.communication_source == null) {
//				for (int i=0; i<Port.values().length; ++i) {
//					if ((target.port_read_mask & (1 << i)) != 0) {
//						// target wait for input on this port
//						if (target.port_partner[i] == this) {
//							// target expects input through this port from this
//							target.communication = value;
//							target.communication_source = this;
//							target.notify();
//							return true;
//						}
//					}
//				}
//			}
//		}
//		return false;
//	}

	public boolean canReadFromPort(int p)
	{
		return ((this.port_read_mask & (1 << p)) != 0);
	}

	public boolean canWriteToPort(int p)
	{
		return ((this.port_write_mask & (1 << p)) != 0);
	}

	public void readFromPort(int p)
	{
		int reg = this.communication_register;
		if (reg < BIT_PER_CELL) {
			this.setRegister(reg, this.communication);
		}
		else {
			reg -=  BIT_PER_CELL;
			this.setSystemRegister(reg, this.communication);
		}
		this.setFlag(Flag.UPREAD.ordinal()+p, true);
		this.setPort(p, false, this.communication);
		this.port_read_mask = 0;
		this.waiting = false;
	}

	public void writeToPort(int p)
	{
		this.setFlag(Flag.UPWRITE.ordinal()+p, true);
		this.setPort(p, true, this.communication);
		this.port_write_mask = 0;
		this.waiting = false;
	}

	public boolean readPort()
	{
		Processor partner;
		if (this.port_read_mask != 0) {
			int limit = Port.values().length;
			for (int i=0; i<limit; ++i) {
				if (canReadFromPort(i)) {
					partner = this.port_partner[i];
					if ((partner != null) && partner.canWriteToPort(i)) {
						// communication settled
						this.communication = partner.communication;
						this.readFromPort(i);
						partner.writeToPort(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean writePort()
	{
		Processor partner;
		if (this.port_write_mask != 0) {
			int limit = Port.values().length;
			for (int i=0; i<limit; ++i) {
				if (canWriteToPort(i)) {
					partner = this.port_partner[i];
					if ((partner != null) && partner.canReadFromPort(i)) {
						// communication settled
						partner.communication = this.communication;
						partner.readFromPort(i);
						this.writeToPort(i);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean externalWriteToPort(int p, long value)
	{
		if (this.port_read_mask != 0) {
			if (canReadFromPort(p)) {
				this.communication = value;
				this.readFromPort(p);
			}
		}
		return false;
	}

	
//	public void writePort(int mask, boolean wait, long value)
//	{
//		Processor partner;
//		int i,limit = Port.values().length;
//		this.port_write_mask = mask;
//		for (i=0; i<limit; ++i) {
//			if ((mask & (1 << i)) != 0) {
//				partner = this.port_partner[i];
//				if (partner != null) {
//					if (partner.writeToPort(i, value)) {
//						this.setFlag(Flag.UPWRITE.ordinal()+i, true);
//						this.setPort(i, true, value);
//						this.port_write_mask = 0;
//						this.waiting = false;
//						return;
//					}
//				}
//			}
//		}
//		if (wait) {
//			this.waiting = true;
//		}
//		else {
//			this.waiting = false;
//			this.port_write_mask = 0;
//		}
//	}
//
//	public long readPort(int mask, boolean wait)
//	{
//		int i,limit = Port.values().length;
//		this.port_read_mask = mask;
//		if (this.communication_source != null) {
//			// someone has communicated with us
//			this.port_read_mask = 0;
//			for (i=0; i<limit; ++i) {
//				if ((mask & (1 << i)) != 0) {
//					if (this.port_partner[i] == this.communication_source) {
//						this.waiting = false;
//						long value = this.communication;
//						this.communication = 0;
//						this.setFlag(Flag.UPREAD.ordinal()+i, true);
//						this.setPort(i, false, value);
//						return value;
//					}
//				}
//			}
//		}
//		if (wait) {
//			this.waiting = true;
//		}
//		else {
//			this.waiting = false;
//			this.port_read_mask = 0;
//		}
//		return 0;
//	}

	public long getPort(int p, boolean writing)
	{
		if (writing) {return this.write_port[p];}
		return this.read_port[p];
	}

	public void setPort(int p, boolean writing, long value)
	{
		if (writing) {this.write_port[p] = value;}
		else {this.read_port[p] = value;}
	}

	
	

	


	public boolean getFlag(int fl)
	{
		long mask = 1;
		mask <<= fl;
		return (this.system_register[SystemRegister.FLAG.ordinal()] & mask) != 0;
	}

	public boolean getFlag(Flag fl)
	{
		return (this.system_register[SystemRegister.FLAG.ordinal()] & fl.getMask()) != 0;
	}
	
	public void setFlag(int fl, boolean value)
	{
		long mask = 1;
		mask <<= fl;
		if (value) {
			this.system_register[SystemRegister.FLAG.ordinal()] |= mask;
		}
		else {
			this.system_register[SystemRegister.FLAG.ordinal()] &= ~mask;			
		}
	}

	public void setFlag(Flag fl, boolean value)
	{
		if (value) {
			this.system_register[SystemRegister.FLAG.ordinal()] |= fl.getMask();
		}
		else {
			this.system_register[SystemRegister.FLAG.ordinal()] &= ~fl.getMask();			
		}
	}

//	public boolean getFlag(Register reg, Flag fl)
//	{
//		return (this.register[reg.ordinal()] & fl.getMask()) != 0;
//	}

	public boolean getFlag(SystemRegister reg, Flag fl)
	{
		return (this.system_register[reg.ordinal()] & fl.getMask()) != 0;
	}

	public boolean getInterruptFlag(SystemRegister reg, int fl)
	{
		long mask = 1;
		mask <<= fl;
		return (this.system_register[reg.ordinal()] & mask) != 0;
	}

	public void setFlag(SystemRegister reg, Flag fl, boolean value)
	{
		if (value) {
			this.system_register[reg.ordinal()] |= fl.getMask();
		}
		else if (reg != SystemRegister.INTE) {
			this.system_register[reg.ordinal()] &= ~fl.getMask();			
		}
		else {
			this.system_register[SystemRegister.INTE.ordinal()] &= ~fl.getMask();			
			this.system_register[SystemRegister.INTE.ordinal()] |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
	}

	public void setInterruptFlag(SystemRegister reg, int fl, boolean value)
	{
		long mask = 1;
		mask <<= fl;
		if (value) {
			this.system_register[reg.ordinal()] |= mask;
		}
		else if (reg != SystemRegister.INTE) {
			this.system_register[reg.ordinal()] &= ~mask;			
		}
		else {
			this.system_register[SystemRegister.INTE.ordinal()] &= ~mask;			
			this.system_register[SystemRegister.INTE.ordinal()] |= Flag.RESET.getMask() | Flag.NMI.getMask();
		}
	}

	public long getStackPosition(int offset)
	{
		long pos = this.system_register[SystemRegister.SP.ordinal()] + offset;
		while (pos > this.system_register[SystemRegister.SL.ordinal()]) {
			pos -= this.system_register[SystemRegister.SL.ordinal()] + 1;
			pos += this.system_register[SystemRegister.S0.ordinal()];
		}
		while (pos < this.system_register[SystemRegister.S0.ordinal()]) {
			pos += this.system_register[SystemRegister.SL.ordinal()] + 1;
			pos -= this.system_register[SystemRegister.S0.ordinal()];			
		}
		return pos;
	}

	public long getReturnStackPosition(int offset)
	{
		long pos = this.system_register[SystemRegister.RP.ordinal()] + offset;
		while (pos > this.system_register[SystemRegister.RL.ordinal()]) {
			pos -= this.system_register[SystemRegister.RL.ordinal()] + 1;
			pos += this.system_register[SystemRegister.R0.ordinal()];
		}
		while (pos < this.system_register[SystemRegister.R0.ordinal()]) {
			pos += this.system_register[SystemRegister.RL.ordinal()] + 1;
			pos -= this.system_register[SystemRegister.R0.ordinal()];			
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
	 * Increment register
	 */
	public void inc(Register reg)
	{
		this.setRegister(reg, this.getRegister(reg)+1);
	}


	/**
	 * Increment register
	 */
	public void dec(Register reg)
	{
		this.setRegister(reg, this.getRegister(reg)-1);
	}

	/**
	 * Increment register
	 */
	public void inc(SystemRegister reg)
	{
		this.setRegister(reg, this.getRegister(reg)+1);
	}


	/**
	 * Increment register
	 */
	public void dec(SystemRegister reg)
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
	 * Increment register. Do nothing if the register points not into memory
	 */
	public void incPointer(Register reg)
	{
		long value = this.getRegister(reg);
		if (value >= 0) {
			this.setRegister(reg, value+1);
		}
	}


	/**
	 * Increment register. Do nothing if the register points not into memory
	 */
	public void decPointer(SystemRegister reg)
	{
		long value = this.getRegister(reg);
		if (value >= 0) {
			this.setRegister(reg, value-1);
		}
	}

	public void incPointer(SystemRegister reg)
	{
		long value = this.getRegister(reg);
		if (value >= 0) {
			this.setRegister(reg, value+1);
		}
	}


	/**
	 * Increment register. Do nothing if the register points not into memory
	 */
	public void decPointer(Register reg)
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
		incPointer(SystemRegister.P);
	}

	public long fetchPInc()
	{
		long res = system.getMemory(this.getRegister(SystemRegister.P));
		incPointer(SystemRegister.P);
		return res;
		
	}

	public long remainingSlots()
	{
		long res = this.system_register[SystemRegister.I.ordinal()] & REMAINING_MASKS[this.slot];
		this.slot = NO_OF_SLOTS;
		return res;
	}

	/**
	 * Jump to an address. The content of I is lost.
	 */
	public void jumpRemainigSlots()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = REMAINING_MASKS[this.slot];
		long data = this.getRegister(SystemRegister.I) & mask;
		long pc = this.system_register[SystemRegister.P.ordinal()];
		// replace lower bits of pc with the bits from the remaining slots
		this.system_register[SystemRegister.P.ordinal()] |= pc ^ ((pc ^ data) & mask);
		this.slot = NO_OF_SLOTS; // force to load new instruction cell & start with slot 0
	}

	/**
	 * Replace the lowest 6 bits of a value with the next slot
	 * @param base
	 * @return
	 */
	public long replaceNextSlot(long base)
	{
		long res = (-1 << SLOT_BITS) & base;
		return res | nextSlot();
	}

	public void shortJump(int slot_bits)
	{
		long mask = SLOT_MASK;
		this.system_register[SystemRegister.P.ordinal()] &= ~mask;
		this.system_register[SystemRegister.P.ordinal()] |= slot_bits;
		this.slot = 0;
	}

	public boolean doConditionalJump(int condition)
	{
		switch (Condition.values()[(condition >> 4) & 3]) {
		case EQ0:		if (this.register[Register.T.ordinal()] != 0) {this.doDrop(); return false;} this.doDrop(); break;
		case GE0:		if (this.register[Register.T.ordinal()] < 0) {this.doDrop(); return false;} this.doDrop(); break;
		case CARRY:		if (!this.getFlag(Flag.CARRY)) {return false;} break;
		default: break;
		}
		switch (Branch.values()[condition & 0xf]) {
		case SLOT0:		this.slot = 0; break;
		case SLOT1:		this.slot = 1; break;
		case SLOT2:		this.slot = 2; break;
		case SLOT3:		this.slot = 3; break;
		case SLOT4:		this.slot = 4; break;
		case SLOT5:		this.slot = 5; break;
		case SLOT6:		this.slot = 6; break;
		case SLOT7:		this.slot = 7; break;
		case SLOT8:		this.slot = 8; break;
		case SLOT9:		this.slot = 9; break;
		case SLOT10:	this.slot = 10; break;
		case NEXT:		this.slot = NO_OF_SLOTS; break;
		case SHORT:		this.shortJump(this.nextSlot()); break;
		case IO:		this.doJumpIO(this.nextSlot()); break;
		case LONG:		this.setRegister(SystemRegister.P, this.fetchPInc()); this.slot = 0; break;
		case REM:		this.jumpRemainigSlots(); break;
		}
		return true;
	}
	
	public long drop()
	{
		long res = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();
		return res;
	}

	public void doDrop()
	{
		this.register[Register.T.ordinal()] = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.popStack();		
	}

	public void doDup()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];		
	}

	public void doNext()
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			if (doConditionalJump(this.nextSlot())) {
				--this.register[Register.R.ordinal()];
			}
		}
	}
	
	public boolean doCallMethod(long index)
	{
		// push P on return stack
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.P.ordinal()];
		// check index is in range of method table
		if (index >= system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			if (this.interrupt(Flag.BOUND)) {
				return false;
			}
		}
		// load address of method
		this.system_register[SystemRegister.P.ordinal()] = system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY);
		this.slot = NO_OF_SLOTS;
		return true;
	}

	
	public boolean doJumpMethod(long index)
	{
		// check index is in range of method table
		if (index >= system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			if (this.interrupt(Flag.BOUND)) {
				return false;
			}
		}
		// load address of method
		this.system_register[SystemRegister.P.ordinal()] = system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY);
		this.slot = NO_OF_SLOTS;
		return true;
	}

	public void doSwap(int src, int dst)
	{
		long tmp = this.register[dst];
		this.setRegister(dst, this.register[src]);
		this.setRegister(src, tmp);
	}

	public void doMove(int src, int dst)
	{
		this.setRegister(dst, this.register[src]);
	}

	public void doMoveStack(int src, int dst)
	{
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

	public void doRFetch(int reg)
	{
		long tmp = this.getRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doRStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		this.setRegister(reg, tmp);
	}

	public void doFetchR(int reg)
	{
		this.doDup();
		this.register[Register.T.ordinal()] = system.getMemory(this.getRegister(reg));
	}
	

	public void doStoreR(int reg)
	{
		system.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
		this.doDrop();
	}

	public void doFetchPInc()
	{
		doDup();
		this.register[Register.T.ordinal()] = system.getMemory(this.getRegister(SystemRegister.P));
		incPointer(SystemRegister.P);
	}
	

	public void doStorePInc()
	{
		system.setMemory(this.getRegister(SystemRegister.P), this.register[Register.T.ordinal()]);
		doDrop();
		incPointer(SystemRegister.P);
	}

	public void doFetchRegisterInc(int reg)
	{
		doDup();
		this.register[Register.T.ordinal()] = system.getMemory(this.getRegister(reg));
		this.incPointer(reg);
	}
	

	public void doStoreRegisterInc(int reg)
	{
		system.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
		doDrop();
		this.incPointer(reg);
	}

	public void doExit()
	{
		this.system_register[SystemRegister.P.ordinal()] = this.register[Register.R.ordinal()];
		this.register[Register.R.ordinal()] = this.popReturnStack();
		this.slot = NO_OF_SLOTS;
	}

	public void doUNext()
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.slot = 0;
		}
	}

	public void doCont()
	{
		this.system_register[SystemRegister.I.ordinal()] = this.register[Register.T.ordinal()];
		this.doDrop();
//		this.register[Register.R.ordinal()] = this.popReturnStack();
		this.slot = 0;		
	}

	public void doAdd(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 + src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAddi(int d, int s1, int src2)
	{
		this.setRegister(d, this.getRegister(s1) + src2);
	}

	public void doAddWithCarry(int d, int s1, int s2)
	{
		boolean carry = getFlag(Flag.CARRY);
		long a = this.getRegister(s1);
		long b = this.getRegister(s2);
		this.setRegister(d, this.adc(a, b, carry));
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	
	public void doSub(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 - src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doSubi(int d, int s1, int src2)
	{
		this.setRegister(d, this.getRegister(s1) - src2);
	}

	public void doSubtractWithCarry(int d, int s1, int s2)
	{
		boolean carry = getFlag(Flag.CARRY);
		long a = this.getRegister(s1);
		long b = ~this.getRegister(s2);
		this.setRegister(d, this.adc(a, ~b, carry));
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAnd(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 & src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doOr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 | src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doXor(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doXNor(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ ~src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}


	public void doAsl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.asl(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAsr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.asr(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doLsl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.lsl(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doLsr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.lsr(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doAsli(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.asl(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doAsri(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.asr(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doLsli(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.lsl(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doLsri(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.lsr(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}


	public void doRol(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.rol(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doRor(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.ror(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doRcl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.rcl(src1, (int)src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doRcr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.rcr(src1, (int)src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doRoli(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.rol(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doRori(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.ror(src1, src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doRcli(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.rcl(src1, src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doRcri(int d, int s1, int src2)
	{
		long src1 = this.getRegister(s1);
		long dest = this.rcr(src1, src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	
	public void doMul2Add(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = (src1 << 1) + src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doDiv2Sub(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = (src1 >> 1) - src2;
		this.setRegister(d, dest);
		if ((s1 == Register.S.ordinal()) || (s2 == Register.S.ordinal())) {this.doNip();}
	}

	public void doOver()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		long tmp = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doUnder()
	{
		this.pushStack(this.register[Register.S.ordinal()]);
		long tmp = this.register[Register.S.ordinal()];
		this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doTuck()
	{
		this.pushStack(this.register[Register.T.ordinal()]);
	}

	public void doNip()
	{
		this.register[Register.S.ordinal()] = this.popStack();
	}

	public void doLit()
	{
		this.doDup();
		this.register[Register.T.ordinal()] = this.nextSlot();
	}

	public void doBitLit()
	{
		this.doDup();
		long tmp = 1;
		this.register[Register.T.ordinal()] = tmp << this.nextSlot();
	}

	public void doExtendLiteral()
	{
		this.register[Register.T.ordinal()] <<= SLOT_BITS;
		this.register[Register.T.ordinal()] |= this.nextSlot();
	}

	public void doShortJump()
	{
		this.system_register[SystemRegister.P.ordinal()] = this.replaceNextSlot(this.system_register[SystemRegister.P.ordinal()]);
		this.slot = NO_OF_SLOTS;		
	}

	public void doCall()
	{
		// push P on return stack
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.P.ordinal()];
		// load new P
		this.jumpRemainigSlots();		
	}

	public void doPush(int reg)
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

	public void doPop(int reg)
	{
		if (reg == Register.S.ordinal()) {
			this.pushStack(this.register[Register.S.ordinal()]);
		}
		else if (reg == Register.T.ordinal()) {
			this.doDup();
		}
		this.setRegister(reg, this.system_register[SystemRegister.P.ordinal()]);
		this.system_register[SystemRegister.P.ordinal()] = this.popReturnStack();
	}

	public void doLoadSelf()
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.SELF.ordinal()];
		this.system_register[SystemRegister.SELF.ordinal()] = this.register[Register.T.ordinal()];
		this.doDrop();
	}

	public void doLoadMT()
	{
		this.system_register[SystemRegister.MT.ordinal()] = system.getMemory(this.system_register[SystemRegister.SELF.ordinal()]);
	}

	public void step()
	{
		try {
			if (this.waiting) {
				// try to finish communication
				if (reading) {
					this.readPort();
				}
				else {
					this.writePort();
				}
			}
			if (!this.waiting) {
				this.saved_slot = this.slot; // save slot # in case of an interrupt before the operation
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
				case BRANCH:	this.doConditionalJump(this.nextSlot()); break;
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
				case SWAP:		this.doSwap(this.nextSlot(), this.nextSlot()); break;
				case SWAP0:		this.doSwap(this.nextSlot(), this.nextSlot()); this.slot = 0; break;
				case MOV:		this.doMove(this.nextSlot(), this.nextSlot()); break;
				case MOVS:		this.doMoveStack(this.nextSlot(), this.nextSlot()); break;
				case LOADSELF:	this.doLoadSelf(); break;
				case LOADMT:	this.doLoadMT(); break;
				case RFETCH:	this.doRFetch(this.nextSlot()); break;
				case RSTORE:	this.doRStore(this.nextSlot()); break;
				case FETCHR:	this.doFetchR(this.nextSlot()); break;
				case STORER:	this.doStoreR(this.nextSlot()); break;
				case RINC:		this.inc(Register.values()[this.nextSlot()]); break;
				case RDEC:		this.dec(Register.values()[this.nextSlot()]); break;
				case RPINC:		this.incPointer(Register.values()[this.nextSlot()]); break;
				case RPDEC:		this.decPointer(Register.values()[this.nextSlot()]); break;
				case FETCHPINC:	this.doFetchPInc(); break;
				case STOREPINC:	this.doStorePInc(); break;
				case ADD:		this.doAdd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
				case SUB:		this.doSub(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
				case OR:		this.doOr(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
				case NOT:		this.doXNor(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
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
				case REGOP:		this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				case SIMD:		this.doSIMDOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				default: if (this.interrupt(Flag.ILLEGAL)) {return;}
				}
			}
			// check if there is some interrupt pending
			if ((this.system_register[SystemRegister.FLAG.ordinal()] & this.system_register[SystemRegister.INTE.ordinal()]) != 0) {
				// there is some pending interrupt
				triggerInterrupts();
			}
			//
			if ((!this.waiting) && (this.slot > FINAL_SLOT)) {
				// load new instruction cell
				long adr = this.system_register[SystemRegister.P.ordinal()];
				if (adr >= 0) {
					// normal memory
					this.setSystemRegister(SystemRegister.I.ordinal(), system.getMemory(adr));
					// increment P
					inc(SystemRegister.P);
				}
				else {
					// I/O memory
					this.communication_register = SystemRegister.I.ordinal() + BIT_PER_CELL;
					this.port_read_mask = (int)adr & 0xff;
					if (!this.readPort()) {
						this.waiting = true;
						this.reading = true;
					}
				}
			}
		}
		catch (java.lang.Exception ex) {
			ex.printStackTrace();
			this.failed = true;
			throw ex;
		}
	}

	/**
	 * set the system register MDP with sign information about S & T.
	 * Convert T & S into unsigned numbers
	 */
	public void doMultiplyDividePrepare()
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
		this.system_register[SystemRegister.MDP.ordinal()] = res;
	}

	/*
	 * S = multiplicand
	 * T = result low
	 * MD = result high
	 */
	public void doMultiplyFinished()
	{
		int mdp = (int)this.system_register[SystemRegister.MDP.ordinal()];
		if ((mdp & 2) != 0) {
			this.register[Register.T.ordinal()] = -this.register[Register.T.ordinal()];
		}
		this.doNip();
	}

	public void doDivideModFinished()
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

	
	/**
	 *
	 */
	public void doMultiplyStep()
	{
		long tmp = 0;
		long t = this.register[Register.T.ordinal()];
		if ((t & 1) != 0) {
			tmp = this.register[Register.S.ordinal()];
		}
		long md = this.system_register[SystemRegister.MD.ordinal()];
		md = this.adc(tmp, md, false);
		md = this.rcr(md, 1, this.getFlag(Flag.CARRY));
		t = this.rcr(t, 1, this.getFlag(Flag.CARRY));
		this.system_register[SystemRegister.MD.ordinal()] = md;
		this.register[Register.T.ordinal()] = t;
	}

	/**
	 * S = Dividend
	 * T = Divisor
	 * MD = Remainder
	 */
	public void doDivideStep()
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


//	/**
//	 * S = System(MD):S / T (floored division)
//	 * T = System(MD):S MOD T (floored division)
//	 */
//	public void doDivMod()
//	{
//		//TODO
//	}

	public void doRol(int reg, int shift)
	{
		this.register[reg] = this.rol(this.register[reg], shift);
		setFlag(Flag.CARRY, this.carry);
	}

	public void doRor(int reg, int shift)
	{
		this.register[reg] = this.ror(this.register[reg], shift);
		setFlag(Flag.CARRY, this.carry);
	}

	public void doRcl(int reg, int shift)
	{
		this.register[reg] = this.rcl(this.register[reg], shift, getFlag(Flag.CARRY));
		setFlag(Flag.CARRY, this.carry);
	}

	public void doRcr(int reg, int shift)
	{
		this.register[reg] = this.rcr(this.register[reg], shift, getFlag(Flag.CARRY));
		setFlag(Flag.CARRY, this.carry);
	}

	public void doSetBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, value | mask);
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doClearBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, clearBit(value, bitpos));
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doToggleBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		this.setRegister(reg, value ^ mask);
		setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {this.doNip();}
	}

	public void doReadBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? this.getRegister(reg) : this.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? this.getRegister(bit) : this.getRegister(reg);
		setFlag(Flag.CARRY, (value & mask) != 0);
	}

	public void doWriteBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		if (getFlag(Flag.CARRY)) {
			this.doSetBit(reg, bit, bit_is_reg, swap_source);
		}
		else {
			this.doClearBit(reg, bit, bit_is_reg, swap_source);
		}
	}

	public void doEnterM()
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.MT.ordinal()];
		this.system_register[SystemRegister.MT.ordinal()] = system.getMemory(this.system_register[SystemRegister.SELF.ordinal()]);
	}

	public void doEnterInterrupt(int no)
	{
		// mark interrupt as in service
		this.setInterruptFlag(SystemRegister.INTS, no, true);
		// clear interrupt flag
		this.setFlag(no, false);
		//
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		// save flags (with current slot)
		this.pushReturnStack(this.getFlagForInterrupt());
		this.port_read_mask = 0;
		this.port_write_mask = 0;
		this.waiting = false;
		// save I
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.I.ordinal()];
		// load instruction from interrupt vector table
		this.system_register[SystemRegister.I.ordinal()] = system.getMemory(this.system_register[SystemRegister.INTV.ordinal()]+no);
		// start with slot 0
		this.slot = 0;
	}

	public void doExitInterrupt(int no)
	{
		// restore I
		this.system_register[SystemRegister.I.ordinal()] = this.register[Register.R.ordinal()];
		// restore flags with slot
		this.setFlagForInterrupt(this.popReturnStack());
		this.waiting = (this.port_read_mask != 0) || (this.port_write_mask != 0);
		// restore R
		this.register[Register.R.ordinal()] = this.popReturnStack();
		// clear interrupt service register
		this.setInterruptFlag(SystemRegister.INTS, no, false);
	}

	public void doConfigFetch(int no)
	{
		this.doDup(); 
		if (no > Config.values().length) {
			this.register[Register.T.ordinal()] = 0;
			return;
		}
		switch (Config.values()[no]) {
		case VERSION:		this.register[Register.T.ordinal()] = VERSION; break;
		case BITPERCELL:	this.register[Register.T.ordinal()] = BIT_PER_CELL; break;
		case BITPERSLOT:	this.register[Register.T.ordinal()] = SLOT_BITS; break;
		case FLAGS:			this.register[Register.T.ordinal()] = Flag.values().length; break;
		case X:				this.register[Register.T.ordinal()] = x; break;
		case Y:				this.register[Register.T.ordinal()] = y; break;
		case Z:				this.register[Register.T.ordinal()] = z; break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}
	
	public void doJumpIO(int mask)
	{
		this.setRegister(SystemRegister.P, getIOAddress(mask));
		this.slot = NO_OF_SLOTS; // leave slot
	}

	public void doBitCnt(int src, int dest)
	{
		this.setRegister(dest, countBits(this.getRegister(src)));
	}


	public void doBitFindFirst1(int src, int dest)
	{
		this.setRegister(dest, findFirstBit1(this.getRegister(src)));
	}

	public void doBitFindLast1(int src, int dest)
	{
		this.setRegister(dest, findLastBit1(this.getRegister(src)));
	}

	public void doLiteralNot(int value)
	{
		long data = value;
		this.pushT(~data);
	}

	public void doRDrop()
	{
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}


	public void doSetFlags(int flag)
	{
		this.setFlag(flag, true);
	}

	public void doClearFlags(int flag)
	{
		this.setFlag(flag, false);
	}

	/**
	 * ( a - n )
	 * Fetch a value and mark the location as reserved. Any write access to this location
	 * may generate a RESERVED exception.
	 * The RESERVE register is set to the observed address.
	 * @throws Exception
	 */
	public boolean doFetchReserved()
	{
		long adr = this.getRegister(Register.T);
		if (this.getRegister(SystemRegister.RES) != 0) {
			// some memory has already been reserved
			if (interrupt(Flag.TOUCHED)) {
				return false;
			}
//			this.setFlag(Flag.RESERVED, true);
		}
		this.setRegister(SystemRegister.RES, adr);
		this.setRegister(Register.T, system.getMemory(adr));
		return true;
	}

	/**
	 * ( n1 a - n2 )
	 * Store the value n1 at address a, iff a is a reserved location and it has not been touched.
	 * The result n2 is 0 if the store was successful, otherwise it contains an reason code.
	 * The RESERVE register is reset to 0.
	 */
	public void doStoreConditional()
	{
		long adr = this.getRegister(Register.T);
		long value = this.getRegister(Register.S);
		if (this.getRegister(SystemRegister.RES) != adr) {
			// memory has not been reserved
		}
		if (getFlag(Flag.TOUCHED)) {
			this.setRegister(Register.T, -1);
		}
		else {
			system.setMemory(adr, value);
			this.setRegister(Register.T, 0);
		}
		this.setRegister(SystemRegister.RES, 0);
		this.doNip();
	}

	public void doFetchPort(int mask, boolean wait)
	{
		this.doDup();
		this.communication_register = Register.T.ordinal();
		this.port_read_mask = mask;
		if (!readPort() && wait) {
			this.waiting = true;
			this.reading = true;
		}
	}

	public void doStorePort(int mask, boolean wait)
	{
		this.communication = this.getRegister(Register.T);
		this.doDrop();
		this.port_write_mask = mask;
		if (!this.writePort() && wait) {
			this.waiting = true;
			this.reading = false;
		}
	}


	public void doFetchSystem(int reg)
	{
		this.doDup();
		this.setRegister(Register.T, this.getSystemRegister(reg));
	}

	public void doStoreSystem(int reg)
	{
		this.setSystemRegister(reg, this.getRegister(Register.T));
		this.doDrop();
	}

	public void doEQ0Q(int reg)
	{
		if (this.register[reg] == 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doNE0Q(int reg)
	{
		if (this.register[reg] != 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doGT0Q(int reg)
	{
		if (this.register[reg] > 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doGE0Q(int reg)
	{
		if (this.register[reg] >= 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doLT0Q(int reg)
	{
		if (this.register[reg] < 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doLE0Q(int reg)
	{
		if (this.register[reg] <= 0) {
			this.register[reg] = TRUE;
		}
		else {
			this.register[reg] = FALSE;
		}
	}

	public void doAbs(int reg)
	{
		long data = this.register[reg];
		if (data < 0) {
			this.register[reg] = -data;
		}
		
	}

	public void doNegate(int reg)
	{
		this.register[reg] = -this.register[reg];
	}

	public void doReverse(int reg)
	{
		this.register[reg] = reverseBits(this.register[reg]);
	
	}

	public void doExt1()
	{
		switch (Ext1.values()[this.nextSlot()]) {
		case RDROP:		this.doRDrop(); break;
		case EXITI:		this.doExitInterrupt(this.nextSlot()); break;
		case ADDC:		this.doAddWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case SUBC:		this.doSubtractWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case ROL:		this.doRol(Register.T.ordinal(), 1); break;
		case ROR:		this.doRor(Register.T.ordinal(), 1); break;
		case RCL:		this.doRcl(Register.T.ordinal(), 1); break;
		case RCR:		this.doRcr(Register.T.ordinal(), 1); break;
		case SFLAG:		this.doSetFlags(this.nextSlot()); break;
		case CFLAG:		this.doClearFlags(this.nextSlot()); break;
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
		case FETCHINC:	this.doFetchRegisterInc(this.nextSlot()); break;
		case STOREINC:	this.doStoreRegisterInc(this.nextSlot()); break;
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
		case BITCNT:	this.doBitCnt(this.nextSlot(), this.nextSlot()); break;
		case BITFF1:	this.doBitFindFirst1(this.nextSlot(), this.nextSlot()); break;
		case BITFL1:	this.doBitFindLast1(this.nextSlot(), this.nextSlot()); break;
		case NLIT:		this.doLiteralNot(this.nextSlot()); break;
		case JMPIO:		this.doJumpIO(this.nextSlot()); break;
		case CONFIGFETCH:	this.doConfigFetch(this.nextSlot()); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt2()
	{
		switch (Ext2.values()[this.nextSlot()]) {
		case TUCK:			this.doTuck(); break;
		case UNDER:			this.doUnder(); break;
		case MULS:			this.doMultiplyStep(); break;
		case DIVS:			this.doDivideStep(); break;
		case MDP:			this.doMultiplyDividePrepare(); break;
		case MULF:			this.doMultiplyFinished(); break;
		case DIVMODF:		this.doDivideModFinished(); break;
		case ABS:			this.doAbs(Register.T.ordinal()); break;
		case NEGATE:		this.doNegate(Register.T.ordinal()); break;
		case ROL:			this.doRol(Register.T.ordinal(), this.nextSlot()); break;
		case ROR:			this.doRor(Register.T.ordinal(), this.nextSlot()); break;
		case RCL:			this.doRcl(Register.T.ordinal(), this.nextSlot()); break;
		case RCR:			this.doRcr(Register.T.ordinal(), this.nextSlot()); break;
		case EQ0Q:			this.doEQ0Q(Register.T.ordinal()); break;
		case NE0Q:			this.doNE0Q(Register.T.ordinal()); break;
		case GT0Q:			this.doGT0Q(Register.T.ordinal()); break;
		case GE0Q:			this.doGE0Q(Register.T.ordinal()); break;
		case LT0Q:			this.doLT0Q(Register.T.ordinal()); break;
		case LE0Q:			this.doLE0Q(Register.T.ordinal()); break;
		case FETCHSYSTEM:	this.doFetchSystem(this.nextSlot()); break;
		case STORESYSTEM:	this.doStoreSystem(this.nextSlot()); break;
		case FETCHRES:		this.doFetchReserved(); break;
		case STORECOND:		this.doStoreConditional(); break;
		case FETCHPORT: 	this.doFetchPort(this.nextSlot(), false); break;
		case STOREPORT: 	this.doStorePort(this.nextSlot(), false); break;
		case FETCHPORTWAIT: this.doFetchPort(this.nextSlot(), true); break;
		case STOREPORTWAIT: this.doStorePort(this.nextSlot(), true); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt3()
	{
		switch (Ext3.values()[this.nextSlot()]) {
		case ABS:			this.doAbs(this.nextSlot()); break;
		case NEGATE:		this.doNegate(this.nextSlot()); break;
		case REVERSE:		this.doReverse(Register.T.ordinal()); break;
		case ROL:			this.doRol(this.nextSlot(), 1); break;
		case ROR:			this.doRor(this.nextSlot(), 1); break;
		case RCL:			this.doRcl(this.nextSlot(), 1); break;
		case RCR:			this.doRcr(this.nextSlot(), 1); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt4()
	{
		switch (Ext4.values()[this.nextSlot()]) {
		case ROL:			this.doRol(this.nextSlot(), this.nextSlot()); break;
		case ROR:			this.doRor(this.nextSlot(), this.nextSlot()); break;
		case RCL:			this.doRcl(this.nextSlot(), this.nextSlot()); break;
		case RCR:			this.doRcr(this.nextSlot(), this.nextSlot()); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt5()
	{
	}

	public void doExt6()
	{
	}

	public boolean interrupt(Flag no)
	{
		// signaling interrupt
		this.setFlag(no, true);
		if (this.getInterruptFlag(SystemRegister.INTE, no.ordinal())) {
			// interrupt is enabled
			if (!this.getFlag(SystemRegister.INTS, Flag.values()[no.ordinal()])) {
				// interrupt is not serviced yet
				this.slot = this.saved_slot;
				this.doEnterInterrupt(no.ordinal());
				return true;
			}
		}
		return false;
	}

	public void triggerInterrupts()
	{
		// scan interrupts in reverse priority
		for (int i=Flag.values().length-1; i>=0; --i) {
			if (this.getFlag(i) && this.getInterruptFlag(SystemRegister.INTE, i)) {
				// interrupt is enabled
				if (!this.getFlag(SystemRegister.INTS, Flag.values()[i])) {
					// interrupt is not serviced yet
					doEnterInterrupt(i);
				}
			}
		}
	}

	public void doThrow(Exception ex)
	{
		
	}
	
	public void nmi()
	{
		this.interrupt(Flag.NMI);
	}

	public void incrementExternalClock()
	{
		this.inc(SystemRegister.CLK);
		if (this.getRegister(SystemRegister.CLK) == this.getRegister(SystemRegister.CLI)) {
			this.setFlag(Flag.CLOCK, true);
		}
	}

	public void reset()
	{
		//
		this.waiting = false;
		this.failed = false;
		this.port_read_mask = 0;
		this.port_write_mask = 0;
		// initialize instruction pointer
		this.setRegister(SystemRegister.P, 0);
		this.setRegister(SystemRegister.FLAG, 0);
		this.slot = 0;
		// initialize stack
		if (parameter_stack == null) {
			this.setRegister(SystemRegister.SP, system.getStackTop(0, false));
			this.setRegister(SystemRegister.S0, system.getStackBottom(0, false));
			this.setRegister(SystemRegister.SL, system.getStackTop(0, false));
		}
		else {
			this.setRegister(SystemRegister.SP, parameter_stack.length - 1);
			this.setRegister(SystemRegister.S0, 0);
			this.setRegister(SystemRegister.SL, parameter_stack.length - 1);
		}
		// initialize return stack
		if (return_stack == null) {
			this.setRegister(SystemRegister.RP, system.getStackTop(0, true));
			this.setRegister(SystemRegister.R0, system.getStackBottom(0, true));
			this.setRegister(SystemRegister.RL, system.getStackTop(0, true));
		}
		else {
			this.setRegister(SystemRegister.RP, return_stack.length - 1);
			this.setRegister(SystemRegister.R0, 0);
			this.setRegister(SystemRegister.RL, return_stack.length - 1);
		}
		// system register
		this.setRegister(SystemRegister.INTV, 0);
		this.setRegister(SystemRegister.INTE, 0);
		//
		this.interrupt(Flag.RESET);
	}
	
	public void powerOn()
	{
		this.reset();
		// initial bootcode
		int slot = 0;
		long bootcode = 0;
		bootcode = writeSlot(bootcode, slot++, ISA.EXT1.ordinal());
		bootcode = writeSlot(bootcode, slot++, Ext1.RDROP.ordinal());
		bootcode = writeSlot(bootcode, slot++, ISA.EXT1.ordinal());
		bootcode = writeSlot(bootcode, slot++, Ext1.RDROP.ordinal());
		bootcode = writeSlot(bootcode, slot++, ISA.LIT.ordinal());
		bootcode = writeSlot(bootcode, slot++, 0);
		bootcode = writeSlot(bootcode, slot++, ISA.NOT.ordinal());
		bootcode = writeSlot(bootcode, slot++, ISA.EXT2.ordinal());
		bootcode = writeSlot(bootcode, slot++, Ext2.STORESYSTEM.ordinal());
		bootcode = writeSlot(bootcode, slot++, SystemRegister.P.ordinal());
		this.setRegister(SystemRegister.I, bootcode);
		// power-on reset clears the reset interrupt flags
		this.setFlag(Flag.RESET, false);
		this.setFlag(SystemRegister.INTS, Flag.RESET, false);
	}

	
	public void doRegisterOperation(int op, int d, int s1, int s2)
	{
		switch (RegOp1.values()[op]) {
		case ADD: this.doAdd(d, s1, s2); break;
		case ADDI: this.doAddi(d, s1, s2); break;
		case ADDC: this.doAddWithCarry(d, s1, s2); break;
		case ADDCC: this.setFlag(Flag.CARRY, false); this.doAddWithCarry(d, s1, s2); break;
		case ADDCS: this.setFlag(Flag.CARRY, true); this.doAddWithCarry(d, s1, s2); break;
		case SUB: this.doSub(d, s1, s2); break;
		case SUBI: this.doSubi(d, s1, s2); break;
		case SUBC: this.doSubtractWithCarry(d, s1, s2); break;
		case SUBCC: this.setFlag(Flag.CARRY, false); this.doSubtractWithCarry(d, s1, s2); break;
		case SUBCS: this.setFlag(Flag.CARRY, true); this.doSubtractWithCarry(d, s1, s2); break;
		case AND: this.doAnd(d, s1, s2); break;
		case OR: this.doOr(d, s1, s2); break;
		case XOR: this.doXor(d, s1, s2); break;
		case XNOR: this.doXNor(d, s1, s2); break;
		case ASL: this.doAsl(d, s1, s2); break;
		case ASR: this.doAsr(d, s1, s2); break;
		case LSL: this.doLsl(d, s1, s2); break;
		case LSR: this.doLsr(d, s1, s2); break;
		case ROL: this.doRol(d, s1, s2); break;
		case ROR: this.doRor(d, s1, s2); break;
		case RCL: this.doRcl(d, s1, s2); break;
		case RCR: this.doRcr(d, s1, s2); break;
		case ASLI: this.doAsli(d, s1, s2); break;
		case ASRI: this.doAsri(d, s1, s2); break;
		case LSLI: this.doLsli(d, s1, s2); break;
		case LSRI: this.doLsri(d, s1, s2); break;
		case ROLI: this.doRoli(d, s1, s2); break;
		case RORI: this.doRcri(d, s1, s2); break;
		case RCLI: this.doRcli(d, s1, s2); break;
		case RCRI: this.doRori(d, s1, s2); break;
		case MUL2ADD: this.doMul2Add(d, s1, s2); break;
		case DIV2SUB: this.doDiv2Sub(d, s1, s2); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public boolean doSIMDOperation(int op, int par, int d, int s1, int s2)
	{
		SimdSize size = SimdSize.values()[par & 3];
		switch (size) {
		case BIT64:
			break;
		case BIT128:
			if (((s1 & 1) != 0) || ((s2 & 1) != 0) || ((d & 1) != 0)) {
				if (this.interrupt(Flag.ALIGNED)) {return false;}
			}
			break;
		case BIT256:
			if (((s1 & 3) != 0) || ((s2 & 3) != 0) || ((d & 3) != 0)) {
				if (this.interrupt(Flag.ALIGNED)) {return false;}
			}
			break;
		case BIT512:
			if (((s1 & 7) != 0) || ((s2 & 7) != 0) || ((d & 7) != 0)) {
				if (this.interrupt(Flag.ALIGNED)) {return false;}
			}
			break;
		}
		return true;
	}
	
	public synchronized void start()
	{
		if (!this.running) {
			this.running = true;
			Thread thread = new Thread(this);
			thread.start();
		}
	}

	public synchronized void stop()
	{
		this.running = false;
	}

	@Override
	public void run()
	{
		while (this.running) {
			try {
				this.step();
			}
			catch (java.lang.Exception e) {
				this.failed = true;
				break;
			}
		}
	}

}
