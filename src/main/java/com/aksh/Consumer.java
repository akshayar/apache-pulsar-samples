package com.aksh;

import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionType;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Consumer {

    public static void main(String[] args) throws Exception{
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar://localhost:6650")
                .build();

        org.apache.pulsar.client.api.Consumer consumer = client.newConsumer()
                .topic("data-pocdb.products")
                .subscriptionName("my-subscription")
                .ackTimeout(10, TimeUnit.SECONDS)
                .subscriptionType(SubscriptionType.Exclusive)
                .subscribe();

        while (true) {
            // Wait for a message
            Message msg = consumer.receive();

            try {
                // Do something with the message
                System.out.printf("Message received: %s", new String(msg.getData()));

                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }


    }
}
