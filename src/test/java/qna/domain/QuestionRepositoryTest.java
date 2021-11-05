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

    Question saveQuestion1;
    Question saveQuestion2;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    void init() {
        saveQuestion1 = questionRepository.save(QuestionTest.Q1);
        saveQuestion2 = questionRepository.save(QuestionTest.Q2);
    }

    @DisplayName("USER가 잘 저장되는지 확인한다.")
    @Test
    void saveUserTest() {
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
        assertEquals(2, questionRepository.findByDeletedFalse().size());
    }
    @DisplayName("QUESTION ID로 삭제되지 않은 질문을 확인한다.")
    @Test
    void findByIdAndDeletedFalseTest() {
        assertSame(saveQuestion2, questionRepository.findByIdAndDeletedFalse(QuestionTest.Q2.getId()).get());
    }
}
