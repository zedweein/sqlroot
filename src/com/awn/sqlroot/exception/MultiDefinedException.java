package com.awn.sqlroot.exception;

public class MultiDefinedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public MultiDefinedException() {
		super();
	}

	public MultiDefinedException(String message) {
		super(message);
	}
}
