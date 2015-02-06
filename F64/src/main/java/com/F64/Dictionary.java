package com.F64;

import com.F64.word.*;

public class Dictionary {
	private Dictionary							parent;
	private System								system;
	private java.util.Map<String, Word>			macro_map;
	private java.util.Map<String, Word>			compile_map;
	private java.util.Map<String, Word>			normal_map;
	private java.util.Map<String, Dictionary>	package_map;

	public static String[] splitName(String name)
	{
		return name.split("\\|");
	}
	
	public Dictionary(System system)
	{
		this.system = system;
	}
	
	public Dictionary(Dictionary parent)
	{
		this.system = parent.system;
		this.parent = parent;
	}
	
	public Dictionary getParent() {return parent;}
	
	public void register(String[] name_list, boolean compiling, Word w)
	{
		Dictionary next, current = this;
		int last = name_list.length -1;
		int i = 0;
		while (i<last) {
			if (current.package_map == null) {
				current.package_map = new java.util.TreeMap<String, Dictionary>();
			}
			next = current.package_map.get(name_list[i]);
			if (next == null) {
				next = new Dictionary(current);
				current.package_map.put(name_list[i], next);
			}
			current = next;
			++i;
		}
		if (compiling) {
			if (current.compile_map == null) {
				current.compile_map = new java.util.TreeMap<String, Word>();
			}
			current.compile_map.put(name_list[last], w);
		}
		else {
			if (current.normal_map == null) {
				current.normal_map = new java.util.TreeMap<String, Word>();
			}
			current.normal_map.put(name_list[last], w);			
		}
	}

	public void register(String name, boolean compiling, Word w)
	{
		this.register(Dictionary.splitName(name), compiling, w);
	}
	
	public Word lookup(String[] name_list, boolean compiling, boolean macro)
	{
		Word res = null;
		Dictionary current = this;
		int last = name_list.length -1;
		int i = 0;
		if ((last > 0) && (current.package_map == null)) {return null;}
		while (i<last) {
			current = current.package_map.get(name_list[i]);
			if (current == null) {return null;}
			++i;
		}
		if (macro) {
			if (current.macro_map != null) {res = current.macro_map.get(name_list[last]);}
		}
		else {
			if (compiling && (current.compile_map != null)) {res = current.compile_map.get(name_list[last]);}
			if ((res == null) && (current.normal_map != null)) {res = current.normal_map.get(name_list[last]);}
		}
		return res;
	}

	public Word lookup(String name, boolean compiling, boolean macro)
	{
		return this.lookup(Dictionary.splitName(name), compiling, macro);
	}

