package qna.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	Optional<Question> findByIdAndDeletedFalse(Long id);
}
