package qna.repository.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    User loginUser;
    Question question;
    Answer answer;

    @BeforeEach
    void setUp() {
        loginUser = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        question = new Question(1L, "title1", "contents1");
        answer = new Answer(1L, loginUser, question, "Answers Contents2");
    }

    @Test
    @DisplayName("Answer 생성 테스트")
    void creates() {
        assertAll(
                () -> assertThat(answer.getWriter()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isEqualTo(1L),
                () -> assertThat(answer.getQuestion()).isNotNull(),
                () -> assertThat(answer.getQuestion().getId()).isEqualTo(1L),
                () -> assertThat(answer.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("다른 유저의 답변을 삭제하면 오류를 반환하는지")
    void delete_with_other_user() {
        User otherUser = new User(2L, "stevejkang", "password", "steve", "steve@steve.com");
        Answer otherAnswer = new Answer(2L, otherUser, question, "Answers Contents5");
        assertThatThrownBy(() -> otherAnswer.delete(loginUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변 삭제 테스트")
    void delete_with_user() {
        answer.delete(loginUser);
        assertThat(answer.isDeleted()).isTrue();
    }
}
