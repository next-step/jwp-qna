package qna.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.entity.Answer;


public interface AnswerRepository extends JpaRepository<Answer, Long> {

	Optional<Answer> findByIdAndDeletedFalse(Long id);
}
