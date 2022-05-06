/**
 * 
 */
package com.gn.dao;




import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.gn.service.GnMap;
import com.mongodb.BasicDBObject;
import com.mongodb.ConnectionString;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * @author NARESH GUMMAGUTTA
 * @since 30 Sep, 2021
 */
public class MongoExample {
	
//	static final Logger logger = Logger.getLogger(MongoExample.class);
	public static void main2(String[] args) {
//		MongoClient mongoClient = new MongoClient("localhost", 27017);
		MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://127.0.0.1:27017"));
		DB database = mongoClient.getDB("admin");

		// print existing databases
		mongoClient.getDatabaseNames().forEach(System.out::println);

		database.createCollection("customers", null);

		// print all collections in customers database
		database.getCollectionNames().forEach(System.out::println);

		// create data
		DBCollection collection = database.getCollection("customers");
		BasicDBObject document = new BasicDBObject();
		document.put("name", "Shubham");
		document.put("company", "Baeldung");
		collection.insert(document);

		// update data
		BasicDBObject query = new BasicDBObject();
		query.put("name", "Shubham");
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("name", "John");
		BasicDBObject updateObject = new BasicDBObject();
		updateObject.put("$set", newDocument);
		collection.update(query, updateObject);

		// read data
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("name", "John");
		DBCursor cursor = collection.find(searchQuery);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

		// delete data
		BasicDBObject deleteQuery = new BasicDBObject();
		deleteQuery.put("name", "John");
		collection.remove(deleteQuery);
	}

	public static void liveCconnection() {

		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://Naresh:<password>@cluster0.ztwxv.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
		MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
		com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);
		MongoDatabase database = mongoClient.getDatabase("test");

	}
	
	private static void selectAllRecordsFromACollection(DBCollection collection) 
	{
	    DBCursor cursor = collection.find();
	    while(cursor.hasNext())
	    {
	        System.out.println(cursor.next());
	    }
	}
	
	public static void main(String[] args) throws Exception {
		String uri = "mongodb://naresh:12345@localhost";
//		 mongodb://127.0.0.1:27017/?compressors=disabled&gssapiServiceName=mongodb//
		com.mongodb.client.MongoClient client = MongoClients.create(uri);
//		com.mongodb.client.MongoClient client = MongoClients.create();
		
		MongoIterable<String> s = client.listDatabaseNames();
		for(String ss : s) {
			System.out.println(ss);
		}
		
		MongoDatabase db  = client.getDatabase("practice"); // schema name
		MongoCollection<Document> col = db.getCollection("licenses"); //table name
		 FindIterable<Document> mydatabaserecords = db.getCollection("templates").find();
		MongoCursor<Document> iterator = mydatabaserecords.iterator();
//		List<GnMap> dss = MongoUtils.getMapList(db, "templates");
//		GnMap lss = MongoUtils.get(db, "templates", "template1");
//		System.out.println(lss.getS("tss.localTemplate"));
//		System.out.println(dss);
		GnMap gn = null;
		String t = "";
//		while (iterator.hasNext()) {
//			 gn = new GnMap();
////			gn.put("data", iterator.next());
//            Document doc = iterator.next();
//            gn.put(doc.keySet().toString(), iterator.next());
//            listOfRec.add(gn);
//             System.out.println(doc.get("localTemplate"));
//            System.out.println(doc);
//        }
//		System.out.println(gn);
		
		
//		System.out.println(t);
		
//		MongoClient mongoClient = new MongoClient("localhost");
//		DB dbs = mongoClient.getDB("practice");
//		DBCollection coll = dbs.getCollection("templates");

		
		Document myDoc = col.find(eq("localTemplate", null)).first();
//		System.out.println(myDoc.toJson());
		

		BasicDBObject allQuery = new BasicDBObject();
		BasicDBObject fields = new BasicDBObject();
		fields.put("template", 0);
		
//		DBCursor cursor = coll.find(allQuery, fields);
//		GnMap maps = new GnMap();
//		while (cursor.hasNext()) {
//			maps.put("data", cursor.next());
//		}
//		System.out.println(maps.getS("data.template"));
//		System.out.println(maps);
		
		
		
//		ArrayList<Document> unfecthedEvents = col.find(
//		        new Document("fetchStatus", new Document("$lte", null))
//		    ).projection(
//		        new Document("localTemplate",1)
//		    ).into(new ArrayList<Document>());
//		System.out.println(unfecthedEvents);
		
		
//		DBObject dbObject  = new BasicDBObject("_id", "naresh");
//		dbObject   = col.find(dbObject).next();
//	    String result =  dbObject.get("template").toString();
		
//		String temp = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\template.json";
//		String lc = "C:\\javaworkplaces\\javaworkproject\\utilities-common\\src\\main\\resources\\localTemplate.json";
//		String templateData = Utility.getJsonFromFile(temp);
//		String templateData1 = Utility.getJsonFromFile(lc);
//		Document doc = new Document("localTemplate", templateData1);
		GnMap res = new GnMap();
		res.put("template", null);
		res.put("destination", "des");
		res.put("from", 1); //start client number
		res.put("to", 1); // end client number
		res.put("client", "naarm"); //client name
		res.put("isEncrypted", true); // if true encrypted licenses also created along with normal files
		res.put("host", "");
		res.put("port", "3406");
		res.put("password", "The@1234");
		col.insertOne(new Document(res));
		System.out.println("Inserted.......");
	}
}
