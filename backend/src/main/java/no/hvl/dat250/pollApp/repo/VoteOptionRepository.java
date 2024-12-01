package no.hvl.dat250.pollApp.repo;

import no.hvl.dat250.pollApp.entity.VoteOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    List<VoteOption> findByPollId(Long pollId);
}