package qna.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.NotFoundException;
import qna.UnAuthorizedException;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @Test
    @DisplayName("생성 테스트")
    void answerTest() {
        assertThatNoException().isThrownBy(() -> new Answer(1L, UserTest.JAVAJIGI, QuestionTest.Q1, "testContent"));
    }
    
    @Test
    @DisplayName("오류 핸들링 테스트")
    void ExceptionTest() {
        assertAll(
            () -> assertThrows(UnAuthorizedException.class, () -> {
                new Answer(1L, null, QuestionTest.Q1, "testContent");
            }),
            () -> assertThrows(NotFoundException.class, () -> {
                new Answer(1L, UserTest.JAVAJIGI, null, "testContent");
            })
        );
    }
    
    @Test
    @DisplayName("isOwner 메소드 테스트")
    void isOwnerTest() {
        assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue();
    }
    
    @Test
    @DisplayName("toQuestion 메소드 테스트")
    void toQuestionTest() {
        A1.toQuestion(QuestionTest.Q2);
        assertThat(A1.getQuestionId()).isEqualTo(QuestionTest.Q2.getId());
    }
}
