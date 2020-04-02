package com.swctools.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextFileReader {
	final static Charset ENCODING = StandardCharsets.UTF_8;
	

	public static String readLargerTextFile(String aFileName) throws IOException {
	    StringBuilder sb = new StringBuilder();
//		Path path = Paths.get(aFileName);
//		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)){
//	      String line = null;
//	      while ((line = reader.readLine()) != null) {
//	        //process each line in some way
//	    	  sb.append(String.valueOf(line));
////	        log(line);
//	      }
//	    }
	    return sb.toString();
	  }
}
