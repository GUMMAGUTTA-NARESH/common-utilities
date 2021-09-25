/**
 * 
 */
package com.tss.db;

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
	
	public void polishDatabase(Connection connection, String schema,  boolean isTruncate) throws SQLException {
		String query = isTruncate ? Constants.TRUNCATE : Constants.DELETE;
		query = query.replace("{{schema}}", schema);
		List<Map<String, Object>> listMap = DbUtils.getMapList(connection, query);
//		DbUtils.execute("SET SQL_SAFE_UPDATES = 0;", connection);
//		DbUtils.execute("SET FOREIGN_KEY_CHECKS = 0;", connection);
		for(Map<String, Object> maps : listMap) {
			for(Object s : maps.values()) {
				String _q = s.toString();
				if(isTruncate) {
					DbUtils.execute(_q, connection);
					System.out.println(_q);
				}else if(DbUtils.getResultset(connection, _q.substring(_q.indexOf("FROM")+5, _q.indexOf("WHERE")))){
					 DbUtils.execute(_q, connection);
				}
			}
		}
	}
}
