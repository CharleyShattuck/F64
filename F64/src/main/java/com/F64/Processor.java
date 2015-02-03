package com.F64;

public class Processor implements Runnable {

	public static final int		VERSION = 0x010000;
	public static final long	IO_BASE = 0xFFFF_FFFF_FFFF_FF00L;
	public static final int		SLOT_ENCODE_BITS = 4;
	public static final int		SLOT_BITS = 6;
	public static final int		SIMD_BITS = 9;
	public static final int		SIMD_SLICE_BITS = SIMD_BITS - SLOT_BITS;
	public static final int		SIMD_SLICE_SIZE = 1 << SIMD_SLICE_BITS;
	public static final int		SIMD_SLICE_MASK = SIMD_SLICE_SIZE - 1;
	public static final int		BIT_PER_CELL = 1 << SLOT_BITS;
	public static final int		SIMD_REGISTER_BITS = 1 << SIMD_BITS;
	public static final int		NO_OF_SIMD_REGISTER_CELLS = 1 << SIMD_SLICE_BITS;
	public static final int		NO_OF_REG = BIT_PER_CELL;
	public static final int		NO_OF_FULL_SLOTS = BIT_PER_CELL / SLOT_BITS;
	public static final int		SLOT_SIZE = 1 << SLOT_BITS;
	public static final int		SLOT_MASK = SLOT_SIZE - 1;
	public static final int		FIRST_SLOT_BITS = BIT_PER_CELL - (NO_OF_FULL_SLOTS*SLOT_BITS);
	public static final int		FIRST_SLOT_SIZE = 1 << FIRST_SLOT_BITS;
	public static final int		FIRST_SLOT_MASK = FIRST_SLOT_SIZE - 1;
	public static final int		FIRST_SLOT = 0;
	public static final int		NO_OF_SLOTS = NO_OF_FULL_SLOTS+1;
	public static final long	TRUE = -1L;
	public static final long	FALSE = 0L;

	public static final long	SLOT0_MASK = -1L << (BIT_PER_CELL-FIRST_SLOT_BITS);

	public static final int[]	SLOT_SHIFT = {
		SLOT_BITS*10,
		SLOT_BITS*9,
		SLOT_BITS*8,
		SLOT_BITS*7,
		SLOT_BITS*6,
		SLOT_BITS*5,
		SLOT_BITS*4,
		SLOT_BITS*3,
		SLOT_BITS*2,
		SLOT_BITS*1,
		0,
	};
	
	public static final long[]	REMAINING_MASKS = {
		-1L >>> FIRST_SLOT_BITS,
		-1L >>> (FIRST_SLOT_BITS + SLOT_BITS),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*2)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*3)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*4)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*5)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*6)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*7)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*8)),
		-1L >>> (FIRST_SLOT_BITS + (SLOT_BITS*9)),
		0
	};

//	public static final long[]	SLOT_MASKS = {
//		-1L >>> SLOT_BITS,
//		-1L >>> (SLOT_BITS*2),
//		-1L >>> (SLOT_BITS*3),
//		-1L >>> (SLOT_BITS*4),
//		-1L >>> (SLOT_BITS*5),
//		-1L >>> (SLOT_BITS*6),
//		-1L >>> (SLOT_BITS*7),
//		-1L >>> (SLOT_BITS*8),
//		-1L >>> (SLOT_BITS*9),
//		-1L >>> (SLOT_BITS*10)
//	};

	
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

	public static int getSlotBits(int slot_no)
	{
		if (slot_no == FIRST_SLOT) {return FIRST_SLOT_BITS;}
		if (slot_no < NO_OF_SLOTS) {return SLOT_BITS;}
		return 0;
	}

	public static int getSlotMask(int slot_no)
	{
		if (slot_no == FIRST_SLOT) {return FIRST_SLOT_MASK;}
		if (slot_no < NO_OF_SLOTS) {return SLOT_MASK;}
		return 0;
	}

	public static long getIOAddress(int slot_bits)
	{
		assert(slot_bits >= 0);
		assert(slot_bits < SLOT_SIZE);
		return IO_BASE + slot_bits;
	}
	
	public static long geRemainingMask(int slot)
	{
		return REMAINING_MASKS[slot];
	}

	public static long incAdr(long data)
	{
		if (data >= 0) {
			return data+1;
		}
		return data;
	}

	public static long decAdr(long data)
	{
		if (data >= 0) {
			return data-1;
		}
		return data;
	}

	/**
	 * Count 1 bits in data.
	 * @param data
	 * @return
	 */
	public static int countBits(long data)
	{
		int res = 0;
		while (data != 0) {
			++res;
			data &= data-1;
		}
		return res;
	}

	/**
	 * Calculate the parity of a 64 bit integer.
	 * @param data
	 * @return TRUE on odd parity, otherwise FALSE
	 */
	public static boolean parityBits(long data)
	{
		int v = (int)(data ^ (data >>> 32));
		v ^= v >>> 16;
		v ^= v >>> 8;
		v ^= v >>> 4;
		v ^= v >>> 2;
		v ^= v >>> 1;
		return (v & 1) != 0;
	}
	
	/**
	 * Calculate the sign of a 64 bit integer.
	 * @param data
	 * @return -1 if data<0, 0 if data==0, 1 if data>0
	 */
	public static long sign(long data)
	{
		if (data > 0) {return 1L;}
		if (data < 0) {return -1L;}
		return 0L;
	}

	
	
	/**
	 * Reverse bits in a cell (MSB <-> LSB)
	 * @param data
	 * @return
	 */
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

	
	/**
	 * Find next higher value which is a power of 2.
	 * @param data
	 * @return
	 */
	public static long nextPow2(long data)
	{
		--data;
		data |= data >>> 1;
		data |= data >>> 2;
		data |= data >>> 4;
		data |= data >>> 8;
		data |= data >>> 16;
		data |= data >>> 32;
		++data;
		return data;
	}

	public static int countBytes(long data, byte pattern)
	{
		int cnt = 0;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		data = data >> 8;
		if (((byte)data) == pattern) {++cnt;}
		return cnt;
	}
	
	public static long min(long src1, long src2)
	{
		if (src1 < src2) {return src1;}
		return src2;
	}
	
	public static long max(long src1, long src2)
	{
		if (src1 > src2) {return src1;}
		return src2;
	}

	
	/**
	 * Calculate the absolute value of an integer. This function fails if data is MIN_LONG
	 * @param data
	 * @return absolute value of data.
	 */
	public static long abs(long data)
	{
		if (data >= 0) {return data;}
		return -data;
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
		assert(slot >= 0);
		assert(value >= 0);
		if (slot >= NO_OF_SLOTS) {
			assert(value == 0);
			return data;
		}
		if (slot == FIRST_SLOT) {assert(value < FIRST_SLOT_SIZE);}
		else {assert(value < SLOT_SIZE);}
		int shift = SLOT_SHIFT[slot];
		long mask = SLOT_MASK;
		long val = value;
		mask <<= shift;
		val <<= shift;
		return data ^ ((data ^ val) & mask);
	}

	public static int readSlot(long value, int slot)
	{
		assert(slot >= 0);
		return (int)(value >>> SLOT_SHIFT[slot]) & SLOT_MASK;
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


	private System				system;
	private long[]				register;
	private long[]				local_register;
	private long[]				system_register;
	private long[]				read_port;
	private long[]				write_port;
	private long[]				parameter_stack;
	private long[]				return_stack;
	private Processor[]			port_partner;
	private	com.F64.SIMD.Unit	simd;
	private long				communication;
	private int					communication_register;
	private int					x;
	private int					y;
	private int					z;
	private int					port_read_mask;
	private int					port_write_mask;
	private int					slot;
	private int					slice;
	private int					saved_slot;
//	private int					max_slot;	// max # of slots
	private boolean				carry;
	private boolean				failed;
	private boolean				waiting;
	private boolean				reading;
	private volatile boolean	running;

	
	public Processor(System system, int x, int y, int z, int stack_size, int return_stack_size)
	{
		this.simd = new com.F64.SIMD.Unit();
		this.system = system;
		this.x = x;
		this.y = y;
		this.z = z;
		this.register = new long[NO_OF_REG];
		this.local_register = new long[NO_OF_REG];
		this.system_register = new long[NO_OF_REG];
		this.read_port = new long[Port.values().length];
		this.write_port = new long[Port.values().length];
		this.port_partner = new Processor[Port.values().length];
		this.register[Register.Z.ordinal()] = 0;
		if (stack_size > 0) {parameter_stack = new long[stack_size];}
		if (return_stack_size > 0) {return_stack = new long[return_stack_size];}
	}

	public long getRegister(Register reg) {return this.getRegister(reg.ordinal());}
	public void setRegister(Register reg, long value) {this.setRegister(reg.ordinal(), value);}

	public long[] getSIMDRegister(int reg) {return simd.getRegister(reg);}
	
	public long getSystemRegister(SystemRegister reg) {return this.getSystemRegister(reg.ordinal());}
	public void setSystemRegister(SystemRegister reg, long value) {this.setSystemRegister(reg.ordinal(), value);}

	public boolean getInternalCarry() {return this.carry;}
	public boolean hasFailed() {return this.failed;}
	public boolean isWaiting() {return this.waiting;}
	public int getSlot() {return this.slot;}
	public int getSlice() {return this.slice;}
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

	public void setSlice(int value) {slice = value % NO_OF_SIMD_REGISTER_CELLS;}

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
		assert(reg < NO_OF_REG);
		return this.register[reg];
	}

	
	public void setRegister(int reg, long value)
	{
		assert(reg < NO_OF_REG);
		register[reg] = value;
	}

	public long getFlagForInterrupt()
	{
		long res = this.system_register[SystemRegister.FLAG.ordinal()];
		int aux_data = this.slot;
		aux_data <<= SIMD_SLICE_BITS;
		aux_data |= slice & SIMD_SLICE_MASK;
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
	
	public void setFlagFromInterrupt(long data)
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
		this.slice = aux_data & SIMD_SLICE_MASK;
		aux_data >>= SIMD_SLICE_BITS;
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
		this.setSystemRegister(reg, this.getSystemRegister(reg)+1);
	}


	/**
	 * Increment register
	 */
	public void dec(SystemRegister reg)
	{
		this.setSystemRegister(reg, this.getSystemRegister(reg)-1);
	}

