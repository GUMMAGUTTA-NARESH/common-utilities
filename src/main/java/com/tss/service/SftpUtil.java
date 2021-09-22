/**
 * 
 */
package com.tss.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author NARESH GUMMAGUTTA
 *@since 19 Jul, 2021
 */
public class SftpUtil {
	static Session session;
	static String separator = "/";
	public static void getConnection(String user, String pass, String host) {
		JSch jSch = new JSch();
        
		try {
			session = jSch.getSession(user,host);
			 session.setPassword(pass);
			 Properties config = new Properties();
	            config.put("StrictHostKeyChecking","no");
		        session.setConfig(config);
		        session.connect();
		} catch (JSchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getCloseConnection() {
		 session.disconnect();
	}
	
	public static Map<String, Object> saveUpdateThemeResources(Map<String, Object> reqData, String basePath, boolean isUpdate) {
		Map<String, Object> res = new LinkedHashMap<String, Object>();
    //TODO: Theme Nginx path need to work on the above path    
//		String basePath = "/home/demo-ftp" + separator +"theme"+ separator+ reqData.get("themeCode");		
		if(!isExists(basePath,null)) System.out.println("Error");;
		if (res.isEmpty()) {
			String resourcePath = basePath;
			String path = reqData.get("path").toString();
			 resourcePath = basePath + separator + path;
			File baseDir = new File(resourcePath);
			if ( !isUpdate) baseDir.mkdirs();
			if (isExists(null, baseDir)) {
				File file = new File(resourcePath + separator + reqData.get("file"));
				if(!isUpdate) {
					if (!file.exists())fileWriter(reqData.get("content").toString(), file);
					else System.out.println("Exits");
				} else {
					if (file.exists()) fileWriter(reqData.get("content").toString(), file);
					else System.out.println("Resource doesn't exists");
				}
			} else System.out.println("Resource path doesn't exists");
		}  
		return res; 
	}
	public static void fileWriter(String s, File file) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(s);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isExists(String path, File file) {
		if(path != null) { 
			File f = new File(path);
			return (f.exists() || f.isDirectory()) ? true : false;
		}else return (file.exists() || file.isDirectory()) ? true : false;
	} 

}
