package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.entity.Vote;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("polls/{pollId}/voteoptions/{voteoptionId}/votes")
public class VoteController {

    private static final String EXCHANGE_NAME = "vote_events";

    @Autowired
    private DomainManager domainManager;

    @GetMapping
    public ResponseEntity<List<Vote>> getVotes(@PathVariable String pollId) {
        List<Vote> votes = new ArrayList<>(domainManager.getVotesByPollId(pollId));

        return ResponseEntity.ok(votes);
    }

    @PostMapping
    public ResponseEntity<Vote> createVote(@PathVariable String pollId, @PathVariable String voteoptionId, @RequestBody Vote vote) {
        Vote submittedVote = domainManager.castVote(voteoptionId, vote);

        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                channel.exchangeDeclare(EXCHANGE_NAME, "topic");

                String message = String.format("{\"pollId\":\"%s\",\"voteOptionId\":\"%s\",\"voteId\":\"%s\"}",
                        pollId, voteoptionId, vote.getId());

                String routingKey = "vote.cast";
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println("Sent message: " + message);

            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return ResponseEntity.ok(submittedVote);
    }

    @DeleteMapping("/{voteId}")
    public ResponseEntity<Vote> deleteVote(@PathVariable String voteId) {
        domainManager.deleteVote(voteId);

        return ResponseEntity.noContent().build();
    }
}