//	/**
//	 * Increment register. Do nothing if the register points not into memory
//	 */
//	public void incPointer(int reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value+1);
//		}
//	}
//
//
//	/**
//	 * Increment register. Do nothing if the register points not into memory
//	 */
//	public void decPointer(int reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value-1);
//		}
//	}
//
//
//	/**
//	 * Increment register. Do nothing if the register points not into memory
//	 */
//	public void incPointer(Register reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value+1);
//		}
//	}


//	/**
//	 * Increment register. Do nothing if the register points not into memory
//	 */
//	public void decPointer(SystemRegister reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value-1);
//		}
//	}
//
//	public void incPointer(SystemRegister reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value+1);
//		}
//	}
//
//
//	/**
//	 * Increment register. Do nothing if the register points not into memory
//	 */
//	public void decPointer(Register reg)
//	{
//		long value = this.getRegister(reg);
//		if (value >= 0) {
//			this.setRegister(reg, value-1);
//		}
//	}

	/**
	 * Advance P to next location
	 */
	public void nextP()
	{
		system_register[SystemRegister.P.ordinal()] = incAdr(system_register[SystemRegister.P.ordinal()]);
	}

	public long fetchPInc()
	{
		long res = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
		return res;
		
	}

	public long remainingSlots()
	{
		long res = this.system_register[SystemRegister.I.ordinal()] & REMAINING_MASKS[this.slot];
		this.slot = NO_OF_SLOTS;
		return res;
	}

	/**
	 * Replace the lowest 6 bits of a value with the next slot
	 * @param base
	 * @return
	 */
//	public long replaceNextSlot(long base)
//	{
//		long res = (-1 << SLOT_BITS) & base;
//		return res | nextSlot();
//	}

	public void shortJump(int slot_bits, boolean forward)
	{
		if (slot_bits == 0) {slot_bits = SLOT_SIZE;}
//		long mask = SLOT_MASK;
		if (forward) {
			this.system_register[SystemRegister.P.ordinal()] += slot_bits;
		}
		else {
			this.system_register[SystemRegister.P.ordinal()]-= slot_bits;
		}
		this.slot = NO_OF_SLOTS;
	}

	public void longJump()
	{
		this.setSystemRegister(SystemRegister.P, this.fetchPInc());
		this.slot = NO_OF_SLOTS; 
	}

	private void doSkipConditionalJump(int condition)
	{
		switch (Branch.values()[condition & 0xf]) {
		case FORWARD:	this.nextSlot(); break;
		case BACK:		this.nextSlot(); break;
		case IO:		this.nextSlot(); break;
		case LONG:		++this.system_register[SystemRegister.P.ordinal()]; break;
//		case REM:		this.slot = NO_OF_SLOTS; break;
		default:
		}
	}

	
	public boolean doConditionalJump(int condition)
	{
		Condition cond = Condition.values()[(condition >> 4) & 3];
		switch (cond) {
		case EQ0:		if (this.register[Register.T.ordinal()] != 0) {doDrop(); doSkipConditionalJump(condition); return false;} doDrop(); break;
		case QEQ0:		if (this.register[Register.T.ordinal()] != 0) {doSkipConditionalJump(condition); return false;} doDrop(); break;
//		case NE0:		if (this.register[Register.T.ordinal()] == 0) {doDrop(); doSkipConditionalJump(condition); return false;} doDrop(); break;
//		case GE0:		if (this.register[Register.T.ordinal()] < 0) {doDrop(); doSkipConditionalJump(condition); return false;} doDrop(); break;
//		case CARRY:		if (!this.getFlag(Flag.CARRY)) {doSkipConditionalJump(condition); return false;} break;
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
		case SKIP:		this.slot = NO_OF_SLOTS; break;
		case FORWARD:	this.shortJump(this.nextSlot(), true); break;
		case BACK:		this.shortJump(this.nextSlot(), false); break;
		case IO:		this.doJumpIO(this.nextSlot()); break;
		case LONG:		this.longJump(); break;
//		case REM:		this.jumpRemainigSlots(); break;
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

	public void doQDup()
	{
		if (this.register[Register.T.ordinal()] != 0) {
			this.pushStack(this.register[Register.S.ordinal()]);
			this.register[Register.S.ordinal()] = this.register[Register.T.ordinal()];
		}
	}

	public void doShortNext(int slot, boolean forward)
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.shortJump(slot, forward);
		}
	}

	public void doRemainingNext()
	{
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.jumpRemainigSlots();
		}
	}


	public void doLongNext()
	{
		long target = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
		if (this.register[Register.R.ordinal()] == 0) {
			this.register[Register.R.ordinal()] = this.popReturnStack();
		}
		else {
			--this.register[Register.R.ordinal()];
			this.system_register[SystemRegister.P.ordinal()] = target;
			this.slot = NO_OF_SLOTS;
		}
	}

