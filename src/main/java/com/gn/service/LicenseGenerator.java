package com.gn.service;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gn.encryption.EncryptDecryptUtil;
import com.gn.util.GnUtil;

public class LicenseGenerator {
	
	public LicenseGenerator() {
		
	}
	
	public LicenseGenerator(GnMap map) {
		
	}
	public static void getLicenses(Map<String, Object> reqData) throws Exception {
//		getLicenses(GnUtil.toGnMap(reqData));
		GnMap res = new GnMap();
		res.putAll(reqData);
		System.out.println(res);
		getLicenses(res);
	}
	public static void main(String[] args) throws Exception {
		Map<String, Object> res = new LinkedHashMap<String, Object>();
		res.put("template", null);
		res.put("destination", null);
		res.put("from", 1); //start client number
		res.put("to", 1); // end client number
		res.put("client", "theme"); //client name
		res.put("isEncrypted", true); // if true encrypted licenses also created along with normal files
		res.put("host", "");
		res.put("port", "3406");
		res.put("password", "12345");
		getLicenses(res);
	}
	
	
//	public static void getLicenses(String template, String filePath, int from, int to, String client, boolean isEncrypted)
//			throws Exception {
		public static void getLicenses(GnMap reqData) throws Exception {
		String enc = "";
		int from = reqData.getI("from");
		int to = reqData.getI("to");
		String client = GnUtil.getClientNameFormat(reqData.getS("client"));
		String filePath = reqData.isBlank("destination") ? System.getProperty("java.io.tmpdir") :reqData.getS("destination");
		boolean isEncrypted = reqData.getB("isEncrypted");
		if( from > to) throw new Exception("From is > to");
		from = (int) ((from < 0)? GnUtil.getDefaults(from) : from);
		to = (int) ((to < 0)? GnUtil.getDefaults(to) : to);
		// if true it generates client in Lower case else upper case
		client = GnUtil.getStringCases(client, true);
		if(reqData.getB("isSingleFolder")){
			filePath = GnUtil.getDirectory(filePath, client, true, from, to, "DefaultFolder");
		}else {
			filePath = GnUtil.getDirectory(filePath, client, false, from, to,null); // if false gets folder with time else gets sample1to100
		}
		enc = isEncrypted ? GnUtil.getDirectory(filePath, "encryptedFiles") : "";
		String templateData = GnUtil.getJsonFromFile(reqData.isBlank("template") ? Constants.LOCALHOST_TEMPLATE:reqData.getS("template"));
		String data = templateData;
			data = (GnUtil.isBlank(reqData.getS("host"))) ? data.replaceAll("#\\?.*?#\\?", "").replaceAll("(?m)^[ \t]*\r?\n", "") : data.replaceAll("#\\?", ""); 
			data = GnUtil.isBlank(reqData.getS("host")) ? data : data.replace("$host$", reqData.getS("host")).replace("$port$", reqData.getS("port")).replace("$pwd$", reqData.getS("password"));
		for (int i = from; i <= to; i++) {
			String fileName = (i < 10 && i != 0 && from != to) ? (client + "0" + i + ".zc_lic") : (client + (i<=0 || from == to ?"":i) + ".zc_lic");
			File file = new File(filePath + File.separator + fileName);
			file.createNewFile();
//			String json = Utility.getJsonFromFile(reqData.getS("template")).replace("c$no$", client + (i<=0 || from == to ?"":i));
			String json = data.replace("c$no$", client + (i<=0 || from == to ?"":i));
			GnUtil.fileWriter(json, file);
			if(isEncrypted) {
				File encryptPath = new File(enc + File.separator + fileName);
				encryptPath.createNewFile();
				String s = GnUtil.mapToJson(EncryptDecryptUtil.encrypt(json));
				GnUtil.fileWriter(s, encryptPath);
			}
			System.out.println(fileName);
		}
		System.out.println(filePath);
	}
		
		public static void getLicenses(GnMap reqData, List<String> clients) throws Exception {
			if(clients == null) {
				getLicenses(reqData);
			}
			reqData.put("isSingleFolder", true);
			for(String s: clients) {
				reqData.put("client", s);
				getLicenses(reqData);
			}
		}
		
		public static String getResource(String file) {
			try {
				return Paths.get(ClassLoader.getSystemClassLoader().getResource(file).toURI()).toString();
			} catch (URISyntaxException e) {
				return null;
			}
		}
		
}
