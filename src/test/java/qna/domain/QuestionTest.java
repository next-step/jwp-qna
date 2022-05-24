package qna.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);


    @Test
    @DisplayName("로그인 사용자와 질문한 사람이 같지 않은 경우 삭제할 수 없다.")
    void deleteAuth() {
        Question q1 = new Question("질문", "질문").writeBy(UserTest.SANJIGI);
        assertThatThrownBy(() -> q1.deleteQuestion(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }


    @Test
    @DisplayName("답변이 없는 경우 삭제가 가능하다.")
    void successDeleteNoAnswer() throws CannotDeleteException {
        User actionUser = UserTest.SANJIGI;
        Question q1 = new Question("답변없음", "답변없음").writeBy(actionUser);
        q1.deleteQuestion(actionUser);
        assertThat(q1.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("질문자와 답변자가 다른 경우 답변을 삭제할 수 없다.")
    void failDeleteQuestionNotSameAnswerWriter() {
        User actionUser = UserTest.SANJIGI;
        Question q1 = new Question("질문", "질문없음").writeBy(actionUser);
        q1.addAnswer(new Answer(actionUser, q1, "답변"));
        q1.addAnswer(new Answer(actionUser, q1, "답변2"));
        q1.addAnswer(new Answer(UserTest.JAVAJIGI, q1, "답변3"));

        assertThatThrownBy(() -> q1.deleteQuestion(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문자와 답변자가 같은 경우 답변을 삭제할 수 있다.")
    void successDeleteQuestionSameAnswerWriter() throws CannotDeleteException {
        User actionUser = UserTest.SANJIGI;
        Question q1 = new Question("질문", "질문없음").writeBy(actionUser);
        q1.addAnswer(new Answer(actionUser, q1, "답변"));
        q1.addAnswer(new Answer(actionUser, q1, "답변2"));
        q1.addAnswer(new Answer(actionUser, q1, "답변3"));

        q1.deleteQuestion(actionUser);

        assertAll(
                () -> assertThat(q1.isDeleted()).isTrue(),
                () -> assertThat(q1.getAnswers().stream().filter(Answer::isDeleted)).hasSize(3)
        );
    }

    @Test
    @DisplayName("삭제 이력을 생성한다.")
    void makeDeleteHistoryes() {
        User actionUser = UserTest.SANJIGI;
        Question q1 = new Question("질문", "질문없음").writeBy(actionUser);
        q1.addAnswer(new Answer(actionUser, q1, "답변"));
        q1.addAnswer(new Answer(actionUser, q1, "답변2"));
        q1.addAnswer(new Answer(actionUser, q1, "답변3"));

        final List<DeleteHistory> deleteHistories = q1.makeDeleteHistoryes();

        final Long answerCount = deleteHistories.stream()
                .filter((deleteHistory -> deleteHistory.getContentType() == ContentType.ANSWER))
                .count();

        assertAll(
                () -> assertThat(deleteHistories).hasSize(4),
                () -> assertThat(answerCount).isEqualTo(3L)
        );

    }
}
