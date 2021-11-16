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
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questions;

    @Autowired
    private UserRepository users;

    @DisplayName("Question을 저장한다")
    @Test
    void testSave() {
        User writer = users.save(new User("user1", "1234", "userName", "email"));
        Question question = Question.of("title1", "contents1", writer);
        Question savedQuestion = questions.save(question);
        assertAll(
                () -> assertThat(savedQuestion.getId()).isNotNull(),
                () -> assertThat(savedQuestion.getTitle()).isEqualTo(question.getTitle()),
                () -> assertThat(savedQuestion.getContents()).isEqualTo(question.getContents()),
                () -> assertThat(savedQuestion.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
                () -> assertThat(savedQuestion.getWriter()).isEqualTo(question.getWriter())
        );
    }

    @Test
    void testDelete() throws CannotDeleteException {
        // 질문 삭제에 대한 학습 테스트
        User writer = users.save(new User("user1", "1234", "userName", "email"));
        Question question = Question.of("title1", "contents1", writer);
        question.addAnswer(writer, "내용1");
        question.addAnswer(writer, "내용2");
        question.addAnswer(writer, "내용3");
        Question savedQuestion = questions.save(question);
        savedQuestion.delete(writer);
        questions.flush();
    }
}
