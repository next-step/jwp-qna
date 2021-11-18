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
    private User firstWriter;
    private User secondWriter;
    private Question firstQuestion;
    private Question secondQuestion;
    private Answer firstAnswer;
    private Answer secondAnswer;

    @BeforeEach
    void setUp() {
        firstWriter = users.save(UserTest.JAVAJIGI);
        secondWriter = users.save(UserTest.SANJIGI);

        firstQuestion = questions.save(QuestionTest.Q1);
        secondQuestion = questions.save(QuestionTest.Q2);

        firstAnswer = new Answer(firstWriter, firstQuestion, "firstContents");
        secondAnswer = new Answer(secondWriter, secondQuestion, "secondContents");
    }

    @DisplayName("A1 Answer 정보 저장 및 데이터 확인")
    @Test
    void saveAnswer() {

        final Answer target = answers.save(firstAnswer);

        assertAll(
            () -> assertThat(target.getContents()).isEqualTo(firstAnswer.getContents()),
            () -> assertThat(target.getQuestion()).isEqualTo(firstAnswer.getQuestion()),
            () -> assertThat(target.getWriter()).isEqualTo(firstAnswer.getWriter()),
            () -> assertThat(target).isEqualTo(firstAnswer),
            () -> assertThat(target).isSameAs(firstAnswer)
        );
    }

    @DisplayName("writer_id로 데이터 조회")
    @Test
    void findByWriterId() {
        final Answer standard = answers.save(firstAnswer);
        final Answer target = answers.findByWriterId(UserTest.JAVAJIGI.getId());

        User standardWriter = standard.getWriter();
        User targetWriter = target.getWriter();

        assertThat(standardWriter).isEqualTo(targetWriter);
    }

    @DisplayName("QuestionId로 데이터 조회")
    @Test
    void findByQuestionId() {
        final Answer standard = answers.save(firstAnswer);
        final List<Answer> target = answers.findByQuestion(firstAnswer.getQuestion());

        assertThat(target).contains(standard);
    }

    @DisplayName("Id 와 Deleted 값이 fasle 인 값 찾기")
    @Test
    void findByIdAndDeletedFalse() {
        final Answer standard = answers.save(secondAnswer);
        final Answer target = answers.findByIdAndDeletedFalse(standard.getId()).get();

        boolean targetDeleted = target.isDeleted();

        assertThat(targetDeleted).isFalse();
    }

    @DisplayName("Contents 값 'ents1' Like 찾기")
    @Test
    void findByContentsLike() {
        answers.save(firstAnswer);
        final Answer target = answers.findByContentsLike("%first%");

        String targetContents = target.getContents();

        assertThat(targetContents).contains("first");
    }
}
