package qna.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	Optional<Question> findByIdAndDeletedFalse(Long id);
}
