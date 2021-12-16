package com.tss.service;

import java.io.File;
import java.util.List;

import com.tss.encryption.EncryptDecryptUtil;
import com.tss.util.GnUtil;

public class LicenseGenerator {
	
//	public static void getLicenses(String template, String filePath, int from, int to, String client, boolean isEncrypted)
//			throws Exception {
		public static void getLicenses(GnMap reqData) throws Exception {
		String enc = "";
		int from = reqData.getI("from");
		int to = reqData.getI("to");
		String client = reqData.getS("client");
		String filePath = reqData.getS("destination");
		boolean isEncrypted = reqData.getB("isEncrypted");
		if( from > to) throw new Exception("From is > to");
		from = (int) ((from < 0)? GnUtil.getDefaults(from) : from);
		to = (int) ((to < 0)? GnUtil.getDefaults(to) : to);
		// if true it generates client in Lower case else upper case
		client = GnUtil.getStringCases(client, true);
		filePath = GnUtil.getDirectory(filePath, client, false, from, to); // if false gets folder with time else gets sample1to100
		enc = isEncrypted ? GnUtil.getDirectory(filePath, "encryptedFiles") : "";
		String templateData = GnUtil.getJsonFromFile(reqData.getS("template"));
		String data = templateData;
			data = (GnUtil.isBlank(reqData.getS("host"))) ? data.replaceAll("#\\?.*?#\\?", "").replaceAll("(?m)^[ \t]*\r?\n", "") : data.replaceAll("#\\?", ""); 
			data = GnUtil.isBlank(reqData.getS("host")) ? data : data.replace("$host$", reqData.getS("host")).replace("$port$", reqData.getS("port")).replace("$pwd$", reqData.getS("password"));
		
		for (int i = from; i <= to; i++) {
			String fileName = (i < 10 && i != 0 && from != to) ? (client + "0" + i + ".zc_lic") : (client + (i<=0 || from == to ?"":i) + ".zc_lic");
			File file = new File(filePath + "\\" + fileName);
			file.createNewFile();
//			String json = Utility.getJsonFromFile(reqData.getS("template")).replace("c$no$", client + (i<=0 || from == to ?"":i));
			String json = data.replace("c$no$", client + (i<=0 || from == to ?"":i));
			GnUtil.fileWriter(json, file);
			if(isEncrypted) {
				File encryptPath = new File(enc + "\\" + fileName);
				encryptPath.createNewFile();
				String s = GnUtil.mapToJson(EncryptDecryptUtil.encrypt(json));
				GnUtil.fileWriter(s, encryptPath);
			}
			System.out.println(fileName);
		}
		System.out.println(filePath);
	}
		
		public static void getLicenses(GnMap reqData, List<String> clients) throws Exception {
			String enc = "";
			int from = 0;
			int to = clients.size()-1;
			String client = reqData.getS("client");
			String filePath = reqData.getS("destination");
			boolean isEncrypted = reqData.getB("isEncrypted");
			if( from > to) throw new Exception("From is > to");
			from = (int) ((from < 0)? GnUtil.getDefaults(from) : from);
			to = (int) ((to < 0)? GnUtil.getDefaults(to) : to);
			// if true it generates client in Lower case else upper case
			client = GnUtil.getStringCases(client, true);
			filePath = GnUtil.getDirectory(filePath, client, false, from, to); // if false gets folder with time else gets sample1to100
			enc = isEncrypted ? GnUtil.getDirectory(filePath, "encryptedFiles") : "";
			for (int i = from; i <= to; i++) {
				String files = clients.get(i).toLowerCase();
				String fileNames = files+".zc_lic";
				fileNames=fileNames.replaceAll("\\s", "");
				File file = new File(filePath + "\\" + fileNames);
				file.createNewFile();
				String json = GnUtil.getJsonFromFile(reqData.getS("template")).replace("c$no$", files.replaceAll("\\s", ""));
				GnUtil.fileWriter(json, file);
				if(isEncrypted) {
					File encryptPath = new File(enc + "\\" + fileNames);
					encryptPath.createNewFile();
					String s = GnUtil.mapToJson(EncryptDecryptUtil.encrypt(json));
					GnUtil.fileWriter(s, encryptPath);
				}
				System.out.println(fileNames);
			}
			System.out.println(filePath);
		}
}
