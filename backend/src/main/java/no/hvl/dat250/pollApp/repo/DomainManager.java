package no.hvl.dat250.pollApp.repo;

import jakarta.transaction.Transactional;
import no.hvl.dat250.pollApp.entity.Poll;
import no.hvl.dat250.pollApp.entity.User;
import no.hvl.dat250.pollApp.entity.Vote;
import no.hvl.dat250.pollApp.entity.VoteOption;
import no.hvl.dat250.pollApp.security.AuthRequest;
import no.hvl.dat250.pollApp.security.AuthResponse;
import no.hvl.dat250.pollApp.security.JwtUtil;
import no.hvl.dat250.pollApp.service.PasswordManager;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Component
public class DomainManager {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private VoteOptionRepository voteOptionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    // User Management

    public User addUser(User user) {
        String salt = PasswordManager.generateSalt();
        user.setSalt(salt);
        user.setPassword(PasswordManager.hashing(user.getPassword(), salt));
        return userRepository.save(user);
    }

    public User updateUser(String username , User user) {
        // Allows users to change username
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User getUserById(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(String username) {
        // remove the user itself
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
        // remove the users polls and votes
        for(Poll poll : user.getPolls()) {
            pollRepository.delete(poll);
            // Remove the options in the poll
            for(VoteOption option : poll.getOptions()) {
                voteOptionRepository.delete(option);

                // remove the votes on the poll
                for(Vote vote : option.getVotes()) {
                    voteRepository.delete(vote);
                }
            }
        }
        for(Vote vote : user.getVotes()) {
            voteRepository.delete(vote);
        }
    }

    // Poll Management

    public Poll createPoll(Poll poll) {
        // Ensure options list is initialized and has at least 2 options
        if (poll.getOptions() == null || poll.getOptions().size() < 2) {
            throw new IllegalArgumentException("A poll must have at least 2 options.");
        }

        // Ensure the `createdUser` is managed (already saved in the database)
        if (poll.getCreatedUser() != null) {
            User existingUser = userRepository.findByUsername(poll.getCreatedUser().getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            poll.setCreatedUser(existingUser); // Attach the managed `User` entity
        } else {
            throw new IllegalArgumentException("A poll must have a valid createdUser.");
        }

        // Save the poll entity
        pollRepository.save(poll);

        // Set the poll reference for each option and save them
        for (VoteOption option : poll.getOptions()) {
            option.setPoll(poll); // Set the poll reference for each option
            voteOptionRepository.save(option);
        }

        return poll;
    }



    public Poll getPollById(Long pollId) {
        return pollRepository.getReferenceById(pollId);
    }

    public List<Poll> getAllPolls() {
        List<Poll> polls = pollRepository.findAll();
        polls.forEach(poll -> Hibernate.initialize(poll.getOptions()));
        return polls;
    }

    public void deletePoll(Long pollId) {
        // removes the poll
        Poll poll = pollRepository.getReferenceById(pollId);
        pollRepository.delete(poll);

        // removes the votes
        for(Vote vote : voteRepository.findAll()) {
            if (vote.getOption().getPoll().equals(poll)) {
                voteRepository.delete(vote);
            }
        }

        // remove all options
        for(VoteOption voteOption : poll.getOptions()) {
            voteOptionRepository.delete(voteOption);
        }

        // remove the poll from the user
        User user = poll.getCreatedUser();
        user.getPolls().remove(poll);
        userRepository.save(user);
    }

    // VoteOption Management

    public VoteOption getVoteOptionById(Long voteOptionId) {
        return voteOptionRepository.getReferenceById(voteOptionId);
    }

    public List<VoteOption> getVoteOptionsByPollId(Long pollId) {
        List<VoteOption> options = voteOptionRepository.findAll(); // Replace with proper filter
        options.forEach(option -> Hibernate.initialize(option.getVotes())); // Initialize lazy collection
        return options;
    }

    // Vote Management

    public Vote castVote(Long voteOptionId, Vote vote) {
        if (vote.getUser() != null) {
            User existingUser = userRepository.findByUsername(vote.getUser().getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            vote.setUser(existingUser); // Attach the managed `User` entity
        }

        VoteOption option = voteOptionRepository.findById(voteOptionId).orElseThrow(() -> new IllegalArgumentException("Invalid Vote Option ID"));
        vote.setOption(option);

        voteRepository.save(vote);

        return vote;
    }

    public Collection<Vote> getVotesByPollId(Long pollId) {
        List<Vote> pollVotes = new ArrayList<>();
        for (Vote vote : voteRepository.findAll()) {
            if (vote.getOption().getPoll().getId().equals(pollId)) {
                pollVotes.add(vote);
            }
        }
        return pollVotes;
    }

    public Collection<Vote> getVotesByUserId(String userId) {
        List<Vote> userVotes = new ArrayList<>();
        for (Vote vote : voteRepository.findAll()) {
            if (vote.getUser().getUsername().equals(userId)) {
                userVotes.add(vote);
            }
        }
        return userVotes;
    }

    public void deleteVote(Long voteId) {
        // Remove the vote itself
        Vote vote = voteRepository.getReferenceById(voteId);
        voteRepository.delete(vote);

        // Remove the vote from the user object
        vote.getUser().getVotes().remove(vote);
        userRepository.save(vote.getUser());

        // Remove the vote from the voteOption and poll objects
        vote.getOption().getVotes().remove(vote);
        voteOptionRepository.save(vote.getOption());
    }

    public AuthResponse authenticate(AuthRequest authRequest) {

        String token;
        UserDetails userDetails;
        // Load user details
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
            token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
        } catch (AuthenticationException e) {
            System.out.println("UsernameNotFoundException");
            userDetails = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                    .username(authRequest.getUsername())
                    .password(authRequest.getPassword())
                    .authorities("ROLE_USER")
                    .build();


            // Add the user to the UserDetailsService (only works with InMemoryUserDetailsManager)
            if (userDetailsService instanceof InMemoryUserDetailsManager) {
                ((InMemoryUserDetailsManager) userDetailsService).createUser(userDetails);
            } else {
                throw new RuntimeException("UserDetailsService does not support adding new users");
            }
            token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities());
        }

        // Generate the JWT token
        return new AuthResponse(token);
    }








}
