package com.tss.service;

import com.tss.util.Utility;

public class TestMain {
	public static void main(String[] args) {
		String fileName = "zc_live_base.sql";
		String dumpFile = "";
		boolean isAdmin = fileName.contains("admin");
		fileName = Utility.isBlank(dumpFile) ? isAdmin ? "zc_base_admin" : "zc_base" : dumpFile;
		System.out.println(fileName);
		System.out.println("Done");
	}

}
