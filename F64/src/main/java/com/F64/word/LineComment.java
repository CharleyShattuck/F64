package com.F64.word;

import com.F64.Interpreter;

public class LineComment extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		i.skipLine();
	}

	@Override
	public void compile(Interpreter i)
	{
		i.skipLine();
	}


}
