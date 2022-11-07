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

        assertThat(deleteHistories.size()).isEqualTo(4);
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
    @DisplayName("질문을 삭제할 때 답변 또한 삭제해야 하며, 답변의 삭제 또한 삭제 상태(deleted)를 변경한다.")
    void delete4() throws CannotDeleteException {

        Question question = new Question("title", "contets", UserTest.JAVAJIGI);
        List<Answer> answers = Arrays.asList(
                new Answer(UserTest.JAVAJIGI, question, "contents"),
                new Answer(UserTest.JAVAJIGI, question, "contents")
        );

        answers.forEach(question::addAnswer);

        List<DeleteHistory> deleteHistories = question.delete(UserTest.JAVAJIGI);

        assertThat(question.isDeleted()).isTrue();
        assertThat(deleteHistories.size()).isEqualTo(3);
        assertThat(answers.stream().allMatch(Answer::isDeleted)).isTrue();
    }


    @Test
    void checkOwnerOrThrow() throws CannotDeleteException {
        assertThatThrownBy(() -> Q1.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
