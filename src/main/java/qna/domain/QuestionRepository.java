package qna.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDeletedFalse();

    List<Question> findByDeletedTrue();

    Optional<Question> findByIdAndDeletedFalse(Long id);
}
