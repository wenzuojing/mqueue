package com.mongodb.queue;

import java.util.concurrent.Executor;

import com.mongodb.BasicDBObject;

public interface MessageListener {

	public void recieveMessages(BasicDBObject message) ;

	public Executor getExecutor();

}
