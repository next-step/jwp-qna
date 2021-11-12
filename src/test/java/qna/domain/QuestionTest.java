package qna.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Autowired
    private QuestionRepository questionRepository;
    private Question question;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        question = questionRepository.save(Q1);
    }

    @DisplayName("question 생성")
    @Test
    void saveQuestionTest() {
        assertAll(
                () -> assertThat(question.getWriterId()).isEqualTo(UserTest.JAVAJIGI.getId()),
                () -> assertThat(question.getContents()).isEqualTo("contents1"),
                () -> assertThat(question.getCreatedAt()).isAfter(now),
                () -> assertThat(question.getUpdatedAt()).isAfter(now)
        );
    }

    @DisplayName("삭제되지 않은 question 목록 찾기")
    @Test
    void findByDeletedFalseTest() {
        List<Question> noneDeletedQuestions = questionRepository.findByDeletedFalse();
        assertThat(noneDeletedQuestions.size()).isEqualTo(1);
        Question question = noneDeletedQuestions.get(0);
        assertEquals(question, this.question);
    }

    @DisplayName("삭제되지 않은 question 하나 찾기")
    @Test
    void findByIdAndDeletedFalseTest() {
        Question question = questionRepository.findByIdAndDeletedFalse(this.question.getId())
                .orElseThrow(NoSuchElementException::new);
        assertEquals(question, this.question);
    }

    @DisplayName("삭제한 question 찾기")
    @Test
    void findByIdAndDeletedTrueTest() {
        question.setDeleted(true);
        assertThatThrownBy(() -> {
            questionRepository.findByIdAndDeletedFalse(question.getId())
                    .orElseThrow(NoSuchElementException::new);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("question 수정")
    @Test
    void updateQuestionTest() {
        question.setContents("Changed Contents");
        Question questionFromRepository = questionRepository.findById(question.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(questionFromRepository.getContents()).isEqualTo("Changed Contents");
    }

    @DisplayName("question 삭제")
    @Test
    void removeQuestionTest() {
        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        questionRepository.delete(question);
        assertThat(questionRepository.findAll().size()).isZero();
    }

    @AfterEach
    void beforeFinish() {
        questionRepository.flush();
    }
}
