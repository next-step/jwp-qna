package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionAndDeletedFalse(Question question);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
