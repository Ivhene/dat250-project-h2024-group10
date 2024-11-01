package no.hvl.dat250.pollApp.aggregation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VoteAggregationResult {

    @JsonProperty("voteOptionId")
    private String voteOptionId; // ID of the vote option

    @JsonProperty("count")
    private long count; // Number of votes for this option

    // Constructor
    public VoteAggregationResult(String voteOptionId, long count) {
        this.voteOptionId = voteOptionId;
        this.count = count;
    }

    // Getters and Setters
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

    @Override
    public String toString() {
        return "VoteAggregationResult{" +
                "voteOptionId='" + voteOptionId + '\'' +
                ", count=" + count +
                '}';
    }
}

