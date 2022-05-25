package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionAndDeletedFalse(Long questionId);

    Answer findByWriter(User writer);

    Answer findByContents(String contents);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
