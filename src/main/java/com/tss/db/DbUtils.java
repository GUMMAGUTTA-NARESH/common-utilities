package com.tss.db;

import java.io.ByteArrayOutputStream;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tss.service.DbExcelUtils;
import com.tss.util.Utility;



public class DbUtils {
	public static final char GRAVE = '`';
	public static String encloseGrave(String s) {
		return (Utility.isBlank(s))? null:GRAVE + s.trim() + GRAVE;
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
	
	public static int execute(String sql, Connection connection) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		return preparedStatement.executeUpdate();
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
		String tableWithSchema = (Utility.isBlank(schemaName) ? encloseGrave(tableName): (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
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
		String tableWithSchema = (Utility.isBlank(schemaName) ? encloseGrave(tableName): (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
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
		if(Utility.isBlank(password)) return null;
		else return Base64.getEncoder().encodeToString(password.getBytes());
	}

	public static String getDecodedString(String encryptedpassword) {
		if(Utility.isBlank(encryptedpassword)) return null;
		else return new String(Base64.getMimeDecoder().decode(encryptedpassword));
	}
	public static String generateFindAllQuery(String schemaName, String tableName, String[] columns) {
		String tableWithSchema = (Utility.isBlank(schemaName) ? encloseGrave(tableName)
				: (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + " ,";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		return "SELECT " + columnNames + " FROM " + tableWithSchema + ";";
	}
	public static String generateFindQuery(String schemaName, String tableName, String[] columns) {
		String tableWithSchema = (Utility.isBlank(schemaName) ? encloseGrave(tableName)
				: (encloseGrave(schemaName) + "." + encloseGrave(tableName)));
		String columnNames = "";
		for (String c : columns) {
			columnNames += encloseGrave(c) + ",";
		}
		columnNames = columnNames.substring(0, columnNames.length() - 1);
		return "SELECT " + columnNames + " FROM " + tableWithSchema + " WHERE pk_id=?;";
	}
	public static String convertToBase64(java.sql.Blob blob) throws Exception {
		if(Utility.isBlank(blob)) return "";
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
	
	public static boolean getResultset(Connection connection, String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM "+sql);
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
	
	
//	public static String extractFileName(Part part) {
//		String contentDisp = part.getHeader("content-disposition");
//		String[] items = contentDisp.split(";");
//		for (String s : items) {
//			if (s.trim().startsWith("filename")) {
//				return s.substring(s.indexOf("=") + 2, s.length() - 1);
//			}
//		}
//		return "";
//	}
//	public static String fileUpload(Part part) throws IOException {
//		File fileSaveDir = new File(FILE_PATH+"/"+part.getSubmittedFileName());
//		InputStream stream = part.getInputStream();
//		Files.copy(stream, fileSaveDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
//		stream.close();
//		return fileSaveDir.toString();
//	}
	

}
