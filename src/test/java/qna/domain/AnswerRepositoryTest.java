package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import qna.CannotDeleteException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answers;
    @Autowired
    private QuestionRepository questions;
    @Autowired
    private UserRepository users;

    @DisplayName("Answer가 저장된다")
    @Test
    void testSave() {
        // Answer에 대한 학습테스트
        User writer = users.save(UserTest.JAVAJIGI);
        Question question = questions.save(Question.of("title1", "contents1", writer));
        Answer answer = Answer.of(writer, question, "Answers Contents1");
        Answer savedAnswer = answers.save(answer);
        assertAll(
                () -> assertThat(savedAnswer.getId()).isNotNull(),
                () -> assertThat(savedAnswer.getWriter()).isEqualTo(answer.getWriter()),
                () -> assertThat(savedAnswer.getQuestion()).isEqualTo(answer.getQuestion()),
                () -> assertThat(savedAnswer.getContents()).isEqualTo(answer.getContents()),
                () -> assertThat(savedAnswer.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
                () -> assertThat(question.getAnswers().contains(answer)).isTrue()
        );
    }

    @DisplayName("Answer를 삭제한다")
    @Test
    void testAnswer() throws CannotDeleteException {
        // 학습 테스트
        User writer = users.save(UserTest.JAVAJIGI);
        Question question = questions.save(Question.of("title1", "contents1", writer));
        Answer answer = answers.save(Answer.of(writer, question, "Answers Contents1"));
        answer.delete(writer);
        answers.flush();
    }
}
