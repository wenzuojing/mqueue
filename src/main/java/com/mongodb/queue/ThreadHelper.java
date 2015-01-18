package com.mongodb.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


/**
 * @author wens
 */
public class ThreadHelper {

	public static ExecutorService newExecutor(final String name, int num) {
		return Executors.newFixedThreadPool(num, new ThreadFactory() {
			private int i = 0;

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, name + "_" + String.valueOf(i++));
			}
		});
	}

}
