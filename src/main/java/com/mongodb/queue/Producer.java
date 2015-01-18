package com.mongodb.queue;

import com.mongodb.BasicDBObject;

public interface Producer {
	

	public void send(String topic, BasicDBObject message);
	
	
	public boolean safeSend(String topic, BasicDBObject message);
	
	

}
