/**
 * 
 */
package com.tss.db;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.tss.util.GnUtil;

/**
 * @author NARESH GUMMAGUTTA
 *@since 5 Oct, 2021
 */
public class TestNaarm {
	
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_naarm";
	static final String PATH = "G:\\InputFiles\\Resultset";

	@SuppressWarnings({ "unused" })
	public static void main(String[] args) throws Throwable {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
		String[] name = {"WoS ICAR updated 2000-19 Corrected.xlsx","WoS SAU updated 2000-19 Corrected.xlsx"};
		for(String file_name : name) {
			System.out.println(file_name);
		insertData(connection,null,"D:\\achyuth\\threshold_works\\naarm\\"+file_name,"Journals");
		}
		DbUtils.getCloseConnection(connection);
	}
	
	public static boolean insertData(Connection con,String query,String sheet,String sheet_name) throws Throwable {
		List<Object> list = new ArrayList<Object>();
		File file = new File(sheet); // creating a new file
		FileInputStream fis = new FileInputStream(file);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFSheet sheet1 = workbook.getSheet(sheet_name);
		Iterator<Row> iterator = sheet1.iterator();
		Row row = (Row) iterator.next();
		int order = 0;
		int numOfRecs = 0;
		int rowCount = 2;
		List<String> s = new LinkedList<>();
		//List<String> r = new LinkedList<>();
		while (iterator.hasNext()) {
			Cell cell = row.getCell(5); // Get the Cell at the Index / Column you want.
			if (cell.getStringCellValue() != null) {
				// System.out.println(cell.getRow().getLastCellNum());
				for (int i = 4; i <= cell.getRow().getLastCellNum() - 1; i++) {
//                    System.out.println(cell.getRow().getCell(i));
					if (checkNull(cell.getRow().getCell(i)) != null) {
						s.add(checkNull(cell.getRow().getCell(i)));
					}
				}
				System.out.println(s);
			}
			break;
		}
		
		while (iterator.hasNext()) {
//        	Cell cell = row.getCell(1);
			int v = 3;
			int k = 4;
			int o =0;
			row = (Row) iterator.next();
//        	System.out.println(cell.getRow().getLastCellNum());
			for (int i = 1; i <= s.size(); i++) {
				if (checkNull(row.getCell(k)) != null) {
					numOfRecs = DbUtils.getGeneratedKey(con,
							"INSERT INTO `zc_naarm`.`wos_journal_data` (`data`,`temp_institution`, `temp_journal`) VALUES (?,?,?)",
							GnUtil.removeSpaces(row.getCell(k).toString()), GnUtil.removeSpaces(row.getCell(v).toString()), GnUtil.removeSpaces(s.get(order)));
//				System.out.println(row.getCell(k).toString()+row.getCell(v).toString()+s.get(order));
				}
				k++;
				order++;
			}
			v++;
			order = 0;
		}
		System.out.println("done");
		return true;
	}

	public static String checkNull(Object o) {
		return GnUtil.isBlank(o) ? null : GnUtil.removeSpaces(o.toString()) ;
	}
}
