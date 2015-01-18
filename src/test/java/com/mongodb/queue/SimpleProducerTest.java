package com.mongodb.queue;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;

import com.mongodb.BasicDBObject;

public class SimpleProducerTest {
	
	private AtomicLong counter  = new AtomicLong() ;
	
	
	@Test
	public void testSend(){
		
		Producer producer  = new SimpleProducer() ;
		
		BasicDBObject message  = new BasicDBObject() ;
		
		message.put("name", "zuojing wen") ;
		message.put("age", 28) ;
		
		
		new Thread(){
			
			private long pre  =0 ;
			
			public void run() {
				
				while(true){
					System.out.println("speed :" +( counter.longValue() -pre ) + " tps");
					pre  =  counter.get() ;
					
					try {
						Thread.sleep(1000) ;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start() ;
		
		while(true){
			producer.send("test-topic2", message ) ;
			counter.incrementAndGet() ;
		}
		
	}

}
