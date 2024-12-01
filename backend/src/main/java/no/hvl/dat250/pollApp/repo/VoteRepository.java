package no.hvl.dat250.pollApp.repo;

import no.hvl.dat250.pollApp.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByOption_Poll_Id(Long pollId);
    List<Vote> findByUserId(Long userId);
}