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
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("pollId", "voteOptionId").count().as("count"),
                Aggregation.project("pollId", "voteOptionId", "count")
                        .andExclude("_id")
        );
        AggregationResults<VoteAggregationResult> results = mongoTemplate.aggregate(
                aggregation, "vote_message", VoteAggregationResult.class
        );
        return results.getMappedResults();
    }
}
