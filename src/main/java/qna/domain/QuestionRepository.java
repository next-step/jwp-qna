package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    List<Question> findByWriter(User writer);

    void deleteByWriterAndDeletedTrue(User writer);

    List<Question> findByContentsContaining(String contents);

    List<Question> findByWriterIn(Collection<User> writers);

    List<Question> findByWriterOrderByIdDesc(User writer);
}
