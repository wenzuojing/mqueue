package com.mongodb.queue;

import com.mongodb.MongoClient;

public class MongoClientHelper {

	private static MongoClient mongoClient;

	private static Throwable initException;

	static {

		try {
			Config config = ConfigHelper.getConfig();
			mongoClient = new MongoClient(config.getMongoServers(), config.getMongoOptions());
		} catch (Throwable t) {
			initException = t;
		}

	}

	public static MongoClient getMongoClient() {

		if (initException != null) {
			throw new IllegalStateException(initException);
		}

		return mongoClient;
	}

}
