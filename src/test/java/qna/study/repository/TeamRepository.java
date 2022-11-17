package qna.study.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import qna.study.domain.lazy.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
