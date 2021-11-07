package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
  List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

  Optional<Answer> findByIdAndDeletedFalse(Long id);

  List<Answer> findByWriterId(Long writerId);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Answer a set a.contents = ?2 where a.id = ?1")
  int updateContentsById(Long id, String contents);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Answer a set a.deleted = ?2 where a.id = ?1")
  int updateDeletedById(Long id, boolean deleted);
}
