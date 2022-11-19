package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {

    private User userA;
    private User userB;

    private Question questionFromA;
    private Question deletedQuestionFromA;

    @BeforeEach
    void setUp() {
        userA = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
        userB = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

        questionFromA = new Question(100L, "JPA", "JPA Content")
            .writeBy(userA);
        deletedQuestionFromA = new Question(110L, "JPA", "Deleted JPA Content")
            .writeBy(userA);
        deletedQuestionFromA.setDeleted(true);
    }

    @DisplayName("이미 삭제된 질문의 경우, 삭제 불가능 해야 한다")
    @Test
    void delete_alreadyDeleted() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() ->  deletedQuestionFromA.delete(userA))
            .withMessageContaining("이미 삭제된 질문입니다");
    }

    @DisplayName("로그인 사용자와 질문한 사람이 다른 경우, 질문 삭제 불가능 해야 한다")
    @Test
    void delete_differentUser() {
        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> questionFromA.delete(userB))
            .withMessageContaining("질문을 삭제할 권한이 없습니다");
    }

    @DisplayName("로그인 사용자와 질문한 사람이 같은 경우, 질문 삭제 가능 해야 한다")
    @Test
    void delete_sameUser() throws CannotDeleteException {
        final List<DeleteHistory> deleteHistories = questionFromA.delete(userA);

        assertAll(
            () -> assertThat(questionFromA.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories).containsExactly(
                new DeleteHistory(ContentType.QUESTION, 100L, userA, LocalDateTime.now()))
        );

    }

    @DisplayName("다른 사람이 쓴 답변이 존재하는 경우, 질문 삭제 불가능 해야 한다")
    @Test
    void delete_containsOtherUserAnswers() {
        questionFromA.addAnswer(new Answer(1L, userB, questionFromA,"It's me again, but.."));

        assertThatExceptionOfType(CannotDeleteException.class)
            .isThrownBy(() -> questionFromA.delete(userA))
            .withMessageContaining("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다");
    }

    @DisplayName("본인 외에 다른 사람이 쓴 답변이 존재하지 않는 경우, 질문과 답변 모두 삭제 가능 해야 한다")
    @Test
    void delete_onlyContainsOwnAnswers() throws CannotDeleteException {
        questionFromA.addAnswer(new Answer(1L, userA, questionFromA,"It's me again, but.."));

        final List<DeleteHistory> deleteHistories = questionFromA.delete(userA);
        assertAll(
            () -> assertThat(questionFromA.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories).containsExactly(
                new DeleteHistory(ContentType.QUESTION, 100L, userA, LocalDateTime.now()),
                new DeleteHistory(ContentType.ANSWER, 1L, userA, LocalDateTime.now()))
        );
    }
}
