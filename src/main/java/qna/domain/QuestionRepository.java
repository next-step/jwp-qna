package qna.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findByDeletedFalse();


    Optional<Question> findByIdAndDeletedIsFalse(Long id);

    Optional<Question> findByIdAndDeletedFalse(Long id);

    @Modifying(clearAutomatically = true)
    @Query("update Question q set q.deleted=true where q.id in :ids")
    int updateDeleteOfQuestions(@Param("ids") List<Long> questionIds);

    @Query("select q from Question q where q.id in :ids")
    List<Question> findAllByIds(@Param("ids") List<Long> questionIds);
}
