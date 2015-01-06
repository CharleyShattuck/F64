package com.F64.codepoint;

import com.F64.Compiler;
<<<<<<< HEAD
import com.F64.Optimization;

public class Negate extends com.F64.Codepoint {

	@Override
	public boolean optimize(Optimization opt)
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if (p instanceof Negate) {
				// 2 negates do nothing
				this.getOwner().remove(p);
				this.getOwner().remove(this);
=======

public class Negate extends com.F64.Codepoint {

	@Override
	public boolean optimize()
	{
		if (this.getPrevious() == null) {return false;}
		com.F64.Codepoint p = this.getPrevious();
		if (p != null) {
			if (p instanceof Negate) {
				// 2 negates do nothing
				this.getScope().remove(p);
				this.getScope().remove(this);
>>>>>>> refs/remotes/origin/master
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void generate(Compiler c)
	{

	}

}
