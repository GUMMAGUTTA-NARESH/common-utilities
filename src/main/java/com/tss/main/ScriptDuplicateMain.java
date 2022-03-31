package com.tss.main;

import java.io.File;
import java.sql.Connection;
import java.util.List;

import com.tss.db.DbUtils;
import com.tss.db.Script;
import com.tss.service.Constants;

public class ScriptDuplicateMain {
	static final String USER_NAME = "admin";
	static final String PASSWORD = "12345";
	static final String SCHEMA = "zc_base_admin";

	public static void main(String[] args) throws Exception {
		Connection connection = DbUtils.getConnection("localhost", 3306, SCHEMA, USER_NAME, PASSWORD);
		Script script = new Script();

		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
//		script.polishDatabase(connection, SCHEMA, true);
		List<String>queries = script.polishDatabase(connection, true, SCHEMA);
		List<String> autoIncrements = script.getAutoIncreatmentQueries(connection, SCHEMA);
		
		if ("zc_base_admin".equalsIgnoreCase(SCHEMA)) {
			queries.add("UPDATE zc_base_admin.app_api SET version=1 AND is_fixed = 1, is_publish = 1;");
			queries.add("UPDATE zc_base_admin.app_page SET version=1 AND is_fixed = 1, is_publish = 1;");
			queries.add("UPDATE zc_base_admin.app_process SET version=1 AND is_fixed = 1, is_publish = 1;");
			queries.add("UPDATE zc_base_admin.app_dataset SET can_delete = 0;");
			queries.add("UPDATE zc_base_admin.app_dataset_field SET can_delete = 0;");
			queries.add("UPDATE zc_base_admin.app_publish_task SET `status`= Published , `release` = R-0;");
		}
		queries.add("UPDATE {{schema}}.app_user SET `first_name` = NULL,`email` = NULL, `password` = NULL, `phone` = NULL, `last_login` = NULL, `last_login_ip` = NULL, `last_login_agent` = NULL WHERE (`pk_id` = '1');".replace("{{schema}}", SCHEMA));
		queries.add("DELETE  FROM  {{schema}}.app_user where user_code NOT IN ( 'USR-1' ,'USR-2','USR-3')".replace("{{schema}}", SCHEMA));
		queries.add("DELETE  FROM  {{schema}}.user where user_code NOT IN ( 'USR-1' ,'USR-2','USR-3')".replace("{{schema}}", SCHEMA));
			queries.addAll(autoIncrements);
		queries.add("SET SQL_SAFE_UPDATES = 1;");
		queries.add("SET FOREIGN_KEY_CHECKS = 1;");
		for(String s:queries) {
			System.out.println(s);
		}
//		DbUtils.batchUpdate(connection, queries);
//		DbUtils.getCloseConnection(connection);

	}

}
