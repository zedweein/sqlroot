package com.awn.sqlroot.exception;

public class XmlNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public XmlNotFoundException() {
		super();
	}

	public XmlNotFoundException(String message) {
		super(message);
	}
}
