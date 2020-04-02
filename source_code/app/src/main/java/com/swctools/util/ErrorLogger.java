package com.swctools.util;

public class ErrorLogger {
	private static final String ERROROUTPUT = "";
	public static void LogError(String exceptionStr, String module, String method, String customMessage){
		//I'll do this at some point....
		String errorReport = exceptionStr + "|" + module + "|" + method + "|" + customMessage;
	}
	
	
}
