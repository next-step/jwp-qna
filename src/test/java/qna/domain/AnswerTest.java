package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class AnswerTest {

    @Autowired
    private AnswerRepository answers;
    @Autowired
    private UserRepository users;
    @Autowired
    private QuestionRepository questions;

    private Answer firstAnswer;
    private Answer secondAnswer;

    @BeforeEach
    void setUp() {
        User firstUser = users.save(UserTest.JAVAJIGI);
        User secondUser = users.save(UserTest.SANJIGI);

        Question firstQuestion = questions.save(QuestionTest.Q1);
        Question secondQuestion = questions.save(QuestionTest.Q2);

        firstAnswer = new Answer(firstUser, firstQuestion, "Answers Contents1");
        secondAnswer = new Answer(secondUser, secondQuestion, "Answers Contents2");
    }

    @DisplayName("A1 Answer 정보 저장 및 데이터 확인")
    @Test
    void saveAnswer() {
        final Answer actual = answers.save(firstAnswer);

        User writer = actual.getWriter();
        Question question = actual.getQuestion();

        assertThat(writer).isEqualTo(firstAnswer.getWriter());
        assertThat(question).isEqualTo(firstAnswer.getQuestion());
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
        answers.save(secondAnswer);
        final Answer target = answers.findByIdAndDeletedFalse(secondAnswer.getId()).get();

        boolean targetDeleted = target.isDeleted();

        assertThat(targetDeleted).isFalse();
    }

    @DisplayName("Contents 값 'ents1' Like 찾기")
    @Test
    void findByContentsLike() {
        answers.save(firstAnswer);
        final Answer target = answers.findByContentsLike("%ents1%");

        String targetContents = target.getContents();

        assertThat(targetContents).contains("ents1");
    }
}
