package no.hvl.dat250.pollApp.service;

import no.hvl.dat250.pollApp.aggregation.VoteAggregationResult;
import no.hvl.dat250.pollApp.aggregation.VoteAggregationRepository;
import no.hvl.dat250.pollApp.entity.Poll;
import no.hvl.dat250.pollApp.aggregation.PollWithVotes;
import no.hvl.dat250.pollApp.repo.DomainManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PollService {

    private final DomainManager domainManager;
    private final VoteAggregationRepository voteAggregationRepository;

    public PollService(DomainManager domainManager, VoteAggregationRepository voteAggregationRepository) {
        this.domainManager = domainManager;
        this.voteAggregationRepository = voteAggregationRepository;
    }

    public List<PollWithVotes> getPollsWithVoteCounts() {
        List<Poll> polls = domainManager.getAllPolls().stream().toList();
        List<VoteAggregationResult> aggregatedVotes = voteAggregationRepository.aggregateVotesForAllPolls();

        // Group aggregated votes by pollId
        Map<String, List<VoteAggregationResult>> votesByPollId = aggregatedVotes.stream()
                .collect(Collectors.groupingBy(VoteAggregationResult::getPollId));

        // Map each Poll to PollWithVotes, including only relevant vote counts
        return polls.stream()
                .map(poll -> new PollWithVotes(
                        poll,
                        votesByPollId.getOrDefault(poll.getId(), List.of())
                ))
                .collect(Collectors.toList());
    }
}

