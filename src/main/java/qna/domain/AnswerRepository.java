package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndDeletedFalse(Long id);
    Optional<Answer> findByIdAndDeletedTrue(Long id);
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);
    List<Answer> findByWriterIdAndQuestionId(Long writerId, Long questionId);
    List<Answer> findByContentsStartingWith(String content);
    List<Answer> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
