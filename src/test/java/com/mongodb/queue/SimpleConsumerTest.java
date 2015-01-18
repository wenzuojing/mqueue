package com.mongodb.queue;

import java.util.concurrent.Executor;

import org.junit.Test;

import com.mongodb.BasicDBObject;

public class SimpleConsumerTest {

	
	@Test
	public void testSubscribe() throws InterruptedException {

		Consumer consumer = new SimpleConsumer("default");
		
		consumer.subscribe("test-topic2", new MessageListener() {
			
			@Override
			public void recieveMessages(BasicDBObject message) {
				System.out.println("test-topic : " + message );
				
			}
			
			@Override
			public Executor getExecutor() {
				// TODO Auto-generated method stub
				return null;
			}
		}) ;
		
		Thread.sleep(5 * 60 * 1000) ;
		
		consumer.shutdown() ;

	}

}
