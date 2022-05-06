/**
 * 
 */
package com.gn.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.print.Doc;

import org.bson.Document;

import com.gn.service.GnMap;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

/**
 * @author NARESH GUMMAGUTTA
 * @since 4 Oct, 2021
 */
public class MongoUtilsOld {

	public void getConnection(String uri, String schema, String table) throws Exception {
		MongoClient client = MongoClients.create(uri);
		MongoDatabase db = client.getDatabase(schema); // schema name
//		MongoCollection<Document> col = db.getCollection(table); //table name
	}
	
	public void getGeneretedKey(GnMap reqData, MongoClient client) {
		MongoDatabase db  = client.getDatabase("practice"); // schema name
		MongoCollection<Document> col = db.getCollection("templates"); //table name
		new Document(reqData);
		
	}

	public GnMap get(MongoDatabase db, String collection, String key) throws Exception {
		int index = "template".equalsIgnoreCase(key) ? 0 : 1;
		List<GnMap> res = getMapList(db, collection);
		return (res != null) ? res.get(index) : null;
	}

	public List<GnMap> getMapList(MongoDatabase db, String collection, Object... args) throws Exception {
		List<GnMap> listOfRec = new ArrayList<GnMap>();
		GnMap record = null;
		FindIterable<Document> mydatabaserecords = db.getCollection(collection).find();
		MongoCursor<Document> iterator = mydatabaserecords.iterator();
		Document docs = null;
		while (iterator.hasNext()) {
			record = new GnMap();
			docs = iterator.next();
			record.put("tss", docs);
			listOfRec.add(record);
		}
		return listOfRec;
	}
}
