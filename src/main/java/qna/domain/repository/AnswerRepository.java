package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import qna.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
