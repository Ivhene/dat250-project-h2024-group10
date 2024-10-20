package no.hvl.dat250.pollApp.service;

import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class PollAggregator {

    private final static String QUEUE_NAME = "poll";
    private final static String EXCHANGE_NAME = "vote_events";
    private Connection connection;
    private Channel channel;

    @PostConstruct
    public void startListening() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            // Establish connection and channel (no try-with-resources)
            connection = factory.newConnection();
            channel = connection.createChannel();

            // Declare exchange and queue
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String routingKey = "vote.cast";
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);

            System.out.println("Waiting for messages. To exit press CTRL+C");

            // Define the consumer callback
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                aggregateVoteData(message);
            };

            // Start consuming messages asynchronously
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("Failed to start RabbitMQ listener: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void aggregateVoteData(String voteData) {
        // Add aggregation logic here
        System.out.println("Aggregating vote data: " + voteData);
    }

    @PreDestroy
    public void cleanUp() {
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("RabbitMQ connection closed.");
        } catch (Exception e) {
            System.err.println("Error while closing RabbitMQ connection: " + e.getMessage());
        }
    }
}
