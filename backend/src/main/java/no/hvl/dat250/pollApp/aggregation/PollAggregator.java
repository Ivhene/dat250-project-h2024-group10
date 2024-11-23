package no.hvl.dat250.pollApp.aggregation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollAggregator {

    private final static String QUEUE_NAME = "poll";
    private final static String EXCHANGE_NAME = "vote_events";
    private Connection connection;
    private Channel channel;

    @Autowired
    private VoteRepository voteRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void startListening() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");

            // Establish connection and channel (no try-with-resources)
            connection = factory.newConnection();
            channel = connection.createChannel();

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String routingKey = "vote.cast";
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, routingKey);

            System.out.println("Waiting for messages. To exit press CTRL+C");

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println("Received '" + delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
                saveVoteData(message);
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (Exception e) {
            System.err.println("Failed to start RabbitMQ listener: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveVoteData(String voteData) {
        System.out.println("Processing vote data: " + voteData);
        try {
            VoteMessage voteMessage = objectMapper.readValue(voteData, VoteMessage.class);

            voteRepository.save(voteMessage);

            System.out.println("Saved individual vote for pollId " + voteMessage.getPollId() + " and optionId " + voteMessage.getVoteOptionId());
        } catch (Exception e) {
            System.err.println("Error while saving vote data: " + e.getMessage());
            e.printStackTrace();
        }
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
