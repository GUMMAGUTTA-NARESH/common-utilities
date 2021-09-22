package com.tss.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Profile {
	public static String getMyName() throws UnknownHostException {
		String name = System.getProperty("user.name");
		return (name.equalsIgnoreCase("G NARESH"))? "GUMMAGUTTA NARESH"  :name;
	}
	public static String getMyIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}
	
	public static String getMyPhone() throws UnknownHostException {
		return (getMyName().equalsIgnoreCase("GUMMAGUTTA NARESH")? "8639223068": "Phone doesn't exits");
	}
	
	public static String getOs() {
		return System.getProperty("os.name");
	}
	
	
}
