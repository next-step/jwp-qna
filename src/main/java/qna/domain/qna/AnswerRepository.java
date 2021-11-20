package qna.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import qna.domain.qna.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
