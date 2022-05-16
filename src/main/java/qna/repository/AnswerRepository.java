package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import qna.domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
