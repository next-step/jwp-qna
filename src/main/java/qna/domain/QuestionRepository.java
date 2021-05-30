package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    List<Question> findByWriterId(Long id);

    void deleteByWriterIdAndDeletedTrue(Long id);

    List<Question> findByContentsContaining(String contents);

    List<Question> findByWriterIdIn(Collection<Long> writers);

    List<Question> findByWriterIdOrderByIdDesc(Long writerId);
}
