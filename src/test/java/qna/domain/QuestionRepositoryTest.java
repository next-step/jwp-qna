package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @Test
    void save() {
        Question question = questionRepository.save(QuestionTest.Q1);
        assertThat(question.getId()).isEqualTo(QuestionTest.Q1.getId());
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 찾는다")
    void findByDeletedFalse() {
        questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2));
        List<Question> findQuestions = questionRepository.findByDeletedFalse();

        assertThat(findQuestions.size()).isEqualTo(2);
        for (Question question : findQuestions) {
            assertThat(question.isDeleted()).isFalse();
        }
    }

    @Test
    @DisplayName("삭제되지 않은 질문들을 id 를 통해서 찾는다")
    void findByIdAndDeletedFalse() {
        questionRepository.saveAll(Arrays.asList(QuestionTest.Q1, QuestionTest.Q2));
        Optional<Question> findQuestion = questionRepository.findByIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(findQuestion.get().getId()).isEqualTo(QuestionTest.Q1.getId());
        assertThat(findQuestion.get().isDeleted()).isFalse();
    }
}