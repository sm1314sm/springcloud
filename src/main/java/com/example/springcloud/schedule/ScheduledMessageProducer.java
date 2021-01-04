package com.example.springcloud.schedule;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;

public class ScheduledMessageProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ExampleProducerGroup");
        producer.setNamesrvAddr("localhost:9876");
        producer.start();
        for (int i = 0; i < 100; i++) {
            Message message = new Message("TestTopic", ("Hello scheduled message " + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            message.setDelayTimeLevel(3);
            producer.send(message);
        }
        producer.shutdown();
    }
}
