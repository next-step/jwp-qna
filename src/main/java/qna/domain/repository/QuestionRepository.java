package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import qna.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndDeletedFalse(Long id);
}
