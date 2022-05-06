package com.gn.main;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gn.encryption.EncryptDecryptUtil;
import com.gn.files.FileOperations;
import com.gn.service.GnMap;
import com.gn.service.LicenseGenerator;
import com.gn.service.Rsa;
import com.gn.util.GnUtil;

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
//		Map<String, Object> res = new LinkedHashMap<String, Object>();
		GnMap res = new GnMap();
		res.put("template", LOCALHOST_TEMPLATE);
		res.put("destination", DESTINATION);
		res.put("from", 1); //start client number
		res.put("to", 1); // end client number
		res.put("client", "theme"); //client name
		res.put("isEncrypted", true); // if true encrypted licenses also created along with normal files
//		res.put("host", "65.2.156.162"); v3.5
		res.put("host", "");
		res.put("port", "3406");
		res.put("password", "12345");
//		res.put("password", "WmNATXlzcUwyMDIw"); v3.5
		System.out.println(GnUtil.toGnMap(res));
		List<String> clients = GnUtil.readFileIntoList("C:\\Users\\G NARESH\\Desktop\\clients.txt");
		LicenseGenerator.getLicenses(res,clients);
//		System.out.println(EncryptDecryptUtil.decrypt(null));
		
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
		System.out.println(GnUtil.getFileInfo(s, false));
		System.out.println(GnUtil.getFileInfo(s, true));
		System.out.println(GnUtil.convertMapToSet(map, false));
		System.out.println(FileOperations.getFileExtensions(path));
		System.out.println(FileOperations.getCreationTime(path,false));
		System.out.println(FileOperations.getExtensionsByTime(path));
		System.out.println(GnUtil.isValidPanCard("AYRPN7272Q"));
		System.out.println(GnUtil.isValidPassport("Test13"));
		Rsa rsa = new Rsa();
		byte[] enc = rsa.encrypt("GUMMAGUTTA NARESH".getBytes());
		System.out.println(GnUtil.bytesToString(enc));
		
		byte[] dec = rsa.decrypt(enc);
		System.out.println(new String(dec));
		System.out.println(GnUtil.bytesToString(dec));
		System.out.println(GnUtil.removeSpecialChars(GnUtil.bytesToString(enc)));
		}
	}

