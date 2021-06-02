package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

/**
 * Question 클래스의 기능 테스트
 */
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    private User ownerUser;
    private User otherUser;
    private Question question;

    @BeforeEach
    public void setUp() {
        this.ownerUser = UserTest.JAVAJIGI;
        this.otherUser = UserTest.HAGI;
        this.question = QuestionTest.Q1.writeBy(UserTest.JAVAJIGI);
    }

    @Test
    @DisplayName("질문 작성 사용자 검증")
    void is_owner() {
        // then
        assertAll(
                () -> assertThat(Q1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(Q1.isOwner(UserTest.HAGI)).isFalse(),
                () -> assertThat(Q1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("대답 등록 후 질문에 맞는 대답인지 확인")
    void add_answer() {
        // given
        Q1.setId(1L);
        Q2.setId(2L);

        // when
        Q1.addAnswer(AnswerTest.A1);

        // then
        assertAll(
                () -> assertThat(Q1).isSameAs(AnswerTest.A1.getQuestion()),
                () -> assertThat(Q2).isNotSameAs(AnswerTest.A1.getQuestion())
        );
    }

    @Test
    @DisplayName("로그인한 사용자의 게시물에 대한 권한 유효성 검증")
    void delete_validate_by_loginUser() {
        assertThatThrownBy(() -> this.question.deleteByWriter(this.otherUser))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("게시물에 대한 권한을 가진 사용자 게시물 삭제")
    void delete_question() throws CannotDeleteException {
        // when
        this.question.deleteByWriter(this.ownerUser);

        // then
        assertThat(this.question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("게시물 삭제 후 삭제이력 반환")
    void delete_and_return_deleteHistory() throws CannotDeleteException {
        // when
        DeleteHistory deleteHistory = this.question.deleteByWriter(this.ownerUser);

        // then
        assertAll(
                () -> assertThat(deleteHistory.getDeletedByUser()).isSameAs(this.ownerUser),
                () -> assertThat(deleteHistory.getContentType()).isEqualTo(ContentType.QUESTION)
        );
    }
}
