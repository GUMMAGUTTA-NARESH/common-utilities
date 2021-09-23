/**
 * 
 */
package com.tss.main;

import java.io.IOException;
import java.util.List;

import com.tss.util.Utility;

/**
 * @author NARESH GUMMAGUTTA
 *@since 6 Jul, 2021
 */
public class FilesMain {
	public static void main(String[] args) throws IOException {
//		System.out.println(Utility.validateAadharNumber("545283344448"));
		long start = System.currentTimeMillis();
		String s = Utility.generateAadhar();
		List<String> list = Utility.generateAadhar(1000);
		System.out.println(list);
		long end = System.currentTimeMillis();
		System.out.println("Total Time Taken: "+(end-start)/1000+" seconds");
		System.out.println("commit added here for dev");
		System.out.println("commit added here for new branch");
		System.out.println("commit added here for new branch");
	
	}
}
