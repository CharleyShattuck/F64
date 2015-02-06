package com.F64.word;

import com.F64.Block;
import com.F64.Exception;
import com.F64.Interpreter;
import com.F64.Processor;
import com.F64.Compiler;

public class Locals extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		Processor p = i.getProcessor();
		p.doThrow(Exception.COMPILE_ONLY);
	}

	@Override
	public void compile(Interpreter i)
	{
//		Processor p = i.getProcessor();
		Compiler c = i.getCompiler();
		Block blk = c.getBlock();
		String name = i.getNextWord();
		boolean is_active = true;
		while (!name.equals("}")) {
			if (is_active) {
				if (name.equals("--")) {
					is_active = false;
				}
				else {
					blk.addLocal(c, name);
				}
			}
			name = i.getNextWord();
		}
	}


}
