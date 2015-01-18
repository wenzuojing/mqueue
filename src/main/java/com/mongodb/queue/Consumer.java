package com.mongodb.queue;

import java.util.concurrent.Executor;

public interface Consumer {

	public void subscribe(String topic, MessageListener listener);
	
	public void shutdown() ;


}
