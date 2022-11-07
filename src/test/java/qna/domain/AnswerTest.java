package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;
import qna.fixture.TestAnswerFactory;
import qna.fixture.TestQuestionFactory;
import qna.fixture.TestUserFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AnswerTest {
    @DisplayName("작성자가 없는 답변은 예외가 발생한다")
    @Test
    void no_writer_exception_test() {
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "컨텐츠"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("질문이 없는 답변은 예외가 발생한다")
    @Test
    void no_question_exception_test() {
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "컨텐츠"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("답변은 삭제될 수 있다")
    @Test
    void delete_test() throws CannotDeleteException {
        User writer = TestUserFactory.create("서정국");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);

        answer.delete(writer);

        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("답변이 삭제되면 삭제이력이 반환된다")
    @Test
    void delete_history_test() throws CannotDeleteException {
        User writer = TestUserFactory.create("서정국");
        Question question = TestQuestionFactory.create(writer);
        Answer answer = TestAnswerFactory.create(writer, question);

        assertThat(answer.delete(writer)).isInstanceOf(DeleteHistory.class);
    }
}
