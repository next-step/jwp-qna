package qna.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.study.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
