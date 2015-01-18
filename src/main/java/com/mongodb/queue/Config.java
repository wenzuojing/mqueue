package com.mongodb.queue;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.MongoClientOptions.Builder;

/**
 * @author wens
 */
public class Config {
	

	private String queueDbName;

	private List<ServerAddress> mongoServers;

	private MongoClientOptions mongoOptions;

	public String getQueueDbName() {
		return queueDbName;
	}

	public void setQueueDbName(String queueDbName) {
		this.queueDbName = queueDbName;
	}

	public List<ServerAddress> getMongoServers() {
		return mongoServers;
	}

	public void setMongoServers(List<ServerAddress> mongoServers) {
		this.mongoServers = mongoServers;
	}

	public MongoClientOptions getMongoOptions() {
		return mongoOptions;
	}

	public void setMongoOptions(MongoClientOptions mongoOptions) {
		this.mongoOptions = mongoOptions;
	}

	public static Config parse(String confName) {
		InputStream inputStream = null;

		try {
			inputStream = Config.class.getResourceAsStream(confName);
		} catch (Exception e) {
			throw new ConfigException("Fail to read config [confName = " + confName + "]", e);
		}

		Properties pop = new Properties();

		try {
			pop.load(inputStream);
		} catch (Exception e) {
			throw new ConfigException("Fail to load config [confName = " + confName + "]", e);
		}

		Config config = new Config();
		String queueDbName = pop.getProperty("queue.db.name", "default-queue");
		config.setQueueDbName(queueDbName);

		String servers = pop.getProperty("mongodb.servers", "localhost:270017");

		config.setMongoServers(parseMongodbServers(servers));

		config.setMongoOptions(parserMongoClientOptions(pop));

		return config;

	}

	private static MongoClientOptions parserMongoClientOptions(Properties pop) {
		Builder builder = new MongoClientOptions.Builder();

		String acceptableLatencyDifference = pop.getProperty("acceptableLatencyDifference");
		String alwaysUseMBeans = pop.getProperty("alwaysUseMBeans");
		String autoConnectRetry = pop.getProperty("autoConnectRetry");
		String connectionsPerHost = pop.getProperty("connectionsPerHost");
		String connectTimeout = pop.getProperty("connectTimeout");
		String cursorFinalizerEnabled = pop.getProperty("cursorFinalizerEnabled");
		String description = pop.getProperty("description");
		String heartbeatConnectRetryFrequency = pop.getProperty("heartbeatConnectRetryFrequency");
		String heartbeatConnectTimeout = pop.getProperty("heartbeatConnectTimeout");
		String heartbeatFrequency = pop.getProperty("heartbeatFrequency");
		String heartbeatSocketTimeout = pop.getProperty("heartbeatSocketTimeout");
		String heartbeatThreadCount = pop.getProperty("heartbeatThreadCount");
		String maxAutoConnectRetryTime = pop.getProperty("maxAutoConnectRetryTime");
		String maxConnectionIdleTime = pop.getProperty("maxConnectionIdleTime");
		String maxConnectionLifeTime = pop.getProperty("maxConnectionLifeTime");
		String maxWaitTime = pop.getProperty("maxWaitTime");
		String minConnectionsPerHost = pop.getProperty("minConnectionsPerHost");
		String requiredReplicaSetName = pop.getProperty("requiredReplicaSetName");
		String socketKeepAlive = pop.getProperty("socketKeepAlive");
		String socketTimeout = pop.getProperty("socketTimeout");

		if (acceptableLatencyDifference != null) {
			builder.acceptableLatencyDifference(Integer.valueOf(acceptableLatencyDifference));
		}

		if (alwaysUseMBeans != null) {
			builder.alwaysUseMBeans(Boolean.valueOf(alwaysUseMBeans));
		}

		if (autoConnectRetry != null) {
			builder.autoConnectRetry(Boolean.valueOf(autoConnectRetry));
		}
		if (connectionsPerHost != null) {
			builder.connectionsPerHost(Integer.valueOf(connectionsPerHost));
		}

		if (connectTimeout != null) {
			builder.connectTimeout(Integer.valueOf(connectTimeout));
		}

		if (cursorFinalizerEnabled != null) {
			builder.cursorFinalizerEnabled(Boolean.valueOf(cursorFinalizerEnabled));
		}
		if (description != null) {
			builder.description(description);
		}
		if (heartbeatConnectRetryFrequency != null) {
			builder.heartbeatConnectRetryFrequency(Integer.valueOf(heartbeatConnectRetryFrequency));
		}
		if (heartbeatConnectTimeout != null) {
			builder.heartbeatConnectTimeout(Integer.valueOf(heartbeatConnectTimeout));
		}

		if (heartbeatFrequency != null) {
			builder.heartbeatFrequency(Integer.valueOf(heartbeatFrequency));
		}

		if (heartbeatSocketTimeout != null) {
			builder.heartbeatSocketTimeout(Integer.valueOf(heartbeatSocketTimeout));
		}

		if (heartbeatThreadCount != null) {
			builder.heartbeatThreadCount(Integer.valueOf(heartbeatThreadCount));
		}
		if (maxAutoConnectRetryTime != null) {
			builder.maxAutoConnectRetryTime(Long.valueOf(maxAutoConnectRetryTime));
		}

		if (maxConnectionIdleTime != null) {
			builder.maxConnectionIdleTime(Integer.valueOf(maxConnectionIdleTime));
		}

		if (maxConnectionLifeTime != null) {
			builder.maxConnectionLifeTime(Integer.valueOf(maxConnectionLifeTime));
		}

		if (maxWaitTime != null) {
			builder.maxWaitTime(Integer.valueOf(maxWaitTime));
		}

		if (minConnectionsPerHost != null) {
			builder.minConnectionsPerHost(Integer.valueOf(minConnectionsPerHost));
		}

		if (requiredReplicaSetName != null) {
			builder.requiredReplicaSetName(requiredReplicaSetName);
		}

		if (socketKeepAlive != null) {
			builder.socketKeepAlive(Boolean.valueOf(socketKeepAlive));
		}

		if (socketTimeout != null) {
			builder.socketTimeout(Integer.valueOf(socketTimeout));
		}
		return builder.build();
	}

	private static List<ServerAddress> parseMongodbServers(String servers) {
		List<ServerAddress> seeds = new LinkedList<ServerAddress>();

		for (String server : servers.split(",| ")) {

			String[] split = server.split(":");

			if (split.length == 0) {
				throw new ConfigException("Illegal mongod servers ");
			}

			String host = split[0];
			int port = split.length == 2 ? Integer.parseInt(split[1]) : 27017;

			seeds.add(newServerAddress(host, port));

		}
		return seeds;
	}

	private static ServerAddress newServerAddress(String host, int port) {
		try {
			return new ServerAddress(host, port);
		} catch (UnknownHostException e) {
			throw new ConfigException("Unknown host [host=" + host + "]");
		}
	}

}
