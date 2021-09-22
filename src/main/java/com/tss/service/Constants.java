package com.tss.service;

public class Constants {
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
}
