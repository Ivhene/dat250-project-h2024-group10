package no.hvl.dat250.pollApp.aggregation;

import no.hvl.dat250.pollApp.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<VoteMessage, String> {

    List<VoteMessage> findByPollId(String pollId);

    List<VoteMessage> findByVoteOptionId(String voteOptionId);
}
