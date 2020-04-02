package com.swctools.util;

public enum EscapeChars {
	DOUBLE_QUOTES("\""),
	SINGLE_QUOTE("\'"),
	FWDSLASH("/");
	
	
	
	private String nameAsString;
	
	
	private EscapeChars(String nameAsString) {
		this.nameAsString = nameAsString;
		
	}
	
	@Override
	public String toString() {
		return this.nameAsString;
	}	
	
}
