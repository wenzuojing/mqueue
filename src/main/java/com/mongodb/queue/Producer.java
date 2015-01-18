package com.mongodb.queue;

import com.mongodb.BasicDBObject;

/**
 * @author wens
 */
public interface Producer {


    /**
     * 快速发送消息
     * @param topic
     * @param message
     */
	public void send(String topic, BasicDBObject message);

    /**
     * 安全方式发送，等待服务端响应结果
     * @param topic
     * @param message
     * @return
     */
	public boolean safeSend(String topic, BasicDBObject message);
	
	

}
