package qna.domain;

import static org.assertj.core.api.Assertions.*;
import static qna.domain.AnswerTest.*;
import static qna.domain.ContentType.*;
import static qna.domain.UserTest.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import qna.CannotDeleteException;

public class QuestionTest {
    public static Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);

    public static Question Q1() {
        return new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    }

    public static Question Q2 = new Question("title2", "contents2").writeBy(UserTest.SANJIGI);

    public static Question Q2() {
        return new Question("title2", "contents2").writeBy(UserTest.SANJIGI);
    }

    public static Question newQuestion(String appendString) {
        return new Question("title" + appendString, "contents" + appendString);
    }

    @Test
    void 삭제불가_작성자와_삭제자가_다를때() throws Exception {
        assertThatThrownBy(() -> Q1().delete(UserTest.SANJIGI))
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("질문을 삭제할 권한이 없습니다.");
    }

    @Test
    void 삭제불가_답변자와_삭제자가_같은_답변이_있을때() throws Exception {
        assertThatThrownBy(() -> {
            Question question = A1().getQuestion();
            A2().toQuestion(question);
            question.delete(UserTest.JAVAJIGI);
        })
            .isInstanceOf(CannotDeleteException.class)
            .hasMessage("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
    }

    @Test
    void 삭제성공_deleted_를_true_로_변경() throws Exception {
        Question question = Q1();
        question.delete(JAVAJIGI);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    void 삭제성공_DeleteHistory_리스트_반환() throws Exception {
        Question question = A1().getQuestion();
        List<DeleteHistory> deleteHistories = question.delete(JAVAJIGI);
        assertThat(deleteHistories).hasSize(2);
        assertThat(deleteHistories).contains(
            new DeleteHistory(QUESTION, Q1().getId(), JAVAJIGI),
            new DeleteHistory(ANSWER, A1().getId(), JAVAJIGI));
    }

    @Test
    void 삭제성공_답변없는_경우_리스트_1개짜리만_반환() throws Exception {
        Question question = Q1();
        question.getAnswers().clear();
        List<DeleteHistory> deleteHistories = question.delete(JAVAJIGI);
        assertThat(deleteHistories).hasSize(1);
        assertThat(deleteHistories).containsOnly(
            new DeleteHistory(QUESTION, question.getId(), JAVAJIGI, LocalDateTime.now()));
    }
}
