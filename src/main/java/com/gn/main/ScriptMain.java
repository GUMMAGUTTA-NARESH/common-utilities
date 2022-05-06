/**
 * 
 */
package com.gn.main;

import java.io.File;
import java.sql.Connection;

import com.gn.db.DbUtils;
import com.gn.db.Script;

/**
 * @author NARESH GUMMAGUTTA
 *@since 24 Sep, 2021
 */
public class ScriptMain {
	
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_base_admin";
	
	public static void main(String[] args) throws Exception {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
		Script script = new Script();
		
		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
//		
		script.polishDatabase(connection, SCHEMA, true);
//		
		if ("zc_base_admin".equalsIgnoreCase(SCHEMA)) {
			DbUtils.execute("UPDATE zc_base_admin.app_api SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
			DbUtils.execute("UPDATE zc_base_admin.app_page SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
			DbUtils.execute("UPDATE zc_base_admin.app_process SET version=1 AND is_fixed = 1, is_publish = 1;", connection);
			DbUtils.execute("UPDATE zc_base_admin.app_dataset SET can_delete = 0;", connection);
			DbUtils.execute("UPDATE zc_base_admin.app_dataset_field SET can_delete = 0;", connection);
//
			DbUtils.execute("UPDATE zc_base_admin.app_publish_task SET `status`= Published , `release` = R-0;", connection);
		}
//		
		DbUtils.execute("SET SQL_SAFE_UPDATES = 1;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 1;", connection);
//		
		String updateAppUser = "UPDATE {{schema}}.app_user SET `first_name` = NULL,`email` = NULL, `password` = NULL, `phone` = NULL, `last_login` = NULL, `last_login_ip` = NULL, `last_login_agent` = NULL WHERE (`pk_id` = '1');";
		updateAppUser =updateAppUser.replace("{{schema}}", SCHEMA);
		String uQ = "DELETE  FROM  {{schema}}.app_user where user_code NOT IN ( 'USR-1' ,'USR-2','USR-3')";
		String uQQ = "DELETE  FROM  {{schema}}.user where user_code NOT IN ( 'USR-1' ,'USR-2','USR-3')";
		
		DbUtils.execute(updateAppUser,connection);
		DbUtils.getCloseConnection(connection);
		
		script.export(USER_NAME, PASSWORD, "127.0.0.1", new File("G:\\Notepad ++ Files\\xyz.sql"), 3306, SCHEMA);
//		script.dbImport("127.0.0.1", USER_NAME, PASSWORD, SCHEMA, "G:\\InputFiles\\Dbs\\Base script dbs v35\\zc_v35_base_admin.sql");
		script.readSql("G:\\InputFiles\\Dbs\\Base script dbs v35\\16th script\\zc_v35_base_admin.sql",null);
		System.out.println("Done");
		
		//TODO:Need to add the query
		//PK_ID alternation ex: 1,2,3,6,7,15 out: 1,2,3,4,5,6,7
		//SET @count = 0;
		//UPDATE zc_base_admin.app_api_fields SET `app_api_fields`.`pk_id` = @count:= @count + 1;
		
		/**
		 *  SET @count = 0;
		UPDATE zc_base_admin.app_api_fields SET `app_api_fields`.`pk_id` = @count:= @count + 1;
                SELECT `AUTO_INCREMENT`
FROM  INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'zc_base_admin'
AND   TABLE_NAME   = 'app_api';
		 */
	}
}
