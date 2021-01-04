package com.example.springcloud.openmessage;

import io.openmessaging.Message;
import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.OMS;
import io.openmessaging.OMSBuiltinKeys;
import io.openmessaging.consumer.PushConsumer;
import io.openmessaging.internal.DefaultKeyValue;
import io.openmessaging.internal.MessagingAccessPointAdapter;
import io.openmessaging.rocketmq.domain.NonStandardKeys;

public class OMSPushConsumer {
    public static void main(String[] args) {
        DefaultKeyValue defaultKeyValue = new DefaultKeyValue();
        defaultKeyValue.put(NonStandardKeys.CONSUMER_GROUP, "consume1");
        defaultKeyValue.put(OMSBuiltinKeys.CONSUMER_ID, "2");
        defaultKeyValue.put(OMSBuiltinKeys.REGION, "OMS_HELLO_TOPIC");

        final MessagingAccessPoint messagingAccessPoint = MessagingAccessPointAdapter.getMessagingAccessPoint("oms:rocketmq://localhost:9876/TopicTest", defaultKeyValue);
        final PushConsumer consumer = messagingAccessPoint.createPushConsumer(OMS.newKeyValue().put(NonStandardKeys.CONSUMER_GROUP, "OMS_CONSUMER"));

        consumer.attachQueue("OMS_HELLO_TOPIC", (message, context) -> {
            System.out.println("body:>>>" + new String(message.getBody(byte[].class)));
            System.out.printf("Received one message: %s%n", message.sysHeaders().getString(Message.BuiltinKeys.MESSAGE_ID));
            context.ack();
        });

        messagingAccessPoint.startup();
        System.out.printf("MessagingAccessPoint startup OK%n");

        consumer.startup();
        System.out.printf("Consumer Started.%n");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            consumer.shutdown();
            messagingAccessPoint.shutdown();
        }));
    }
}
