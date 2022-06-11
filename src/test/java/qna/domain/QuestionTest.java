package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @Test
    @DisplayName("질문 작성자와 로그인 유저가 동일하다.")
    void checkValidWriter() {
        assertAll(
            () -> {
                assertThat(Q1.getUser().getId()).isEqualTo(UserTest.JAVAJIGI.getId());
                assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue();
            },
            () -> {
                assertThat(Q2.getUser().getId()).isEqualTo(UserTest.SANJIGI.getId());
                assertThat(Q2.isOwner(UserTest.SANJIGI)).isTrue();
            }
        );
    }

    @Test
    @DisplayName("질문에 답변을 작성할 수 있다.")
    void checkAddAnswer() {
        assertAll(
            () -> {
                Q1.addAnswer(AnswerTest.A1);
                assertThat(AnswerTest.A1.getQuestion().getId()).isEqualTo(Q1.getId());
            },
            () -> {
                Q2.addAnswer(AnswerTest.A2);
                assertThat(AnswerTest.A2.getQuestion().getId()).isEqualTo(Q2.getId());
            }
        );
    }

    @Test
    @DisplayName("질문 작성자와 로그인 사용자가 다르면 삭제할 수 없다.")
    void checkExceptionByInvalidUserId() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI)).isExactlyInstanceOf(CannotDeleteException.class);
        assertThatThrownBy(() -> Q2.delete(UserTest.JAVAJIGI)).isExactlyInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문 작성자와 로그인 사용자가 같으면 삭제할 수 있다.")
    void checkDeleteQuestion() {
        assertAll(
            () -> {
                Q1.addAnswer(AnswerTest.A1);
                Q1.delete(UserTest.JAVAJIGI);
                assertThat(Q1.isDeleted()).isTrue();
            },
            () -> {
                Q2.addAnswer(AnswerTest.A2);
                Q2.delete(Q2.getUser());
                assertThat(Q2.isDeleted()).isTrue();
            });
    }
}
