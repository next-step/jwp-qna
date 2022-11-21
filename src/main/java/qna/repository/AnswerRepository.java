package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
