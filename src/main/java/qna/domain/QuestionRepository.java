package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import qna.domain.field.Title;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    List<Question> findByWriter(User writer);

    Long countByWriter(User writer);

    List<Question> findByTitle(Title title);

    Long countByTitle(Title title);

    void deleteByWriter(User writer);
}
