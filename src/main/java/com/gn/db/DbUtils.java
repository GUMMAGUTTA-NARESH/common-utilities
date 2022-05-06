package com.gn.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gn.service.DbExcelUtils;
import com.gn.util.GnUtil;



public class DbUtils {
	public static final char GRAVE = '`';
	public static String encloseGrave(String s) {
		return (GnUtil.isBlank(s))? null:GRAVE + s.trim() + GRAVE;
	}
	public static Connection getConnection(String host, int port, String schema, String userName, String password)
			throws ClassNotFoundException, SQLException {
		Connection connection = null;
		Class.forName("com.mysql.cj.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + schema, userName,	password);
		//"?rewriteBatchedStatements=true"
		connection.setAutoCommit(false);
		return connection;
	}

	public static void getCloseConnection(Connection connection) throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.commit();
			connection.close();
		}
	}
	public static Map<String, Object> get(Connection conn, String sql, Object... args) throws SQLException {
		return (getMapList(conn, sql, args) != null) ? getMapList(conn, sql, args).get(0): null;
	}
	
	
	private static PreparedStatement query(Connection conn, String sql, Object... args) throws SQLException {
		int index = 0;
		PreparedStatement preStmt = conn.prepareStatement(sql);
		for (Object value : args)
			preStmt.setObject(++index, value);
		return preStmt;
	}
	
	public static List<Map<String, Object>> getMapList(Connection conn, String sql, Object... args)
			throws SQLException {
		PreparedStatement preStmt = query(conn, sql, args);
		ResultSet resultSet = preStmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		List<Map<String, Object>> listOfRec = new ArrayList<Map<String, Object>>();
		Map<String, Object> record = null;
		while (resultSet.next()) {
			record = new HashMap<String, Object>();
			for (int i = 1; i <= metaData.getColumnCount(); i++)
				record.put(metaData.getColumnName(i), resultSet.getObject(i));
			listOfRec.add(record);
		}
		preStmt.close();
		resultSet.close();
		if (listOfRec.size() == 0)
			return null;
		return listOfRec;
	}
	
	public static void execute(String sql, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		 preparedStatement.executeUpdate();
	}
	
	public static int[] batchUpdate(Connection conn, List<String> queries) throws SQLException {
		Statement stm = conn.createStatement();
		for(String q : queries) {
			stm.addBatch(q);
		}
		int[] status = stm.executeBatch();
		stm.close();
		return status;
	}
	
	public static int createTable(Connection connection, String file,String schema, boolean wantDefaults) throws Exception {
		String sql = DbExcelUtils.queryBuilder(file, schema, wantDefaults);
		PreparedStatement statement = connection.prepareStatement(sql);
	return 	statement.executeUpdate();
	}

	
	public static Integer getGeneratedKey(Connection connection, String sql, Object... args) throws SQLException {
		int index = 0;
		PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for (Object value : args)
			preparedStatement.setObject(++index, value);
		preparedStatement.executeUpdate();
		ResultSet resultSet = preparedStatement.getGeneratedKeys();
		if (resultSet.next()) {
			return resultSet.getInt(1);
		}
		return 0;
	}

	public static String generateInsertQuery(String schemaName, String tableName, String[] columns) {
		String tableWithSchema = (GnUtil.isBlank(schemaName) ? encloseGrave(tableName): (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "", placeHolders = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + ",";
			placeHolders += "?,";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		placeHolders = placeHolders.substring(0, placeHolders.length() - 1);
		return "INSERT INTO " + tableWithSchema + " (" + columnNames + ") VALUES (" + placeHolders + ");";
	}
	public static String generateUpdateQuery(String schemaName, String tableName, String[] columns,int id) {
		String tableWithSchema = (GnUtil.isBlank(schemaName) ? encloseGrave(tableName): (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + "=? ,";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		return "UPDATE " + tableWithSchema + " SET is_modified = ?," + columnNames + " WHERE pk_id=?";
	}
	public static String generateInnerUpdateQuery(String schemaName, String tableName) {
		return "UPDATE " + encloseGrave(tableName) + " SET is_deleted = ?, is_modified=? WHERE pk_id=?;";
	}
	
	public static String generateDeleteQuery(String schemaName, String tableName,int id) {
//		return "UPDATE " +encloseGrave(tableName) + " SET is_deleted = true WHERE pk_id= ?;";
		return "UPDATE"+encloseGrave(tableName) +" SET `is_deleted` = true, `is_modified`= ? WHERE "+encloseGrave("pk_id")+" = ?";
	}
	public static String generateSelectQuery(String tableName) {
		return "SELECT * FROM "+encloseGrave(tableName)+" where is_deleted=false";
	}
	public static String generateSelectQuery(String tableName, String where) {
		return "SELECT * FROM "+encloseGrave(tableName)+" WHERE "+where+" =? AND is_deleted=false";
	}
	
	public static String generateSelectQueryById(String tableName) {
		return "SELECT * FROM "+encloseGrave(tableName)+" WHERE pk_id=? AND is_deleted = false";
	}
	public static String generateFindByIdPassword(String tableName) {
		return "SELECT * FROM " +encloseGrave(tableName)+" WHERE user_name=? AND password = ? AND is_deleted=false" ;
	}
	
	public static String generateIsDeletedRecord(String tableName, String column) {
		return "SELECT * FROM "+encloseGrave(tableName)+" WHERE is_deleted = true AND "+encloseGrave(column)+" =?";
	}
	
	public static Map<String, Object> setData(Map<String, Object> fromDb, Map<String, Object> fromUser) {
		Set<String> s = fromUser.keySet();
		for(String s1 : s) {
			fromDb.replace(s1, fromDb.get(s1), fromUser.get(s1));
		}
		return fromDb;
	}
	public static String getEncodedString(String password) {
		if(GnUtil.isBlank(password)) return null;
		else return Base64.getEncoder().encodeToString(password.getBytes());
	}

	public static String getDecodedString(String encryptedpassword) {
		if(GnUtil.isBlank(encryptedpassword)) return null;
		else return new String(Base64.getMimeDecoder().decode(encryptedpassword));
	}
	public static String generateFindAllQuery(String schemaName, String tableName, String[] columns) {
		String tableWithSchema = (GnUtil.isBlank(schemaName) ? encloseGrave(tableName)
				: (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + " ,";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		return "SELECT " + columnNames + " FROM " + tableWithSchema + ";";
	}
	public static String generateFindQuery(String schemaName, String tableName, String[] columns) {
		String tableWithSchema = (GnUtil.isBlank(schemaName) ? encloseGrave(tableName)
				: (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + ",";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		return "SELECT " + columnNames + " FROM " + tableWithSchema + " WHERE pk_id=?;";
	}
	public static String convertToBase64(java.sql.Blob blob) throws Exception {
		if(GnUtil.isBlank(blob)) return "";
		 InputStream inputStream = blob.getBinaryStream();
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         byte[] buffer = new byte[4096];
         int bytesRead = -1;
         while ((bytesRead = inputStream.read(buffer)) != -1) {
             outputStream.write(buffer, 0, bytesRead);                  
         }
         byte[] imageBytes = outputStream.toByteArray();
         String base64Image = Base64.getEncoder().encodeToString(imageBytes);
		return base64Image;
	}
	
	private static PreparedStatement getPreparedStatement(Connection connection, String query, Object... args) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		int index = 0;
		for (Object value : args)
			preparedStatement.setObject(++index, value);
		return preparedStatement;
	}
	
	public static boolean getResultset(Connection connection, String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+sql);
//		preparedStatement.addBatch(arg0);
		ResultSet resultSet = preparedStatement.executeQuery();
		ResultSetMetaData metadata = resultSet.getMetaData();
		int columnCount = metadata.getColumnCount();
		System.out.println("test_table columns : ");
		for (int i=1; i<=columnCount; i++) {
			  String columnName = metadata.getColumnName(i);
			  if("is_deleted".equalsIgnoreCase(columnName)) return true;
			}
		return false;
	}
	
	private static ResultSet getExecuteQuery(Connection connection, String query, Object... args) throws SQLException {
		PreparedStatement preparedStatement = getPreparedStatement(connection, query, args);
		ResultSet resultSet = getPreparedStatement(connection, query, args).executeQuery();
		preparedStatement.close();
//		resultSet.close();
		return resultSet;
	}
	
	public static ResultSet getExecuteQueryForNaarms(Connection connection, String query, Object... args) throws SQLException {
		PreparedStatement preparedStatement = getPreparedStatement(connection, query, args);
		ResultSet resultSet = getPreparedStatement(connection, query, args).executeQuery();
		preparedStatement.close();
//		resultSet.close();
		return resultSet;
	}
	
	public static String getResultsetToExcel(Connection connection, String query,String fileName,Object...args) throws SQLException, FileNotFoundException, IOException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = getExecuteQuery(connection, query, args);
		preparedStatement.close();
		return DatabaseToExcel.databaseToExcel(resultSet, fileName, connection);
	}
	
	public static String getResultsetToExcelForNaarm(Connection connection, String query,String fileName,String s,Object...args) throws SQLException, FileNotFoundException, IOException {
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		ResultSet resultSet = getExecuteQuery(connection, query, args);
		preparedStatement.close();
		return DatabaseToExcel.databaseToExcelForNaarm(resultSet, fileName, connection, s);
	}
	
	public static List<Map<String, Object>> getMapListForNaarm(Connection conn, String sql)
			throws SQLException {
		PreparedStatement preStmt = query(conn, sql);
		ResultSet resultSet = preStmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		List<Map<String, Object>> listOfRec = new ArrayList<Map<String, Object>>();
		Map<String, Object> record = null;
		while (resultSet.next()) {
			record = new HashMap<String, Object>();
			for (int i = 1; i <= metaData.getColumnCount(); i++)
				record.put(metaData.getColumnName(i), resultSet.getObject(i));
			listOfRec.add(record);
		}
		preStmt.close();
		resultSet.close();
		if (listOfRec.size() == 0)
			return null;
		return listOfRec;
	}
	
	public static void insertDataFromDbToExcelForNaarm(String file, Connection conn, String query) throws Exception {
		Statement statement = conn.createStatement();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Research areas");
		XSSFRow row = null;
		XSSFCell cell = null;
		int rowNum = 0;
		row = sheet.createRow(rowNum);
		List<Map<String, Object>> headings = DbUtils.getMapList(conn, "SELECT distinct temp_research_areas FROM `zc_naarm`.`wos_research_areas_data`;");
		List<String> headingsList = new ArrayList<String>();
		headingsList.add("INSTITUTE NAME");
		for(Map<String, Object> m : headings) m.forEach((key, value) -> headingsList.add(value.toString()));
		//headings
		for (int cellNum = 0; cellNum < headingsList.size(); cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(headingsList.get(cellNum));
			sheet.autoSizeColumn(cellNum);
		}
		rowNum++;
		
		List<Map<String, Object>> institute = DbUtils.getMapList(conn, "SELECT   distinct `temp_institution`  FROM  `zc_naarm`.`wos_research_areas_data`;");
		List<String> instituteList = new ArrayList<String>();
		for(Map<String, Object> m : institute) m.forEach((key, value) -> instituteList.add(value.toString()));
		
		for(int a = 0; a < institute.size(); a++) {
			String s = instituteList.get(a);
			ResultSet set = statement.executeQuery( "SELECT temp_institution, data ,temp_research_areas FROM `zc_naarm`.`wos_research_areas_data` WHERE  temp_institution = '"+s+"';");
			row = sheet.createRow(rowNum);
			int cellNum = 0;
			cell = row.createCell(cellNum++);
			cell.setCellValue(s); //insertion
			sheet.autoSizeColumn(cellNum);
			while (set.next()) {
				cell = row.createCell(cellNum++);
//				if (row.getCell(cellNum).toString().equalsIgnoreCase(set.getString(3))) {
//					if (row.getCell(cellNum).getStringCellValue().equalsIgnoreCase(set.getString(3))) {
//						System.out.println(row.getCell(cellNum).getStringCellValue());
					cell.setCellValue((int) Double.parseDouble(set.getString(2))); // insertion
//				}
			}
			rowNum++;
		}
		FileOutputStream out = new FileOutputStream(new File(file));
		workbook.write(out);
		out.close();
		workbook.close();
	}
	
	private static List<String> getData(Connection conn, String query) throws Exception{
		List<Map<String, Object>> headings = DbUtils.getMapList(conn, query);
		List<String> headingsList = new ArrayList<String>();
//		headingsList.add("INSTITUTE NAME");
		for (Map<String, Object> m : headings) m.forEach((key, value) -> headingsList.add(value.toString()));
		return headingsList;
	}
	
	public static void insertDataFromDbToExcelForNaarmIt(String file, Connection conn, String query) throws Exception {
		Statement statement = conn.createStatement();
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Research Areas");
		String[] cols = GnUtil.fileDataToStrArr("C:\\Users\\G NARESH\\Downloads\\narmdata.txt");
		XSSFRow row = null;
		XSSFCell cell = null;
		int rowNum = 0;
		row = sheet.createRow(rowNum);
		List<Map<String, Object>> headings = DbUtils.getMapList(conn, "SELECT distinct temp_research_areas FROM `zc_naarm`.`wos_research_areas_data`;");
		List<String> headingsList = new ArrayList<String>();
		headingsList.add("INSTITUTE NAME");
		for (Map<String, Object> m : headings) m.forEach((key, value) -> headingsList.add(value.toString()));
		// headings
		for (int cellNum = 0; cellNum < headingsList.size(); cellNum++) {
			cell = row.createCell(cellNum);
			cell.setCellValue(headingsList.get(cellNum));
			sheet.autoSizeColumn(cellNum);
		}
		rowNum++;
		List<Map<String, Object>> institute = DbUtils.getMapList(conn, "SELECT   distinct `temp_institution`  FROM  `zc_naarm`.`wos_research_areas_data`;");
		List<String> instituteList = new ArrayList<String>();
		for (Map<String, Object> m : institute)
			m.forEach((key, value) -> instituteList.add(value.toString()));
		for (int a = 0; a < institute.size(); a++) {
			String s = instituteList.get(a);
			ResultSet set = statement.executeQuery("SELECT temp_institution, data, temp_research_areas FROM `zc_naarm`.`wos_research_areas_data` WHERE  temp_institution = '" + s + "';");
			row = sheet.createRow(rowNum);
			int cellNum = 0;
			int i = 1;
			int j = 0;
			List<String> arr = new ArrayList<String>();
			List<String> data = new ArrayList<String>();
			cell = row.createCell(cellNum++);
			cell.setCellValue(s); // institute insertion
			sheet.autoSizeColumn(cellNum);
			while (set.next()) {
				arr.add(set.getString(3));
				data.add(set.getString(2));
				cell = row.createCell(cellNum++);
				if (headingsList.get(i).equalsIgnoreCase(arr.get(j))) {
					cell.setCellValue((int) Double.parseDouble(data.get(j))); // data insertion
				} else {
					cell.setCellValue("NULL");
					j--;
//					cell = row.createCell(cellNum++);
				}
				i++;
				j++;
			}
			rowNum++;
		}
		FileOutputStream out = new FileOutputStream(new File(file));
		workbook.write(out);
		out.close();
		workbook.close();
	}
}
