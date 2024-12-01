package no.hvl.dat250.pollApp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class VoteOption {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("caption")
    private String caption;

    @JsonProperty("presentationOrder")
    private int presentationOrder;

    @ManyToOne
    @JsonBackReference("poll-options") // Unique reference name
    @JsonProperty("poll")
    private Poll poll;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL)
    @JsonManagedReference("option-votes") // Unique reference name
    @JsonProperty("votes")
    private List<Vote> votes = new ArrayList<>();

    public VoteOption() {
    }

    public VoteOption(String caption, int presentationOrder, Poll poll) {
        this.caption = caption;
        this.presentationOrder = presentationOrder;
        this.poll = poll;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("caption")
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @JsonProperty("presentationOrder")
    public int getPresentationOrder() {
        return presentationOrder;
    }

    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }

    @JsonProperty("poll")
    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @JsonProperty("votes")
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }
}
