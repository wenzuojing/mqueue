package com.mongodb.queue;

import java.util.concurrent.Executor;

/**
 * @author wens
 */
public interface Consumer {

    /**
     * 消息订阅
     * @param topic
     * @param listener
     */
	public void subscribe(String topic, MessageListener listener);

    /**
     * 关闭Consumer
     */
	public void shutdown() ;


}
