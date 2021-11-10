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
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questions;
    private Question actual;

    @BeforeEach
    void setUp() {
        actual = questions.save(Q1);
    }

    @DisplayName("question 생성")
    @Test
    void saveQuestionTest() {
        assertAll(
                () -> assertThat(actual.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(actual.getContents()).isEqualTo("contents1")
        );
    }

    @DisplayName("삭제되지 않은 question 목록 찾기")
    @Test
    void findByDeletedFalseTest() {
        List<Question> noneDeletedQuestions = questions.findByDeletedFalse();
        assertThat(noneDeletedQuestions.size()).isEqualTo(1);
        Question question = noneDeletedQuestions.get(0);
        assertEquals(question, actual);
    }

    @DisplayName("삭제되지 않은 question 하나 찾기")
    @Test
    void findByIdAndDeletedFalseTest() {
            Question question = questions.findByIdAndDeletedFalse(actual.getId())
                    .orElseThrow(NoSuchElementException::new);
            assertEquals(question, actual);
    }

    @DisplayName("삭제한 question 찾기")
    @Test
    void findByIdAndDeletedTrueTest() {
        actual.setDeleted(true);
        assertThatThrownBy(() -> {
            questions.findByIdAndDeletedFalse(actual.getId())
            .orElseThrow(NoSuchElementException::new);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("question 수정")
    @Test
    void updateQuestionTest() {
        actual.setContents("Changed Contents");
        Question questionFromRepository = questions.findById(actual.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(questionFromRepository.getContents()).isEqualTo("Changed Contents");
    }

    @DisplayName("question 삭제")
    @Test
    void removeQuestionTest() {
        assertThat(questions.findAll().size()).isEqualTo(1);
        questions.delete(actual);
        assertThat(questions.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        questions.flush();
    }
}
