/**
 * 
 */
package com.tss.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author NARESH GUMMAGUTTA
 *@since 19 Jul, 2021
 */
public class FTP {
	 public static void main(String[] args) {
         
		  FTPClient client = new FTPClient();
		  
		  try {
		 
//		client.connect("65.2.156.162");
//		client.connect("65.2.156.162");
		client.connect("65.2.156.162", 22);
		  
		// Try to login and return the respective boolean value
		boolean login = client.login("demo-ftp", "The@1234");
		  
		// If login is true notify user
		 
		if (login) {
		 
		    System.out.println("Connection established...");
		  
		    // Try to logout and return the respective boolean value
		    boolean logout = client.logout();
		 
		    // If logout is true notify user
		    if (logout) {
		 
		  System.out.println("Connection close...");
		 
		    }
		//  Notify user for failure  
		} else {
		    System.out.println("Connection fail...");
		}
		  
		  } catch (IOException e) {
		 
		e.printStackTrace();
		 
		  } finally {
		 
		try {
		    // close connection
		 
		    client.disconnect();
		 
		} catch (IOException e) {
		 
		    e.printStackTrace();
		 
		}
		 
		  }
		    }

}
