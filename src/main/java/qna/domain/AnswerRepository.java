package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
  List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

  Optional<Answer> findByIdAndDeletedFalse(Long id);

  List<Answer> findByWriterId(Long writerId);

  @Modifying
  @Query("update Answer a set a.contents = :contents where a.id = :id")
  int updateContentsById(@Param("id") Long id, @Param("contents") String contents);

  @Modifying
  @Query("update Answer a set a.deleted = :deleted where a.id = :id")
  int updateDeletedById(@Param("id") Long id, @Param("deleted") boolean deleted);

  @Transactional
  void deleteAll();

  @Transactional
  void deleteById(Long id);
}
