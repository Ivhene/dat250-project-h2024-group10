package no.hvl.dat250.pollApp.controller;

import no.hvl.dat250.pollApp.service.PollService;
import no.hvl.dat250.pollApp.aggregation.PollWithVotes;
import no.hvl.dat250.pollApp.entity.Poll;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/polls")
public class PollController {

    private DomainManager domainManager;
    private PollService pollService;

    @Autowired
    public PollController(DomainManager domainManager, PollService pollService) {
        this.domainManager = domainManager;
        this.pollService = pollService;
    }

//    @GetMapping(produces = "application/json")
//    public ResponseEntity<List<Poll>> getPolls() {
//        List<Poll> polls = domainManager.getAllPolls().stream().toList();
//        return ResponseEntity.ok(polls);
//    }

    // Fetches both Polls and their voteOptions with the current count for each option
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Poll>> getPollsWithVoteCounts() {
        List<Poll> response = pollService.getPollsWithVoteCounts();
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        Poll createdPoll = domainManager.createPoll(poll);
        return ResponseEntity.status(201).body(createdPoll);
    }

    @GetMapping(value = "/{pollId}", produces = "application/json")
    public ResponseEntity<Poll> getPollById(@PathVariable Long pollId) {
        Poll poll = domainManager.getPollById(pollId);
        return ResponseEntity.ok(poll);
    }

    @PutMapping(value = "/{pollId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long pollId, @RequestBody Poll poll) {
        Poll updatedPoll = domainManager.createPoll(poll);
        return ResponseEntity.ok(updatedPoll);
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long pollId) {
        domainManager.deletePoll(pollId);
        return ResponseEntity.noContent().build();
    }
}
