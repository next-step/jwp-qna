package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class QuestionRepositoryTest {

    @Autowired
    QuestionRepository questionRepository;

    @AfterEach
    void afterEach() {
        questionRepository.deleteAll();
    }

    @Test
    @DisplayName("생성된 Question의 title은 실제 저장한 title과 동일해야 한다.")
    void saveTest() {
        // given
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);

        // when
        Question actualQuestion = QuestionTest.Q1;

        // then
        assertAll(
            () -> assertThat(savedQuestion.getId()).isNotNull(),
            () -> assertThat(savedQuestion.getTitle()).isEqualTo(actualQuestion.getTitle())
        );
    }

    @Test
    @DisplayName("저장된 question List 조회시 요소가 일치해야 한다.")
    void findByDeletedFalseTest() {
        // given
        Question savedQuestion1 = questionRepository.save(QuestionTest.Q1);
        Question savedQuestion2 = questionRepository.save(QuestionTest.Q2);

        // when
        List<Question> foundQuestions = questionRepository.findByDeletedFalse();

        // then
        assertThat(foundQuestions).containsExactly(savedQuestion1, savedQuestion2);
    }

    @Test
    @DisplayName("question id로 저장된 Question 조회시 해당되는 Question이 조회되어야 한다.")
    void findByIdAndDeletedFalseTest() {
        // given
        Question savedQuestion = questionRepository.save(QuestionTest.Q1);

        // when
        Optional<Question> foundQuestion = questionRepository.findByIdAndDeletedFalse(
            savedQuestion.getId());

        // then
        assertAll(
            () -> assertThat(foundQuestion.isPresent()).isTrue(),
            () -> assertThat(foundQuestion.orElse(null)).isSameAs(savedQuestion)
        );
    }
}
