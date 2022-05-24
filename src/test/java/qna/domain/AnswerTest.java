package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("Answer writer NotNull 테스트")
    void Answer_writer_notnull(){
        assertThatThrownBy(() -> {
            Answer answer = new Answer(null, QuestionTest.Q1, "Answers Contents1");
        }).isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("Answer question NotNull 테스트")
    void Answer_question_notnull(){
        assertThatThrownBy(() -> {
            Answer A1 = new Answer(UserTest.JAVAJIGI, null, "Answers Contents1");
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Answer 작성자 테스트")
    void Answer_작성자(){
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A1.isOwner(UserTest.SANJIGI)).isFalse()
        );
    }

    @Test
    @DisplayName("Answer 삭제 테스트: 정상")
    void Answer_삭제() throws CannotDeleteException {
        A1.delete(UserTest.JAVAJIGI);
        assertThat(A1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Answer 삭제 테스트: 작성자가 맞지 않아 실패")
    void Answer_삭제_실패() throws CannotDeleteException {
        assertThatThrownBy(() -> {
            A1.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
