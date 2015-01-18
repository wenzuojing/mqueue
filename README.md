

#mqueue

mqueue是一个基于mongodb的简单消息队列，其的使用方式和其他MQ基本相同，只是在一些名词命名上有些不同。为了更好的讨论，这里对这些名词做简单解释。

* Producer：就是网发消息的客户端
* Consumer：从取消息的客户端
* Topic （T）：可以理解为一个队列
* Consumer Group （CG）：一个 topic可以有多个CG。topic的消息会复制（不是真的复制，是概念上的）到所有的CG，但每个CG只会把消息发给该CG中的一个 consumer。如果需要实现广播，只要每个consumer有一个独立的CG就可以了。要实现单播只要所有的consumer在同一个CG。用CG还 可以将consumer进行自由的分组而不需要多次发送消息到不同的topic。

#使用方式

设置mongodb-queue.properties

```
#消息队列使用的库名
queue.db.name=test-queue
＃mongodb server 地址
mongodb.servers=localhost:28010
```
消息发送

```java
        Producer producer  = new SimpleProducer() ;
    	
		BasicDBObject message  = new BasicDBObject() ;
		
		message.put("name", "wens") ;
		message.put("age", 28) ;
        
        producer.send("test-topic2", message ) ;
```

消息接收

```java
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
```

