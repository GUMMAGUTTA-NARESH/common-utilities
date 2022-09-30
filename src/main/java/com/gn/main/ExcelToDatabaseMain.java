package com.gn.main;

import java.sql.Connection;

import com.gn.db.DbUtils;
import com.gn.db.ExcelToDatabase;

public class ExcelToDatabaseMain {

	public static final String USER_NAME = "admin";
	public static final String PASSWORD = "Naresh@9493";
	public static final String SCHEMA = "zc_apollo_rider";
	public static final int PORT = 3306;

	public static void main(String[] args) throws Exception {
		Connection connection = DbUtils.getConnection("localhost", PORT, SCHEMA, USER_NAME, PASSWORD);
		System.out.println("Connection Established");
		ExcelToDatabase.insertData("/home/naresh/Downloads/Tamil Nadu PINCODE.xlsx", connection);
		DbUtils.getCloseConnection(connection);
	}
}
