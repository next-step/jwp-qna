package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import qna.domain.Answer;
import qna.domain.Question;
import qna.domain.User;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndDeletedFalse(Long id);
    Optional<Answer> findByIdAndDeletedTrue(Long id);
    List<Answer> findByContentsStartingWith(String content);
    List<Answer> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Answer> findByQuestionAndDeletedFalse(Question question);
    List<Answer> findByWriterAndQuestion(User writer, Question question);
}