//	public boolean doJumpMethod(long index)
//	{
//		// check index is in range of method table
//		if (index >= system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
//			if (this.interrupt(Flag.BOUND)) {
//				return false;
//			}
//		}
//		// load address of method
//		this.system_register[SystemRegister.P.ordinal()] = system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY);
//		this.slot = NO_OF_SLOTS;
//		return true;
//	}

	public void doSwapRR(int dst, int src)
	{
		long tmp = this.getRegister(dst);
		this.setRegister(dst, this.getRegister(src));
		this.setRegister(src, tmp);
	}

	public void doMoveRR(int dst, int src)
	{
		this.setRegister(dst, this.getRegister(src));
	}

	public void doMoveRI(int dst, int src)
	{
		this.setRegister(dst, src);
	}

	public void doSwapRS(int dst, int src)
	{
		long tmp = this.getRegister(dst);
		this.setRegister(dst, this.getSystemRegister(src));
		this.setSystemRegister(src, tmp);
	}

	public void doMoveRS(int dst, int src)
	{
		this.setRegister(dst, this.getSystemRegister(src));
	}

	public void doMoveSR(int dst, int src)
	{
		this.setSystemRegister(dst, this.getRegister(src));
	}

	public void doMoveSI(int dst, int src)
	{
		this.setSystemRegister(dst, src);
	}

	public void doSwapRL(int dst, int src)
	{
		long tmp = this.getRegister(dst);
		this.setRegister(dst, this.getLocalRegister(src));
		this.setLocalRegister(src, tmp);
	}

	public void doMoveRL(int dst, int src)
	{
		this.setRegister(dst, this.getLocalRegister(src));
	}

	public void doMoveLR(int dst, int src)
	{
		this.setLocalRegister(dst, this.getRegister(src));
	}

	public void doMoveLI(int dst, int src)
	{
		this.setLocalRegister(dst, src);
	}

//	public void doMoveStack(int src, int dst)
//	{
//		long value = this.register[src];
//		if (dst == Register.R.ordinal()) {
//			this.pushReturnStack(this.register[Register.R.ordinal()]);			
//		}
//		else if (dst == Register.S.ordinal()) {
//			this.pushStack(this.register[Register.S.ordinal()]);
//		}
//		else if ((dst == Register.T.ordinal()) && (src == Register.S.ordinal())) {
//			this.doDup();
//		}
//		this.setRegister(dst, value);
//		if (src == Register.R.ordinal()) {
//			this.register[Register.R.ordinal()] = this.popReturnStack();
//		}
//		else if (src == Register.S.ordinal()) {
//			this.register[Register.S.ordinal()] = this.popStack();
//		}
//		else if ((src == Register.T.ordinal()) && (dst == Register.S.ordinal())) {
//			this.doDrop();
//		}
//	}


	public void doFetch()
	{
		this.register[Register.T.ordinal()] = system.getMemory(this.register[Register.T.ordinal()]);
	}

	public void doStore()
	{
		system.setMemory(this.register[Register.T.ordinal()], this.register[Register.S.ordinal()]);
		this.doDrop();
		this.doDrop();
	}

	public void doRFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		long tmp = this.getRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doRStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		this.setRegister(reg, tmp);
	}

	public void doSFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		long tmp = this.getSystemRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doSStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		this.setSystemRegister(reg, tmp);
	}

	public void doLFetchIndirect(int ireg)
	{
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		long tmp = this.getLocalRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doLStoreIndirect(int ireg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		int reg = (int)(this.getRegister(ireg) & SLOT_MASK);
		this.setLocalRegister(reg, tmp);
	}

	public void doLFetch(int reg)
	{
		long tmp = this.getLocalRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doLStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		this.setLocalRegister(reg, tmp);
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

	public void doSFetch(int reg)
	{
		long tmp = this.getSystemRegister(reg);
		this.doDup();
		this.register[Register.T.ordinal()] = tmp;
	}

	public void doSStore(int reg)
	{
		long tmp = this.register[Register.T.ordinal()];
		this.doDrop();
		this.setSystemRegister(reg, tmp);
	}


	public void doRFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
		this.doDup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
	}

	public void doRFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.doDrop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setRegister(reg, tmp);
		}
	}

	public void doLFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getLocalRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
		this.doDup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
	}

	public void doLFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getLocalRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.doDrop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setLocalRegister(reg, tmp);
		}
	}

	public void doSFetchFetch(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getSystemRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
		this.doDup();
		this.register[Register.T.ordinal()] = this.system.getMemory(tmp);
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
	}

	public void doSFetchStore(int reg, boolean dec, boolean pre, boolean post)
	{
		long tmp = this.getSystemRegister(reg);
		if (pre) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
		this.system.setMemory(tmp, this.register[Register.T.ordinal()]);
		this.doDrop();
		if (post) {
			if (dec) {--tmp;}
			else {++tmp;}
			this.setSystemRegister(reg, tmp);
		}
	}

//	public void doSFetch(int reg)
//	{
//		long tmp = this.getSystemRegister(reg);
//		this.doDup();
//		this.register[Register.T.ordinal()] = tmp;
//	}
//
//	public void doSStore(int reg)
//	{
//		long tmp = this.register[Register.T.ordinal()];
//		this.doDrop();
//		this.setSystemRegister(reg, tmp);
//	}

