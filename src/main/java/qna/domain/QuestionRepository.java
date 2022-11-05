package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    @Override
    default void deleteById(Long aLong) {
        throw new UnsupportedOperationException("해당 메소드를 사용해 질문을 삭제할 수 없습니다.");
    }
}
