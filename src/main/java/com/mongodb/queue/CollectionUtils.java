package com.mongodb.queue;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class CollectionUtils {
	
	public static DBCollection getCollection(DB db , String topic) {
		DBCollection collection = db.getCollection(topic);
		return collection;
	}

}