//	public void doFetchR(int reg)
//	{
//		this.doDup();
//		this.register[Register.T.ordinal()] = system.getMemory(this.getRegister(reg));
//	}
//	
//
//	public void doStoreR(int reg)
//	{
//		system.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
//		this.doDrop();
//	}
//
//	public void doFetchL(int reg)
//	{
//		this.doDup();
//		this.register[Register.T.ordinal()] = system.getMemory(this.getLocalRegister(reg));
//	}
//	
//
//	public void doStoreL(int reg)
//	{
//		system.setMemory(this.getLocalRegister(reg), this.register[Register.T.ordinal()]);
//		this.doDrop();
//	}
//
//	public void doFetchS(int reg)
//	{
//		this.doDup();
//		this.register[Register.T.ordinal()] = system.getMemory(this.getSystemRegister(reg));
//	}
//	
//
//	public void doStoreS(int reg)
//	{
//		system.setMemory(this.getSystemRegister(reg), this.register[Register.T.ordinal()]);
//		this.doDrop();
//	}

	public void doFetchPInc()
	{
		doDup();
		this.register[Register.T.ordinal()] = system.getMemory(this.getSystemRegister(SystemRegister.P));
		nextP();
	}
	

	public void doStorePInc()
	{
		system.setMemory(this.getSystemRegister(SystemRegister.P), this.register[Register.T.ordinal()]);
		doDrop();
		nextP();
	}

