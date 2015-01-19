package com.F64.codepoint;

import com.F64.Compiler;
import com.F64.ISA;
import com.F64.Optimization;
import com.F64.Processor;
import com.F64.RegOp2;
import com.F64.RegOp3;
import com.F64.Register;

public class Add extends com.F64.Codepoint {
	private int			src1;
	private int			src2;
	private int			dest;
	private long		constant;
	private boolean		constant_valid;


	public Add()
	{
		src1 = src2 = dest = -1;
	}

	public Add(int d, int s1, int s2)
	{
		src1 = s1;
		src2 = s2;
		dest = d;
	}

	public boolean isConstant() {return constant_valid;}
	public long getConstant() {return constant;}
	public void setConstant(long value) {constant = value; constant_valid = true;}
	
	@Override
	public int countSlots(int slot)
	{
		if (dest == src1) {
			if (dest == -1) {
				if (isConstant()) {
					if (constant > 0) {
						if (constant == 1) {
							return ISA.INC.size();
						}
						if (constant < Processor.SLOT_SIZE) {
							return ISA.REGOP3.size();
						}
						return Compiler.countLiteralSlots(slot, constant) + ISA.ADD.size();
					}
					if (constant < 0) {
						if (constant == -1) {
							return ISA.DEC.size();
						}
						if (constant > -Processor.SLOT_SIZE) {
							return ISA.REGOP3.size();
						}
						return Compiler.countLiteralSlots(slot, constant) + ISA.ADD.size();
					}
					return 0;
				}
				else {
					return ISA.ADD.size();
				}
			}
			else {
				return ISA.REGOP2.size();
			}
		}
		return ISA.REGOP3.size();
	}

	@Override
	public boolean optimize(Processor processor, Optimization opt)
	{
		long data;
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if ((p != null) && (dest==-1)) {
			switch (opt) {
			case CONSTANT_FOLDING:
				com.F64.Codepoint pp = p.getPrevious();
				if (pp != null) {
					if ((p instanceof Literal) && (pp instanceof Literal)) {
						// constant
						Literal lit1 = (Literal) pp;
						Literal lit2 = (Literal) p;
						lit1.setValue(lit1.getValue() + lit2.getValue());
						lit2.remove();
						this.remove();
						return true;
					}
				}
				break;

			case PEEPHOLE:
				if ((p instanceof Literal) && !constant_valid) {
					Literal lit = (Literal) p;
					this.setConstant(lit.getValue());
					lit.remove();
					return true;
//					if (data == 0) {
//						// add by 0 does nothing
//						lit.remove();
//						this.remove();
//						return true;
//					}
//					if (data > 0) {
//						if (data == 1) {
//							// increment
//							lit.replaceWith(new Inc());
//							this.remove();
//							return true;
//						}
//						if (data < Processor.SLOT_SIZE) {
//							lit.replaceWith(new RegOpCode(RegOp3.ADDI, Register.T.ordinal(), Register.T.ordinal(), (int)data));
//							this.remove();
//							return true;
//						}
//					}
//					else {
//						if (data == -1) {
//							// decrement
//							lit.replaceWith(new Dec());
//							this.remove();
//							return true;
//						}
//						if (data > -Processor.SLOT_SIZE) {
//							lit.replaceWith(new RegOpCode(RegOp3.SUBI, Register.T.ordinal(), Register.T.ordinal(), -(int)data));
//							this.remove();
//							return true;
//						}
//					}
				}
				if ((p instanceof Add) && constant_valid) {
					Add op = (Add) p;
					if (op.isConstant()) {
						data = op.getConstant() + constant;
						if (data == 0) {p.remove();}
						else {op.setConstant(data);}
						this.remove();
						return true;
					}
				}
				break;
			default:
				break;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{
		if (dest == src1) {
			if (dest == -1) {
				if (isConstant()) {
					if (constant > 0) {
						if (constant == 1) {
							c.generate(ISA.INC, Register.T.ordinal());						
						}
						else if (constant < Processor.SLOT_SIZE) {
							c.generate(RegOp3.ADDI, Register.T.ordinal(), Register.T.ordinal(), (int)constant);						
						}
						else {
							c.generateLiteral(constant);
							c.generate(ISA.ADD);
						}
					}
					else if (constant < 0) {
						if (constant == -1) {
							c.generate(ISA.DEC, Register.T.ordinal());						
						}
						else if (constant > -Processor.SLOT_SIZE) {
							c.generate(RegOp3.SUBI, Register.T.ordinal(), Register.T.ordinal(), -(int)constant);						
						}
						else {
							c.generateLiteral(constant);
							c.generate(ISA.ADD);
						}
					}
				}
				else {
					c.generate(ISA.ADD);
				}
			}
			else {
				c.generate(RegOp2.ADD, dest, src2);
			}
		}
		else {
			c.generate(RegOp3.ADD, dest, src1, src2);
		}
	}

}
