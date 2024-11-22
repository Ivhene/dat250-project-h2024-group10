package no.hvl.dat250.pollApp.aggregation;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "vote_message")
public class VoteMessage {

    @Id
    private String id;
    private String pollId;
    private String voteOptionId;
    private String voteId;

    public VoteMessage() {
    }

    public VoteMessage(String pollId, String voteOptionId, String voteId) {
        this.pollId = pollId;
        this.voteOptionId = voteOptionId;
        this.voteId = voteId;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "VoteMessage{" +
                "pollId='" + pollId + '\'' +
                ", voteOptionId='" + voteOptionId + '\'' +
                ", voteId='" + voteId + '\'' +
                '}';
    }
}