//	public void doFetchRegisterInc(int reg)
//	{
//		doDup();
//		this.register[Register.T.ordinal()] = system.getMemory(this.getRegister(reg));
//		this.register[reg] = incAdr(this.register[reg]);
//	}
//	
//
//	public void doStoreRegisterInc(int reg)
//	{
//		system.setMemory(this.getRegister(reg), this.register[Register.T.ordinal()]);
//		doDrop();
//		this.register[reg] = incAdr(this.register[reg]);
//	}

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

	public void doMin(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = min(src1, src2);
		this.setRegister(d, dest);
	}

	public void doMax(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = max(src1, src2);
		this.setRegister(d, dest);
	}

	public void doAdd(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 + src2;
		this.setRegister(d, dest);
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
	}

	
	public void doSub(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 - src2;
		this.setRegister(d, dest);
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
	}

	public void doAnd(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 & src2;
		this.setRegister(d, dest);
	}

	public void doOr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 | src2;
		this.setRegister(d, dest);
	}

	public void doXor(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ src2;
		this.setRegister(d, dest);
	}

	public void doEquivalent(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = src1 ^ ~src2;
		this.setRegister(d, dest);
	}


	public void doAsl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.asl(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doAsr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.asr(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doLsl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.lsl(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doLsr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.lsr(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
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
	}

	public void doRor(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.ror(src1, (int)src2);
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doRcl(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.rcl(src1, (int)src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
	}

	public void doRcr(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = this.rcr(src1, (int)src2, getFlag(Flag.CARRY));
		this.setFlag(Flag.CARRY, this.carry);
		this.setRegister(d, dest);
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
	}

	public void doDiv2Sub(int d, int s1, int s2)
	{
		long src1 = this.getRegister(s1);
		long src2 = this.getRegister(s2);
		long dest = (src1 >> 1) - src2;
		this.setRegister(d, dest);
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

	public void doLit(int data)
	{
		this.pushT(data);
	}

	public void doNLit(int data)
	{
		this.pushT(~(long)data);
	}

	public void doBLit(int data)
	{
		this.pushT(1L << data);
	}

	public void doExtendLiteral(int data)
	{
		this.register[Register.T.ordinal()] <<= SLOT_BITS;
		this.register[Register.T.ordinal()] |= data;
	}

//	public void doShortJump(int slot)
//	{
//		this.shortJump(slot);
//	}

	public void doLongJump()
	{
		this.system_register[SystemRegister.P.ordinal()] = system.getMemory(this.getSystemRegister(SystemRegister.P));
		this.slot = NO_OF_SLOTS;		
	}

	
	/**
	 * Jump to an address. Replace I with the content of the target address.
	 */
	public void jumpRemainigSlots()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = REMAINING_MASKS[this.slot];
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
		this.slot = NO_OF_SLOTS; // start with first slot
	}

	
	/**
	 * Call a method. Replace I with the content of the target method address.
	 */
	public boolean doCallMethod(long index)
	{
		// check index is in range of method table
		if (index >= system.getMemory(this.system_register[SystemRegister.MT.ordinal()] + MethodTable.OFFSET_TO_SIZE)) {
			if (this.interrupt(Flag.BOUND)) {
				return false;
			}
		}
		// load address of method
		long adr = this.system_register[SystemRegister.MT.ordinal()] + index + MethodTable.OFFSET_TO_METHOD_ARRAY;
		this.system_register[SystemRegister.W.ordinal()] = incAdr(adr);
		// load I with content of adr
		this.system_register[SystemRegister.I.ordinal()] = system.getMemory(adr);
		this.slot = 0; // start with first slot
		return true;
	}

	public void doExecute()
	{
		this.system_register[SystemRegister.I.ordinal()] = writeSlot(register[Register.T.ordinal()], 0, ISA.CALL.ordinal());
		this.doDrop();
		this.slot = 0; // start with first slot
	}

	/**
	 * Call an address. Replace I with the content of the target address.
	 */
	public void doCall()
	{
		// replace to lowest bits of P with the bits in the remaining slots
		long mask = REMAINING_MASKS[this.slot];
		long adr = this.getSystemRegister(SystemRegister.I) & mask;
		// P contains return address
		// save next address to register W
		this.system_register[SystemRegister.W.ordinal()] = incAdr(adr);
		// load I with content of adr
		this.system_register[SystemRegister.I.ordinal()] = system.getMemory(adr);
		this.slot = 0; // start with first slot
	}


	public void doExit()
	{
		this.system_register[SystemRegister.P.ordinal()] = this.register[Register.R.ordinal()];
		this.register[Register.R.ordinal()] = this.popReturnStack();
		this.slot = NO_OF_SLOTS;
	}

	public void doEnter()
	{
		// push P on return stack
		this.doPushSystem(SystemRegister.P.ordinal());
		// set P with saved address in W. W is set by CALL
		this.setSystemRegister(SystemRegister.P, this.getSystemRegister(SystemRegister.W));
		
	}

//	public void doLeave()
//	{
//		// pop P from return stack
//		this.doPopSystem(SystemRegister.P.ordinal());		
//	}

	public void doSave()
	{
		// push SELF on return stack
		this.doPushSystem(SystemRegister.SELF.ordinal());
		// load MT
		doLoadMT();
	}

	public void doRestore()
	{
		// pop SELF from return stack
		this.doPopSystem(SystemRegister.SELF.ordinal());
		// load MT
		doLoadMT();
	}

	public void doLoadMT()
	{
		this.system_register[SystemRegister.MT.ordinal()] = system.getMemory(this.system_register[SystemRegister.SELF.ordinal()]);
	}

	public void doPush(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getRegister(reg);
	}

	public void doPop(int reg)
	{
		this.setRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void doPushSystem(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getSystemRegister(reg);
	}

	public void doPopSystem(int reg)
	{
		this.setSystemRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void doPushLocal(int reg)
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.getLocalRegister(reg);
	}

	public void doPopLocal(int reg)
	{
		this.setLocalRegister(reg, this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.popReturnStack();
	}

	public void doLoadSelf()
	{
		this.pushReturnStack(this.register[Register.R.ordinal()]);
		this.register[Register.R.ordinal()] = this.system_register[SystemRegister.SELF.ordinal()];
		this.system_register[SystemRegister.SELF.ordinal()] = this.register[Register.T.ordinal()];
		this.doDrop();
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
				case AND:		this.doAnd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
				case XOR:		this.doXor(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
				case DUP:		this.doDup(); break;
				case DROP:		this.doDrop(); break;
				case OVER:		this.doOver(); break;
				case NIP:		this.doNip(); break;
				case LIT:		this.doLit(this.nextSlot()); break;
				case NLIT:		this.doNLit(this.nextSlot()); break;
//				case EXT:		this.doExtendLiteral(this.nextSlot()); break;
				case SNEXT:		this.doShortNext(this.nextSlot(), false); break;
				case BRANCH:	this.doConditionalJump(this.nextSlot()); break;
				case CALL:		this.doCall(); break;
				case CALLM:		this.doCallMethod(this.nextSlot()); break;
				case FJMP:		this.shortJump(this.nextSlot(), true); break;
				case BJMP:		this.shortJump(this.nextSlot(), false); break;
				case SAVE:		this.doSave(); break;
				case RESTORE:	this.doRestore(); break;
				case USKIP:		this.slot = NO_OF_SLOTS; break;
				case UJMP6:		this.slot = 6; break;
				case UJMP7:		this.slot = 7; break;
				case UJMP8:		this.slot = 8; break;
				case UJMP9:		this.slot = 9; break;
				case UJMP10:	this.slot = 10; break;
				case SWAP:		this.doSwapRR(this.nextSlot(), this.nextSlot()); break;
				case MOV:		this.doMoveRR(this.nextSlot(), this.nextSlot()); break;
				case OR:		this.doOr(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
				case ENTER:		this.doEnter(); break;
				case LOADSELF:	this.doLoadSelf(); break;
				case LOADMT:	this.doLoadMT(); break;
				case RFETCH:	this.doRFetch(this.nextSlot()); break;
				case RSTORE:	this.doRStore(this.nextSlot()); break;
				case LFETCH:	this.doLFetch(this.nextSlot()); break;
				case LSTORE:	this.doLStore(this.nextSlot()); break;
				case INC:		this.inc(this.nextSlot()); break;
				case DEC:		this.doFetch(); break;
				case FETCH:		this.doStore(); break;
				case STORE:		this.dec(this.nextSlot()); break;
				case FETCHPINC:	this.doFetchPInc(); break;
				case STOREPINC:	this.doStorePInc(); break;
				case ADD:		this.doAdd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
				case SUB:		this.doSub(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
				case MUL2:		this.doMul2Add(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
				case DIV2:		this.doDiv2Sub(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
				case PUSH:		this.doPush(Register.T.ordinal()); this.doDrop(); break;
				case POP:		this.doDup(); this.doPop(Register.T.ordinal()); break;
				case EXT1:		this.doExt1(); break;
				case EXT2:		this.doExt2(); break;
				case EXT3:		this.doExt3(); break;
				case EXT4:		this.doExt4(); break;
				case EXT5:		this.doExt5(); break;
				case EXT6:		this.doExt6(); break;
				case REGOP1:	this.doRegisterOperation(this.nextSlot(), this.nextSlot()); break;
				case REGOP2:	this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				case REGOP3:	this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				case SIMD:		simd.doOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				default: if (this.interrupt(Flag.ILLEGAL)) {return;}
				}
			}
			// check if there is some interrupt pending
			if ((this.system_register[SystemRegister.FLAG.ordinal()] & this.system_register[SystemRegister.INTE.ordinal()]) != 0) {
				// there is some pending interrupt
				triggerInterrupts();
			}
			//
			if ((!this.waiting) && (this.slot >= NO_OF_SLOTS)) {
				// load new instruction cell
				long adr = this.system_register[SystemRegister.P.ordinal()];
				if (adr >= 0) {
					// normal memory
					if (adr >= system.getMemorySize()) {
						this.running = false;
					}
					else {
						this.setSystemRegister(SystemRegister.I.ordinal(), system.getMemory(adr));
						// increment P
						this.system_register[SystemRegister.P.ordinal()] = ++adr;
					}
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
		this.setFlagFromInterrupt(this.popReturnStack());
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
		setSystemRegister(SystemRegister.P, getIOAddress(mask));
		slot = NO_OF_SLOTS; // leave slot
	}

	public void doSign(int dest, int src)
	{
		setRegister(dest, sign(getRegister(src)));
	}

	public void doBitCnt1(int dest, int src)
	{
		setRegister(dest, countBits(getRegister(src)));
	}

	public void doBitCnt0(int dest, int src)
	{
		setRegister(dest, BIT_PER_CELL-countBits(getRegister(src)));
	}


	public void doBitFindFirst1(int dest, int src)
	{
		setRegister(dest, findFirstBit1(getRegister(src)));
	}

	public void doBitFindLast1(int dest, int src)
	{
		setRegister(dest, findLastBit1(getRegister(src)));
	}


	public void doByteCount(int dest, int src1, int src2)
	{
		setRegister(dest, countBytes(getRegister(src1), (byte)getRegister(src2)));
	}

	public void doRDrop()
	{
		register[Register.R.ordinal()] = popReturnStack();
	}


	public void doRDup()
	{
		pushReturnStack(register[Register.R.ordinal()]);
	}


	public void doSetFlags(int flag)
	{
		setFlag(flag, true);
	}

	public void doClearFlags(int flag)
	{
		setFlag(flag, false);
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
		if (this.getSystemRegister(SystemRegister.RES) != 0) {
			// some memory has already been reserved
			if (interrupt(Flag.TOUCHED)) {
				return false;
			}
//			this.setFlag(Flag.RESERVED, true);
		}
		this.setSystemRegister(SystemRegister.RES, adr);
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
		if (this.getSystemRegister(SystemRegister.RES) != adr) {
			// memory has not been reserved
		}
		if (getFlag(Flag.TOUCHED)) {
			this.setRegister(Register.T, -1);
		}
		else {
			system.setMemory(adr, value);
			this.setRegister(Register.T, 0);
		}
		this.setSystemRegister(SystemRegister.RES, 0);
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

	public void doFetchSIMD(int reg)
	{
		this.doDup();
		int slice = (int)(this.getRegister(Register.R) & SIMD_SLICE_MASK);
		this.setRegister(Register.T, this.getSIMDRegister(reg)[slice]);
	}

	public void doStoreSIMD(int reg)
	{
		int slice = (int)(this.getRegister(Register.R) & SIMD_SLICE_MASK);
		this.getSIMDRegister(reg)[slice] = this.getRegister(Register.T);
		this.doDrop();
	}

	public void doEQ0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data == 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doNE0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data != 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doGT0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data > 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doGE0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data >= 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doLT0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data < 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doLE0Q(int dest, int src, boolean pushStack)
	{
		long data = this.register[src];
		if (pushStack) {
			if (dest == Register.T.ordinal()) {this.doDup();}
			else if (dest == Register.S.ordinal()) {this.doUnder();}
		}
		if (data <= 0) {
			this.register[dest] = TRUE;
		}
		else {
			this.register[dest] = FALSE;
		}
	}

	public void doAbs(int dest, int src)
	{
		this.register[dest] = Processor.abs(this.register[src]);
	}

	public void doNegate(int dest, int src)
	{
		this.register[dest] = -this.register[src];
	}

	public void doReverse(int dest, int src)
	{
		this.register[dest] = reverseBits(this.register[src]);
	
	}

	public void doNextPow2(int dest, int src)
	{
		this.register[dest] = nextPow2(this.register[src]);
	
	}

	public void doParity(int dest, int src)
	{
		this.register[dest] = parityBits(this.register[src]) ? TRUE : FALSE;
	
	}

	public void doExt1()
	{
		switch (Ext1.values()[this.nextSlot()]) {
		case RDROP:			this.doRDrop(); break;
		case RDUP:			this.doRDup(); break;
		case QDUP:			this.doQDup(); break;
		case EXECUTE:		this.doExecute(); break;
		case EXITI:			this.doExitInterrupt(this.nextSlot()); break;
//		case SWAP0:			this.doSwap(this.nextSlot(), this.nextSlot()); this.slot = 0; break;
		case LJMP:			this.doLongJump(); break;
		case RNEXT:			this.doRemainingNext(); break;
		case LNEXT:			this.doLongNext(); break;
		case MIN:			this.doMin(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
		case MAX:			this.doMax(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); this.doNip(); break;
		case ADDC:			this.doAddWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case SUBC:			this.doSubtractWithCarry(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); break;
		case ROL:			this.doRol(Register.T.ordinal(), 1); break;
		case ROR:			this.doRor(Register.T.ordinal(), 1); break;
		case RCL:			this.doRcl(Register.T.ordinal(), 1); break;
		case RCR:			this.doRcr(Register.T.ordinal(), 1); break;
		case SBITS:			this.doSetBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case CBITS:			this.doClearBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case TBITS:			this.doToggleBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case RBITS:			this.doReadBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case WBITS:			this.doWriteBit(Register.T.ordinal(), Register.S.ordinal(), true, true); break;
		case ENTERM:		this.doEnterM(); break;
		case LCALLM:		this.doCallMethod(this.drop()); break;
//		case LJMPM:			this.doJumpMethod(this.drop()); break;
		case TUCK:			this.doTuck(); break;
		case UNDER:			this.doUnder(); break;
		case MULS:			this.doMultiplyStep(); break;
		case DIVS:			this.doDivideStep(); break;
		case MDP:			this.doMultiplyDividePrepare(); break;
		case MULF:			this.doMultiplyFinished(); break;
		case DIVMODF:		this.doDivideModFinished(); break;
		case EQ0Q:			this.doEQ0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case NE0Q:			this.doNE0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case GT0Q:			this.doGT0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case GE0Q:			this.doGE0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case LT0Q:			this.doLT0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case LE0Q:			this.doLE0Q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case FETCHRES:		this.doFetchReserved(); break;
		case STORECOND:		this.doStoreConditional(); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt2()
	{
		switch (Ext2.values()[this.nextSlot()]) {
		case ROL:			this.doRol(this.nextSlot(), 1); break;
		case ROR:			this.doRor(this.nextSlot(), 1); break;
		case RCL:			this.doRcl(this.nextSlot(), 1); break;
		case RCR:			this.doRcr(this.nextSlot(), 1); break;
		case ROLI:			this.doRol(Register.T.ordinal(), this.nextSlot()); break;
		case RORI:			this.doRor(Register.T.ordinal(), this.nextSlot()); break;
		case RCLI:			this.doRcl(Register.T.ordinal(), this.nextSlot()); break;
		case RCRI:			this.doRcr(Register.T.ordinal(), this.nextSlot()); break;
		case EQ0Q:			this.doEQ0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case NE0Q:			this.doNE0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case GT0Q:			this.doGT0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case GE0Q:			this.doGE0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case LT0Q:			this.doLT0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case LE0Q:			this.doLE0Q(Register.T.ordinal(), this.nextSlot(), true); break;
		case SFLAG:			this.doSetFlags(this.nextSlot()); break;
		case CFLAG:			this.doClearFlags(this.nextSlot()); break;
		case SBIT:			this.doSetBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case CBIT:			this.doClearBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case TBIT:			this.doToggleBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case RBIT:			this.doReadBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
		case WBIT:			this.doWriteBit(Register.T.ordinal(), this.nextSlot(), false, false); break;
//		case FETCHINC:		this.doFetchRegisterInc(this.nextSlot()); break;
//		case STOREINC:		this.doStoreRegisterInc(this.nextSlot()); break;
//		case FETCHR:		this.doFetchR(this.nextSlot()); break;
//		case STORER:		this.doStoreR(this.nextSlot()); break;
//		case FETCHL:		this.doFetchL(this.nextSlot()); break;
//		case STOREL:		this.doStoreL(this.nextSlot()); break;
//		case FETCHS:		this.doFetchS(this.nextSlot()); break;
//		case STORES:		this.doStoreS(this.nextSlot()); break;
		case SFETCH:		this.doFetchSystem(this.nextSlot()); break;
		case SSTORE:		this.doStoreSystem(this.nextSlot()); break;
		case SIMDFETCH:		this.doFetchSIMD(this.nextSlot()); break;
		case SIMDSTORE:		this.doStoreSIMD(this.nextSlot()); break;
//		case SFETCHI:		this.doSFetch(this.nextSlot()); break;
//		case SSTOREI:		this.doSStore(this.nextSlot()); break;
		case FETCHPORT:		this.doFetchPort(this.nextSlot(), false); break;
		case STOREPORT:		this.doStorePort(this.nextSlot(), false); break;
		case FETCHPORTWAIT:	this.doFetchPort(this.nextSlot(), true); break;
		case STOREPORTWAIT:	this.doStorePort(this.nextSlot(), true); break;
		case PUSHR:			this.doPush(this.nextSlot()); break;
		case POPR:			this.doPop(this.nextSlot()); break;
		case PUSHL:			this.doPushLocal(this.nextSlot()); break;
		case POPL:			this.doPopLocal(this.nextSlot()); break;
		case PUSHS:			this.doPushSystem(this.nextSlot()); break;
		case POPS:			this.doPopSystem(this.nextSlot()); break;
		case BLIT:			this.doBLit(this.nextSlot()); break;
		case JMPIO:			this.doJumpIO(this.nextSlot()); break;
		case CONFIGFETCH:	this.doConfigFetch(this.nextSlot()); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt3()
	{
		switch (Ext3.values()[this.nextSlot()]) {
		case SWAPRS:		this.doSwapRS(this.nextSlot(), this.nextSlot()); break;
		case SWAPRL:		this.doSwapRL(this.nextSlot(), this.nextSlot()); break;
		case MOVSR:			this.doMoveSR(this.nextSlot(), this.nextSlot()); break;
		case MOVRS:			this.doMoveRS(this.nextSlot(), this.nextSlot()); break;
		case MOVLR:			this.doMoveLR(this.nextSlot(), this.nextSlot()); break;
		case MOVRL:			this.doMoveRL(this.nextSlot(), this.nextSlot()); break;
		case MOVRI:			this.doMoveRI(this.nextSlot(), this.nextSlot()); break;
		case MOVSI:			this.doMoveSI(this.nextSlot(), this.nextSlot()); break;
		case MOVLI:			this.doMoveLI(this.nextSlot(), this.nextSlot()); break;
		case ROL:			this.doRol(this.nextSlot(), this.nextSlot()); break;
		case ROR:			this.doRor(this.nextSlot(), this.nextSlot()); break;
		case RCL:			this.doRcl(this.nextSlot(), this.nextSlot()); break;
		case RCR:			this.doRcr(this.nextSlot(), this.nextSlot()); break;
		case EQ0Q:			this.doEQ0Q(this.nextSlot(), this.nextSlot(), true); break;
		case NE0Q:			this.doNE0Q(this.nextSlot(), this.nextSlot(), true); break;
		case GT0Q:			this.doGT0Q(this.nextSlot(), this.nextSlot(), true); break;
		case GE0Q:			this.doGE0Q(this.nextSlot(), this.nextSlot(), true); break;
		case LT0Q:			this.doLT0Q(this.nextSlot(), this.nextSlot(), true); break;
		case LE0Q:			this.doLE0Q(this.nextSlot(), this.nextSlot(), true); break;
		case BITFF1:		this.doBitFindFirst1(this.nextSlot(), this.nextSlot()); break;
		case BITFL1:		this.doBitFindLast1(this.nextSlot(), this.nextSlot()); break;
		case RSBIT:			this.doSetBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RCBIT:			this.doClearBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RTBIT:			this.doToggleBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RRBIT:			this.doReadBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RWBIT:			this.doWriteBit(this.nextSlot(), this.nextSlot(), false, false); break;
		case RRSBIT:		this.doSetBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRCBIT:		this.doClearBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRTBIT:		this.doToggleBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRRBIT:		this.doReadBit(this.nextSlot(), this.nextSlot(), true, false); break;
		case RRWBIT:		this.doWriteBit(this.nextSlot(), this.nextSlot(), true, false); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt4()
	{
		switch (Ext4.values()[this.nextSlot()]) {
		case RFETCHI:	this.doRFetchIndirect(this.nextSlot()); break;
		case RSTOREI:	this.doRStoreIndirect(this.nextSlot()); break;
		case LFETCHI:	this.doLFetchIndirect(this.nextSlot()); break;
		case LSTOREI:	this.doLStoreIndirect(this.nextSlot()); break;
		case SFETCHI:	this.doSFetchIndirect(this.nextSlot()); break;
		case SSTOREI:	this.doSStoreIndirect(this.nextSlot()); break;

		case RFETCH:	this.doRFetchFetch(this.nextSlot(), false, false, false); break;
		case RSTORE:	this.doRFetchStore(this.nextSlot(), false, false, false); break;
		case LFETCH:	this.doLFetchFetch(this.nextSlot(), false, false, false); break;
		case LSTORE:	this.doLFetchStore(this.nextSlot(), false, false, false); break;
		case SFETCH:	this.doSFetchFetch(this.nextSlot(), false, false, false); break;
		case SSTORE:	this.doSFetchStore(this.nextSlot(), false, false, false); break;

		case RFETCHPEI:	this.doRFetchFetch(this.nextSlot(), false, true, false); break;
		case RSTOREPEI:	this.doRFetchStore(this.nextSlot(), false, true, false); break;
		case LFETCHPEI:	this.doLFetchFetch(this.nextSlot(), false, true, false); break;
		case LSTOREPEI:	this.doLFetchStore(this.nextSlot(), false, true, false); break;
		case SFETCHPEI:	this.doSFetchFetch(this.nextSlot(), false, true, false); break;
		case SSTOREPEI:	this.doSFetchStore(this.nextSlot(), false, true, false); break;

		case RFETCHPOI:	this.doRFetchFetch(this.nextSlot(), false, false, true); break;
		case RSTOREPOI:	this.doRFetchStore(this.nextSlot(), false, false, true); break;
		case LFETCHPOI:	this.doLFetchFetch(this.nextSlot(), false, false, true); break;
		case LSTOREPOI:	this.doLFetchStore(this.nextSlot(), false, false, true); break;
		case SFETCHPOI:	this.doSFetchFetch(this.nextSlot(), false, false, true); break;
		case SSTOREPOI:	this.doSFetchStore(this.nextSlot(), false, false, true); break;

		case RFETCHPED:	this.doRFetchFetch(this.nextSlot(), true, true, false); break;
		case RSTOREPED:	this.doRFetchStore(this.nextSlot(), true, true, false); break;
		case LFETCHPED:	this.doLFetchFetch(this.nextSlot(), true, true, false); break;
		case LSTOREPED:	this.doLFetchStore(this.nextSlot(), true, true, false); break;
		case SFETCHPED:	this.doSFetchFetch(this.nextSlot(), true, true, false); break;
		case SSTOREPED:	this.doSFetchStore(this.nextSlot(), true, true, false); break;

		case RFETCHPOD:	this.doRFetchFetch(this.nextSlot(), true, false, true); break;
		case RSTOREPOD:	this.doRFetchStore(this.nextSlot(), true, false, true); break;
		case LFETCHPOD:	this.doLFetchFetch(this.nextSlot(), true, false, true); break;
		case LSTOREPOD:	this.doLFetchStore(this.nextSlot(), true, false, true); break;
		case SFETCHPOD:	this.doSFetchFetch(this.nextSlot(), true, false, true); break;
		case SSTOREPOD:	this.doSFetchStore(this.nextSlot(), true, false, true); break;

		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt5()
	{
		switch (Ext5.values()[this.nextSlot()]) {
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt6()
	{
		switch (Ext6.values()[this.nextSlot()]) {
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	
	public void doRegisterOperation(int op, int d)
	{
		switch (RegOp1.values()[op]) {
		case NOT:		this.doEquivalent(d, d, Register.Z.ordinal()); break;
		case ABS:		this.doAbs(d, d); break;
		case NEGATE:	this.doNegate(d, d); break;
		case SIGN:		this.doSign(d, d); break;
		case REVERSE:	this.doReverse(d, d); break;
		case NEXTPOW2:	this.doNextPow2(d, d); break;
		case PARITY:	this.doParity(d, d); break;
		case BITCNT1:	this.doBitCnt1(d, d); break;
		case BITCNT0:	this.doBitCnt0(d, d); break;
		case ASL1:		this.doAsl(d, d, 1); break;
		case ASR1:		this.doAsr(d, d, 1); break;
		case LSL1:		this.doLsl(d, d, 1); break;
		case LSR1:		this.doLsr(d, d, 1); break;
		case ROL1:		this.doRol(d, d, 1); break;
		case ROR1:		this.doRor(d, d, 1); break;
		case RCL1:		this.doRcl(d, d, 1); break;
		case RCR1:		this.doRcr(d, d, 1); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doRegisterOperation(int op, int d, int s)
	{
		switch (RegOp2.values()[op]) {
		case MIN:		this.doMin(d, d, s); break;
		case MAX:		this.doMax(d, d, s); break;
		case NOT:		this.doEquivalent(d, s, Register.Z.ordinal()); break;
		case ABS:		this.doAbs(d, s); break;
		case NEGATE:	this.doNegate(d, s); break;
		case SIGN:		this.doSign(d, s); break;
		case REVERSE:	this.doReverse(d, s); break;
		case NEXTPOW2:	this.doNextPow2(d, s); break;
		case PARITY:	this.doParity(d, s); break;
		case BYTECOUNT:	this.doByteCount(d, d, s); break;
		case BITCNT1:	this.doBitCnt1(d, s); break;
		case BITCNT0:	this.doBitCnt0(d, s); break;
		case ADD:		this.doAdd(d, d, s); break;
		case ADDI:		this.doAddi(d, d, s); break;
		case ADDC:		this.doAddWithCarry(d, d, s); break;
		case SUB:		this.doSub(d, d, s); break;
		case SUBI:		this.doSubi(d, d, s); break;
		case SUBC:		this.doSubtractWithCarry(d, s, d); break;
		case AND:		this.doAnd(d, d, s); break;
		case OR:		this.doOr(d, d, s); break;
		case XOR:		this.doXor(d, d, s); break;
		case EQV:		this.doEquivalent(d, d, s); break;
		case ASL:		this.doAsl(d, d, s); break;
		case ASR:		this.doAsr(d, d, s); break;
		case LSL:		this.doLsl(d, d, s); break;
		case LSR:		this.doLsr(d, d, s); break;
		case ROL:		this.doRol(d, d, s); break;
		case ROR:		this.doRor(d, d, s); break;
		case RCL:		this.doRcl(d, d, s); break;
		case RCR:		this.doRcr(d, d, s); break;
		case ASLI:		this.doAsli(d, d, s); break;
		case ASRI:		this.doAsri(d, d, s); break;
		case LSLI:		this.doLsli(d, d, s); break;
		case LSRI:		this.doLsri(d, d, s); break;
		case ROLI:		this.doRoli(d, d, s); break;
		case RORI:		this.doRcri(d, d, s); break;
		case RCLI:		this.doRcli(d, d, s); break;
		case RCRI:		this.doRori(d, d, s); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doRegisterOperation(int op, int d, int s1, int s2)
	{
		switch (RegOp3.values()[op]) {
		case MIN:		this.doMin(d, s1, s2); break;
		case MAX:		this.doMax(d, s1, s2); break;
		case ADD:		this.doAdd(d, s1, s2); break;
		case ADDI:		this.doAddi(d, s1, s2); break;
		case ADDC:		this.doAddWithCarry(d, s1, s2); break;
		case SUB:		this.doSub(d, s1, s2); break;
		case SUBI:		this.doSubi(d, s1, s2); break;
		case SUBC:		this.doSubtractWithCarry(d, s1, s2); break;
		case AND:		this.doAnd(d, s1, s2); break;
		case OR:		this.doOr(d, s1, s2); break;
		case XOR:		this.doXor(d, s1, s2); break;
		case EQV:		this.doEquivalent(d, s1, s2); break;
		case ASL:		this.doAsl(d, s1, s2); break;
		case ASR:		this.doAsr(d, s1, s2); break;
		case LSL:		this.doLsl(d, s1, s2); break;
		case LSR:		this.doLsr(d, s1, s2); break;
		case ROL:		this.doRol(d, s1, s2); break;
		case ROR:		this.doRor(d, s1, s2); break;
		case RCL:		this.doRcl(d, s1, s2); break;
		case RCR:		this.doRcr(d, s1, s2); break;
		case ASLI:		this.doAsli(d, s1, s2); break;
		case ASRI:		this.doAsri(d, s1, s2); break;
		case LSLI:		this.doLsli(d, s1, s2); break;
		case LSRI:		this.doLsri(d, s1, s2); break;
		case ROLI:		this.doRoli(d, s1, s2); break;
		case RORI:		this.doRcri(d, s1, s2); break;
		case RCLI:		this.doRcli(d, s1, s2); break;
		case RCRI:		this.doRori(d, s1, s2); break;
		case BYTECOUNT:	this.doByteCount(d, s1, s2); break;
		case MUL2ADD:	this.doMul2Add(d, s1, s2); break;
		case DIV2SUB:	this.doDiv2Sub(d, s1, s2); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
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
		if (this.getSystemRegister(SystemRegister.CLK) == this.getSystemRegister(SystemRegister.CLI)) {
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
		this.setSystemRegister(SystemRegister.P, 0);
		this.setSystemRegister(SystemRegister.FLAG, 0);
		this.slot = 0;
		// initialize stack
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
		bootcode = writeSlot(bootcode, slot++, ISA.NLIT.ordinal());
		bootcode = writeSlot(bootcode, slot++, 0);
		bootcode = writeSlot(bootcode, slot++, ISA.EXT2.ordinal());
		bootcode = writeSlot(bootcode, slot++, Ext2.SSTORE.ordinal());
		bootcode = writeSlot(bootcode, slot++, SystemRegister.P.ordinal());
		this.setSystemRegister(SystemRegister.I, bootcode);
		// power-on reset clears the reset interrupt flags
		this.setFlag(Flag.RESET, false);
		this.setFlag(SystemRegister.INTS, Flag.RESET, false);
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

	public void execute(long instr)
	{
		this.doPushSystem(SystemRegister.P.ordinal());
		system_register[SystemRegister.I.ordinal()] = instr;
		system_register[SystemRegister.P.ordinal()] = 0x7fff_ffff_ffff_ffffL;
		this.slot = 0;
		this.running = true;
		while (this.running) {
			try {
				this.step();
			}
			catch (java.lang.Exception e) {
				this.failed = true;
				break;
			}
		}
		this.doPopSystem(SystemRegister.P.ordinal());
	}

	
}
