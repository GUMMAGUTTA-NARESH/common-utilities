package com.gn.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.gn.service.GnMap;
import com.gn.util.GnUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Sorts;


public class MongoUtils {
	
	
	public static MongoClient getConnection(GnMap credentials) {
		ServerAddress serverAddress = new ServerAddress(credentials.getS("host"), credentials.getI("port"));
		if ("SCRAM-SHA-1".equalsIgnoreCase(credentials.getS("authMode"))) {
			return new MongoClient(serverAddress, MongoCredential.createCredential(credentials.getS("user"),
							credentials.getS("authSource"),
							credentials.getS("password").toCharArray()),
					new MongoClientOptions.Builder().build());
		} else {
			return new MongoClient(serverAddress);
		}
	}
	
	@SuppressWarnings("resource")
	public static GnMap getConnectionWithCollection(GnMap credentials) {
		GnMap res = new GnMap();
		MongoClient mongo = null;
		ServerAddress serverAddress = new ServerAddress(credentials.getS("host"), credentials.getI("port"));
		if ("SCRAM-SHA-1".equalsIgnoreCase(credentials.getS("authMode"))) {
			mongo= new MongoClient(serverAddress, MongoCredential.createCredential(credentials.getS("user"),
							credentials.getS("authSource"),
							credentials.getS("password").toCharArray()),
					new MongoClientOptions.Builder().build());
		} else {
			mongo = new MongoClient(serverAddress);
		}
		res.put("connection", mongo);
		res.put("mongoDatabase", mongo.getDatabase(credentials.getS("db")));
		res.put("mongoCollection", ((MongoDatabase) res.get("mongoDatabase")).getCollection(credentials.getS("collection")));
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static void save(GnMap credentials, GnMap reqData) {
		Document doc = new Document("_id", new ObjectId());
		for ( Map.Entry<String, Object> entry : reqData.entrySet()) {
		    String key = entry.getKey();
		    Object value = entry.getValue();
		    doc.append(key, value);
		}
        ((MongoCollection<Document>) credentials.get("mongoCollection")).insertOne(doc);
//        gradesCollection.insertMany(grades, new InsertManyOptions().ordered(false));
	}
	
	public static GnMap getList(GnMap credentials) throws Exception {
		return getList(credentials,null);
	}
	
	@SuppressWarnings("unchecked")
	public static GnMap getList(GnMap credentials, GnMap reqData) throws Exception {
		List<GnMap>  rows = new ArrayList<GnMap>();
		GnMap connection = getConnectionWithCollection(credentials);
		MongoClient mongo = (MongoClient) connection.get("connection");
//		MongoDatabase mongoDatabase = (MongoDatabase) connection.get("mongoDatabase");
		MongoCollection<Document> mongoCollection = (MongoCollection<Document>) connection.get("mongoCollection");
		long totalCount = 0;
		Iterable<Document> iterable = null;
		iterable = GnUtil.isBlank(reqData) ? mongoCollection.find().limit(1000).sort(Sorts.ascending("pk_id")) 
							: mongoCollection.find(new Document(reqData.getS("key"), reqData.getS("value"))).sort(Sorts.ascending("pk_id"));
		totalCount = mongoCollection.countDocuments();
		for (Document item : iterable) {
			rows.add(new GnMap(item));
		}
		totalCount = totalCount==0 || GnUtil.hasData(reqData)? rows.size(): totalCount;
		mongo.close();
		GnMap resData = new GnMap();
		resData.put("totalCount", (int)totalCount);
		resData.put("rows", rows);
		return resData;	
	}

}
