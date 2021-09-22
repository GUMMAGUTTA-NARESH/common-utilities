/**
 * 
 */
package com.tss.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import com.tss.files.FileOperations;

/**
 * @author NARESH GUMMAGUTTA
 * @since 17 Aug, 2021
 */
public class TestAllMains {
	
	public static void main(String[] args) throws ParseException, IOException {
		Map<String, Object> files = FileOperations.search("G:\\InputFiles", "txt", true);
		System.out.println(files);
	}
}
