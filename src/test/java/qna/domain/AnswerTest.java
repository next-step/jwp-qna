package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    User testWriter;
    Answer answer;

    @BeforeEach
    void setUp() {
        testWriter = UserTest.JAVAJIGI;
        answer = AnswerTest.A1;
    }

    @Test
    @DisplayName("답변 삭제 정상 동작 검증")
    void delete() throws CannotDeleteException {
        answer.delete(UserTest.JAVAJIGI);
        assertThat(answer.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("답변 삭제시 로그인 유저가 다를 경우 예외 발생")
    void deleteException() {
        assertThatThrownBy(() -> answer.delete(UserTest.SANJIGI)).isInstanceOf(CannotDeleteException.class);
    }
}
