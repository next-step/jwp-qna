package qna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
	List<Answer> findByQuestion_IdAndDeletedFalse(@Param(value = "questionId") Long questionId);

	Optional<Answer> findByIdAndDeletedFalse(Long id);
}
