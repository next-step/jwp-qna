package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findByTitle(String title);
    Question findByWriter(User write);
    List<Question> findByDeletedFalse();
    Optional<Question> findByIdAndDeletedFalse(Long id);
}
