package qna.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.domain.Question;
import qna.domain.QuestionTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("질문을 저장할 수 있다")
    @Test
    void save() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.isDeleted()).isEqualTo(false)
        );
    }

    @DisplayName("전체 질문을 조회할 수 있다")
    @Test
    void findAll() {
        questionRepository.save(QuestionTest.Q1);
        questionRepository.save(QuestionTest.Q2);

        List<Question> results = questionRepository.findByDeletedFalse();
        assertThat(results).contains(QuestionTest.Q1, QuestionTest.Q2);
    }

    @DisplayName("삭제된 질문은 전체 조회에 검색되지 않는다")
    @Test
    void deletedFindAll() {
        Question question = questionRepository.save(QuestionTest.Q1);

        question.setDeleted(true);

        assertThat(questionRepository.findByDeletedFalse()).hasSize(0);
    }

    @DisplayName("id로 조회할 수 있다")
    @Test
    void findById() {
        Question actual = questionRepository.save(QuestionTest.Q1);

        Question result = questionRepository.findByIdAndDeletedFalse(actual.getId()).get();

        assertThat(actual == result).isTrue();
    }

    @DisplayName("삭제된 질문은 id로 조회할 수 없다")
    @Test
    void findDeletedById() {
        Question question = questionRepository.save(QuestionTest.Q1);
        question.setDeleted(true);

        Optional<Question> result = questionRepository.findByIdAndDeletedFalse(question.getId());

        assertThat(result).isNotPresent();
    }
}
