package com.mongodb.queue;


/**
 * @author wens
 */
public class ConfigHelper {

	private static final Config config = Config.parse("/mongodb-queue.properties");

	public static Config getConfig() {
		return config;
	}

}
