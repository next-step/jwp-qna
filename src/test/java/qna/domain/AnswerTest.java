package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

/**
 * Answer 클래스의 기능 테스트
 */
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    private User loginUser;
    private User otherUser;

    @BeforeEach
    public void setUp() {
        this.loginUser = UserTest.JAVAJIGI;
        this.otherUser = UserTest.HAGI;
    }

    @Test
    @DisplayName("답변 작성 사용자 검증")
    void is_owner() {
        // then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A1.isOwner(UserTest.HAGI)).isFalse(),
                () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("대답 등록 후 질문에 맞는 대답인지 확인")
    void add_answer() {
        // given
        QuestionTest.Q1.setId(1L);
        A1.toQuestion(QuestionTest.Q1);
        A2.toQuestion(QuestionTest.Q1);

        // then
        assertAll(
                () -> assertThat(A1.getQuestion()).isSameAs(QuestionTest.Q1),
                () -> assertThat(A2.getQuestion()).isSameAs(QuestionTest.Q1),
                () -> assertThat(A2.getQuestion()).isNotSameAs(QuestionTest.Q2)
        );
    }

    @Test
    @DisplayName("로그인한 사용자의 게시물에 대한 권한 유효성 검증")
    void delete_validate_for_ownership() {
        assertThatThrownBy(() -> A1.deleteByWriter(this.otherUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("답변글에 대한 권한을 가진 사용자 게시물 삭제")
    void delete_answer() throws CannotDeleteException {
        // when
        A1.deleteByWriter(this.loginUser);

        // then
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변글 삭제 후 삭제이력 반환")
    void deleteAnswer_and_return_deleteHistory() throws CannotDeleteException {
        // when
        DeleteHistory deleteHistory = A1.deleteByWriter(this.loginUser);

        // then
        assertAll(
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.ANSWER),
                () -> assertThat(deleteHistory.getDeletedByUser()).isSameAs(this.loginUser)
        );
    }
}
