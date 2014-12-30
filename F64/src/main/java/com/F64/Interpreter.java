package com.F64;

import java.io.IOException;

public class Interpreter {
	private	boolean		compiling;
	private byte[]		buffer;
	private int			buffer_pos;
	private long		number;
	private	System		system;
	private	Processor	processor;
	private	Compiler	compiler;
	private	Dictionary	dictionary;
	private	Dictionary	context;
	
	public final int BUFFER_SIZE = 1024;
	
	public Interpreter(System system, Processor processor, Compiler compiler, Dictionary dictionary)
	{
		buffer = new byte[BUFFER_SIZE];
		this.system = system;
		this.processor = processor;
		this.compiler = compiler;
		this.dictionary = dictionary;
		this.context = dictionary;
	}

	public boolean processNumber(int offset, int base)
	{
		String txt = new String(buffer, offset, buffer_pos, java.nio.charset.StandardCharsets.UTF_8);
		try {
			number = Long.parseLong(txt, base);
			return true;
		}
		catch (NumberFormatException ex) {
		}
		return false;
	}

	public Word lookup(String name)
	{
		String[] name_list = Dictionary.splitName(name);
		Dictionary current = this.context;
		while (current != null) {
			Word w = current.lookup(name_list, this.compiling);
			if (w != null) {return w;}
			current = current.getParent();
		}
		return null;
	}

	public void processWord()
	{
		if (buffer_pos > 0) {
			if (buffer[0] == '$') {
				// try hex number
				if (processNumber(1, 16)) {
					this.processor.pushT(number);
					return;
				}
			}
			else if (buffer[0] == '#') {
				// try decimal number
				if (processNumber(1, 10)) {
					this.processor.pushT(number);
					return;
				}
			}
			else if (buffer[0] == '%') {
				// try binary number
				if (processNumber(1, 10)) {
					this.processor.pushT(number);
					return;
				}
			}
			String name = new String(buffer, 0, buffer_pos, java.nio.charset.StandardCharsets.UTF_8);
			Word word = this.lookup(name);
			if (word == null) {
				if (!processNumber(0, 10)) {
					java.lang.System.err.println("Could not find word \""+name+"\"");
					return;
				}
				this.processor.pushT(number);
			}
			else {
				if (compiling) {
					word.compile(compiler);
				}
				else {
					word.execute(processor);
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
