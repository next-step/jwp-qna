package qna.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    @EntityGraph(attributePaths = "answers")
    Optional<Question> findByIdAndDeletedFalse(Long id);
}
