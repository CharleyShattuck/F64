package com.F64;

import java.io.IOException;

public class Compiler {
	private byte[]		buffer;
	private int			buffer_pos;
	private	System		target;
	private	Dictionary	dictionary;
	
	public final int BUFFER_SIZE = 1024;
	
	public Compiler()
	{
		buffer = new byte[BUFFER_SIZE];
	}

	void setEnvironment(System target, Dictionary dictionary)
	{
		this.target = target;
		this.dictionary = dictionary;
	}

	public boolean processNumber(int offset, int base)
	{
		return false;
	}

	public void processWord()
	{
		if (buffer_pos > 0) {
			if (buffer[0] == '$') {
				// try hex number
			}
			else if (buffer[0] == '#') {
				// try decimal number
			}
			else if (buffer[0] == '%') {
				// try binary number
			}
			String name = new String(buffer, 0, buffer_pos, java.nio.charset.StandardCharsets.UTF_8);
			Word word = this.dictionary.lookup(name.split("|"));
			if (word == null) {
				if (!processNumber(0, 10)) {
					java.lang.System.err.println("Could not find word \""+name+"\"");
				}
			}
			else {
				if (target.getCompiling()) {
					word.compile();
				}
				else {
					word.execute();
				}
			}
		}
	}
	
	public boolean readWord(java.io.InputStream stream)
	{
		try {
			int data = stream.read();
			if (data >= 0) {
				buffer_pos = 0;
				while (data <= 0x20) {
					data = stream.read();
					if (data < 0) {break;}
				}
				if (data >= 0) {
					buffer[buffer_pos++] = (byte)data;
					data = stream.read();
					while (data > 0x20) {
						buffer[buffer_pos++] = (byte)data;
						data = stream.read();
						if (data < 0) {break;}
					}
					return true;
				}
			}
		}
		catch (IOException ex) {
			
		}
		return false;
	}
	
	public void interpret(java.io.InputStream stream) throws IOException
	{
		while (readWord(stream)) {
			processWord();
		}
	}

}
