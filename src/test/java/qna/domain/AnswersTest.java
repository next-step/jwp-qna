package qna.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

class AnswersTest {
    private User user;
    private Answer answer1;
    private Answer answer2;
    private Answer answer3;

    @BeforeEach
    void setUp() {
        user = new User(1L, "userId", "password", "name", "email");
        User user2 = new User(2L, "userId2", "password2", "name2", "email2");
        Question question = new Question(1L, "title", "contents").writeBy(user);
        answer1 = new Answer(user, question, "Answers Contents1");
        answer2 = new Answer(user2, question, "Answers Contents2");
        answer3 = new Answer(user, question, "Answers Contents3");
    }


    @DisplayName("답변 추가")
    @Test
    void add() {
        Answers answers = new Answers();

        answers.add(answer1);

        assertThat(answers).isEqualTo(new Answers(Arrays.asList(answer1)));
    }

    @DisplayName("모든 답변자가 작성자인지 확인")
    @Test
    void validateOwner_success() {
        Answers answers = new Answers(Arrays.asList(answer1));

        answers.validateOwner(user);
        // 에러 발생 X
    }

    @DisplayName("답변자가 아닌 답변이 있는 경우 에러 확인")
    @Test
    void validateOwner_error() {
        Answers answers = new Answers(Arrays.asList(answer1, answer2));

        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answers.validateOwner(user))
            .withMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @DisplayName("삭제")
    @Test
    void delete() {
        Answers answers = new Answers(Arrays.asList(answer1, answer3));
        DeleteHistories expected = new DeleteHistories(Arrays.asList(
            new DeleteHistory(new Content(answer1), LocalDateTime.now()),
            new DeleteHistory(new Content(answer3), LocalDateTime.now())
        ));

        DeleteHistories deleteHistories = answers.delete(user);

        assertThat(expected).isEqualTo(deleteHistories);
    }
}