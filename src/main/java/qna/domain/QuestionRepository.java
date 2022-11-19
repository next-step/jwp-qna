package qna.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(
        "select distinct q from Question q "
        + "left join fetch q.answers.values "
        + "where q.id = :id "
        + "and q.deleted = false"
    )
    Optional<Question> findByIdAndDeletedFalse(@Param("id") Long id);
}
