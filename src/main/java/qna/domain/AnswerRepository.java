package qna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndDeletedFalse(Long id);

    Answer findByWriterId(Long id);

    List<Answer> findByQuestionId(Long id);
}
