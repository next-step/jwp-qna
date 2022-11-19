package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import qna.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    @Query("select q " +
            "from Question q " +
            "left join fetch q.answers.answerItems a " +
            "where q.id = :id")
    Optional<Question> findByIdAndDeletedFalse(Long id);
}
