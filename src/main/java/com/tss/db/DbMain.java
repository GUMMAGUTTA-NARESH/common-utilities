/**
 * 
 */
package com.tss.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author NARESH GUMMAGUTTA
 *@since 4 Oct, 2021
 */
public class DbMain {
	
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_naarm";
	static final String PATH = "G:\\InputFiles\\Resultset";
	public static void main(String[] args) throws Exception {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
		String s = "SELECT zci.name as 'NAME OF ORGANIZATION',rad.data\r\n" + 
				"FROM `zc_naarm`.`wos_research_areas_data` rad\r\n" + 
				"  JOIN `zc_naarm`.`zcn_institution` zci ON   zci.pk_id = rad.institution\r\n" + 
				"  JOIN `zc_naarm`.`wos_research_areas` ra ON  ra.pk_id =rad.wos_research_areas GROUP BY ra.name;";
		
		String ss = "SELECT ra.name,rad.data\r\n" + 
				"FROM `zc_naarm`.`wos_research_areas_data` rad\r\n" + 
				"  JOIN `zc_naarm`.`zcn_institution` zci ON   zci.pk_id = rad.institution\r\n" + 
				"  JOIN `zc_naarm`.`wos_research_areas` ra ON  ra.pk_id =rad.wos_research_areas \r\n" + 
				"  GROUP BY ra.name;\r\n";
		
		
		String rs = " select data from `zc_naarm`.`wos_research_areas_data` where  temp_institution = 'ICAR- SUGARCANE BREEDING INSTITUTE';\r\n" + 
				"";
//		DbUtils.getResultsetToExcel(connection, "SELECT * FROM zc_naarm.wos_research_areas;", "sample.xlsx","SELECT name FROM zc_naarm.wos_research_areas;");
	
//		List<Map<String, Object>> test = DbUtils.getMapList(connection, rs);
//		List<Object> columns = new LinkedList<Object>();
//		for(Map<String, Object> m : test) {
//			m.forEach((key, value) -> columns.add(value.toString()));
////			columns = m.values().stream().collect(Collectors.toList());
//
//		}
//		System.out.println(columns);
//		DbUtils.getResultsetToExcelForNaarm(connection, rs, "test2.xlsx","SELECT name FROM zc_naarm.wos_research_areas;");
//		DbUtils.insertDataFromDbToExcel(PATH+"\\"+"ac.xlsx", connection, rs, "SELECT name FROM zc_naarm.wos_research_areas;");
//		DbUtils.insertDataFromDbToExcelForNaarm(PATH+"\\"+"sample13.xlsx", connection, rs);
		DbUtils.insertDataFromDbToExcelForNaarmIt(PATH+"\\"+"sample26.xlsx", connection, rs);
		System.out.println("done");//""
		DbUtils.getCloseConnection(connection);
		
	}
	
	

}
