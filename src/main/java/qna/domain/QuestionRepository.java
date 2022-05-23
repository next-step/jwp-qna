package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    @Query(value = "select distinct q from Question q "
        + "left join fetch q.answers.answers "
        + "where q.id = :id "
        + "and q.deleted = false ")
    Optional<Question> findByIdAndDeletedFalse(Long id);
}
