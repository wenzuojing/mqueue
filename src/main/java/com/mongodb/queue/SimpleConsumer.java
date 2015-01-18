package com.mongodb.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author wens
 */
public class SimpleConsumer implements Consumer {
	private MongoClient mongoClient;
	private DB queueDb;

	private ExecutorService executorService = ThreadHelper.newExecutor("simple-consumer", 10);

	private ConcurrentHashMap<String, List<MessageListener>> topicListeners = new ConcurrentHashMap<String, List<MessageListener>>();

	public SimpleConsumer(String group) {
		this(MongoClientHelper.getMongoClient(), ConfigHelper.getConfig().getQueueDbName(), group);
	}

	public SimpleConsumer(MongoClient mongoClient, String queueDbName, final String group) {
		this.mongoClient = mongoClient;
		this.queueDb = this.mongoClient.getDB(queueDbName);

		start(group);

	}

	@Override
	public void subscribe(String topic, MessageListener listener) {

		List<MessageListener> list = new LinkedList<MessageListener>();

		List<MessageListener> list2 = topicListeners.putIfAbsent(topic, list);

		if (list2 == null) {
			list2 = list;
		}

		list2.add(listener);
	}

	private void start(final String group) {
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				while (true) {
					Set<String> topics = topicListeners.keySet();

					for (final String topic : topics) {

						DBCollection collection = CollectionUtils.getCollection(queueDb, topic);

						final DBObject dbObject = collection.findAndModify(new BasicDBObject("groups." + group, new BasicDBObject("$exists", false)),
								new BasicDBObject("$set", new BasicDBObject("groups." + group, System.currentTimeMillis())));

						if (dbObject != null) {
							executorService.execute(new Runnable() {

								@Override
								public void run() {
									List<MessageListener> ls = topicListeners.get(topic);
									final BasicDBObject message = (BasicDBObject) dbObject.get("message");
									for (final MessageListener l : ls) {
										Executor executor = l.getExecutor();

										if (executor != null) {
											executor.execute(new Runnable() {

												@Override
												public void run() {
													l.recieveMessages(message);

												}
											});
										} else {
											l.recieveMessages(message);
										}

									}
								}
							});
						}else{

                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }

                        }

					}
				}

			}
		});
	}

	@Override
	public void shutdown() {
		executorService.shutdown();

	}

}
