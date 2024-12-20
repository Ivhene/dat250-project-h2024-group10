package no.hvl.dat250.pollApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonIgnore
    private String salt;

    @OneToMany(mappedBy = "createdUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("user-polls") // Unique reference name
    @JsonProperty("polls")
    private List<Poll> polls = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference("user-votes") // Unique reference name
    @JsonProperty("votes")
    private List<Vote> votes = new ArrayList<>();

    public User() {
        this.username = "";
        this.email = "";
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @JsonProperty("polls")
    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    @JsonProperty("votes")
    public List<Vote> getVotes() {
        return votes;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
