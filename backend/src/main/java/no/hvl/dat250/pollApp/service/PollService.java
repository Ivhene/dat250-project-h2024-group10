package no.hvl.dat250.pollApp.service;

import no.hvl.dat250.pollApp.aggregation.VoteAggregationResult;
import no.hvl.dat250.pollApp.aggregation.VoteAggregationRepository;
import no.hvl.dat250.pollApp.entity.Poll;
import no.hvl.dat250.pollApp.entity.VoteOption;
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

    public List<Poll> getPollsWithVoteCounts() {
        List<Poll> polls = domainManager.getAllPolls().stream().toList();

        List<VoteAggregationResult> aggregatedVotes = voteAggregationRepository.aggregateVotesForAllPolls();

        Map<String, Map<String, Long>> votesByPoll = aggregatedVotes.stream()
                .collect(Collectors.groupingBy(
                        VoteAggregationResult::getPollId,
                        Collectors.toMap(
                                VoteAggregationResult::getVoteOptionId,
                                VoteAggregationResult::getCount
                        )
                ));

        votesByPoll.forEach((pollId, voteMap) -> {
            voteMap.forEach((voteOptionId, count) -> {
            });
        });


        // Assign vote counts to each poll option
        polls.forEach(poll -> {
            poll.getOptions().forEach(option -> {

                Map<String, Long> pollVotes = votesByPoll.get(poll.getId().toString());

                if (pollVotes != null) {
                    long voteCount = pollVotes.getOrDefault(option.getId().toString(), 0L);
                    option.setCount(voteCount);
                } else {
                    System.out.println("No votes found for Poll ID: " + poll.getId() + ", Option ID: " + option.getId());
                }
            });
        });

        return polls;
    }




}
