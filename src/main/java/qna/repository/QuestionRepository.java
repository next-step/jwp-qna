package qna.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import qna.domain.Question;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();
    Optional<Question> findByIdAndDeletedFalse(Long id);
    Optional<Question> findByWriterId(long writerId);
}
