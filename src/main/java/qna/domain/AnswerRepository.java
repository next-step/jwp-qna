package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findByWriterAndDeletedFalse(User writer);
    List<Answer> findByQuestionAndDeletedFalse(Question question);
    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
