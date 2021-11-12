package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.ForbiddenException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    @DisplayName("Question을 생성한다")
    @Test
    void testCreate() {
        String title = "title1";
        String contents = "contents1";
        Question question = new Question(title, contents);
        assertAll(
            () -> assertThat(question.getTitle()).isEqualTo(title),
            () -> assertThat(question.getContents()).isEqualTo(contents),
            () -> assertThat(question.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.SECONDS)),
            () -> assertThat(question.getUpdatedAt()).isNull()
        );
    }

    @DisplayName("Question에 작성자를 입력한다")
    @Test
    void testWriteBy() {
        Question question = new Question("title1", "contents1");
        User writer = new User("user1", "1234", "userName", "email");
        question.writeBy(writer);
        assertThat(question.getWriterId()).isEqualTo(writer.getId());
    }

    @DisplayName("내 질문에 대한 답변을 등록한다")
    @Test
    void testAddAnswer() {
        Question question = new Question("title1", "contents1");
        User writer = new User("user1", "1234", "userName", "email");
        Answer answer = Answer.of(writer, question, "대답");
        question.addAnswer(answer);
        assertThat(question.getAnswers()).hasSize(1);
    }

    @DisplayName("내 질문에 대한 답변이 아닌 것을 등록하면 오류를 던진다")
    @Test
    void testGivenAnotherAnswerThrowException() {
        Question question1 = new Question("title1", "contents1");
        Question question2 = new Question("title2", "contents3");
        User writer = new User("user1", "1234", "userName", "email");
        Answer answer = Answer.of(writer, question1, "대답");
        assertThatThrownBy(() -> question2.addAnswer(answer))
                .isInstanceOf(ForbiddenException.class);
    }
}
