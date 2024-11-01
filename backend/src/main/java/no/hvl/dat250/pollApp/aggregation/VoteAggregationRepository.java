package no.hvl.dat250.pollApp.aggregation;

import no.hvl.dat250.pollApp.aggregation.VoteAggregationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoteAggregationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<VoteAggregationResult> aggregateVotesByPollId(String pollId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("pollId").is(pollId)), // Match by pollId
                Aggregation.group("optionId") // Group by vote option ID
                        .count().as("count"), // Count votes for each option
                Aggregation.project("count").and("optionId").previousOperation() // Project the results
        );

        // Execute the aggregation query and map to VoteAggregationResult class
        AggregationResults<VoteAggregationResult> results = mongoTemplate.aggregate(
                aggregation, "votes", VoteAggregationResult.class // "votes" is the MongoDB collection
        );

        return results.getMappedResults(); // Return a list of VoteAggregationResult objects
    }
}
