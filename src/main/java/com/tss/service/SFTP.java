/**
 * 
 */
package com.tss.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.tss.sftp.UploadDownload;

/**
 * @author NARESH GUMMAGUTTA
 *@since 19 Jul, 2021
 */
public class SFTP {
	private static void moveFile(String src, String target, boolean isReplace, boolean override) {
		try {
			if(isReplace && override)  Files.move(new File(src).toPath(), new File(target).toPath(),StandardCopyOption.REPLACE_EXISTING);
			else if ((!isReplace && !override) || (!isReplace && override))  Files.move(new File(src).toPath(), new File(target).toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void move(ChannelSftp sftp, String from, String to) throws SftpException {
		sftp.put( from,to);
		new File(from).delete();
	}
	
	public static void main2(String[] args) throws FileNotFoundException {
		
		
		String des = "/home/demo-ftp/theme/brd/";
		System.out.println(UploadDownload.getF("C:\\home\\demo-ftp\\theme\\brd\\ftp.txt"));
		UploadDownload.upload("C:\\home\\demo-ftp\\theme\\brd\\ftp.txt", des+"test.txt");
		System.out.println("Done");
        try {
            String user = "demo-ftp";
            String pass = "The@1234";
            Properties config = new Properties();
            config.put("StrictHostKeyChecking","no");
            String host = "65.2.156.162";
            Map<String, Object> map = new LinkedHashMap<String, Object>(){{
            	put("themeCode", "brd");
            	put("path", "");
            	put("file", "ftp3.txt");
            	put("content", "Welcome to File Transfer Protocal");
            }};
//            SftpUtil.getConnection(user, pass, host);
           String base =  "/home/demo-ftp" + "/" +"theme"+ "/"+ map.get("themeCode");
           System.out.println(base+"/"+map.get("file"));
            System.out.println("Sucess");
            JSch jSch = new JSch();
            Session session = jSch.getSession(user,host);
            session.setPassword(pass);
            session.setConfig(config);
            System.out.println("Session connected: "+session.isConnected());
            session.connect();
            System.out.println(SftpUtil.saveUpdateThemeResources(map,base, false));
           
            System.out.println("Session connected: "+session.isConnected());
            ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            System.out.println("+++++++++++++++++++++++++++++++++");
            String path = "home/demo-ftp/theme/tss-zc-resource-path/zc-admin/temp";
    		String[] folders = path.split("/");
    		for ( String folder : folders ) {
    		    if ( folder.length() > 0 ) {
    		        try {
    		        	channelSftp.cd(folder);
    		        	System.out.println(folder);
    		        }
    		        catch ( SftpException e ) {
    		        	channelSftp.mkdir( folder );
    		        	channelSftp.cd( folder );
    		        }
    		    }
    		}
            System.out.println("Put Called");
//            moveFile("G:\\NginxPath\\theme\\brd\\css\\styles1.css", "/home/demo-ftp/theme/brd/styles1.css", false, false);
            move(channelSftp, "C:\\home\\demo-ftp\\theme\\brd"+"\\"+map.get("file"),"/home/demo-ftp/theme/brd/"+"/"+map.get("file"));
            System.out.println("Done");
//            channelSftp.put( "G:\\NginxPath\\theme\\brd\\css\\styles1.css","/home/demo-ftp/theme/brd/styles1.css");
//            channelSftp.put("G:\\NginxPath\\theme\\brd\\css\\styles.css","/home/demo-ftp/brd/style.css");
            Vector fileList = channelSftp.ls("/home/demo-ftp/theme/brd");
            for (int i = 0; i < fileList.size(); i++) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) fileList.get(i);
                System.out.println(lsEntry.getFilename());
            }
            
//            String existingfile = "abc.jpg";
//            String newfile = "123.jpg";
//            String dir = "/appl/user/home/test/";
//            sftp.cd(dir+"temp/");
//            if (sftp.get( newfile ) != null){
//                sftp.rename(dir + "temp/" + newfile , 
//                    dir + newfile );
//                sftp.cd(dir);
//                sftp.rm(existingfile );
//            }


//channelSftp.rename("inbound/newFile.txt","inbound/newFile2.txt");
                       

//            channelSftp.get("/home/demo-ftp/brd/newFile2.txt","newFile2.txt");
            System.out.println("Session connected: "+session.isConnected());
            channelSftp.disconnect();
            session.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		FileInputStream fis = null;

		try {
		    client.connect("65.2.156.162");
		 boolean sucess =    client.login("demo-ftp", "The@1234");
		 System.out.println(sucess);

		    //
		    // Create an InputStream of the file to be uploaded
		    //
		    String filename = "ftp3.txt";
		    fis = new FileInputStream(filename);

		    //
		    // Store file to server
		    //
		    client.storeFile(filename, fis);
		    System.out.println("Done");
		    client.logout();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        if (fis != null) {
		            fis.close();
		        }
		        client.disconnect();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}

}
