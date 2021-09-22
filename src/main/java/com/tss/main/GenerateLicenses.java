package com.tss.main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.tss.encryption.EncryptDecryptUtil;
import com.tss.files.FileOperations;
import com.tss.service.GnMap;
import com.tss.service.LicenseGenerator;
import com.tss.service.Rsa;
import com.tss.util.Utility;

public class GenerateLicenses {
	public static final String TEMPLATE = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\template.json";
	public static final String LOCALHOST_TEMPLATE = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\localTemplate.json";
	public static final String DESTINATION = "G:\\InputFiles\\Licenses";
	public static final String TEMPLATE_INFO = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\template.json";
	/**
	 * @author GUMMAGUTTA NARESH
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		GnMap res = new GnMap();
		res.put("template", LOCALHOST_TEMPLATE);
		res.put("destination", DESTINATION);
		res.put("from", 1); //start client number
		res.put("to", 1); // end client number
		res.put("client", "naarm"); //client name
		res.put("isEncrypted", true); // if true encrypted licenses also created along with normal files
		res.put("host", "");
		res.put("port", "3406");
		res.put("password", "The@1234");
		
		List<String> clients = Utility.readFileIntoList("C:\\Users\\G NARESH\\Desktop\\clients.txt");
		LicenseGenerator.getLicenses(res);
		
//		LicenseGenerator.getLicenses(LOCALHOST_TEMPLATE, DESTINATION, 1, 15, "abc",true);
//		System.out.println(Utility.isExcel("acb.csv"));
//		System.out.println(Utility.dbColumn("NU__LL"));
//		System.out.println(Utility.getSheets("H:\\InputFiles\\Test.xlsx"));
//		System.out.println(Utility.getColumns("H:\\InputFiles\\Test.xlsx"));
//		Object o =  Utility.dbColumn(Utility.getColumns("H:\\InputFiles\\Test.xlsx")[2]);
//		String a = Arrays.deepToString(Utility.getConvertedColumns(Utility.getColumns("H:\\InputFiles\\Test.xlsx"))));
//		System.out.println(DbUtils.generateSelectQuery("test",Utility.getConvertedColumns(Utility.getConvertedColumns(Utility.getColumns("H:\\InputFiles\\Test.xlsx")))[1]));
//		System.out.println(Utility.generateInsertQuery(null, "Test", Utility.getConvertedColumns(Utility.getColumns("H:\\InputFiles\\Test.xlsx"))));
//		System.out.println("**********");
//		System.out.println(DbUtils.generateSelectQuery("test",Utility.getConvertedColumns(Utility.getConvertedColumns(Utility.getColumns("H:\\InputFiles\\Test.xlsx")))[1]));
//		Connection connection = DbUtils.getConnection("localhost", 3306, "threshold", "admin", "12345");
//		DbExcelUtils.insert("H:\\InputFiles\\Test1.xlsx", connection,"Test", true);
//		DbUtils.getCloseConnection(connection);
//		System.out.println(Utility.createTable("H:\\InputFiles\\Test.xlsx", null));
//		System.out.println(DbExcelUtils.createTable("test", "Name", "To Date", "Is DeletED"));
//		DbExcelUtils.getDatatypes("H:\\InputFiles\\Test.xlsx");
//		System.out.println(DbExcelUtils.getSheets("H:\\InputFiles\\BRDVillageList.xlsx"));
//		System.out.println(DbExcelUtils.queryBuild("H:\\InputFiles\\Test1.xlsx",true));
//		System.out.println(Profile.getMyName());
//		System.out.println(Profile.getMyIp());
//		System.out.println(Profile.getMyPhone());
		String path = "C:\\Users\\G NARESH\\Downloads";
		String source = "E:\\All Downloads\\test";
		String sourceExe = "E:\\All Downloads\\extensions";
//		System.out.println(FileOperations.getFiles(path));
		Map<String, Object> map =  FileOperations.getCreationTime(path,false);
//		System.out.println(FileOperations.createFolders(sourceExe, path, true));
//		System.out.println(FileOperations.moveFiles(path, source, false));
//		System.out.println(FileOperations.transferFiles(path, sourceExe, false));
		String s = "G:\\InputFiles\\Test.xlsx";
		System.out.println(Utility.getFileInfo(s, false));
		System.out.println(Utility.getFileInfo(s, true));
		System.out.println(Utility.convertMapToSet(map, false));
		System.out.println(FileOperations.getFileExtensions(path));
		System.out.println(FileOperations.getCreationTime(path,false));
		System.out.println(FileOperations.getExtensionsByTime(path));
		System.out.println(Utility.isValidPanCard("AYRPN7272Q"));
		System.out.println(Utility.isValidPassport("Test13"));
		Rsa rsa = new Rsa();
		byte[] enc = rsa.encrypt("GUMMAGUTTA NARESH".getBytes());
		System.out.println(Utility.bytesToString(enc));
		
		byte[] dec = rsa.decrypt(enc);
		System.out.println(new String(dec));
		System.out.println(Utility.bytesToString(dec));
		System.out.println(Utility.removeSpecialChars(Utility.bytesToString(enc)));
		}
	}

