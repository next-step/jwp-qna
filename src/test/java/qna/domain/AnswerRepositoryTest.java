package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    UserRepository users;
    @Autowired
    QuestionRepository questions;
    @Autowired
    AnswerRepository answers;
    private User javaJigi;
    private User sanJgi;
    private Question javaJigiQuestion;
    private Question sanJigiQuestion;
    private Answer javaJigiAnswer;
    private Answer sanJigiAnswer;

    @BeforeEach
    void setUp() {
        javaJigi = users.save(UserTest.JAVAJIGI);
        sanJgi = users.save(UserTest.SANJIGI);

        javaJigiQuestion = questions.save(QuestionTest.Q1);
        sanJigiQuestion = questions.save(QuestionTest.Q2);

        javaJigiAnswer = new Answer(javaJigi, javaJigiQuestion, "firstContents");
        sanJigiAnswer = new Answer(sanJgi, sanJigiQuestion, "secondContents");
    }

    @DisplayName("A1 Answer 정보 저장 및 데이터 확인")
    @Test
    void saveAnswer() {
        final Answer target = answers.save(javaJigiAnswer);

        assertAll(
            () -> assertThat(target.getContents()).isEqualTo(javaJigiAnswer.getContents()),
            () -> assertThat(target.getQuestion()).isEqualTo(javaJigiAnswer.getQuestion()),
            () -> assertThat(target.getWriter()).isEqualTo(javaJigiAnswer.getWriter()),
            () -> assertThat(target).isEqualTo(javaJigiAnswer),
            () -> assertThat(target).isSameAs(javaJigiAnswer)
        );
    }

    @DisplayName("writer_id로 데이터 조회")
    @Test
    void findByWriterId() {
        final Answer standard = answers.save(javaJigiAnswer);
        final Answer target = answers.findByWriterId(javaJigi.getId());

        User standardWriter = standard.getWriter();
        User targetWriter = target.getWriter();

        assertThat(standardWriter).isEqualTo(targetWriter);
    }

    @DisplayName("QuestionId로 데이터 조회")
    @Test
    void findByQuestionId() {
        final Answer standard = answers.save(javaJigiAnswer);
        final List<Answer> target = answers.findByQuestion(javaJigiAnswer.getQuestion());

        assertThat(target).contains(standard);
    }

    @DisplayName("Id 와 Deleted 값이 fasle 인 값 찾기")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer standard = answers.save(sanJigiAnswer);
        final Answer target = answers.findByIdAndDeletedFalse(standard.getId()).get();

        boolean targetDeleted = target.isDeleted();

        assertThat(targetDeleted).isFalse();
    }

    @DisplayName("Contents 값 'first' Like 찾기")
    @Test
    void findByContentsLike() {
        answers.save(javaJigiAnswer);
        final Answer target = answers.findByContentsLike(new Contents("%first%"));

        String targetContents = target.getContents();

        assertThat(targetContents).contains("first");
    }
}
