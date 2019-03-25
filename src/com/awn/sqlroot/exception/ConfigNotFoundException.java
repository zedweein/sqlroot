package com.awn.sqlroot.exception;

public class ConfigNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ConfigNotFoundException() {
		super();
	}

	public ConfigNotFoundException(String message) {
		super(message);
	}
	
}
