/**
 * 
 */
package com.gn.main;

import java.sql.Connection;

import com.gn.db.DbUtils;
import com.gn.db.Script;

/**
 * @author NARESH GUMMAGUTTA
 * @since 1 Aug, 2021
 */
public class ExcelDbUtilsMain {

	public static final String FILE_PATH = "G:\\InputFiles\\BRDVillageList.xlsx";
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_base_admin";

	public static void main(String[] args) throws Exception {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
//		System.out.println(DbExcelUtils.queryBuilder(FILE_PATH, "threshold", true));
//		DbUtils.createTable(connection, FILE_PATH, SCHEMA, true);
//		DbExcelUtils.insert(FILE_PATH, connection, "brdvillagelist", false);
//		String sql = "SHOW TABLES\r\n" + 
//				"FROM zc_base_admin\r\n" + 
//				"WHERE \r\n" + 
//				"    `Tables_in_zc_base_admin` LIKE '%_audit'\r\n" + 
//				"    OR `Tables_in_zc_base_admin` LIKE '%_queue'\r\n" + 
//				"    OR `Tables_in_zc_base_admin` LIKE '%survey%'\r\n" + 
//				"    OR `Tables_in_zc_base_admin` LIKE '%mobile%'\r\n" + 
//				"    OR `Tables_in_zc_base_admin` LIKE '%stage%';\r\n" + 
//				"";
//		String delete = "   SELECT \r\n" + 
//				"    CONCAT('DELETE FROM {{schema}}.',\r\n" + 
//				"            table_name,\r\n" + 
//				"            ' WHERE is_deleted = 1')\r\n" + 
//				"FROM\r\n" + 
//				"    information_schema.tables\r\n" + 
//				"WHERE\r\n" + 
//				"    table_schema = '{{schema}}';";
//		String trucate = "SELECT \r\n" + 
//				"    CONCAT('TRUNCATE TABLE {{schema}}.',\r\n" + 
//				"            table_name)\r\n" + 
//				"FROM\r\n" + 
//				"    INFORMATION_SCHEMA.TABLES\r\n" + 
//				"WHERE\r\n" + 
//				"    table_schema = '{{schema}}' AND (TABLE_NAME LIKE '%audit' OR TABLE_NAME LIKE '%queue'\r\n" + 
//				"    OR TABLE_NAME LIKE '%survey%'\r\n" + 
//				"    OR TABLE_NAME LIKE '%mobile%'\r\n" + 
//				"    OR TABLE_NAME LIKE '%import%'\r\n" + 
//				"    OR TABLE_NAME LIKE '%log'\r\n" +
//				"    OR TABLE_NAME LIKE '%history%'\r\n" +
//				"    OR TABLE_NAME LIKE '%xref%'\r\n" +
//				"    OR TABLE_NAME LIKE '%stage%');";
//		delete = delete.replace("{{schema}}", SCHEMA);
//		trucate = trucate.replace("{{schema}}", SCHEMA);
//		System.out.println(trucate);
//		System.err.println("**********");
//		System.out.println(delete);
//		String tables = "SELECT TABLE_NAME \r\n" + 
//				"FROM INFORMATION_SCHEMA.TABLES\r\n" + 
//				"WHERE table_schema = 'zc_base_admin'";
//		List<Map<String, Object>> map = DbUtils.getMapList(connection, delete);
//		List<Map<String, Object>> trnMap = DbUtils.getMapList(connection, trucate);
//		System.out.println(map);
		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
//		for(Map<String, Object> maps : map) {
//			for(Object s : maps.values()) {
//				System.out.println(s.toString());
//				String ss = s.toString();
//				if(DbUtils.getResultset(connection, ss.substring(ss.indexOf("FROM")+5, ss.indexOf("WHERE")))) {
//					DbUtils.execute(s.toString(), connection);
//				}
//			}
//		}
//		for(Map<String, Object> maps : trnMap) {
//			for(Object s : maps.values()) {
//				System.out.println(s.toString());
//					DbUtils.execute(s.toString(), connection);
//			}
//		}
		Script script = new Script();
		script.polishDatabase(connection, SCHEMA, true);
		DbUtils.execute("UPDATE zc_base_admin.app_api SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		DbUtils.execute("UPDATE zc_base_admin.app_page SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		DbUtils.execute("UPDATE zc_base_admin.app_process SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		
		DbUtils.execute("UPDATE zc_base_admin.app_publish_task SET `status`= Published , `release` = R-0;", connection);
		
		DbUtils.execute("SET SQL_SAFE_UPDATES = 1;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 1;", connection);
		DbUtils.getCloseConnection(connection);
		System.out.println("Done");
		
//		String s = "delete from zc_base_admin.app_addon_widget where is_deleted = 1";
//		System.out.println(s.substring(s.indexOf("from")+5, s.indexOf("where")));
	}
}
