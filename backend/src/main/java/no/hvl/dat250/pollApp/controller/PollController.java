package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.aggregation.PollWithVotes;
import no.hvl.dat250.pollApp.aggregation.VoteAggregationRepository;
import no.hvl.dat250.pollApp.aggregation.VoteAggregationResult;
import no.hvl.dat250.pollApp.entity.Poll;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private DomainManager domainManager;
    private VoteAggregationRepository voteAggregationRepository;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Poll>> getPolls() {
        List<Poll> polls = domainManager.getAllPolls().stream().toList();
        return ResponseEntity.ok(polls);
    }

    // Both Fetches both Polls and their voteOptions with the current count for each option
    @GetMapping(value = "/polls", produces = "application/json")
    public ResponseEntity<List<PollWithVotes>> getPollsWithVoteCounts() {
        List<Poll> polls = domainManager.getAllPolls().stream().toList();
        List<VoteAggregationResult> aggregatedVotes = voteAggregationRepository.aggregateVotesForAllPolls();

        Map<String, List<VoteAggregationResult>> votesByPollId = aggregatedVotes.stream()
                .collect(Collectors.groupingBy(VoteAggregationResult::getPollId));

        // Map each Poll to PollWithVotes, including only relevant vote counts
        List<PollWithVotes> response = polls.stream()
                .map(poll -> new PollWithVotes(
                        poll,
                        votesByPollId.getOrDefault(poll.getId(), List.of())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        Poll createdPoll = domainManager.createPoll(poll);
        return ResponseEntity.status(201).body(createdPoll);
    }

    @GetMapping(value = "/{pollId}", produces = "application/json")
    public ResponseEntity<Poll> getPollById(@PathVariable String pollId) {
        Poll poll = domainManager.getPollById(pollId);
        return ResponseEntity.ok(poll);
    }

    @PutMapping(value = "/{pollId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Poll> updatePoll(@PathVariable String pollId, @RequestBody Poll poll) {
        Poll updatedPoll = domainManager.createPoll(poll);
        return ResponseEntity.ok(updatedPoll);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable String pollId) {
        domainManager.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }
}
