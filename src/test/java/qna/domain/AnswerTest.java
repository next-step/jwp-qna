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
    private Answer answer;

    @BeforeEach
    void setUp() {
        answer = answers.save(A1);
    }

    @DisplayName("answer 생성")
    @Test
    void saveAnswerTest () {
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getContents()).isEqualTo("Answers Contents1"),
                () -> assertThat(answer.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(QuestionTest.Q1.getId())
        );
    }

    @DisplayName("question id 기준으로 삭제되지않은 answer 목록 찾기")
    @Test
    void findByQuestionIdAndDeletedFalseTest() {
        List<Answer> noneDeletedAnswers = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(noneDeletedAnswers.size()).isEqualTo(1);
        Answer answerFromRepository = noneDeletedAnswers.get(0);
        assertEquals(answerFromRepository, answer);
    }

    @DisplayName("question id 기준으로 삭제된 answer 목록 확인")
    @Test
    void findByQuestionIdAndDeletedTrueTest() {
        answer.setDeleted(true);
        List<Answer> noneDeletedAnswers = answers.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(noneDeletedAnswers.size()).isZero();
    }


    @DisplayName("삭제되지않은 answer 찾기")
    @Test
    void findByIdAndDeletedFalseTest() {
        Answer answerFromRepository = answers.findByIdAndDeletedFalse(answer.getId())
                .orElseThrow(NoSuchElementException::new);
        assertEquals(answerFromRepository, answer);
    }

    @DisplayName("삭제한 answer 확인")
    @Test
    void findByIdAndDeletedTrueTest() {
        answer.setDeleted(true);
        assertThatThrownBy(() -> {
            answers.findByIdAndDeletedFalse(answer.getId())
                    .orElseThrow(NoSuchElementException::new);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("answer 수정")
    @Test
    void updateAnswerTest() {
        answer.setContents("Changed Contents1");
        Answer answerFromRepository = answers.findById(answer.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(answerFromRepository.getContents()).isEqualTo("Changed Contents1");
    }

    @DisplayName("answer 삭제")
    @Test
    void removeAnswerTest() {
        assertThat(answers.findAll().size()).isEqualTo(1);
        answers.delete(answer);
        assertThat(answers.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        answers.flush();
    }
}
