package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import qna.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
