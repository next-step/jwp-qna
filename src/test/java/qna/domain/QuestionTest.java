package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.JAVAJIGI;

@DisplayName("Question 테스트")
public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static final Question DELETED_QUESTION1 = new Question("deleted question title1", "deleted question content1").writeBy(UserTest.SANJIGI);
    public static final Question DELETED_QUESTION2 = new Question("deleted question title2", "deleted question content2").writeBy(UserTest.SANJIGI);

    private User javajigi;
    private User sanjigi;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        javajigi = new User("javajigi", "password", "name", "javajigi@slipp.net");
        sanjigi = new User("sanjigi", "password", "name", "sanjigi@slipp.net");

        question = question = new Question("title1", "contents1").writeBy(javajigi);

        answer = new Answer(javajigi, question, "Answers Contents1");
    }

    @Test
    @DisplayName("delete_성공")
    void delete_성공() throws CannotDeleteException {
        // Given
        Question target = question;

        // When
        List<DeleteHistory> deleteHistories = target.delete(javajigi);

        // Then
        assertThat(target.isDeleted()).isTrue();
        for (DeleteHistory deleteHistory : deleteHistories) {
            assertAll(
                    () -> assertThat(deleteHistory.isQuestionType()).isTrue(),
                    () -> assertThat(deleteHistory.isWriter(target.getWriter())).isTrue()
            );
        }
    }

    @Test
    @DisplayName("delete_예외_이미_삭제한_질문_재삭제")
    void delete_예외_이미_삭제한_질문_재삭제() throws CannotDeleteException {
        // Given
        Question target = question;

        // When
        List<DeleteHistory> deleteHistory = target.delete(javajigi);

        // Then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> target.delete(javajigi));
    }

    @Test
    @DisplayName("delete_예외_다른_사람이_쓴_글")
    void delete_예외_다른_사람이_쓴_글() {
        // Given
        Question target = question;

        // When, Then
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> target.delete(sanjigi));
    }

    @Test
    @DisplayName("delete_성공_질문자_답변자_같음")
    void delete_성공_질문자_답변자_같음() throws CannotDeleteException {
        // Given
        Question target = question;
        target.addAnswer(javajigi, "아 질문 올리자 마자 해결되서 삭제합니다;");

        // When
        List<DeleteHistory> deleteHistories = target.delete(javajigi);

        // Then
        assertThat(target.isDeleted()).isTrue();
        for (DeleteHistory deleteHistory : deleteHistories) {
            assertAll(
                    () -> assertThat(deleteHistory.isWriter(target.getWriter())).isTrue()
            );
        }
    }

    @Test
    @DisplayName("delete_예외_답변_중_다른_사람이_쓴_글")
    void delete_예외_답변_중_다른_사람이_쓴_글() {
        // Given
        Question target = question;
        target.addAnswer(sanjigi, "그건 말이죠 답변드립니다.");
        target.addAnswer(javajigi, "아 질문 올리자 마자 해결되서 삭제합니다;");
        target.addAnswer(javajigi, "아 조금 늦었네요.. 답변 감사합니다.");

        // When, Then
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> target.delete(javajigi));
    }
}
