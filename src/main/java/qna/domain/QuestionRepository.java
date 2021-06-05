package qna.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    Optional<Question> findByIdAndDeletedTrue(Long id);

    @Query(value = "SELECT q FROM Question q WHERE q.title = :title")
    List<Question> findByTitle(@Param("title") Title title);

    List<Question> findByContentsContains(String content);

}
