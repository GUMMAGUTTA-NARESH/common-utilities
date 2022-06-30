package com.gn.service;

public class Constants {
	
	public static final String LOCALHOST_TEMPLATE = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\localTemplate.json";
	public static final String QUERY = "";
	String query = ("CREATE TABLE $table$ (" +  
            "pk_id INT AUTO INCREMENT NOT NULL, " + 
            "$name$ VARCHAR(50) NOT NULL, " + 
            "PRIMARY KEY(pk_id) " + 
            ");"); 
	static final String CREATE_TABLE = "CREATE TABLE {{scheme}}.{{table}} (`pk_id` INT NOT NULL AUTO_INCREMENT,{{query}} \n PRIMARY KEY (`pk_id`)) \n ENGINE = InnoDB \n DEFAULT CHARACTER SET = utf8;";
	static final String CREATE_TABLE_DEFAULTS = "CREATE TABLE {{scheme}}.{{table}} (`pk_id` INT NOT NULL AUTO_INCREMENT,{{query}} ,`created_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
			"  `modified_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
			"  `is_deleted` TINYINT(1) NULL DEFAULT '0', \n PRIMARY KEY (`pk_id`)) \n ENGINE = InnoDB \n DEFAULT CHARACTER SET = utf8;";

	public static final String DELETE1 ="SELECT CONCAT('DELETE FROM {{schema}}.',  table_name,  ' WHERE is_deleted = 1')   FROM  information_schema.tables  WHERE  table_schema = '{{schema}}';";
	public static final String DELETE = "SELECT CONCAT('DELETE FROM {{schema}}.',table_name,' WHERE is_deleted = 1') FROM INFORMATION_SCHEMA.COLUMNS "
			+ "WHERE COLUMN_NAME IN ('is_deleted') AND TABLE_SCHEMA='{{schema}}';";
	
	public static final String TRUNCATE ="SELECT \r\n" + 
			"    CONCAT('TRUNCATE TABLE {{schema}}.',table_name)\r\n" + 
			"	FROM INFORMATION_SCHEMA.TABLES\r\n" + 
			"	WHERE table_schema = '{{schema}}' AND (TABLE_NAME LIKE '%audit' OR TABLE_NAME LIKE '%queue'\r\n" + 
			"    OR TABLE_NAME LIKE '%survey%'\r\n" + 
			"    OR TABLE_NAME LIKE '%mobile%'\r\n" + 
			"    OR TABLE_NAME LIKE '%import%'\r\n" + 
			"    OR TABLE_NAME LIKE '%log'\r\n" +
			"    OR TABLE_NAME LIKE '%history%'\r\n" +
			"    OR TABLE_NAME LIKE '%xref%'\r\n" +
			"    OR TABLE_NAME LIKE '%stage%');";
	
	public static final String AUTO_INCREMENT1 = "UPDATE {{schema}}.{{table_name}} SET `{{table_name}}`.`pk_id` = @count:= @count + 1;";
	public static final String AUTO_INCREMENT = "SELECT CONCAT('UPDATE {{schema}}.',  table_name,  CONCAT(' SET ', table_name, '.pk_id = @count:= @count + 1'))   FROM  information_schema.tables  WHERE  table_schema = '{{schema}}';";
	
	public static final String MYSQL_PATH = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin";
	public static final String MYSQL_SHELL_PATH = "C:\\Program Files\\MySQL\\MySQL Shell 8.0\\bin\\";
	
	public static final String V35 = "zc_v35_";
	public static final boolean IS_WINDOWS = System.getProperty("os.name").toLowerCase().indexOf("windows")>-1;
	public static final String DB_BACKUP_BASE_PATH_WINDOWS = "G:\\InputFiles\\Dbs\\{{path}}";
	public static final String DB_BACKUP_BASE_PATH_UBUNTU = "media/naresh/WORK/InputFiles/Dbs/{{path}}";
	
	//LocalHost Db Credentials
	 public static final String MYSQL_HOST = "127.0.0.1";
	 public static final String MYSQL_USER_NAME = "admin";
	 public static final String MYSQL_PASSWORD = "12345";
	 public static final int MYSQL_PORT = 3306;
	 
	
	

}
