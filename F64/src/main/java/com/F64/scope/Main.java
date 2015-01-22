package com.F64.scope;

import com.F64.Block;
import com.F64.Word;

public class Main extends Block {
	private Word	word;

	public Main(Word w)
	{
		super(null);
		word = w;
	}

	public Word getWord() {return word;}
	
}
