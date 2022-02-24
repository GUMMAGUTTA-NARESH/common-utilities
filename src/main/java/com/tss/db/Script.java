/**
 * 
 */
package com.tss.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

import com.tss.service.Constants;
import com.tss.service.GnMap;
import com.tss.util.GnUtil;

/**
 * @author NARESH GUMMAGUTTA
 *@since 23 Sep, 2021
 */
public class Script {
	
	/**
	 * @param conn
	 * @param sql
	 * @return
	 * @throws SQLException
	 * @author NARESH_GUMMAGUTTA
	 */
	public List<GnMap> getMapList(Connection conn, String sql) throws SQLException {
		PreparedStatement preStmt = conn.prepareStatement(sql);
		ResultSet resultSet = preStmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		List<GnMap> listOfRec = new ArrayList<GnMap>();
		Map<String, Object> record = null;
		while (resultSet.next()) {
			record = new HashedMap<String, Object>();
			for (int i = 1; i <= metaData.getColumnCount(); i++)
				record.put(metaData.getColumnName(i), resultSet.getObject(i));
			listOfRec.add((GnMap) record);
		}
		preStmt.close();
		resultSet.close();
		if (listOfRec.size() == 0)
			return null;
		return listOfRec;
	}
	
	/**
	 * @author NARESH_GUMMAGUTTA
	 * @param connection
	 * @param schema
	 * @param isTruncate
	 * @throws SQLException
	 */
	public void polishDatabase(Connection connection, String schema,  boolean isTruncate) throws SQLException {
		String query = isTruncate ? Constants.TRUNCATE:Constants.DELETE;
		query = query.replace("{{schema}}", schema);
		List<Map<String, Object>> listMap = DbUtils.getMapList(connection, query);
//		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
//		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
		for(Map<String, Object> map : listMap) {
			for(Object s : map.values()) {
				String _q = s.toString();
				if(isTruncate) {
					DbUtils.execute(_q, connection);
					System.out.println(_q);
				}else {//if(DbUtils.getResultset(connection, _q.substring(_q.indexOf("FROM")+5, _q.indexOf("WHERE")))){
					 DbUtils.execute(_q, connection);
				}
			}
		}
	}
	
	public void export(String userName, String password, String host, File filePath, int port, String schema) throws IOException {
		// mysqldump -u admin -p12345 -h127.0.0.1 -P3306 --events --routines --comments import > fileName.sql
		Runtime rt = Runtime.getRuntime();
		PrintStream ps;
		try {
			Process child = rt.exec( Constants.MYSQL_PATH + "\\mysqldump -h" + host + " -u" + userName + " -p" + password +"-P"+port+ schema);
			ps = new PrintStream(filePath);
			InputStream in = child.getInputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				ps.write(ch);
				System.out.write(ch); // to view it by console
			}
			InputStream err = child.getErrorStream();
			while ((ch = err.read()) != -1) {
				System.out.write(ch);
			}
		} catch (Exception exc) {exc.printStackTrace();}
	}
	
	/**
	 * This method is not working
	 * TO DO: Need to work on it
	 * @param host
	 * @param userName
	 * @param password
	 * @param schema
	 * @param sqlFile
	 */
	public void dbImport(String host,String userName, String password, String schema, String sqlFile) {
		// goto .sql file location: mysql -h 127.0.0.1 -u admin -p zc_brd < zc_importtest.sql	// Working Fine
		String[] executeCmd = new String[]{"cmd.exe", "mysql -h" + host + " -u" + userName + " -p" + password +" "+ schema+ " < "+sqlFile};
		Runtime rt = Runtime.getRuntime();
		try {
//			Process child = rt.exec(Constants.MYSQL_PATH + "\\mysql -h" + host + " -u" + userName + " -p" + password
//					 + schema+ " < "+new File(sqlFile));
			Process child = rt.exec(executeCmd);
			InputStream in = child.getInputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				System.out.write(ch); // to view it by console
			}

			InputStream err = child.getErrorStream();
			while ((ch = err.read()) != -1) {
				System.out.write(ch);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	
	public void readSql(String file, String dumpFile) throws Exception {
		String str = GnUtil.getJsonFromFile(file);
		String fileName = GnUtil.getFileName(file);
		boolean isAdmin = fileName.contains("admin");
//		fileName = isAdmin ? Utility.isBlank(dumpFile) ? "zc_base_admin" : "zc_base" : dumpFile;
		fileName = GnUtil.isBlank(dumpFile) ? isAdmin ? "zc_base_admin" : "zc_base" : dumpFile;
		String separator = "Dumping routines for database'"+fileName+"'";
		int sepPos = str.lastIndexOf(separator);
		if (sepPos == -1) {
			System.out.println("");
		}
		System.out.println("Substring before last separator = " + str.substring(0, sepPos));

	}
}
