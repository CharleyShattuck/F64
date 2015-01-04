package com.F64;

import java.io.IOException;

import com.F64.codepoint.Literal;

public class Interpreter {
	private java.io.InputStream		stream;
	private	boolean					ignore;
	private	boolean					compiling;
	private byte[]					buffer;
	private int						buffer_pos;
	private int						ignore_level;
	private long					number;
	private	System					system;
	private	Processor				processor;
	private	Compiler				compiler;
	private	Dictionary				dictionary;
	private	Dictionary				context;
	
	public final int BUFFER_SIZE = 1024;
	
	public Interpreter(System system, Processor processor, Compiler compiler, Dictionary dictionary)
	{
		this.buffer = new byte[BUFFER_SIZE];
		this.system = system;
		this.processor = processor;
		this.compiler = compiler;
		this.dictionary = dictionary;
		this.context = dictionary;
	}

	public void setProcessor(Processor p) {processor = p;}
	public Processor getProcessor() {return processor;}
	public Compiler getCompiler() {return compiler;}
	
	public boolean processNumber(int offset, int base)
	{
		String txt = new String(this.buffer, offset, this.buffer_pos, java.nio.charset.StandardCharsets.UTF_8);
		try {
			this.number = Long.parseLong(txt, base);
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
		Word w = current.lookup(name_list, this.compiling, true);
		if (w != null) {return w;}
		while (current != null) {
			w = current.lookup(name_list, this.compiling, false);
			if (w != null) {return w;}
			current = current.getParent();
		}
		return null;
	}

	public void processNumber(long value)
	{
		if (this.compiling) {
			this.compiler.compile(new Literal(value));
		}
		else {
			this.processor.pushT(value);
		}
		
	}

	public void processWord()
	{
		if (this.buffer_pos > 0) {
			if (this.buffer[0] == '$') {
				// try hex number
				if (processNumber(1, 16)) {
					this.processNumber(this.number);
					return;
				}
			}
			else if (this.buffer[0] == '#') {
				// try decimal number
				if (processNumber(1, 10)) {
					this.processNumber(this.number);
					return;
				}
			}
			else if (this.buffer[0] == '%') {
				// try binary number
				if (processNumber(1, 10)) {
					this.processNumber(this.number);
					return;
				}
			}
			String name = new String(this.buffer, 0, this.buffer_pos, java.nio.charset.StandardCharsets.UTF_8);
			Word word = this.lookup(name);
			if (word == null) {
				if (!processNumber(0, 10)) {
					java.lang.System.err.println("Could not find word \""+name+"\"");
					return;
				}
				this.processNumber(this.number);
			}
			else {
				if (this.compiling) {
					word.compile(this);
				}
				else {
					word.execute(this);
				}
			}
		}
	}
	
	public boolean readWord()
	{
		try {
			int data = this.stream.read();
			if (data >= 0) {
				this.buffer_pos = 0;
				while (data <= 0x20) {
					data = this.stream.read();
					if (data < 0) {break;}
				}
				if (data >= 0) {
					this.buffer[this.buffer_pos++] = (byte)data;
					data = this.stream.read();
					while (data > 0x20) {
						this.buffer[this.buffer_pos++] = (byte)data;
						data = this.stream.read();
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
		this.stream = stream;
		while (readWord()) {
			processWord();
		}
	}


}
