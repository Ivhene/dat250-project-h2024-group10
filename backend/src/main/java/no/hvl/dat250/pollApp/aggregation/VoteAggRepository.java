package no.hvl.dat250.pollApp.aggregation;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteAggRepository extends MongoRepository<VoteMessage, String> {

}
