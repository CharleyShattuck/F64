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
//	public static final int		NO_OF_TASKS = 16;

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
		if (slot >= NO_OF_SLOTS) {return 0;}
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
	private long[]				read_port;
	private long[]				write_port;
	private Processor[]			port_partner;
	private Task[]				task_list;
	private Task				task;
	private long				communication;
	private int					communication_register;
	private int					x;
	private int					y;
	private int					z;
	private int					port_read_mask;
	private int					port_write_mask;
	private int					slot;
	private int					current_task;
	private int					slice;
	private int					saved_slot;
	private boolean				carry;
	private boolean				failed;
	private boolean				waiting;
	private boolean				reading;
	private volatile boolean	running;

	
	public Processor(System system, int x, int y, int z, int stack_size, int return_stack_size, int no_of_tasks)
	{
//		this.simd = new com.F64.SIMD.Unit();
		this.system = system;
		this.task_list = new Task[no_of_tasks];
		for (int i=0; i<no_of_tasks; ++i) {
			task_list[i] = new Task(system, this, i, stack_size, return_stack_size);
		}
		this.task = task_list[0];
		this.x = x;
		this.y = y;
		this.z = z;
//		this.register = new long[NO_OF_REG];
//		this.local_register = new long[NO_OF_REG];
//		this.system_register = new long[NO_OF_REG];
		this.read_port = new long[Port.values().length];
		this.write_port = new long[Port.values().length];
		this.port_partner = new Processor[Port.values().length];
//		this.register[Register.Z.ordinal()] = 0;
//		if (stack_size > 0) {parameter_stack = new long[stack_size];}
//		if (return_stack_size > 0) {return_stack = new long[return_stack_size];}
	}

	public System getSystem() {return system;}
	
	public long getRegister(Register reg) {return task.getRegister(reg.ordinal());}
	public void setRegister(Register reg, long value) {task.setRegister(reg.ordinal(), value);}

//	public long[] getSIMDRegister(int reg) {return task.getSIMDRegister(reg);}
	
