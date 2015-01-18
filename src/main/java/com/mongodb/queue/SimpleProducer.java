package com.mongodb.queue;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

/**
 * @author wens
 */
public class SimpleProducer implements Producer {

	private MongoClient mongoClient;
	private DB queueDb;

	public SimpleProducer() {
		this(MongoClientHelper.getMongoClient(), ConfigHelper.getConfig().getQueueDbName());
	}

	public SimpleProducer(MongoClient mongoClient, String queueDbName) {
		this.mongoClient = mongoClient;
		this.queueDb = this.mongoClient.getDB(queueDbName);
	}

	@Override
	public void send(String topic, BasicDBObject message) {

		if (topic == null || topic.length() == 0) {
			throw new NullPointerException("Topic must not be null or empty ");
		}

		DBCollection collection = CollectionUtils.getCollection(queueDb, topic);

        long id  = buidTimeSeqId() ;

		BasicDBObject dbObject = buildDBObject(id , message);

		collection.save(dbObject);

	}

	

	@Override
	public boolean safeSend(String topic, BasicDBObject message) {

		if (topic == null || topic.length() == 0) {
			throw new NullPointerException("Topic must not be null or empty ");
		}

		DBCollection collection = CollectionUtils.getCollection(queueDb, topic);

        long id  = buidTimeSeqId() ;

		BasicDBObject dbObject = buildDBObject(id ,message);

		WriteResult writeResult = collection.save(dbObject, WriteConcern.JOURNAL_SAFE);

		return writeResult.getLastError() != null;

	}

	private BasicDBObject buildDBObject( long id  , BasicDBObject message ) {
		BasicDBObject dbObject = new BasicDBObject("_id" , id  ).append("message", message);
		return dbObject;
	}

    private long buidTimeSeqId(){

        return System.nanoTime() ;

    }

}
