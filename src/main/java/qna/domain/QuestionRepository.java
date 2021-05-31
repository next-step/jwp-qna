package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    @Query(value = "select q from Question q left join q.answers a on a.deleted = false where q.id = :id")
    Optional<Question> getOneNotDeletedById(@Param("id") Long id);
}