	public void createStandardWords()
	{
		this.register(":",				false,	new Colon(false, false));
		this.register(";",				true,	new Semicolon(false));
		this.register("[:",				false,	new Colon(false, true));
		this.register(";]",				true,	new Semicolon(true));
		this.register(".",				false,	new Dot());
		this.register("+",				false,	new Add());
		this.register("-",				false,	new Sub());
		this.register("*",				false,	new Mul());
		this.register("/",				false,	new Div());
		this.register("/mod",			false,	new DivMod());
		this.register("<<",				false,	new Asl());
		this.register("<<<",			false,	new Lsl());
		this.register("==?",			false,	new EqQ());
		this.register("==0?",			false,	new Eq0Q());
		this.register("!=?",			false,	new NeQ());
		this.register("!=0?",			false,	new Ne0Q());
		this.register("<?",				false,	new LtQ());
		this.register("<0?",			false,	new Lt0Q());
		this.register("<=?",			false,	new LeQ());
		this.register("<=0?",			false,	new Le0Q());
		this.register(">>",				false,	new Asr());
		this.register(">>>",			false,	new Lsr());
		this.register(">?",				false,	new GtQ());
		this.register(">0?",			false,	new Gt0Q());
		this.register(">r",				true,	new Push());
		this.register(">=?",			false,	new GeQ());
		this.register(">=0?",			false,	new Ge0Q());
		this.register(">>^",			false,	new Ror());
		this.register(">>.^",			false,	new Rcr());
		this.register("^<<",			false,	new Rol());
		this.register("^.<<",			false,	new Rcl());
		this.register("\\",				false,	new LineComment());
		this.register("(",				false,	new Comment());
		this.register("{",				false,	new Locals());
		this.register("?dup",			false,	new QDup());
		this.register("?for",			false,	new QFor());
		this.register("?if",			true,	new If(Condition.QEQ0));
		this.register("1+",				false,	new Inc());
		this.register("1-",				false,	new Dec());
		this.register("2*",				false,	new Mul2());
		this.register("2/",				false,	new Div2());
		this.register("abs",			false,	new Abs());
		this.register("and",			false,	new And());
		this.register("begin",			true,	new Begin());
		this.register("constant",		false,	new Constant());
		this.register("drop",			false,	new Drop());
		this.register("dup",			false,	new Dup());
		this.register("else",			true,	new Else());
		this.register("execute",		false,	new Execute());
		this.register("exit",			true,	new Exit());
		this.register("for",			true,	new For());
		this.register("if",				true,	new If(Condition.EQ0));
		this.register("inline:",		false,	new Colon(true, false));
		this.register("max",			false,	new Max());
		this.register("min",			false,	new Min());
		this.register("mod",			false,	new Mod());
		this.register("negate",			false,	new Negate());
		this.register("next",			true,	new Next());
		this.register("nip",			false,	new Nip());
		this.register("not",			false,	new Not());
		this.register("ones",			false,	new Ones());
		this.register("or",				false,	new Or());
		this.register("over",			false,	new Over());
		this.register("r>",				true,	new Pop());
		this.register("repeat",			true,	new Repeat());
		this.register("sign",			false,	new Sign());
		this.register("swap",			false,	new Swap());
		this.register("then",			true,	new Then());
		this.register("to",				false,	new To());
		this.register("tuck",			false,	new Tuck());
		this.register("under",			false,	new Under());
		this.register("until",			true,	new Until());
		this.register("value",			false,	new Value());
		this.register("variable",		false,	new Variable());
		this.register("vector",			false,	new Vector());
		this.register("while",			true,	new While());
		this.register("xor",			false,	new Xor());
		this.register("zero",			false,	new Zero());

		this.register("Bit|count0",		false,	new BitCount0());
		this.register("Bit|count1",		false,	new BitCount1());
		this.register("Bit|parity",		false,	new BitParity());
		this.register("Bit|reverse",	false,	new BitReverse());

		this.register("Local|@",		false,	new LocalFetch());
		this.register("Local|!",		false,	new LocalStore());

		this.register("R|drop",			false,	new RDrop());
		this.register("R|dup",			false,	new RDup());

		this.register("System|@",		false,	new SystemFetch());
		this.register("System|!",		false,	new SystemStore());

		this.register("SysReg|^CLI",	false,	new Const(SystemRegister.CLI.ordinal()));
		this.register("SysReg|^CLK",	false,	new Const(SystemRegister.CLK.ordinal()));
		this.register("SysReg|^EXC",	false,	new Const(SystemRegister.EXC.ordinal()));
		this.register("SysReg|^EXF",	false,	new Const(SystemRegister.EXF.ordinal()));
		this.register("SysReg|^FLAG",	false,	new Const(SystemRegister.FLAG.ordinal()));
		this.register("SysReg|^I",		false,	new Const(SystemRegister.I.ordinal()));
		this.register("SysReg|^INTE",	false,	new Const(SystemRegister.INTE.ordinal()));
		this.register("SysReg|^INTS",	false,	new Const(SystemRegister.INTS.ordinal()));
		this.register("SysReg|^INTV",	false,	new Const(SystemRegister.INTV.ordinal()));
		this.register("SysReg|^IT",		false,	new Const(SystemRegister.IT.ordinal()));
		this.register("SysReg|^MD",		false,	new Const(SystemRegister.MD.ordinal()));
		this.register("SysReg|^MDP",	false,	new Const(SystemRegister.MDP.ordinal()));
		this.register("SysReg|^MT",		false,	new Const(SystemRegister.MT.ordinal()));
		this.register("SysReg|^P",		false,	new Const(SystemRegister.P.ordinal()));
		this.register("SysReg|^R0",		false,	new Const(SystemRegister.R0.ordinal()));
		this.register("SysReg|^RES",	false,	new Const(SystemRegister.RES.ordinal()));
		this.register("SysReg|^RL",		false,	new Const(SystemRegister.RL.ordinal()));
		this.register("SysReg|^RP",		false,	new Const(SystemRegister.RP.ordinal()));
		this.register("SysReg|^S0",		false,	new Const(SystemRegister.S0.ordinal()));
		this.register("SysReg|^SELF",	false,	new Const(SystemRegister.SELF.ordinal()));
		this.register("SysReg|^SL",		false,	new Const(SystemRegister.SL.ordinal()));
		this.register("SysReg|^SP",		false,	new Const(SystemRegister.SP.ordinal()));
		this.register("SysReg|^W",		false,	new Const(SystemRegister.W.ordinal()));


		this.register("Reg|^Z",			false,	new Const(Register.Z.ordinal()));
		this.register("Reg|^T",			false,	new Const(Register.T.ordinal()));
		this.register("Reg|^S",			false,	new Const(Register.S.ordinal()));
		this.register("Reg|^R",			false,	new Const(Register.R.ordinal()));
//		this.register("Reg|^W",			false,	new Const(Register.W.ordinal()));
		this.register("Reg|^L",			false,	new Const(Register.L.ordinal()));

		this.register("ISA|^+",			false,	new Const(ISA.ADD.ordinal()));
		this.register("ISA|^++",		false,	new Const(ISA.INC.ordinal()));
		this.register("ISA|^-",			false,	new Const(ISA.SUB.ordinal()));
		this.register("ISA|^--",		false,	new Const(ISA.DEC.ordinal()));
		this.register("ISA|^~LIT",		false,	new Const(ISA.NLIT.ordinal()));
		this.register("ISA|^@P+",		false,	new Const(ISA.FETCHPINC.ordinal()));
		this.register("ISA|^!P+",		false,	new Const(ISA.STOREPINC.ordinal()));
		this.register("ISA|^>R",		false,	new Const(ISA.PUSH.ordinal()));
		this.register("ISA|^2*",		false,	new Const(ISA.MUL2.ordinal()));
		this.register("ISA|^2/",		false,	new Const(ISA.DIV2.ordinal()));
		this.register("ISA|^AND",		false,	new Const(ISA.AND.ordinal()));
		this.register("ISA|^BJMP",		false,	new Const(ISA.BJMP.ordinal()));
		this.register("ISA|^BRANCH",	false,	new Const(ISA.BRANCH.ordinal()));
		this.register("ISA|^CALL",		false,	new Const(ISA.CALL.ordinal()));
		this.register("ISA|^CALLM",		false,	new Const(ISA.CALLM.ordinal()));
		this.register("ISA|^CONT",		false,	new Const(ISA.CONT.ordinal()));
		this.register("ISA|^DROP",		false,	new Const(ISA.DROP.ordinal()));
		this.register("ISA|^DUP",		false,	new Const(ISA.DUP.ordinal()));
		this.register("ISA|^ENTER",		false,	new Const(ISA.ENTER.ordinal()));
		this.register("ISA|^EXIT",		false,	new Const(ISA.EXIT.ordinal()));
//		this.register("ISA|^EXT",		false,	new Const(ISA.EXT.ordinal()));
		this.register("ISA|^EXT1",		false,	new Const(ISA.EXT1.ordinal()));
		this.register("ISA|^EXT2",		false,	new Const(ISA.EXT2.ordinal()));
		this.register("ISA|^EXT3",		false,	new Const(ISA.EXT3.ordinal()));
		this.register("ISA|^EXT4",		false,	new Const(ISA.EXT4.ordinal()));
		this.register("ISA|^EXT5",		false,	new Const(ISA.EXT5.ordinal()));
		this.register("ISA|^EXT6",		false,	new Const(ISA.EXT6.ordinal()));
		this.register("ISA|^FJMP",		false,	new Const(ISA.FJMP.ordinal()));
		this.register("ISA|^L@",		false,	new Const(ISA.LFETCH.ordinal()));
		this.register("ISA|^L!",		false,	new Const(ISA.LSTORE.ordinal()));
		this.register("ISA|^LIT",		false,	new Const(ISA.LIT.ordinal()));
		this.register("ISA|^LOADMT",	false,	new Const(ISA.LOADMT.ordinal()));
		this.register("ISA|^LOADSELF",	false,	new Const(ISA.LOADSELF.ordinal()));
		this.register("ISA|^MOV",		false,	new Const(ISA.MOV.ordinal()));
		this.register("ISA|^NIP",		false,	new Const(ISA.NIP.ordinal()));
		this.register("ISA|^NOP",		false,	new Const(ISA.NOP.ordinal()));
		this.register("ISA|^OR",		false,	new Const(ISA.OR.ordinal()));
		this.register("ISA|^OVER",		false,	new Const(ISA.OVER.ordinal()));
		this.register("ISA|^R@",		false,	new Const(ISA.RFETCH.ordinal()));
		this.register("ISA|^R!",		false,	new Const(ISA.RSTORE.ordinal()));
		this.register("ISA|^R>",		false,	new Const(ISA.POP.ordinal()));
		this.register("ISA|^REGOP1",	false,	new Const(ISA.REGOP1.ordinal()));
		this.register("ISA|^REGOP2",	false,	new Const(ISA.REGOP2.ordinal()));
		this.register("ISA|^REGOP3",	false,	new Const(ISA.REGOP3.ordinal()));
		this.register("ISA|^RESTORE",	false,	new Const(ISA.RESTORE.ordinal()));
		this.register("ISA|^SAVE",		false,	new Const(ISA.SAVE.ordinal()));
		this.register("ISA|^SIMD",		false,	new Const(ISA.SIMD.ordinal()));
		this.register("ISA|^SNEXT",		false,	new Const(ISA.SNEXT.ordinal()));
		this.register("ISA|^SWAP",		false,	new Const(ISA.SWAP.ordinal()));
		this.register("ISA|^UJMP0",		false,	new Const(ISA.UJMP0.ordinal()));
		this.register("ISA|^UJMP1",		false,	new Const(ISA.UJMP1.ordinal()));
		this.register("ISA|^UJMP2",		false,	new Const(ISA.UJMP2.ordinal()));
		this.register("ISA|^UJMP3",		false,	new Const(ISA.UJMP3.ordinal()));
		this.register("ISA|^UJMP4",		false,	new Const(ISA.UJMP4.ordinal()));
		this.register("ISA|^UJMP5",		false,	new Const(ISA.UJMP5.ordinal()));
		this.register("ISA|^UJMP6",		false,	new Const(ISA.UJMP6.ordinal()));
		this.register("ISA|^UJMP7",		false,	new Const(ISA.UJMP7.ordinal()));
		this.register("ISA|^UJMP8",		false,	new Const(ISA.UJMP8.ordinal()));
		this.register("ISA|^UJMP9",		false,	new Const(ISA.UJMP9.ordinal()));
		this.register("ISA|^UJMP10",	false,	new Const(ISA.UJMP10.ordinal()));
		this.register("ISA|^USKIP",		false,	new Const(ISA.USKIP.ordinal()));
		this.register("ISA|^UNEXT",		false,	new Const(ISA.UNEXT.ordinal()));
		this.register("ISA|^XOR",		false,	new Const(ISA.XOR.ordinal()));

		this.register("Exc|^COMPILE_ONLY",			false,	new Const(Exception.COMPILE_ONLY.ordinal()));
		this.register("Exc|^EXECUTE_ONLY",			false,	new Const(Exception.EXECUTE_ONLY.ordinal()));
		this.register("Exc|^INVALID_SCOPE",			false,	new Const(Exception.INVALID_SCOPE.ordinal()));
		this.register("Exc|^UNDEFINED",				false,	new Const(Exception.UNDEFINED.ordinal()));
		this.register("Exc|^UNSPECIFIC",			false,	new Const(Exception.UNSPECIFIC.ordinal()));
		
		
	}

}
