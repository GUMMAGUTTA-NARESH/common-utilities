package com.gn.main;

import com.gn.dao.MongoUtils;
import com.gn.service.GnMap;

public class MongoMain {
	public static void main(String[] args) {
		GnMap credentials = new GnMap();
		credentials.put("host", "localhost");
		credentials.put("port", 27017);
		credentials.put("user", "admin");
		credentials.put("password", "12345");
		credentials.put("db", "admin");
		credentials.put("collection", "nareshdb");
		credentials.put("authMode", "SCRAM-SHA-1");
		credentials.put("authSource", "admin");
		
		GnMap reqData = new GnMap();
		reqData.put("key", "pk_id");
		reqData.put("value", 2);
		try {
			System.out.println(MongoUtils.getList(credentials, null).toJson(false));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
