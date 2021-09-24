/**
 * 
 */
package com.tss.main;

import java.sql.Connection;
import java.sql.SQLException;

import com.tss.db.DbUtils;
import com.tss.db.Script;

/**
 * @author NARESH GUMMAGUTTA
 *@since 24 Sep, 2021
 */
public class ScriptMain {
	
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_base_admin";
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
		Script script = new Script();
		
		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
		
		script.polishDatabase(connection, SCHEMA, true);
		
		DbUtils.execute("UPDATE zc_base_admin.app_api SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		DbUtils.execute("UPDATE zc_base_admin.app_page SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		DbUtils.execute("UPDATE zc_base_admin.app_process SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
		
		DbUtils.execute("UPDATE zc_base_admin.app_publish_task SET `status`= Published , `release` = R-0;", connection);
		
		DbUtils.execute("SET SQL_SAFE_UPDATES = 1;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 1;", connection);
		DbUtils.getCloseConnection(connection);
		System.out.println("Done");
	}
}
