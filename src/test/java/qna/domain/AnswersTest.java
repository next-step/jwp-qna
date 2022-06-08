package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnswersTest {

    @DisplayName("답변이 삭제가 되면 삭제된 history가 list 형태로 리턴이 된다.")
    @Test
    public void deleteTest01() throws CannotDeleteException {
        Answers answers = new Answers();

        answers.add(AnswerTest.A1);
        DeleteHistories delete = answers.delete(UserTest.JAVAJIGI);

        assertThat(delete).isNotNull();
        assertThat(delete.getList())
                .hasSize(1)
                .contains(DeleteHistory.byAnswer(AnswerTest.A1.getId(), UserTest.JAVAJIGI));
    }

    @DisplayName("답변이 중 작성자가 다르면 삭제시 에러가 발생한다.")
    @Test
    public void deleteTest02() {
        Answers answers = new Answers();

        answers.add(AnswerTest.A1);
        answers.add(AnswerTest.A2);

        assertThatThrownBy(() -> {
            answers.delete(UserTest.JAVAJIGI);
        }).isInstanceOf(CannotDeleteException.class);
    }
}