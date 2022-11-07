package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class QuestionAnswersTest {

    @Test
    @DisplayName("질문 작성자와 답변 작성자가 모두 같으면 삭제가능하다고 판단함")
    void isDeletable() throws CannotDeleteException {
        Question question = new Question("title", "contents", UserTest.JAVAJIGI);
        QuestionAnswers questionAnswers = new QuestionAnswers();
        questionAnswers.add(new Answer(UserTest.JAVAJIGI, question, "some contents1"));
        questionAnswers.add(new Answer(UserTest.JAVAJIGI, question, "some contents2"));

        assertThat(questionAnswers.isDeletable(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변이 존재하지 않으면 삭제할 수 있음")
    void isDeletable2() throws CannotDeleteException {
        QuestionAnswers questionAnswers = new QuestionAnswers();

        assertThat(questionAnswers.isDeletable(UserTest.JAVAJIGI)).isTrue();
    }

    @Test
    @DisplayName("답변 작성자가 모두 같지 않으면 삭제할 수 없음")
    void isDeletable3() throws CannotDeleteException {
        Question question = new Question("title", "contents", UserTest.JAVAJIGI);
        QuestionAnswers questionAnswers = new QuestionAnswers();
        questionAnswers.add(new Answer(UserTest.JAVAJIGI, question, "some contents1"));
        questionAnswers.add(new Answer(UserTest.SANJIGI, question, "some contents2"));

        assertThatThrownBy(() -> questionAnswers.isDeletable(UserTest.JAVAJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}