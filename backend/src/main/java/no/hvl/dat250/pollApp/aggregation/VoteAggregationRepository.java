package no.hvl.dat250.pollApp.aggregation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VoteAggregationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<VoteAggregationResult> aggregateVotesForAllPolls() {
        // Aggregation pipeline to group by pollId and voteOptionId and count the votes
        Aggregation aggregation = Aggregation.newAggregation(
                // Step 1: Group by pollId and voteOptionId, count the occurrences
                Aggregation.group("pollId", "voteOptionId").count().as("count"),

                // Step 2: Project the results, exclude the default _id and include pollId, voteOptionId, and count
                Aggregation.project("pollId", "voteOptionId", "count")
                        .andExclude("_id")
        );

        // Execute the aggregation query
        AggregationResults<VoteAggregationResult> results = mongoTemplate.aggregate(
                aggregation, "vote_message", VoteAggregationResult.class
        );

        // Return the mapped results (list of VoteAggregationResult objects)
        return results.getMappedResults();
    }
}
