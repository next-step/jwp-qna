package qna.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findByIdAndDeletedFalse(Long id);

    @Query("SELECT q FROM Question q JOIN FETCH q.answers.values a "
        + "WHERE q.id = :id AND q.deleted = false")
    Optional<Question> findByIdAndDeletedFalseWithAnswers(Long id);
}
