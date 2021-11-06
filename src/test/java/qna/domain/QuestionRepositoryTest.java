package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository questionRepository;

    private Question saveQuestion(Question question){
        return questionRepository.save(question);
    }

    @DisplayName("Question이 잘 저장되는지 확인한다.")
    @Test
    void saveQuestionTest() {

        Question saveQuestion1 = saveQuestion(QuestionTest.Q1);
        Question saveQuestion2 = saveQuestion(QuestionTest.Q2);
        assertAll(
                () -> assertThat(saveQuestion1.getId()).isNotNull(),
                () -> assertThat(saveQuestion1.getTitle()).isEqualTo(QuestionTest.Q1.getTitle()),
                () -> assertThat(saveQuestion2.getId()).isNotNull(),
                () -> assertThat(saveQuestion2.getContents()).isEqualTo(QuestionTest.Q2.getContents())
        );
    }

    @DisplayName("삭제되지 않은 질문을 확인한다.")
    @Test
    void findByDeletedFalseTest() {

        Question saveQuestion1 = saveQuestion(QuestionTest.Q1);
        Question saveQuestion2 = saveQuestion(QuestionTest.Q2);

        assertEquals(2, questionRepository.findByDeletedFalse().size());
    }
    @DisplayName("QUESTION ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByIdAndDeletedFalseTest() {

        Question saveQuestion2 = saveQuestion(QuestionTest.Q2);

        assertSame(saveQuestion2, questionRepository.findByIdAndDeletedFalse(saveQuestion2.getId()).get());
    }
}
