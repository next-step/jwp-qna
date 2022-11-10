package qna.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qna.domain.Answer;
import qna.domain.Question;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionAndDeletedFalse(Question question);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
