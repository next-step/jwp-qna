package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Autowired
    private AnswerRepository answers;
    private Answer actual;

    @BeforeEach
    void setUp() {
        actual = answers.save(A1);
    }

    @DisplayName("answer 생성")
    @Test
    void saveAnswerTest () {
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(QuestionTest.Q1.getId())
        );
    }

    @DisplayName("question id 기준으로 삭제되지않은 answer 목록 찾기")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> noneDeletedAnswers = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(noneDeletedAnswers.size()).isEqualTo(1);
        Answer answer = noneDeletedAnswers.get(0);
        assertEquals(answer, actual);
    }

    @DisplayName("question id 기준으로 삭제된 answer 목록 확인")
    @Test
    void findByQuestionIdAndDeletedTrueTest() {
        actual.setDeleted(true);
        List<Answer> noneDeletedAnswers = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(noneDeletedAnswers.size()).isZero();
    }


    @DisplayName("삭제되지않은 answer 찾기")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer answer = answers.findByIdAndDeletedFalse(actual.getId())
                .orElseThrow(NoSuchElementException::new);
        assertEquals(answer, actual);
    }

    @DisplayName("삭제한 answer 확인")
    @Test
    void findByIdAndDeletedTrueTest() {
        actual.setDeleted(true);
        assertThatThrownBy(() -> {
            answers.findByIdAndDeletedFalse(actual.getId())
                    .orElseThrow(NoSuchElementException::new);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("answer 수정")
    @Test
    void updateAnswerTest() {
        actual.setContents("Changed Contents1");
        Answer questionFromRepository = answers.findById(actual.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(questionFromRepository.getContents()).isEqualTo("Changed Contents1");
    }

    @DisplayName("answer 삭제")
    @Test
    void removeAnswerTest() {
        assertThat(answers.findAll().size()).isEqualTo(1);
        answers.delete(actual);
        assertThat(answers.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        answers.flush();
    }
}
