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

    public List<VoteAggregationResult> aggregateVotesForAllPolls() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("pollId", "optionId")
                        .count().as("count"),
                Aggregation.project("count")
                        .and("pollId").previousOperation()
                        .and("optionId").previousOperation()
        );

        AggregationResults<VoteAggregationResult> results = mongoTemplate.aggregate(
                aggregation, "votes", VoteAggregationResult.class
        );

        return results.getMappedResults();
    }
}
