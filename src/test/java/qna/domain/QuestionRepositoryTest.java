package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.QuestionTest.Q1;
import static qna.domain.QuestionTest.Q2;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문이 저장된다.")
    void save() {
        Question q1 = questionRepository.save(Q1);

        final Optional<Question> findQ1 = questionRepository.findById(q1.getId());

        assertAll(() -> {
            assertThat(findQ1.isPresent()).isTrue();
            assertThat(findQ1.get()).isEqualTo(q1);
        });
    }

    @Test
    @DisplayName("질문이 변경된다.")
    void update() {
        Question q1 = questionRepository.save(Q1);
        q1.setContents("변경데이터");

        final Optional<Question> findQ1 = questionRepository.findById(q1.getId());

        assertAll(() -> {
            assertThat(findQ1.isPresent()).isTrue();
            assertThat(findQ1.get().getContents()).isEqualTo("변경데이터");
        });

        questionRepository.flush();
    }

    @Test
    @DisplayName("질문이 삭제된다")
    void delete() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);

        questionRepository.delete(q1);

        assertThat(questionRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제가 안된 질문을 조회한다.")
    void findByDeletedFalse() {
        questionRepository.save(Q1);
        questionRepository.save(Q2);

        final List<Question> questions = questionRepository.findByDeletedFalse();

        assertThat(questions).hasSize(2);
    }

    @Test
    @DisplayName("삭제가 안된 질문을 ID 값으로 조회한다.")
    void findByIdDeletedFalse() {
        Question q1 = questionRepository.save(Q1);
        Question q2 = questionRepository.save(Q2);
        q1.setDeleted(true);


        Optional<Question> findQ1 = questionRepository.findByIdAndDeletedFalse(q1.getId());
        Optional<Question> findQ2 = questionRepository.findByIdAndDeletedFalse(q2.getId());

        assertAll(() -> {
            assertThat(findQ1.isPresent()).isFalse();
            assertThat(findQ2.isPresent()).isTrue();
        });
    }
}