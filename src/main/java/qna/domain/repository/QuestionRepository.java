package qna.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import qna.domain.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByTitle(String title);

    Optional<Question> findByIdAndDeletedFalse(Long id);
}
