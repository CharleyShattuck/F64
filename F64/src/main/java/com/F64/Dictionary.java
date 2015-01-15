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
		this.register(".",			false,	new Dot());
		this.register("+",			false,	new Add());
		this.register("-",			false,	new Sub());
		this.register("*",			false,	new Mul());
		this.register("/",			false,	new Div());
		this.register("/mod",		false,	new DivMod());
		this.register("<<",			false,	new Asl());
		this.register("<<<",		false,	new Lsl());
		this.register("==?",		false,	new EqQ());
		this.register("==0?",		false,	new Eq0Q());
		this.register("!=?",		false,	new NeQ());
		this.register("!=0?",		false,	new Ne0Q());
		this.register("<?",			false,	new LtQ());
		this.register("<0?",		false,	new Lt0Q());
		this.register("<=?",		false,	new LeQ());
		this.register("<=0?",		false,	new Le0Q());
		this.register(">>",			false,	new Asr());
		this.register(">>>",		false,	new Lsr());
		this.register(">?",			false,	new GtQ());
		this.register(">0?",		false,	new Gt0Q());
		this.register(">=?",		false,	new GeQ());
		this.register(">=0?",		false,	new Ge0Q());
		this.register(">>^",		false,	new Ror());
		this.register(">>.^",		false,	new Rcr());
		this.register("^<<",		false,	new Rol());
		this.register("^.<<",		false,	new Rcl());
		this.register("1+",			false,	new Inc());
		this.register("1-",			false,	new Dec());
		this.register("2*",			false,	new Mul2());
		this.register("2/",			false,	new Div2());
		this.register("abs",		false,	new Abs());
		this.register("and",		false,	new And());
		this.register("drop",		false,	new Drop());
		this.register("dup",		false,	new Dup());
		this.register("exit",		true,	new Exit());
		this.register("if",			false,	new If());
		this.register("mod",		false,	new Mod());
		this.register("negate",		false,	new Negate());
		this.register("nip",		false,	new Nip());
		this.register("not",		false,	new Not());
		this.register("ones",		false,	new Ones());
		this.register("or",			false,	new Or());
		this.register("over",		false,	new Over());
		this.register("swap",		false,	new Swap());
		this.register("tuck",		false,	new Tuck());
		this.register("under",		false,	new Under());
		this.register("xor",		false,	new Xor());
		this.register("zero",		false,	new Zero());

		this.register("Bit|#0",			false,	new BitCount0());
		this.register("Bit|#1",			false,	new BitCount1());
		this.register("Bit|reverse",	false,	new BitReverse());

		this.register("Local|@",		false,	new LocalFetch());

		
	}

}