//	public long getSystemRegister(SystemRegister reg) {return task.getSystemRegister(reg.ordinal());}
//	public void setSystemRegister(SystemRegister reg, long value) {task.setSystemRegister(reg.ordinal(), value);}

	public void setSlot(int s) {this.slot = s;}
	public boolean getInternalCarry() {return this.carry;}
	public boolean hasFailed() {return this.failed;}
	public boolean isWaiting() {return this.waiting;}
	public int getSlot() {return this.slot;}
	public int getSlice() {return this.slice;}
	public void setSlot(int reg, int slot, int value) {task.setRegister(reg, writeSlot(task.getRegister(reg), slot, value));}
	public int getSlot(int reg, int slot) {return readSlot(task.getRegister(reg), slot);}
	public int getSlot(int slot) {return readSlot(task.getSystemRegister(SystemRegister.I), slot);}
	public int nextSlot() {return readSlot(task.getSystemRegister(SystemRegister.I), this.slot++);}
	public Processor getPortPartner(int p) {return this.port_partner[p];}
	public Processor getPortPartner(Port p) {return this.getPortPartner(p.ordinal());}
	public void setPortPartner(int p, Processor value) {this.port_partner[p] = value;}
	public void setPortPartner(Port p, Processor value) {this.setPortPartner(p.ordinal(), value);}
	public long getPort(Port p, boolean writing) {return this.getPort(p.ordinal(), writing);}
	public void setPort(Port p, boolean writing, long value) {this.setPort(p.ordinal(), writing, value);}
	public int getPortReadMask() {return this.port_read_mask;}
	public int getPortWriteMask() {return this.port_write_mask;}
	public int getX() {return this.x;}
	public int getY() {return this.y;}
	public int getZ() {return this.z;}
	public int getCurrentTask() {return this.current_task;}
	public Task getTask() {return this.task;}
	public int getNoOfTasks() {return this.task_list.length;}
	public Task getTask(int i) {return this.task_list[i];}

	public void setSlice(int value) {slice = value % NO_OF_SIMD_REGISTER_CELLS;}

	public long adc(long a, long b, boolean carry)
	{
		long c = a+b;
		boolean overflow = (a > 0 && b > 0 && c < 0) || (a < 0 && b < 0 && c > 0);
		if (carry) {
			if (++c == 0) {overflow = true;}
		}
		task.setFlag(Flag.CARRY, overflow);
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
	
	public long getFlagForInterrupt()
	{
		long res = task.getSystemRegister(SystemRegister.FLAG);
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
		aux_data <<= FIRST_SLOT_BITS;
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
		task.setSystemRegister(SystemRegister.FLAG, data & (mask >>> (FIRST_SLOT_BITS + 3*SLOT_BITS)));
	}

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
			task.setRegister(reg, this.communication);
		}
		else {
			reg -=  BIT_PER_CELL;
			task.setSystemRegister(reg, this.communication);
		}
		task.setFlag(Flag.UPREAD.ordinal()+p, true);
		this.setPort(p, false, this.communication);
		this.port_read_mask = 0;
		this.waiting = false;
	}

	public void writeToPort(int p)
	{
		task.setFlag(Flag.UPWRITE.ordinal()+p, true);
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

	private void doSkipConditionalJump(int condition)
	{
		switch (Branch.values()[condition & 0xf]) {
		case FORWARD:	this.nextSlot(); break;
		case BACK:		this.nextSlot(); break;
		case IO:		this.nextSlot(); break;
		case LONG:		task.nextP(); break;
//		case REM:		this.slot = NO_OF_SLOTS; break;
		default:
		}
	}

	public boolean doConditionalJump(int condition)
	{
		Condition cond = Condition.values()[(condition >> 4) & 3];
		switch (cond) {
		case EQ0:		if (task.getT() != 0) {task.drop(); doSkipConditionalJump(condition); return false;} task.drop(); break;
		case QEQ0:		if (task.getT() != 0) {doSkipConditionalJump(condition); return false;} task.drop(); break;
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
		case FORWARD:	task.shortJump(this.nextSlot(), true); break;
		case BACK:		task.shortJump(this.nextSlot(), false); break;
		case IO:		this.doJumpIO(this.nextSlot()); break;
		case LONG:		task.longJump(); break;
//		case REM:		this.jumpRemainigSlots(); break;
		}
		return true;
	}
	
	public void doSwapRR(int dst, int src)
	{
		long tmp = task.getRegister(dst);
		task.setRegister(dst, task.getRegister(src));
		task.setRegister(src, tmp);
	}

	public void doMoveRR(int dst, int src)
	{
		task.setRegister(dst, task.getRegister(src));
	}

	public void doSwapRS(int dst, int src)
	{
		long tmp = task.getRegister(dst);
		task.setRegister(dst, task.getSystemRegister(src));
		task.setSystemRegister(src, tmp);
	}

	public void doMoveRS(int dst, int src)
	{
		task.setRegister(dst, task.getSystemRegister(src));
	}

	public void doMoveSR(int dst, int src)
	{
		task.setSystemRegister(dst, task.getRegister(src));
	}


	public void doMoveRL(int dst, int src)
	{
		task.setRegister(dst, task.getLocalRegister(src));
	}

	public void doMoveLR(int dst, int src)
	{
		task.setLocalRegister(dst, task.getRegister(src));
	}

	public void doMin(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = min(src1, src2);
		task.setRegister(d, dest);
	}

	public void doMax(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = max(src1, src2);
		task.setRegister(d, dest);
	}

	public void doAdd(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 + src2;
		task.setRegister(d, dest);
	}

	public void doAddi(int d, int s1, int src2)
	{
		task.setRegister(d, task.getRegister(s1) + src2);
	}

	public void doAddWithCarry(int d, int s1, int s2)
	{
		boolean carry = task.getFlag(Flag.CARRY);
		long a = task.getRegister(s1);
		long b = task.getRegister(s2);
		task.setRegister(d, this.adc(a, b, carry));
	}

	
	public void doSub(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 - src2;
		task.setRegister(d, dest);
	}

	public void doSubi(int d, int s1, int src2)
	{
		task.setRegister(d, task.getRegister(s1) - src2);
	}

	public void doSubtractWithCarry(int d, int s1, int s2)
	{
		boolean carry = task.getFlag(Flag.CARRY);
		long a = task.getRegister(s1);
		long b = ~task.getRegister(s2);
		task.setRegister(d, this.adc(a, ~b, carry));
	}

	public void doAnd(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 & src2;
		task.setRegister(d, dest);
	}

	public void doOr(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 | src2;
		task.setRegister(d, dest);
	}

	public void doXor(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 ^ src2;
		task.setRegister(d, dest);
	}

	public void doEquivalent(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = src1 ^ ~src2;
		task.setRegister(d, dest);
	}


	public void doAsl(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.asl(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doAsr(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.asr(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doLsl(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.lsl(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doLsr(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.lsr(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doAsli(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.asl(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doAsri(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.asr(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doLsli(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.lsl(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doLsri(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.lsr(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRol(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.rol(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRor(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.ror(src1, (int)src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRcl(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.rcl(src1, (int)src2, task.getFlag(Flag.CARRY));
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRcr(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = this.rcr(src1, (int)src2, task.getFlag(Flag.CARRY));
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRoli(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.rol(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRori(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.ror(src1, src2);
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRcli(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.rcl(src1, src2, task.getFlag(Flag.CARRY));
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	public void doRcri(int d, int s1, int src2)
	{
		long src1 = task.getRegister(s1);
		long dest = this.rcr(src1, src2, task.getFlag(Flag.CARRY));
		task.setFlag(Flag.CARRY, this.carry);
		task.setRegister(d, dest);
	}

	
	public void doMul2Add(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = (src1 << 1) + src2;
		task.setRegister(d, dest);
	}

	public void doDiv2Sub(int d, int s1, int s2)
	{
		long src1 = task.getRegister(s1);
		long src2 = task.getRegister(s2);
		long dest = (src1 >> 1) - src2;
		task.setRegister(d, dest);
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
				case EXIT:		task.exit(); break;
				case UNEXT:		task.unext(); break;
				case CONT:		task.cont(); break;
				case UJMP0:		this.slot = 0; break;
				case UJMP1:		this.slot = 1; break;
				case UJMP2:		this.slot = 2; break;
				case UJMP3:		this.slot = 3; break;
				case UJMP4:		this.slot = 4; break;
				case UJMP5:		this.slot = 5; break;
				case AND:		this.doAnd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
				case XOR:		this.doXor(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
				case DUP:		task.dup(); break;
				case DROP:		task.drop(); break;
				case OVER:		task.over(); break;
				case NIP:		task.nip(); break;
				case LIT:		task.lit(this.nextSlot()); break;
				case NLIT:		task.nLit(this.nextSlot()); break;
				case SNEXT:		task.snext(this.nextSlot(), false); break;
				case BRANCH:	this.doConditionalJump(this.nextSlot()); break;
				case CALL:		task.call(); break;
				case CALLM:		task.callMethod(this.nextSlot()); break;
				case FJMP:		task.shortJump(this.nextSlot(), true); break;
				case BJMP:		task.shortJump(this.nextSlot(), false); break;
				case SAVE:		task.saveSelf(); break;
				case RESTORE:	task.restoreSelf(); break;
				case USKIP:		this.slot = NO_OF_SLOTS; break;
				case UJMP6:		this.slot = 6; break;
				case UJMP7:		this.slot = 7; break;
				case UJMP8:		this.slot = 8; break;
				case UJMP9:		this.slot = 9; break;
				case UJMP10:	this.slot = 10; break;
				case SWAP:		this.doSwapRR(this.nextSlot(), this.nextSlot()); break;
				case MOV:		this.doMoveRR(this.nextSlot(), this.nextSlot()); break;
				case OR:		this.doOr(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
				case ENTER:		task.enter(); break;
				case LOADSELF:	task.loadSelf(); break;
				case LOADMT:	task.loadMT(); break;
				case RFETCH:	task.rFetch(this.nextSlot()); break;
				case RSTORE:	task.rStore(this.nextSlot()); break;
				case LFETCH:	task.lFetch(this.nextSlot()); break;
				case LSTORE:	task.lStore(this.nextSlot()); break;
				case INC:		task.inc(this.nextSlot()); break;
				case DEC:		task.dec(this.nextSlot()); break;
				case FETCH:		task.fetch(); break;
				case STORE:		task.store(); break;
				case FETCHPINC:	task.fetchPInc(); break;
				case STOREPINC:	task.storePInc(); break;
				case ADD:		this.doAdd(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
				case SUB:		this.doSub(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
				case MUL2:		this.doMul2Add(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
				case DIV2:		this.doDiv2Sub(Register.T.ordinal(), Register.T.ordinal(), Register.Z.ordinal()); break;
				case PUSH:		task.push(Register.T.ordinal()); task.drop(); break;
				case POP:		task.dup(); task.pop(Register.T.ordinal()); break;
				case EXT1:		this.doExt1(); break;
				case EXT2:		this.doExt2(); break;
				case EXT3:		this.doExt3(); break;
				case EXT4:		this.doExt4(); break;
				case EXT5:		this.doExt5(); break;
				case EXT6:		this.doExt6(); break;
				case REGOP1:	this.doRegisterOperation(this.nextSlot(), this.nextSlot()); break;
				case REGOP2:	this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				case REGOP3:	this.doRegisterOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				case SIMD:		task.getSIMD().doOperation(this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot(), this.nextSlot()); break;
				default: if (this.interrupt(Flag.ILLEGAL)) {return;}
				}
			}
			// check if there is some interrupt pending
			if ((task.getSystemRegister(SystemRegister.FLAG) & task.getSystemRegister(SystemRegister.INTE)) != 0) {
				// there is some pending interrupt
				triggerInterrupts();
			}
			//
			if ((!this.waiting) && (this.slot >= NO_OF_SLOTS)) {
				// load new instruction cell
				long adr = task.getSystemRegister(SystemRegister.P);
				if (adr >= 0) {
					// normal memory
					if (adr >= system.getMemorySize()) {
						this.running = false;
					}
					else {
						task.setSystemRegister(SystemRegister.I.ordinal(), system.getMemory(adr));
						// increment P
						task.setSystemRegister(SystemRegister.P, ++adr);
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
	
	public void doRol(int reg, int shift)
	{
		task.setRegister(reg, this.rol(task.getRegister(reg), shift));
		task.setFlag(Flag.CARRY, this.carry);
	}

	public void doRor(int reg, int shift)
	{
		task.setRegister(reg, this.ror(task.getRegister(reg), shift));
		task.setFlag(Flag.CARRY, this.carry);
	}

	public void doRcl(int reg, int shift)
	{
		task.setRegister(reg, this.rcl(task.getRegister(reg), shift, task.getFlag(Flag.CARRY)));
		task.setFlag(Flag.CARRY, this.carry);
	}

	public void doRcr(int reg, int shift)
	{
		task.setRegister(reg, this.rcr(task.getRegister(reg), shift, task.getFlag(Flag.CARRY)));
		task.setFlag(Flag.CARRY, this.carry);
	}

	public void doSetBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? task.getRegister(reg) : task.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? task.getRegister(bit) : task.getRegister(reg);
		task.setRegister(reg, value | mask);
		task.setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {task.nip();}
	}

	public void doClearBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? task.getRegister(reg) : task.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? task.getRegister(bit) : task.getRegister(reg);
		task.setRegister(reg, clearBit(value, bitpos));
		task.setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {task.nip();}
	}

	public void doToggleBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? task.getRegister(reg) : task.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? task.getRegister(bit) : task.getRegister(reg);
		task.setRegister(reg, value ^ mask);
		task.setFlag(Flag.CARRY, (value & mask) != 0);
		if (bit_is_reg && ((swap_source ? reg : bit) == Register.S.ordinal())) {task.nip();}
	}

	public void doReadBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		long mask = 1;
		int bitpos = bit_is_reg ? (int)(swap_source ? task.getRegister(reg) : task.getRegister(bit)) & 0x3f : bit;
		mask <<= bitpos;
		long value = swap_source ? task.getRegister(bit) : task.getRegister(reg);
		task.setFlag(Flag.CARRY, (value & mask) != 0);
	}

	public void doWriteBit(int reg, int bit, boolean bit_is_reg, boolean swap_source)
	{
		if (task.getFlag(Flag.CARRY)) {
			this.doSetBit(reg, bit, bit_is_reg, swap_source);
		}
		else {
			this.doClearBit(reg, bit, bit_is_reg, swap_source);
		}
	}

	public void doEnterInterrupt(int no)
	{
		// mark interrupt as in service
		task.setFlag(SystemRegister.INTS, no, true);
		// clear interrupt flag
		task.setFlag(no, false);
		//
		task.pushReturnStack(task.getRegister(Register.R));
		// save flags (with current slot)
		task.pushReturnStack(this.getFlagForInterrupt());
		this.port_read_mask = 0;
		this.port_write_mask = 0;
		this.waiting = false;
		// save I
		task.setRegister(Register.R, task.getSystemRegister(SystemRegister.I));
		// load instruction from interrupt vector table
		task.setSystemRegister(SystemRegister.I, system.getMemory(task.getSystemRegister(SystemRegister.INTV)+no));
		// start with slot 0
		this.slot = 0;
	}

	public void doExitInterrupt(int no)
	{
		// restore I
		task.setSystemRegister(SystemRegister.I, task.getRegister(Register.R));
		// restore flags with slot
		this.setFlagFromInterrupt(task.popReturnStack());
		this.waiting = (this.port_read_mask != 0) || (this.port_write_mask != 0);
		// restore R
		task.setRegister(Register.R, task.popReturnStack());
		// clear interrupt service register
		task.setFlag(SystemRegister.INTS, no, false);
	}
	
	public void doJumpIO(int mask)
	{
		task.setSystemRegister(SystemRegister.P, getIOAddress(mask));
		slot = NO_OF_SLOTS; // leave slot
	}

	public void doSign(int dest, int src)
	{
		task.setRegister(dest, sign(task.getRegister(src)));
	}

	public void doBitCnt1(int dest, int src)
	{
		task.setRegister(dest, countBits(task.getRegister(src)));
	}

	public void doBitCnt0(int dest, int src)
	{
		task.setRegister(dest, BIT_PER_CELL-countBits(task.getRegister(src)));
	}

	public void doBitFindFirst1(int dest, int src)
	{
		task.setRegister(dest, findFirstBit1(task.getRegister(src)));
	}

	public void doBitFindLast1(int dest, int src)
	{
		task.setRegister(dest, findLastBit1(task.getRegister(src)));
	}

	public void doByteCount(int dest, int src1, int src2)
	{
		task.setRegister(dest, countBytes(task.getRegister(src1), (byte)task.getRegister(src2)));
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
		long adr = task.getRegister(Register.T);
		if (task.getSystemRegister(SystemRegister.RES) != 0) {
			// some memory has already been reserved
			if (interrupt(Flag.TOUCHED)) {
				return false;
			}
//			this.setFlag(Flag.RESERVED, true);
		}
		task.setSystemRegister(SystemRegister.RES, adr);
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
		long adr = task.getRegister(Register.T);
		long value = task.getRegister(Register.S);
		if (task.getSystemRegister(SystemRegister.RES) != adr) {
			// memory has not been reserved
		}
		if (task.getFlag(Flag.TOUCHED)) {
			this.setRegister(Register.T, -1);
		}
		else {
			system.setMemory(adr, value);
			this.setRegister(Register.T, 0);
		}
		task.setSystemRegister(SystemRegister.RES, 0);
		task.nip();
	}

	public void doFetchPort(int mask, boolean wait)
	{
		task.dup();
		this.communication_register = Register.T.ordinal();
		this.port_read_mask = mask;
		if (!readPort() && wait) {
			this.waiting = true;
			this.reading = true;
		}
	}

	public void doStorePort(int mask, boolean wait)
	{
		this.communication = task.getRegister(Register.T);
		task.drop();
		this.port_write_mask = mask;
		if (!this.writePort() && wait) {
			this.waiting = true;
			this.reading = false;
		}
	}

	public void doFetchSystem(int reg)
	{
		task.dup();
		this.setRegister(Register.T, task.getSystemRegister(reg));
	}

	public void doStoreSystem(int reg)
	{
		task.setSystemRegister(reg, task.getRegister(Register.T));
		task.drop();
	}

	public void doFetchSIMD(int reg)
	{
		task.dup();
		int slice = (int)(task.getRegister(Register.R) & SIMD_SLICE_MASK);
		this.setRegister(Register.T, task.getSIMDRegister(reg)[slice]);
	}

	public void doStoreSIMD(int reg)
	{
		int slice = (int)(task.getRegister(Register.R) & SIMD_SLICE_MASK);
		task.getSIMDRegister(reg)[slice] = task.getRegister(Register.T);
		task.drop();
	}

	public void doExt1()
	{
		switch (Ext1.values()[this.nextSlot()]) {
		case RDROP:			task.rDrop(); break;
		case RDUP:			task.rDup(); break;
		case QDUP:			task.qdup(); break;
		case EXECUTE:		task.execute(); break;
		case EXITI:			this.doExitInterrupt(this.nextSlot()); break;
//		case SWAP0:			this.doSwap(this.nextSlot(), this.nextSlot()); this.slot = 0; break;
		case LCOL:			task.longCol(); break;
		case LJMP:			task.longJump(); break;
//		case RNEXT:			this.doRemainingNext(); break;
		case LNEXT:			task.lnext(); break;
		case DO:			task.dodo(); break;
		case QDO:			task.qdo(); break;
		case QFOR:			task.qfor(); break;
		case LOOP:			task.loop(); break;
		case PLOOP:			task.ploop(); break;
		case MIN:			this.doMin(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
		case MAX:			this.doMax(Register.T.ordinal(), Register.S.ordinal(), Register.T.ordinal()); task.nip(); break;
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
		case ENTERM:		task.enterM(); break;
		case LCALLM:		task.callMethod(task.drop()); break;
//		case LJMPM:			this.doJumpMethod(this.drop()); break;
		case TUCK:			task.tuck(); break;
		case UNDER:			task.under(); break;
		case MULS:			task.multiplyStep(); break;
		case DIVS:			task.divideStep(); break;
		case MDP:			task.multiplyDividePrepare(); break;
		case MULF:			task.multiplyFinished(); break;
		case DIVMODF:		task.divideModFinished(); break;
		case EQ0Q:			task.eq0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case NE0Q:			task.ne0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case GT0Q:			task.gt0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case GE0Q:			task.ge0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case LT0Q:			task.lt0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
		case LE0Q:			task.le0q(Register.T.ordinal(), Register.T.ordinal(), false); break;
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
		case EQ0Q:			task.eq0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case NE0Q:			task.ne0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case GT0Q:			task.gt0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case GE0Q:			task.ge0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case LT0Q:			task.lt0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case LE0Q:			task.le0q(Register.T.ordinal(), this.nextSlot(), true); break;
		case SFLAG:			task.setFlag(this.nextSlot()); break;
		case CFLAG:			task.clearFlag(this.nextSlot()); break;
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
		case PUSHR:			task.push(this.nextSlot()); break;
		case POPR:			task.pop(this.nextSlot()); break;
		case PUSHL:			task.pushLocal(this.nextSlot()); break;
		case POPL:			task.popLocal(this.nextSlot()); break;
		case PUSHS:			task.pushSystem(this.nextSlot()); break;
		case POPS:			task.popSystem(this.nextSlot()); break;
		case BLIT:			task.bLit(this.nextSlot()); break;
		case JMPIO:			this.doJumpIO(this.nextSlot()); break;
		case CONFIGFETCH:	task.configFetch(this.nextSlot()); break;
		default: this.interrupt(Flag.ILLEGAL);
		}
	}

	public void doExt3()
	{
		switch (Ext3.values()[this.nextSlot()]) {
		case LSAVE:			task.saveLocal(this.nextSlot(), this.nextSlot()); break;
		case LRESTORE:		task.restoreLocal(this.nextSlot(), this.nextSlot()); break;
		case SWAPRS:		this.doSwapRS(this.nextSlot(), this.nextSlot()); break;
		case SWAPRL:		task.swapRL(this.nextSlot(), this.nextSlot()); break;
		case MOVSR:			this.doMoveSR(this.nextSlot(), this.nextSlot()); break;
		case MOVRS:			this.doMoveRS(this.nextSlot(), this.nextSlot()); break;
		case MOVLR:			this.doMoveLR(this.nextSlot(), this.nextSlot()); break;
		case MOVRL:			this.doMoveRL(this.nextSlot(), this.nextSlot()); break;
		case MOVRI:			task.setRegister(this.nextSlot(), this.nextSlot()); break;
		case MOVSI:			task.setSystemRegister(this.nextSlot(), this.nextSlot()); break;
		case MOVLI:			task.setLocalRegister(this.nextSlot(), this.nextSlot()); break;
		case ROL:			this.doRol(this.nextSlot(), this.nextSlot()); break;
		case ROR:			this.doRor(this.nextSlot(), this.nextSlot()); break;
		case RCL:			this.doRcl(this.nextSlot(), this.nextSlot()); break;
		case RCR:			this.doRcr(this.nextSlot(), this.nextSlot()); break;
		case EQ0Q:			task.eq0q(this.nextSlot(), this.nextSlot(), true); break;
		case NE0Q:			task.ne0q(this.nextSlot(), this.nextSlot(), true); break;
		case GT0Q:			task.gt0q(this.nextSlot(), this.nextSlot(), true); break;
		case GE0Q:			task.ge0q(this.nextSlot(), this.nextSlot(), true); break;
		case LT0Q:			task.lt0q(this.nextSlot(), this.nextSlot(), true); break;
		case LE0Q:			task.le0q(this.nextSlot(), this.nextSlot(), true); break;
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
		case RFETCHI:	task.rFetchIndirect(this.nextSlot()); break;
		case RSTOREI:	task.rStoreIndirect(this.nextSlot()); break;
		case LFETCHI:	task.lFetchIndirect(this.nextSlot()); break;
		case LSTOREI:	task.lStoreIndirect(this.nextSlot()); break;
		case SFETCHI:	task.sFetchIndirect(this.nextSlot()); break;
		case SSTOREI:	task.sStoreIndirect(this.nextSlot()); break;

		case RFETCH:	task.rFetchFetch(this.nextSlot(), false, false, false); break;
		case RSTORE:	task.rFetchStore(this.nextSlot(), false, false, false); break;
		case LFETCH:	task.lFetchFetch(this.nextSlot(), false, false, false); break;
		case LSTORE:	task.lFetchStore(this.nextSlot(), false, false, false); break;
		case SFETCH:	task.sFetchFetch(this.nextSlot(), false, false, false); break;
		case SSTORE:	task.sFetchStore(this.nextSlot(), false, false, false); break;

		case RFETCHPEI:	task.rFetchFetch(this.nextSlot(), false, true, false); break;
		case RSTOREPEI:	task.rFetchStore(this.nextSlot(), false, true, false); break;
		case LFETCHPEI:	task.lFetchFetch(this.nextSlot(), false, true, false); break;
		case LSTOREPEI:	task.lFetchStore(this.nextSlot(), false, true, false); break;
		case SFETCHPEI:	task.sFetchFetch(this.nextSlot(), false, true, false); break;
		case SSTOREPEI:	task.sFetchStore(this.nextSlot(), false, true, false); break;

		case RFETCHPOI:	task.rFetchFetch(this.nextSlot(), false, false, true); break;
		case RSTOREPOI:	task.rFetchStore(this.nextSlot(), false, false, true); break;
		case LFETCHPOI:	task.lFetchFetch(this.nextSlot(), false, false, true); break;
		case LSTOREPOI:	task.lFetchStore(this.nextSlot(), false, false, true); break;
		case SFETCHPOI:	task.sFetchFetch(this.nextSlot(), false, false, true); break;
		case SSTOREPOI:	task.sFetchStore(this.nextSlot(), false, false, true); break;

		case RFETCHPED:	task.rFetchFetch(this.nextSlot(), true, true, false); break;
		case RSTOREPED:	task.rFetchStore(this.nextSlot(), true, true, false); break;
		case LFETCHPED:	task.lFetchFetch(this.nextSlot(), true, true, false); break;
		case LSTOREPED:	task.lFetchStore(this.nextSlot(), true, true, false); break;
		case SFETCHPED:	task.sFetchFetch(this.nextSlot(), true, true, false); break;
		case SSTOREPED:	task.sFetchStore(this.nextSlot(), true, true, false); break;

		case RFETCHPOD:	task.rFetchFetch(this.nextSlot(), true, false, true); break;
		case RSTOREPOD:	task.rFetchStore(this.nextSlot(), true, false, true); break;
		case LFETCHPOD:	task.lFetchFetch(this.nextSlot(), true, false, true); break;
		case LSTOREPOD:	task.lFetchStore(this.nextSlot(), true, false, true); break;
		case SFETCHPOD:	task.sFetchFetch(this.nextSlot(), true, false, true); break;
		case SSTOREPOD:	task.sFetchStore(this.nextSlot(), true, false, true); break;

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
		case ABS:		task.abs(d, d); break;
		case NEGATE:	task.negate(d, d); break;
		case SIGN:		this.doSign(d, d); break;
		case REVERSE:	task.reverse(d, d); break;
		case NEXTPOW2:	task.nextPow2(d, d); break;
		case PARITY:	task.parityq(d, d); break;
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
		case ABS:		task.abs(d, s); break;
		case NEGATE:	task.negate(d, s); break;
		case SIGN:		this.doSign(d, s); break;
		case REVERSE:	task.reverse(d, s); break;
		case NEXTPOW2:	task.nextPow2(d, s); break;
		case PARITY:	task.parityq(d, s); break;
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
		task.setFlag(no, true);
		if (task.getFlag(SystemRegister.INTE, no.ordinal())) {
			// interrupt is enabled
			if (!task.getFlag(SystemRegister.INTS, Flag.values()[no.ordinal()])) {
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
			if (task.getFlag(i) && task.getFlag(SystemRegister.INTE, i)) {
				// interrupt is enabled
				if (!task.getFlag(SystemRegister.INTS, Flag.values()[i])) {
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
		task.inc(SystemRegister.CLK);
		if (task.getSystemRegister(SystemRegister.CLK) == task.getSystemRegister(SystemRegister.CLI)) {
			task.setFlag(Flag.CLOCK, true);
		}
	}

	public void reset()
	{
		//
		this.waiting = false;
		this.failed = false;
		this.port_read_mask = 0;
		this.port_write_mask = 0;
		this.slot = 0;
		this.slice = 0;
		this.current_task = 0;
		this.task = this.task_list[0];
		for (int i=0; i<task_list.length; ++i) {
			task_list[i].reset();
		}
		this.interrupt(Flag.RESET);
	}
	
	public void powerOn()
	{
		this.reset();
		this.task.powerOn();
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
		task.pushSystem(SystemRegister.P.ordinal());
		task.setSystemRegister(SystemRegister.I, instr);
		task.setSystemRegister(SystemRegister.P, 0x7fff_ffff_ffff_ffffL); // invalid address
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
		task.popSystem(SystemRegister.P.ordinal());
	}

	
}
