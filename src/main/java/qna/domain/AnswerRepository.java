package qna.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Answer a set a.deleted=true where a.id in :ids")
    int updateDeleteOfAnswers(@Param("ids") List<Long> answerIds);

    @Override
    List<Answer> findAllById(Iterable<Long> longs);
}
