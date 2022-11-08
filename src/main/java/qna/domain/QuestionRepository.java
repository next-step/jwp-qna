package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);
}
