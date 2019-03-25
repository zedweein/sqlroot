package com.awn.sqlroot.exception;

public class SqlNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SqlNotFoundException() {
		super();
	}

	public SqlNotFoundException(String message) {
		super(message);
	}
}
