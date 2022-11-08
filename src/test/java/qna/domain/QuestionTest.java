package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class QuestionTest {
    public static final Question Q1 = new Question("title1", "contents1", UserTest.JAVAJIGI);
    public static final Question Q2 = new Question("title2", "contents2", UserTest.SANJIGI);

    @Test
    @DisplayName("Question 작성자와 응답자가 모두 같은 경우 전부 함께 삭제됨")
    void delete() throws CannotDeleteException {

        Question question = new Question("title", "contets", UserTest.JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(UserTest.JAVAJIGI, question, "contents"),
                new Answer(UserTest.JAVAJIGI, question, "contents"),
                new Answer(UserTest.JAVAJIGI, question, "contents"));
        answers.forEach(question::addAnswer);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(4);
        assertThat(answers.stream().allMatch(Answer::isDeleted)).isTrue();
    }

    @Test
    @DisplayName("Question에 답변이 없는 경우 삭제가 가능하다")
    void delete2() throws CannotDeleteException {

        Question question = new Question("title", "contets", UserTest.JAVAJIGI);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(deleteHistories.size()).isEqualTo(1);
        assertThat(question.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("Question에 답변이 있지만 작성자가 다른 경우 삭제할 수 없다")
    void delete3() {

        Question question = new Question("title", "contets", UserTest.JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(UserTest.JAVAJIGI, question, "contents"),
                new Answer(UserTest.SANJIGI, question, "contents"),
                new Answer(UserTest.SANJIGI, question, "contents"));
        answers.forEach(question::addAnswer);

        assertThatThrownBy(() -> question.delete(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @Test
    @DisplayName("질문과 답변을 함께 삭제할 때, 이미 삭제된 답변은 제외되어야 함")
    void delete4() throws CannotDeleteException {

        Question question = new Question("title", "contets", UserTest.JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(UserTest.JAVAJIGI, question, "contents"),
                new Answer(UserTest.JAVAJIGI, question, "contents")
        );
        answers.get(0).delete(UserTest.JAVAJIGI);
        answers.forEach(question::addAnswer);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("작성자가 다르면 exception 이 발생함.")
    void checkOwnerOrThrow() {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
