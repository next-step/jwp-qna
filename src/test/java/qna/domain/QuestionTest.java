package qna.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    @DisplayName("delete 성공")
    @Test
    void deleteTest01() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertDoesNotThrow(() -> {
            question.delete(UserTest.JAVAJIGI);
        });
    }

    @DisplayName("delete 성공-다른 사용자의 답변이 있지만 이미 삭제되어 정상적으로 question 삭제")
    @Test
    void deleteTest01_다른_사용자의_답변이_있지만_이미_삭제된_답변() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "본인 답변");
        Answer answerByOtherUser = new Answer(UserTest.SANJIGI, question, "본인 답변");
        question.addAnswer(answer);
        question.addAnswer(answerByOtherUser);

        assertDoesNotThrow(() -> {
            answerByOtherUser.delete(UserTest.SANJIGI);
            question.delete(UserTest.JAVAJIGI);
        });

        Assertions.assertAll(
                () -> assertThat(question.isDeleted()).isTrue(),
                () -> assertThat(answer.isDeleted()).isTrue()
        );
    }

    @DisplayName("delete 실패-다른 사용자의 답변이 있어서 question 삭제 실패")
    @Test
    void deleteTest01_다른_사용자의_답변이_있어서_question_삭제_실패() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        Answer answer = new Answer(UserTest.JAVAJIGI, question, "본인 답변");
        Answer answerByOtherUser = new Answer(UserTest.SANJIGI, question, "본인 답변");
        question.addAnswer(answer);
        question.addAnswer(answerByOtherUser);

        assertThatThrownBy(() -> {
            question.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("delete가 유저가 같지 않아서 실패하는 경우 테스트")
    @Test
    void deleteTest02() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertThatThrownBy(() -> {
            question.delete(UserTest.SANJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("이미 삭제된 질문을 삭제시 exception 발생")
    @Test
    void deleteTest03() {
        Question question = new Question("test question", "test content").writeBy(UserTest.JAVAJIGI);
        assertDoesNotThrow(() -> {
            question.delete(UserTest.JAVAJIGI);
        });

        assertThatThrownBy(() -> {
            question.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}
