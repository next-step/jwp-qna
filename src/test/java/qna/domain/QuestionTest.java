package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static qna.domain.FixtureUtils.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    @Test
    void 동등성() {
        assertAll(
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI()).isEqualTo(QUESTION1_WRITE_BY_JAVAJIGI()),
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI()).isNotEqualTo(QUESTION2_WRITE_BY_JAVAJIGI())
        );
    }

    @Test
    void 작성자_등록() {
        assertAll(
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI().isOwner(JAVAJIGI())).isTrue(),
            () -> assertThat(QUESTION1_WRITE_BY_JAVAJIGI().isOwner(SANJIGI())).isFalse()
        );
    }

    @Test
    void 답변_추가() {
        User JAVAJIGI = JAVAJIGI();
        Question q1 = QUESTION1(JAVAJIGI);
        Answer answer = ANSWER1(JAVAJIGI, q1);
        q1.addAnswer(answer);

        assertAll(
            () -> assertThat(q1.getAnswers()).contains(answer),
            () -> assertThat(answer.getQuestion()).isEqualTo(q1)
        );
    }

    @Test
    void 질문_삭제_권한없음_CannotDeleteException() {
        Question question = QUESTION1(JAVAJIGI());
        User loginUser = SANJIGI();

        assertThatThrownBy(() -> question.delete(loginUser))
            .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    void 질문_삭제() throws CannotDeleteException {
        User loginUser = JAVAJIGI();
        Question question = QUESTION1(loginUser);

        List<DeleteHistory> deleteHistories = question.delete(loginUser);
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories).hasSize(1)
        );
    }

    @Test
    void 질문_답변_삭제_전파() throws CannotDeleteException {
        User user = JAVAJIGI();
        Question question = QUESTION1(user);

        Answer answer = ANSWER1(user, question);
        answer.toQuestion(question);

        List<DeleteHistory> deleteHistories = question.delete(user);
        assertAll(
            () -> assertThat(question.isDeleted()).isTrue(),
            () -> assertThat(answer.isDeleted()).isTrue(),
            () -> assertThat(deleteHistories).hasSize(2)
        );
    }
}
