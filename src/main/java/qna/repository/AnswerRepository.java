package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.Answer;
import qna.domain.User;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);

    List<Answer> findByWriterAndDeletedFalse(User writer);
}
