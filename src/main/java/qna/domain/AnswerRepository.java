package qna.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findByIdAndDeletedFalse(Long id);

    @Override
    default void deleteById(Long aLong) {
        throw new UnsupportedOperationException("해당 메소드를 사용해 답변을 삭제할 수 없습니다.");
    }
}
