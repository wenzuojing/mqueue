package com.mongodb.queue;

import java.util.concurrent.Executor;

import com.mongodb.BasicDBObject;


/**
 * 消息监听器
 * @author wens
 */
public interface MessageListener {

    /**
     *
     * @param message
     */
	public void recieveMessages(BasicDBObject message) ;

	public Executor getExecutor();

}
