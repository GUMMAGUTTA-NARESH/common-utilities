/**
 * 
 */
package com.tss.sftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * @author NARESH GUMMAGUTTA
 *@since 25 Jul, 2021
 */
public class TestFTP {
	
	public static boolean uploadSingleFile(FTPClient ftpClient, String localFilePath, String remoteFilePath) throws IOException {
	    File localFile = new File(localFilePath);
	 
	    InputStream inputStream = new FileInputStream(localFile);
	    try {
	        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
	        return ftpClient.storeFile(remoteFilePath, inputStream);
	    } finally {
	        inputStream.close();
	    }
	}
	 private static void showServerReply(FTPClient ftpClient) {
	        String[] replies = ftpClient.getReplyStrings();
	        if (replies != null && replies.length > 0) {
	            for (String aReply : replies) {
	                System.out.println("SERVER: " + aReply);
	            }
	        }
	    }
	public static void main(String[] args) throws IOException {
		    FTPClient ftpClient = new FTPClient();
		    ftpClient.connect("65.2.156.162", 21);
		    ftpClient.login("demo-ftp", "The@1234");
		    // lists files and directories in the current working directory
		    String dirToCreate = "/Sample";
		    
          boolean  success = ftpClient.makeDirectory(dirToCreate);
            showServerReply(ftpClient);
            if (success) {
                System.out.println("Successfully created directory: " + dirToCreate);
            } else {
                System.out.println("Failed to create directory. See server's reply.");
            }
            uploadSingleFile(ftpClient, "C:\\tss-zc-resource-path\\zc-admin\\temp\\test.txt", dirToCreate);
		    
		    FTPFile[] files = ftpClient.listFiles();
		    System.out.println(files);
		     
		    // iterates over the files and prints details for each
		    DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		     
		    for (FTPFile file : files) {
		        String details = file.getName();
		        if (file.isDirectory()) {
		            details = "[" + details + "]";
		        }
		        details += "\t\t" + file.getSize();
		        details += "\t\t" + dateFormater.format(file.getTimestamp().getTime());
		        System.out.println(details);
		    }
		     
		    ftpClient.logout();
		    ftpClient.disconnect();	    
	}
}
