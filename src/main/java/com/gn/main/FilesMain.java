/**
 * 
 */
package com.gn.main;

import java.io.IOException;
import java.util.List;

import com.gn.util.GnUtil;

/**
 * @author NARESH GUMMAGUTTA
 *@since 6 Jul, 2021
 */
public class FilesMain {
	public static void main(String[] args) throws IOException {
//		System.out.println(Utility.validateAadharNumber("545283344448"));
		long start = System.currentTimeMillis();
		String s = GnUtil.generateAadhar();
		System.out.println(s);
		List<String> list = GnUtil.generateAadhar(3);
		System.out.println(list);
		long end = System.currentTimeMillis();
		System.out.println("Total Time Taken: "+(end-start)/1000+" seconds");
	}
}
