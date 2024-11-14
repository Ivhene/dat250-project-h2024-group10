package no.hvl.dat250.pollApp.aggregation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteAggregationResult {

    @JsonProperty("pollId")
    private String pollId;

    @JsonProperty("voteOptionId")
    private String voteOptionId;

    @JsonProperty("count")
    private long count;

    // Constructor
    public VoteAggregationResult(String pollId, String voteOptionId, long count) {
        this.pollId = pollId;
        this.voteOptionId = voteOptionId;
        this.count = count;
    }

    // Getters and Setters
    public String getPollId() {
        return pollId;
    }

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public String getVoteOptionId() {
        return voteOptionId;
    }

    public void setVoteOptionId(String voteOptionId) {
        this.voteOptionId = voteOptionId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
