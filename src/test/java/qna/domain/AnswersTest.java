package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

class AnswersTest {

    private Answers answers;

    private User userA;
    private User userB;
    private Question questionFromA;

    @BeforeEach
    void setUp() {
        userA = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        userB = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");
        questionFromA = new Question("title1", "contents1").writeBy(userA);
    }

    @DisplayName("로그인 사용자와 답변한 사람이 다른 답변이 존재하는 경우, 해당 답변이후의 답변은 삭제 불가능 해야 한다")
    @Test
    void delete_differentUser() {
        final Answer answerFromA = new Answer(99L, userA, questionFromA, "JPA Answer");
        final Answer answerFromB = new Answer(100L, userB, questionFromA, "Other User JPA Answer");

        answers = new Answers(Arrays.asList(answerFromA, answerFromB));

        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> answers.delete(userB))
            .withMessageContaining("답변을 삭제할 권한이 없습니다");

        for (Answer answer : answers.getValues()) {
            assertThat(answer.isDeleted()).isFalse();
        }
    }

    @DisplayName("로그인 사용자와 모든 답변의 답변한 사람이 같은 경우, 모든 답변이 삭제 가능 해야 한다")
    @Test
    void delete_sameUser() throws CannotDeleteException {
        final Answer answerFromA = new Answer(99L, userA, questionFromA, "JPA Answer");
        final Answer anotherAnswerFromA = new Answer(100L, userA, questionFromA, "Other User JPA Answer");

        answers = new Answers(Arrays.asList(answerFromA, anotherAnswerFromA));

        final List<DeleteHistory> deleteHistories = answers.delete(userA);
        assertThat(deleteHistories).containsExactly(
            new DeleteHistory(ContentType.ANSWER, 99L, userA, LocalDateTime.now()),
            new DeleteHistory(ContentType.ANSWER, 100L, userA, LocalDateTime.now())
        );

        for (Answer answer : answers.getValues()) {
            assertThat(answer.isDeleted()).isTrue();
        }
    }

}