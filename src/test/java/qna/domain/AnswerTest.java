package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static qna.domain.UserTest.SANJIGI;

@DisplayName("Answer 테스트")
public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, new Contents("Answers Contents1"));
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Answers Contents2"));

    public static final Answer DELETED_ANSWER1 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Deleted Content1"));
    public static final Answer DELETED_ANSWER2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Deleted Content2"));
    public static final Answer DELETED_ANSWER3 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, new Contents("Deleted Content3"));

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void setUp() {
        user = new User("javajigi", "password", "name", "javajigi@slipp.net");

        question = question = new Question(new Title("title1"), new Contents("contents1")).writeBy(user);

        answer = new Answer(user, question, new Contents("Answers Contents1"));
    }

    @Test
    @DisplayName("delete_성공")
    void delete_성공() throws CannotDeleteException {
        // Given
        Answer target = answer;

        // When
        DeleteHistory deleteHistory = target.delete(user);

        // Then
        assertAll(
                () -> assertThat(target.isDeleted()).isTrue(),
                () -> assertThat(deleteHistory.isAnswerType()).isTrue(),
                () -> assertThat(deleteHistory.isWriter(target.getWriter())).isTrue()
        );
    }

    @Test
    @DisplayName("delete_예외_이미_삭제한_질문_재삭제")
    void delete_예외_이미_삭제한_질문_재삭제() throws CannotDeleteException {
        // Given
        Answer target = answer;

        // When
        target.delete(user);

        // Then
        assertThatExceptionOfType(CannotDeleteException.class)
                .isThrownBy(() -> target.delete(user));
    }

    @Test
    @DisplayName("delete_예외_다른_사람이_쓴_글")
    void delete_예외_다른_사람이_쓴_글() {
        // Given
        Answer target = answer;

        // When, Then
        assertThatExceptionOfType(UnAuthorizedException.class)
                .isThrownBy(() -> target.delete(SANJIGI));
    }
}
