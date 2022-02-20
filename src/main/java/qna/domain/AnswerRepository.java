package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

//@Repository 대신에 JpaRepository 통해서 save 같은 메서드 제공
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionIdAndDeletedFalse(Long questionId);

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}
