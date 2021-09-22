/**
 * 
 */
package com.tss.sftp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;

/**
 * @author NARESH GUMMAGUTTA
 *@since 20 Jul, 2021
 */
public class UploadDownload {
	
	private static DefaultSftpSessionFactory getFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("65.2.156.162");
        factory.setPort(22);
        factory.setAllowUnknownKeys(true);
        factory.setUser("demo-ftp");
        factory.setPassword("The@1234");
        return factory;
    }
	
	public static InputStream getF(String path) throws FileNotFoundException {
//		return UploadDownload.class.getClassLoader().getResourceAsStream("LogTemplate.txt");
		   return  new FileInputStream(new File(path));
	}
	
	  public static void upload(String path, String des){
		  
	        SftpSession session = getFactory().getSession();
	        InputStream resourceAsStream =
	        		UploadDownload.class.getClassLoader().getResourceAsStream("LogTemplate.txt");
	        try {
	            session.write(getF(path), des+".txt");
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        session.close();
	    }

	    public String download(){
	        SftpSession session = getFactory().getSession();
	        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        try {
	            session.read("upload/downloadme.txt", outputStream);
	            return new String(outputStream.toByteArray());
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	    }
}
