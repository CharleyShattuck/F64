package com.F64.SIMD;


public class Unit {

	public static long AddModI32(long s1, long s2)
	{
		int s1_0 = (int) (s1 & 0xffff_ffff);
		int s2_0 = (int) (s2 & 0xffff_ffff);
		int s1_1 = (int) (s1 >> 32);
		int s2_1 = (int) (s2 >> 32);
		int d_0 = s1_0 + s2_0;
		int d_1 = s1_1 + s2_1;
		long d = d_0;
		d <<= 32;
		d |= ((long)d_1) & 0xffff_ffff;
		return d;
	}

	private long[][]	register;

	public Unit()
	{
		this.register = new long[com.F64.Processor.NO_OF_REG][];
		for (int i=0; i<com.F64.Processor.NO_OF_REG; ++i) {
			this.register[i] = new long[com.F64.Processor.NO_OF_MEDIA_REGISTER_CELLS];
		}

	}
	
	public long[] getRegister(int reg) {return this.register[reg];}

	private void doAddModI32(int d, int s1, int s2)
	{
		long[] da = this.register[d];
		long[] s1a = this.register[s1];
		long[] s2a = this.register[s2];
		for (int i=0; i<com.F64.Processor.MEDIA_SLICE_SIZE; ++i) {
			da[i] = AddModI32(s1a[i], s2a[i]);
		}
	}

	private void doAddModU32(int d, int s1, int s2)
	{
	}

	private void doAddModI16(int d, int s1, int s2)
	{
	}

	private void doAddModU16(int d, int s1, int s2)
	{
	}

	private void doAddModI8(int d, int s1, int s2)
	{
	}

	private void doAddModU8(int d, int s1, int s2)
	{
	}

	private void doAddMod(Type stype, int d, int s1, int s2)
	{
		switch (stype) {
		case SINT16:	doAddModI16(d, s1, s2); return;
		case SINT32:	doAddModI32(d, s1, s2); return;
		case SINT8:		doAddModI8(d, s1, s2); return;
		case UINT16:	doAddModU16(d, s1, s2); return;
		case UINT32:	doAddModU32(d, s1, s2); return;
		case UINT8:		doAddModU8(d, s1, s2); return;
		}
	}

	private void doAddSatI32(int d, int s1, int s2)
	{
	}

	private void doAddSatU32(int d, int s1, int s2)
	{
	}

	private void doAddSatI16(int d, int s1, int s2)
	{
	}

	private void doAddSatU16(int d, int s1, int s2)
	{
	}

	private void doAddSatI8(int d, int s1, int s2)
	{
	}

	private void doAddSatU8(int d, int s1, int s2)
	{
	}

	private void doAddSat(Type stype, int d, int s1, int s2)
	{
		switch (stype) {
		case SINT16:	doAddSatI16(d, s1, s2); return;
		case SINT32:	doAddSatI32(d, s1, s2); return;
		case SINT8:		doAddSatI8(d, s1, s2); return;
		case UINT16:	doAddSatU16(d, s1, s2); return;
		case UINT32:	doAddSatU32(d, s1, s2); return;
		case UINT8:		doAddSatU8(d, s1, s2); return;
		}
	}

	public void doOperation(int op, int par, int d, int s1, int s2)
	{
		Operation soper = Operation.values()[op];
		Type t = Type.values()[par & 0x0f];
		Arithmetic sarit = Arithmetic.values()[(par >> 1) & 0x01];
		if (d == 0) {return;}
		switch (soper) {
		case ADD:	if (sarit == Arithmetic.MODULAR) {doAddMod(t, d, s1, s2);} else {doAddSat(t, d, s1, s2);} return;
		case AND:
			break;
		case DIV:
			break;
		case EQQ:
			break;
		case EQV:
			break;
		case GEQ:
			break;
		case GTQ:
			break;
		case LEQ:
			break;
		case LTQ:
			break;
		case MAX:
			break;
		case MIN:
			break;
		case MOD:
			break;
		case MUL:
			break;
		case MULADD:
			break;
		case NEQ:
			break;
		case OR:
			break;
		case SUB:
			break;
		case XOR:
			break;
		}
	}

}
