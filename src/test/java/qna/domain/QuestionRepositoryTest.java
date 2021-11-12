package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @DisplayName("findByDeletedFalse 테스트")
    @Test
    void findByDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);

        List<Question> questionList = questionRepository.findByDeletedFalse();
        Question question1 = questionList.get(0);
        Question question2 = questionList.get(1);

        assertThat(questionList).hasSize(2);
        assertThat(question1.getId()).isEqualTo(savedQuestion1.getId());
        assertThat(question2.getId()).isEqualTo(savedQuestion2.getId());
    }

    @DisplayName("findByIdAndDeletedFalse 테스트")
    @Test
    void findByIdAndDeletedFalse() {
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);

        Optional<Question> questionOptional = questionRepository.findByIdAndDeletedFalse(1L);
        assertThat(questionOptional).map(Question::getId).hasValue(savedQuestion1.getId());
    }

}
