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
        // Fetch all polls
        List<Poll> polls = domainManager.getAllPolls().stream().toList();

        // Fetch aggregated vote counts
        List<VoteAggregationResult> aggregatedVotes = voteAggregationRepository.aggregateVotesForAllPolls();

        // Debugging: print the aggregated votes
        System.out.println("Aggregated Votes: " + aggregatedVotes);

        // Group the aggregated votes by pollId and voteOptionId, and map to counts
        Map<String, Map<String, Long>> votesByPoll = aggregatedVotes.stream()
                .collect(Collectors.groupingBy(
                        VoteAggregationResult::getPollId, // key by pollId
                        Collectors.toMap(
                                VoteAggregationResult::getVoteOptionId, // map each voteOptionId to its count
                                VoteAggregationResult::getCount
                        )
                ));

        // Debugging: print the grouped vote counts in a more readable format
        System.out.println("Votes by Poll (formatted): ");
        votesByPoll.forEach((pollId, voteMap) -> {
            System.out.println("Poll ID: " + pollId);
            voteMap.forEach((voteOptionId, count) -> {
                System.out.println("\tOption ID: " + voteOptionId + ", Count: " + count);
            });
        });

        // Assign vote counts to each poll option
        polls.forEach(poll -> {
            poll.getOptions().forEach(option -> {
                // Debugging: print the poll and option ID
                System.out.println("Poll ID: " + poll.getId() + ", Option ID: " + option.getId());

                // Get the vote count for the option, defaulting to 0 if not found
                Map<String, Long> pollVotes = votesByPoll.get(poll.getId()); // Get map for this poll

                if (pollVotes != null) {
                    long voteCount = pollVotes.getOrDefault(option.getId(), 0L); // Set count for this option
                    System.out.println("Set vote count for Option " + option.getId() + ": " + voteCount);
                    option.setCount(voteCount); // Set the count for this option
                } else {
                    System.out.println("No votes found for Poll ID: " + poll.getId() + ", Option ID: " + option.getId());
                }
            });
        });

        // Debugging: print the final poll options with updated vote counts
        polls.forEach(poll -> {
            poll.getOptions().forEach(option -> {
                System.out.println("Poll ID: " + poll.getId() + ", Option ID: " + option.getId() + ", Count: " + option.getCount());
            });
        });

        return polls;
    }




}
