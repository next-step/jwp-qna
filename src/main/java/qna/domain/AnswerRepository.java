package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    List<Answer> findByQuestionAndDeletedFalse(Question question);

    List<Answer> findByWriterAndDeletedFalse(User writer);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
