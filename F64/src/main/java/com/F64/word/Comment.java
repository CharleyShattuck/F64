package com.F64.word;

import com.F64.Interpreter;

public class Comment extends com.F64.Word {

	@Override
	public void execute(Interpreter i)
	{
		int level = 1;
		String w = i.getNextWord();
		while (!w.isEmpty()) {
			if (w == "\\") {i.skipLine();}
			if (w == "(") {++level;}
			if (w == ")") {if (--level == 0) {break;}}
			w = i.getNextWord();
		}
	}

	@Override
	public void compile(Interpreter i)
	{
		int level = 1;
		String w = i.getNextWord();
		while (!w.isEmpty()) {
			if (w == "\\") {i.skipLine();}
			if (w == "(") {++level;}
			if (w == ")") {if (--level == 0) {break;}}
			w = i.getNextWord();
		}
	}


}
