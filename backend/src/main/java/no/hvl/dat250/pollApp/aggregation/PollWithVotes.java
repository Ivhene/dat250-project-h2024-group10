package no.hvl.dat250.pollApp.aggregation;

import no.hvl.dat250.pollApp.entity.Poll;

import java.util.List;

public class PollWithVotes {

    private Poll poll;
    private List<VoteAggregationResult> voteCounts;

    public PollWithVotes(Poll poll, List<VoteAggregationResult> voteCounts) {
        this.poll = poll;
        this.voteCounts = voteCounts;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public List<VoteAggregationResult> getVoteCounts() {
        return voteCounts;
    }

    public void setVoteCounts(List<VoteAggregationResult> voteCounts) {
        this.voteCounts = voteCounts;
    }
}

